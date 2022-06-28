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

Note that you will need to add a line for every permission your app will use:
