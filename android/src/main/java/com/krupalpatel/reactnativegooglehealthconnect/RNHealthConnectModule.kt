package com.krupalpatel.reactnativegooglehealthconnect

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.facebook.react.bridge.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Instant


class RNHealthConnectModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

    override fun getName() = "RNHealthConnectModule"

    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("count" to 1)
    }

    private val healthConnectManager by lazy {
        HealthConnectManager(reactApplicationContext)
    }

    private var callback: Callback? = null
    private var errorCallback: Callback? = null

    companion object {
        const val HEALTH_PERMISSION_REQUEST = 1
    }

    init {
        reactApplicationContext.addActivityEventListener(this)
    }

    private val PERMISSIONS = setOf(
        Permission.createReadPermission(Steps::class),
        Permission.createReadPermission(HeartRateSeries::class)
    )

    private val permissions = setOf(
        Permission.createWritePermission(Steps::class),
        Permission.createWritePermission(SpeedSeries::class),
        Permission.createWritePermission(Distance::class),
        Permission.createWritePermission(HeartRateSeries::class)
    )

    private val myPluginScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @ReactMethod
    fun authorize(callback: Callback, errorCallback: Callback) {
        this.callback = callback
        this.errorCallback = errorCallback
        val availability by healthConnectManager.availability
        var hasPermission: Boolean
        if (availability == HealthConnectAvailability.INSTALLED) {
            myPluginScope.launch {
                hasPermission = healthConnectManager.hasAllPermissions(PERMISSIONS)
                Log.d("TAGG", "hasPermission=${hasPermission}")
                if(!hasPermission) {
                    try {
                        val intent = HealthDataRequestPermissions().createIntent(reactApplicationContext, PERMISSIONS)
                        reactApplicationContext.startActivityForResult(intent, HEALTH_PERMISSION_REQUEST, null)
                    } catch (t: Throwable) {

                    }
                }
            }
        }
    }

    @ReactMethod
    fun isAuthorized(promise: Promise) {
        var isAuthorized = false
        val availability by healthConnectManager.availability
        var hasPermission: Boolean
        if (availability == HealthConnectAvailability.INSTALLED) {
            myPluginScope.launch {
                hasPermission = healthConnectManager.hasAllPermissions(PERMISSIONS)
                if(hasPermission){
                    isAuthorized = true
                    val map = Arguments.createMap()
                    map.putBoolean("isAuthorized", isAuthorized)
                    promise.resolve(map)
                } else {
                    val map = Arguments.createMap()
                    map.putBoolean("isAuthorized", isAuthorized)
                    promise.resolve(map)
                }
            }
        } else {
            val map = Arguments.createMap()
            map.putBoolean("isAuthorized", isAuthorized)
            promise.resolve(map)
        }
    }

     @ReactMethod
     fun getDailySteps(start: String, end: String, promise: Promise) {
         myPluginScope.launch {
//             Log.d("TAGG", "start=${Instant.parse(start)},end=${Instant.parse(end)}}")
             val steps = healthConnectManager.getDailyStepCountSamples(Instant.parse(start), Instant.parse(end))
             val array = Arguments.createArray()
             for (stepRecord in steps) {
                 val map = Arguments.createMap()
                 map.putDouble("count", stepRecord.count.toDouble())
                 map.putString("startTime", stepRecord.startTime.toString())
                 map.putString("endTime", stepRecord.endTime.toString())
                 map.putString("startZoneOffset", stepRecord.startZoneOffset.toString())
                 map.putString("endZoneOffset", stepRecord.endZoneOffset.toString())

                 array.pushMap(map)
             }

             promise.resolve(array)
         }
     }

    @ReactMethod
    fun getDailyHeartRate(start: String, end: String, promise: Promise) {
        myPluginScope.launch {
            val heartRateSeries = healthConnectManager.getDailyHeartRateCount(Instant.parse(start), Instant.parse(end))
            for (heartRate in heartRateSeries) {
                for(heart in heartRate.samples){
                    Log.d("TAGG", "beats=${heart.beatsPerMinute},time=${heart.time},start=${heartRate.startTime}")
                }

            }

        }
    }

    @ReactMethod
    fun getDailySleepSamples(start: String, end: String, promise: Promise) {
//        myPluginScope.launch {
////             Log.d("TAGG", "start=${Instant.parse(start)},end=${Instant.parse(end)}}")
//            val sleepSession = healthConnectManager.getDailySleepSamples(Instant.parse(start), Instant.parse(end))
//
//            Log.d("TAGG", "sleepSession=${sleepSession}")
//        }
    }



    @ReactMethod
    fun getAggregatedSteps(start: String, end: String, promise: Promise) {
        myPluginScope.launch {
            val steps = healthConnectManager.getAggregatedStepsSamples(Instant.parse(start), Instant.parse(end))
            val map = Arguments.createMap()
            map.putDouble("steps", steps.toDouble())
            promise.resolve(map)
        }
    }

    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == HEALTH_PERMISSION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                callback?.invoke(data?.data?.toString() ?: "Permission Granted")
            } else {
                errorCallback?.invoke("No Permission")
            }
            callback = null
            errorCallback = null
        }
    }

    override fun onNewIntent(intent: Intent?) = Unit

}
