# Social Media Sign Up

![alt tag](https://github.com/ahmed-adel-said/socialmediasignup/blob/master/screenshots/device-2018-02-18-001508.png)![alt tag](https://github.com/ahmed-adel-said/socialmediasignup/blob/master/screenshots/device-2018-02-18-001702.png)

Nowadays most of the mobile apps are integrating with the most famous social media platforms (Facebook, Instagram, Twitter, Google Plus and LinkedIn). For the developer, it’s going to be a hassle to communicate with all of these platforms.
So, SocialMediaSignUp library makes the magic for you, with only one line of code, you can integrate with the most famous social media platform :
- Facebook
- Instagram
- Twitter
- Google Plus
- LinkedIn

And get the user data without handling anything from your side.
SO EASY SO SIMPLE.

-----------------------------------------------------------------------------------------------------

## Features

- Sign up with the most famous social media platforms which are :
  - **Facebook**
  - **Instagram**
  - **Twitter**
  - **Google Plus**
  - **LinkedIn**
- Adding the **scopes** for each platform according to the application needs.
- Support **default** scopes to get the minimum user data.
- Used inside **Activity**, **Fragment** or event **Custom View**.
- Update the **social media sdk versions** in **gradle file** of **your application**, which guarantee the **up-to-date** social media sdk versions.
- Adding the social media **public**, **secret** and **application keys** in the **gradle file** of **your application** to guarantee the totally isolation between the library and the integrated application.
- **No need** to add anything in **Manifest.xml** file, we did all of these to you in SocialMediaSignUp library ;).
- **Easily integration**, with only one line of code you have what you need from the user data from access token to personal data like name, email and profile picture.
  
-----------------------------------------------------------------------------------------------------

## Benefits

- **Hassle of integration** with more that four social media platforms.
- **Increase isolation**, good code structure and separation.
- **Easy integration**, with only one line of code, and that's enough ;).

-----------------------------------------------------------------------------------------------------

## Installation

For Gradle :
Step 1 : Add it in your root build.gradle at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2 : Add the dependency
```java
	dependencies {
	        implementation 'com.github.ahmed-adel-said:socialmediasignup:-SNAPSHOT'
	}
```
For Maven :
Step 1 : Add it in your root build.gradle at the end of repositories:
```java
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2. Add the dependency
```java
	<dependency>
	    <groupId>com.github.ahmed-adel-said</groupId>
	    <artifactId>socialmediasignup</artifactId>
	    <version>-SNAPSHOT</version>
	</dependency>
```

-----------------------------------------------------------------------------------------------------

# User Documentation :

1. If you want to get user data of any of the five social media platforms, all you have to do is to write this one line of code :
```java
/**
 * @param socialMediaType is enum class that has types of all social media platforms.
 * @param scopes that can be passed to each social media platform.
 * @param callback that will be fired once there is success, error, or sign out happens during the connection with any social media platform.
 */
SocialMediaSignUp.getInstance().connectTo(SocialMediaType socialMediaType, List<String> scopes, SocialMediaSignUpCallback callback)
/**
 * This is only for connecting with LinkedIn social media platform.
 * @param linkedInScope that will be passed to LinkedIn social media platform.
 * @param callback that will be fired once there is success, error, or sign out happened during the connection with LinkedIn social media platform.
 */
SocialMediaSignUp.getInstance().connectToLinkedIn(Scope linkedInScope, SocialMediaSignUpCallback callback)
```

2. While ,if you want to disconnect the connection between any of the five social media platforms, here is how can you make it :
```java
/**
 * @param socialMediaType is enum class that has types of all social media platforms.
 * @param callback that will be fired once there is success, error, or sign out happens during the connection with any social media platform.
 */
SocialMediaSignUp.getInstance().disconnectService(SocialMediaType socialMediaType, SocialMediaSignUpCallback callback)
```

3. Here is the SocialMediaType enum class :
```java
enum SocialMediaType {
        FACEBOOK("facebook"),
        GOOGLE_PLUS("google_plus"),
        TWITTER("twitter"),
        INSTAGRAM("instagram"),
        LINKEDIN("linkedin");
    }
```

4. And the SocialMediaSignUpCallback interface :
```java
public interface SocialMediaSignUpCallback {
    void onSuccess(SocialMediaSignUp.SocialMediaType socialMediaType, SocialMediaUser socialMediaUser);
    void onError(Throwable error);
    void onSignOut(SocialMediaSignUp.SocialMediaType socialMediaType);
}
```

5. To add your public, secret and application keys for every social media platform, all you have to do is to override the default values that the library has and put or own values in the build.gradle file app of your app module :
```java
android.defaultConfig.manifestPlaceholders = [
        facebookAppId         : "FACEBOOK_APP_ID",
        googleWebClientId     : "GOOGLE_WEB_CLIENT_ID",
        twitterConsumerKey    : "TWITTER_CONSUMER_KEY”,
        twitterConsumerSecret : "TWITTER_CONSUMER_SECRET",
        instagramClientId     : "INSTAGRAM_CLIENT_KEY",
        instagramClientSecret : "INSTAGRAM_CLIENT_SECRET",
        instagramRedirectUri  : "INSTAGRAM_REDIRECT_URI"
]
```

5. You must add the social media sdk versions by yourself in the build.gradle file of the project module. Since our SocialMediaSignUp library will wait them from you to be your mission to always making the social media sdk versions up-to-date :
```java
ext {
    socialMediaSignUpLibraries = [
            facebookVersion  : '4.30.0',
            googlePlusVersion: '11.8.0',
            twitterVersion   : '3.2.0'
    ]
}
```

-----------------------------------------------------------------------------------------------------

## Contributing
Any contributions are mote than welcomed from other developers to help us make the SDK even better.
Before you contribute there are a number of things that you should know please see [CONTRIBUTING.md](https://github.com/ahmed-adel-said/socialmediasignup/blob/master/CONTRIBUTING.md) for details.

-----------------------------------------------------------------------------------------------------

## License
[MIT License](https://github.com/ahmed-adel-said/socialmediasignup/blob/master/LICENSE)

Copyright (c) 2018 Ahmed Adel
