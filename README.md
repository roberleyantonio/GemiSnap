# GemiSnap 📸

![Kotlin](https://img.shields.io/badge/Kotlin-2.3+-blue.svg)
![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Koin](https://img.shields.io/badge/DI-Koin-orange.svg)
![Gemini](https://img.shields.io/badge/AI-Gemini_Pro-purple.svg)

**GemiSnap** is a high-performance Android application designed to analyze images using the **Google Gemini AI SDK**. The project is built with a focus on **Clean Architecture**, on the last Android 15+ stack (SDK 36), memory efficiency, and full internationalization.

---

## 🛠 Features

- **Gemini AI Integration**: Advanced image-to-text analysis using Generative AI.
- **Efficient Image Handling**: Custom `AndroidBitmapDecoder` with automatic downsampling to prevent `OutOfMemory` (OOM) errors.
- **Clean Architecture**: Decoupled layers using `UiModel` for highly testable UI logic.
- **Memory Optimized**: Strict bitmap lifecycle management with explicit recycling after Base64 conversion.
- **Modern UI**: Built entirely with Jetpack Compose using the new kotlin-compose plugin (v2.3.0).

## 🏗 Architecture & Design

The project is structured to ensure that business logic is completely separated from Android framework dependencies:

- **Presentation**: `HubSnapUiModelImpl` handles UI states, formatting, and localized messages.
- **Infrastructure**: `AndroidBitmapDecoder` manages low-level image decoding using modern `ImageDecoder` APIs.
- **Dependency Injection**: Powered by **Koin** for clean and scalable module management.

## 🏗 Tech Stack (Modern Specs)
- Language: Kotlin 2.3.0
- UI: Compose BOM 2026.01.00
- DI: Koin 4.1.1
- Network: Retrofit 3.0.0 + OkHttp 5.3.2
- Test: MockK 1.14.7 + Turbine 1.2.1

## 🚀 Getting Started

1. **Get an API Key**: Obtain your key at [Google AI Studio](https://aistudio.google.com/).
2. **Clone the repository**:
   `git clone https://github.com/your-username/gemisnap.git`
3. **Configure the Key**: Add your API key to your project's configuration (e.g., `local.properties`):
   `gemini.api.key=YOUR_API_KEY`
4. **Build**: Sync Gradle and run the app on an emulator (API 26+) or physical device.

## 🧪 Testing

The codebase is designed for high testability:
- **Unit Tests**: Using **MockK** to validate logic without Android dependencies.
- **Flow**: **Turbine** for testing Coroutine Flows.

`./gradlew test`

---
*Developed with a focus on performance, clean code, and AI scalability.*
