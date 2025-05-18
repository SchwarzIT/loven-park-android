
# 🤖 LovenPark Android App – Documentation

## 📝 Overview

- **App Name:** LovenPark
- **Platform:** Android
- **Language/Framework:** Kotlin
- **Minimum SDK:** 26
- **Target SDK:** 33
- **Version:** 2.0.1
- **Architecture:** MVVM + Hilt + Jetpack Navigation
- **Dependency Manager:** Gradle

---

## 🌟 Key Features

- 📍 Interactive map and location features
- 🏛️ Discover interesting sites and articles about Loven Park
- 🚨 Submit issue reports with camera/gallery support
- 📷 Take or select images using custom camera UI
- 👤 User authentication and profile
- 🧭 Custom bottom navigation with central action

---

## 🧩 Architecture

| Layer         | Details |
|---------------|---------|
| **UI**        | Fragments and Views organized by feature |
| **ViewModel** | Manages UI logic, scoped to lifecycle     |
| **Repository**| Abstracts data sources (network/Realm)    |
| **Data Layer**| Realm used for local persistence          |
| **DI**        | Hilt for dependency injection             |
| **Navigation**| Jetpack Navigation + SafeArgs             |

---

## 🗂️ Feature Modules

### `camera/`
- `CameraFragment.kt`: Camera UI
- `CameraViewModel.kt`: State and logic
- `BitmapProcessor.kt`: Image processing
- `GalleryImageAdapter`: Image list support

### `about/`
- `Article.kt`: Info model
- `detail/` & `overview/`: Screens for Loven Park history and facts

### `bottommenu/`
- `BottomNavigationViewWithCenterButton.kt`: Custom tab layout with central action

---

## 🛠️ Build Configuration

| Setting          | Value |
|------------------|-------|
| `applicationId`  | `com.schwarzit.lovenpark` |
| `Flavor Dimensions` | `version` |
| `Flavors`        | `beta`, `prod` |
| `Beta URL`       | `https://lovenpark-backend-dev.apps.01.cf.eu01.stackit.cloud/` |
| `Prod URL`       | `https://lovenpark-backend.apps.01.cf.eu01.stackit.cloud/` |

---

## 🔌 Libraries & Plugins

| Library / Plugin                          | Purpose                                 |
|-------------------------------------------|-----------------------------------------|
| `Jetpack Navigation + SafeArgs`           | Navigation and type-safe arguments      |
| `Hilt`                                    | Dependency Injection                    |
| `Realm Kotlin`                            | Local storage and persistence           |
| `Secrets Gradle Plugin`                   | API key management                      |
| `Kotlin Parcelize`                        | Parcelable support                      |

---

## 🔐 Security & Data

- Uses **Realm** for local secure storage
- API keys managed via secrets plugin
- URL endpoints managed via build flavor constants

---

## 🧪 Testing & CI

- Includes `androidTest` and `test` directories
- Integrated CI config: `odj-azure-pipeline.yml`

---

## 🧠 Future Improvements

- Offline caching for map and site data
- Multi-language support
- Enhanced analytics and user engagement tracking