![Maven Central](https://img.shields.io/maven-central/v/app.infoset.android/infoset-sdk)
![GitHub](https://img.shields.io/github/license/infoset/infoset-android)

<img src="https://user-images.githubusercontent.com/13895224/94475996-8de39c80-01d8-11eb-8771-e590b33c612e.png" alt="Infoset" width="300" />

# Infoset Android SDK

Infoset Android SDK allows you to integrate [Infoset Chat](https://infoset.app) with your Android app.

# Installation

Infoset Android SDK supports API 16 and above.

Add the following dependency to your app's `build.gradle` file:
```gradle
dependencies {
    implementation 'app.infoset.android:infoset-sdk:1.0.2'
}
```

Your application will need a permission to use the Internet. Add the following line to your **AndroidManifest.xml**:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```
<div class="clear"></div>

You will need to include the [READ_EXTERNAL_STORAGE](http://developer.android.com/reference/android/Manifest.permission.html#READ_EXTERNAL_STORAGE) permission if you have enabled attachments in your Infoset chat widget:

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
<div class="clear"></div>

# Usage

There are multiple ways you can use this library. We recommend either using the static method which adds the chat view to your activity, or adding the chat view as an embedded view in your XML. As long as InfosetChatView is initialized, you will get events when a new message comes in.

First, you need to configure your chat view.

## Configuration

Simply use the `InfosetChatConfiguration` constructor. Note that **API key** and **Android key** fields are mandatory and you can get your keys from the [dashboard](https://dashboard.infoset.app/settings/chat?tab=chatWidgets).

```java
configuration = new InfosetChatConfiguration(
    "api_key",
    "android_key",
    "tags", // optional, allows you to route chats to specific tags
    new InfosetChatVisitor(...), // optional, you can give `null` for anonymous visitors or build an InfosetChatVisitor object by providing logged-in user's details
    "override_color"); // optional, allows you to override your chat widget's primary color
```

## Chat View

There are two recommended ways to use InfosetChatView.

* Full screen InfosetChatView added to the root of your activity, or
* XML embedded InfosetChatView to control placement and size

### Full Screen Window

All you need to do is to create, attach and initialize `InfosetChatView`. Example:

```java
private InfosetChatView chatView;

// ... other functions

public void startFullScreenChat() {
    if (chatView == null) {
        chatView = InfosetChatView.createAndAttachChatWindowInstance(getActivity());
        chatView.setUpWindow(configuration);
        chatView.setUpListener(this);
        chatView.initialize();
    }
    chatView.showChatWindow();
}
```

### XML Embedded View

If you like to control the place and size of the InfosetChatView, you might want to add it to your app either by including a view in XML and accessing in your code:

```xml
<app.infoset.android.InfosetChatView
    android:id="@+id/embedded_chat_view"
    android:layout_width="match_parent"
    android:layout_height="400dp"/>
```

```java
InfosetChatView chatView = (InfosetChatView) view.findViewById(R.id.embedded_chat_window);
```

or inflating the view directly:

```java
InfosetChatView chatView = new InfosetChatView(MainActivity.this);
```

Once you have the InfosetChatView object, you can initialize and show the chat view anytime you want, just like the full screen window approach:

```java
public void startEmbeddedChat(View view) {
    if (!chatView.isInitialized()) {
        chatView.setUpWindow(configuration);
        chatView.setUpListener(this);
        chatView.initialize();
    }
    chatView.showChatWindow();
}
```

## Navigation

Depending on your use case you might want to hide InfosetChatView if user hits back button.
You can use our `onBackPressed()` function which hides the view if its visible and returns true.
In your activity/fragment add the following:

```java
@Override
public boolean onBackPressed() {
    return chatView != null && chatView.onBackPressed();
}
```

## ChatEventsListener

This listener gives you opportunity to:

* handle the case when the user wants to attach a file in chat,
* subscribe to incoming messages, so that you can show notifications to the user or an unread message count badge,
* subscribe to room events such as onOpen, onClose and onReopened, so you'll be instantly notified of room status changes,
* react on visibility changes (user can hide the view on its own),
* handle opened links in a custom way,
* handle errors coming from chat view.

You can subscribe to these events by implementing `InfosetChatView.ChatEventsListener` in your activity/fragment.

### File sharing

To provide your users the ability to attach files in chat, you need to implement `ChatEventsListener` and give opportunity to InfosetChatView to handle activity result, i.e.

```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (chatView != null) chatView.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
}
```

### Handling URLs

You can disable chat widget's default behavior when user selects link by implementing `handleUri` method from `ChatEventsListener`.

```java
@Override
public boolean handleUri(Uri uri) {
	// Handle uri here...
	return true; // Return true to disable default behavior.
}
```

### Error handling

You might want to customize how the errors are handled, such as handling the internet connection issues.
By returning `true` in `onError` callback method you're taking responsibility to handle errors coming from the chat view.

Please keep in mind that the chat view, once loaded, can handle connection issues by sporadically trying to reconnect.
This case can be detected by implementing following condition in onError callback method.

```java
@Override
public boolean onError(InfosetChatErrorType errorType, int errorCode, String errorDescription) {
    if (errorType == InfosetChatErrorType.WebViewClient && errorCode == -2 && chatWindow.isChatLoaded()) {
        // Chat view can handle reconnection. You might want to delegate this to chat view
        return false;
    } else {
        reloadChatBtn.setVisibility(View.VISIBLE);
    }
    Toast.makeText(getActivity(), errorDescription, Toast.LENGTH_SHORT).show();
    return true;
}
```

### Device orientation changes

If you do not want the chat view to reload its content every time device orientation changes, add this line to your Activity in the manifest:

```java
android:configChanges="orientation|screenSize"
```

<div class="clear"></div>

The chat view will handle the orientation change by itself.

## Example usage

There are two ways to open the chat view – using Activity or Fragment. You can check the sample app in the `example` folder or follow the instructions below to easily integrate Infoset Chat to your app.

### Using Activity

In order to open a chat view in new Activity, you need to declare **InfosetChatActivity** in your manifest. Add the following line to **AndroidManifest.xml**, between `<application></application>` tags:

```xml
<activity android:name="app.infoset.android.InfosetChatActivity" android:configChanges="orientation|screenSize" />
```

<div class="clear"></div>

Finally, add the following code to your application, in a place where you want to open the chat view (e.g. button listener). You need to provide a Context (your Activity or Application object), your Infoset chat widget API key and Android key (taken from the [dashboard](https://dashboard.infoset.app/settings/chat?tab=chatWidgets) and, optionally, comma-separated tags to route the chats:

```java
Intent intent = new Intent(context, app.infoset.android.InfosetChatActivity.class);
Bundle config = new InfosetChatConfiguration.Builder()
	.setApiKey("<api_key>")
	.setAndroidKey("<android_key>")
	.setTags("<comma_separated_tags>") // optional, allows you to route chats to specific tags
	.setVisitor(new InfosetChatVisitor(...)) // // optional, you can give `null` for anonymous visitors or build an InfosetChatVisitor object by providing logged-in user's details
  .setColor("override_color") // optional, allows you to override your chat widget's primary color
	.build()
	.asBundle();
intent.putExtras(config);
startActivity(intent);
```

### Using Fragment

In order to open chat view in new Fragment, you need to add the following code to your application, in a place where you want to open the chat view (e.g. button listener). You also need to provide your Infoset chat widget API key and Android key:

```java
getFragmentManager()
   .beginTransaction()
   .replace(R.id.frame_layout, InfosetChatFragment.newInstance("api_key", "android_key"), "chat_fragment")
   .addToBackStack("chat_fragment")
   .commit();
```

<div class="clear"></div>

Method `InfosetChatFragment.newInstance()` returns chat view Fragment.

<div class="clear"></div>

It’s also possible to identify users by providing their details, so you can immediately know who you are talking to on the dashboard. Add the following line to the previous code:

```java
getFragmentManager()
   .beginTransaction()
   .replace(R.id.frame_layout, InfosetChatFragment.newInstance("api_key", "android_key", "tags", new InfosetChatVisitor(...)), "chat_fragment")
   .addToBackStack("chat_fragment")
   .commit();
```

# Localization

You can change or localize error messages by defining your own string resources with following ids:

```xml
<string name="failed_to_load_chat">Failed to load chat</string>
<string name="cant_share_files">File sharing is not configured</string>
<string name="reload_chat">Reload</string>
```

## React Native Support

We have a [React Native SDK](https://github.com/infoset/infoset-react-native) for React Native / Expo ⚛️

## Getting help

If you have any questions or want to provide feedback, [shoot an email](mailto:support@infoset.app) or [chat with us!](https://infoset.app/tr/)
