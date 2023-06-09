# OpenAI Android App

This is a sample Android app that demonstrates using speech-to-text and text-to-speech capabilities along with the OpenAI API to provide an interactive conversation experience.

## Prerequisites

- Android Studio (latest version recommended)
- An Android device or emulator running Android API level 21 or higher
- An OpenAI API key

## Importing the Project

1. Open Android Studio.
2. Click on "Open an existing Android Studio project" or go to "File" > "Open".
3. Navigate to the directory where you have the project files, select the project folder, and click "Open".

Android Studio will import the project and configure the required dependencies.

## Adding the OpenAI API Key

1. Open the `MainActivity.java` file in Android Studio.
2. Locate the `OPENAI_API_KEY` constant, and replace `"your_openai_api_key"` with your actual OpenAI API key:

```java
private static final String OPENAI_API_KEY = "your_openai_api_key";
```


## Building and Running the App
1. Connect your Android device to your computer or start an Android emulator.
1. In Android Studio, click on the "Run" button (a green triangle) in the top toolbar, or press Shift + F10.
1. Select the target device (your connected Android device or emulator) and click "OK".
1. Android Studio will build the app and install it on the selected device. The app should start automatically after the installation is complete.

## Using the App
1. Press the "Speak" button to start speech recognition.
1. Speak your query or message.
1. The app will convert your speech to text and send it to the OpenAI API.
1. The AI response will be converted to speech and played back on your device.
1. Note: The app requires internet access and permissions for recording audio to function properly.

## Troubleshooting
If you encounter any issues with the app, ensure that your Android device or emulator has a working internet connection and that the necessary permissions have been granted to the app. You can also check the Android Studio logcat output for any error messages or exceptions.