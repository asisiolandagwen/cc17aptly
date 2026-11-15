# Apt.ly – Lab Act 3 screens (Android Studio, XML + Java)

## Build the APK
1. Android Studio → **File › Open** → select this `AptlyUI` folder. Wait for Gradle sync to finish.
2. In `app/build.gradle`, change `def groupNumber = "X"` to your group number, then click **Sync Now**.
3. **Build › Build APK(s)** → click **locate**. The file is `app/build/outputs/apk/debug/GROUP <number> UI.apk`.

## The 6 screens
| # | Screen | Layout (res/layout) | Java |
|---|---|---|---|
| 1 | Logo | activity_main.xml | MainActivity (app starts here) |
| 2 | Welcome – Select User Type | activity_welcome.xml | WelcomeActivity |
| 3 | Sign In | activity_sign_in.xml | SignInActivity |
| 4 | Create Account | activity_create_account.xml | CreateAccountActivity |
| 5 | Terms & Conditions | activity_terms.xml | TermsActivity |
| 6 | Profile | activity_profile.xml | ProfileActivity |

## Flow
Logo (2 seconds) → Welcome → tap Tenant or Homeowner → Sign In
- Sign In → Continue → Profile
- Sign In → "Create account" → Create Account → "Terms & Conditions" → Okay → back to Create Account → Continue → Profile

Explore, Favorites and Messages in the bottom bar are not connected, because those screens don't exist yet.

## Images (res/drawable-nodpi)
- `profile_photo.png` comes from the prototype screenshot, so it is low resolution. For a sharp photo, replace it with the original image file using the same name.
- `welcome_pattern_top.png` / `welcome_pattern_bottom.png` are the pattern banners from the prototype.
- The logo is a vector (`res/drawable/ic_logo.xml`), so it stays sharp at any size.
