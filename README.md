# iOS Keyboard Ultra

A GitHub-ready Android keyboard (IME) starter inspired by the clean iPhone keyboard aesthetic.

> This is an independent Android project. It is not affiliated with Apple and does not include Apple's proprietary assets, fonts, or emoji artwork.

## Included in this starter
- Real Android system keyboard using `InputMethodService`
- Turkish QWERTY layout, shift, delete, space, enter and number/symbol page
- Emoji quick panel
- Light, rounded key styling
- Setup screen with shortcuts to enable and select the keyboard
- Translate shortcut that opens Google Translate with selected text (or text before the cursor)
- Basic clipboard copy shortcut

## Build
1. Open this folder in Android Studio.
2. Allow Gradle sync to finish.
3. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Install the debug APK on your Android device.

Recommended: Android Studio Ladybug or newer, JDK 17, Android SDK 35.

## Enable the keyboard
1. Open the app.
2. Tap **Enable keyboard** and enable *iOS Keyboard Ultra* in Android settings.
3. Return to the app and tap **Choose keyboard**.
4. Select *iOS Keyboard Ultra*.

Android may show a security warning because third-party keyboards can process text entered into fields. Only enable keyboards you trust.

## Privacy
This starter does not send typed text to a server. The Translate shortcut sends the selected/before-cursor text to Google Translate only when the user taps Translate. Do not add analytics or network transmission without clear disclosure and consent.

## Roadmap
- Language packs and per-language layouts
- Better autocorrect and next-word suggestions
- Speech input via Android speech recognizer
- User-created themes and keyboard settings
- A proper translation provider with configurable API endpoint
- Automated tests and accessibility improvements

## License
MIT — see [LICENSE](LICENSE).
