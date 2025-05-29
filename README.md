# Love'n'park iOS

[![License](https://img.shields.io/badge/License-Apache-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android-green)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-blueviolet.svg)](https://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-7.3.3-brightgreen.svg)](https://gradle.org/)

> A CSR initiative to help protect one of the hidden gems in Sofia - Loven Park. Hearted by Schwarz IT

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Requirements](#requirements)
- [Contributing](#contributing)
- [Codeowner](#codeowner)
- [License](#license)

## Features

* Easily explore and navigate through Loven park
* Find out interesting places in the park and their history
* Create reports for irregularities in the park directly to Sofia Municipality

*Note: if there is no GUI for the project, then you can skip this section.*

## Installation

1.  Clone the repository:
    ```bash
    git clone [Your Repository URL]
    ```
2.  Open the project in Android Studio.

## Usage

To properly configure the application, you will need to adjust the following:

1.  **Google Maps API Key:**
    * As mentioned, the project uses Google Maps for Android which requires an API key.
    * Create a `local.properties` file in the root of your project if it doesn't exist.
    * Add your Google Maps API key to this file:
        ```properties
        gmp.key=YOUR_GMS_SERVICES_API_KEY
        ```
    * This file is usually ignored by Git.

2.  **Base URLs (if applicable):**
    * If your Android app communicates with a backend, you might have different base URLs for different environments. Look for a configuration file (e.g., `build.gradle` or a dedicated config file) and update the base URLs accordingly. For example, you might have debug and release build variants with different API endpoints.

3.  **Google Sign-in Client ID (if applicable):**
    * If your application uses Google Sign-in, you'll need to configure it in your Firebase project and obtain a Web client ID.
    * Look for where the `GoogleSignInOptions` are configured in your code (likely in an `Application` class or a login-related `ViewModel/Activity`) and update the client ID:
        ```kotlin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID_FROM_FIREBASE")
            .requestEmail()
            .build()
        ```

4.  **Map Boundaries (if applicable):**
    * If you want to focus the map on Loven Park or a specific area, you might have constants defining the map's boundaries. Look for these constants in your code (e.g., in a `Constants.kt` file or a map-related `ViewModel/Activity`) and update them:
        ```kotlin
        object MapConstants {
            const val LOVEN_PARK_SW_LAT = 42.656357
            const val LOVEN_PARK_SW_LNG = 23.324131
            const val LOVEN_PARK_NE_LAT = 42.6755
            const val LOVEN_PARK_NE_LNG = 23.3441
            // ... other map related constants
        }
        ```

5.  **Terms and Conditions & Privacy Policy:**
    * If your app displays Terms and Conditions and Privacy Policy, look for where these texts are defined (e.g., in string resources, data classes, or displayed directly in UI files) and update them according to your needs.

## Requirements

In order to be able to build and run the project, you will need:

* Android Studio [version] or higher
* Android SDK [minimum version] or higher
* Gradle [version] or higher
* Kotlin [version] or higher
* A Google Cloud Platform (GCP) project if you are using Google Maps and Google Sign-in.
* An emulator or a physical Android device for testing.

## Contributing

If you want to contribute to the project and help for the protection of other parks or relevant locations, feel free to fork the repository and create a new application.

## Codeowner

Schwarz IT Bulgaria EOOD

## License

See [LICENSE.md](LICENSE.md)
