# react-native-google-health-connect

A React Native bridge module for interacting with Google Health connect

## Get Started

Add the following queries into your ```AndroidManifest.xml```

```text
<queries>
  <package android:name="com.google.android.apps.healthdata" />
</queries>
```

Declare the below in your Activity in ```AndroidManifest.xml``` to handle intent that will explain your app's use of permissions.

```text
<intent-filter>
  <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />
</intent-filter>

<intent-filter>
  <action android:name="androidx.health.ACTION_BIND_HEALTH_DATA_SERVICE" />
</intent-filter>

<!-- Required to specify which Health Connect permissions the app can request -->
<meta-data
  android:name="health_permissions"
  android:resource="@array/health_permissions"/>
```

Declare the permissions your app will use. Create an array resource in ```res/values/health_permissions.xml```

```text
<resources>
  <array name="health_permissions">
    <item>androidx.health.permission.HeartRate.READ</item>
    <item>androidx.health.permission.Steps.READ</item>
  </array>
</resources>
```

Note that you will need to add a line for every permission your app will use:

## USAGE

1. ```import HealthConnect from 'react-native-google-health-connect';```

2. Authorize:

To check whether HealthConnect is already authorized, simply use a function

```text
const isAuthorized = await HealthConnect.checkIsAuthorized()
```

To ask for the permission from health connect

```text
HealthConnect.authorize(async(result) => {

},(error) => {
    
})
```

## TO-DO

get following health data from health connect

- Steps
- Heart Rate
- Cycling Distance
- Activities
- Sleep
