Minimal Notes App

A lightweight, transparent Android notes application built using Jetpack Compose and native SQLiteOpenHelper.
Architecture

    UI: Jetpack Compose (MainActivity + MainViewModel with Kotlin Flows)

    Persistence: Direct SQLiteOpenHelper with zero annotation processors or KMP build-complexity.

This project uses Gradle for building, testing, and running the Android application.

Build, install, and launch debug APK on a connected device:

    ./gradlew :androidApp:installDebug --build-cache

Clean build and fresh install (bypasses stale compiler caches):

    ./gradlew clean :androidApp:installDebug --no-daemon

Check connected ADB devices:

    adb devices

View live Logcat logs filtered for the app:

    adb logcat -s AndroidRuntime System.out com.example.notes

Build & Compilation Workflow

Build project targets without installing:

    ./gradlew :androidApp:compileReleaseKotlin

Wipe all build artifacts and compiler caches:

    ./gradlew clean
