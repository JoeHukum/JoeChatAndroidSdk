# JoeChatAndroidSdk
Plug and play android SDK for Joe Hukum chat.

Add the following dependency to your application's build.gradle
```gradle
repositories {
    maven { url 'http://clojars.org/repo' }
}

dependencies {
	compile 'com.joehukum:chat:0.44'
}
```

Use the following code in the `onCreate()` method of the application class in your app.
```java
JoeHukum.init(getApplicationContext());
```

To initiate a chat add the following code.
```java
JoeHukum.chat(this, "your-auth-key", "99xxxxxxxx", "abc@xyz.com");
```
`e-mail` and `phone number` are nullable / optional in the above method.



Following code needs to be added to the AndroidManifest.xml of your app.

```xml
<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE"/>
```

Inside `<application>`
```xml
<activity
	android:name="com.joehukum.chat.ui.activities.ChatActivity"
    android:theme="@style/AppTheme.NoActionBar"/>

<service android:name="com.joehukum.chat.messages.sync.GenericAccountService">
	<intent-filter>
		<action android:name="android.accounts.AccountAuthenticator"/>
	</intent-filter>

    <meta-data
        android:name="android.accounts.AccountAuthenticator"
		android:resource="@xml/authenticator"/>
</service>

<service android:name="com.joehukum.chat.messages.sync.SyncService">
	<intent-filter>
		<action android:name="android.content.SyncAdapter"/>
	</intent-filter>
	<meta-data
		android:name="android.content.SyncAdapter"
		android:resource="@xml/sync_adapter"/>
</service>

<provider
	android:name="com.joehukum.chat.messages.database.MessageProvider"
	android:authorities="your-package-name"
	android:exported="false"
	android:multiprocess="true"/>

<meta-data
	android:name="com.joehukum.authority"
	android:value="your-package-name"/>

<!-- Mention the resource for notification icon for the app -->
<meta-data
	android:name="com.joehukum.chat.notification.icon"
	android:resource="@drawable/notification_icon"/>

<!-- if your app doesn't implement GCM, Add this -->
<service
	android:name="com.joehukum.chat.messages.pubsub.JhGcmListenerService"
	android:exported="false">
	<intent-filter>
		<action android:name="com.google.android.c2dm.intent.RECEIVE"/>
	</intent-filter>
</service>

<receiver
	android:name="com.google.android.gms.gcm.GcmReceiver"
	android:exported="true"
	android:permission="com.google.android.c2dm.permission.SEND">
	<intent-filter>
		<action android:name="com.google.android.c2dm.intent.RECEIVE"/>
	</intent-filter>
</receiver>
<!-- GCM elements end -->

```

If you have already implemented GCM in your app you can simply add the following code in your app's GcmListenerService

```java
JhGcmListenerService service = new JhGcmListenerService();
service.onMessageReceived(from, data);
```



You need to create a file in the xml directory of resources for account creation on the device.

authenticator.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
                       android:accountType="your-package-name"
                       android:icon="@mipmap/ic_launcher"
                       android:smallIcon="@mipmap/ic_launcher"
                       android:label="@string/app_name" />
```



You will also need to add a file syncing the content provider data to the servers.

sync_adapter.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<sync-adapter xmlns:android="http://schemas.android.com/apk/res/android"
                    android:contentAuthority="your-package-naame"
                    android:accountType="your-package-name"
                    android:userVisible="false"
                    android:supportsUploading="false"
                    android:allowParallelSyncs="false"
                    android:isAlwaysSyncable="true" />
```
