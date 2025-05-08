# React Native Plugin Android Telephony

This plugin allows you to retrieve ALL cellular information from Android devices if available.

# Installation in bare React Native projects

For bare React Native projects, you must ensure that you have [installed and configured the `expo` package](https://docs.expo.dev/bare/installing-expo-modules/) before continuing.

### Add the package to your npm dependencies

```
npm install android-telephony
```

### Configure for Android

Since module requires ACCESS_FINE_LOCATION permission, make sure to add it to your app
AndroidManifest.xml file:
```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```
If you are using Expo EAS Build, modify your app.json or app.config.js to include:
```
{
  "expo": {
    "android": {
      "permissions": ["ACCESS_FINE_LOCATION",...]
    }
  }
}
```