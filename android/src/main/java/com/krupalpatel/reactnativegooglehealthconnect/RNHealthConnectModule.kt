package com.krupalpatel.reactnativegooglehealthconnect

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.ActivitySession
import androidx.health.connect.client.records.Steps
import androidx.health.connect.client.request.ReadRecordsRequest
import com.facebook.react.bridge.*
import kotlinx.coroutines.*
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

    private val PERMISSIONS = setOf(Permission.createReadPermission(Steps::class))

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

//    @ReactMethod
//    fun getDailyStepCountSamples(
//        startDate: Double,
//        endDate: Double,
//        bucketInterval: Int,
//        bucketUnit: String?,
//        promise: Promise
//    ) {
//        try {
//
//        } catch (e: Error) {
//            promise.reject(e)
//        }
//    }

    @ReactMethod
    fun getDailySteps(start: Instant, end: Instant, promise: Promise) {
        myPluginScope.launch {
            val steps = healthConnectManager.getDailyStepCountSamples(start, end)
            Log.d("TAGG", "steps=${steps.toString()}")
            promise.resolve(steps)
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
