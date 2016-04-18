# JoeChatAndroidSdk
Plug and play android SDK for Joe Hukum chat.

Add the following dependency to your application's build.gradle
```gradle
dependencies {
	compile 'com.joehukum:chat:0.1.1'
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
