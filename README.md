Development & Build Cheat Sheet

This project uses Gradle for building, testing, and deploying across the shared modules and Android application.

Android Device Workflow

- Build, install, and launch debug APK on connected device:
  ./gradlew :androidApp:installDebug

- Clean build and fresh install (bypasses stale compiler caches):
  ./gradlew clean :androidApp:installDebug --no-daemon

- Check connected ADB devices:
  adb devices

- View live Logcat logs filtered for the app:
  adb logcat -s AndroidRuntime System.out com.example.notes

Linux & Shared Module Workflow

- Run all tests across the project:
  ./gradlew test

- Run tests specifically for the shared module:
  ./gradlew :shared:test

- Build all project targets without installing:
  ./gradlew build

- Wipe all build artifacts and compiler caches:
  ./gradlew clean

  