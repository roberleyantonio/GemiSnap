# GemiSnap 📸

![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg)
![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Koin](https://img.shields.io/badge/DI-Koin-orange.svg)
![Gemini](https://img.shields.io/badge/AI-Gemini_Pro-purple.svg)

**GemiSnap** is a high-performance Android application designed to analyze images using the **Google Gemini AI SDK**. The project is built with a focus on **Clean Architecture**, memory efficiency, and full internationalization.

---

## 🛠 Features

- **Gemini AI Integration**: Advanced image-to-text analysis using Generative AI.
- **Efficient Image Handling**: Custom `AndroidBitmapDecoder` with automatic downsampling to prevent `OutOfMemory` (OOM) errors.
- **Clean Architecture**: Decoupled layers using `HubSnapContract.UiModel` for highly testable UI logic.
- **Memory Optimized**: Strict bitmap lifecycle management with explicit recycling after Base64 conversion.

## 🏗 Architecture & Design

The project is structured to ensure that business logic is completely separated from Android framework dependencies:

- **Presentation**: `HubSnapUiModelImpl` handles UI states, formatting, and localized messages.
- **Infrastructure**: `AndroidBitmapDecoder` manages low-level image decoding using modern `ImageDecoder` APIs.
- **Dependency Injection**: Powered by **Koin** for clean and scalable module management.

## 🚀 Getting Started

1. **Get an API Key**: Obtain your key at [Google AI Studio](https://aistudio.google.com/).
2. **Clone the repository**:
   `git clone https://github.com/your-username/gemisnap.git`
3. **Configure the Key**: Add your API key to your project's configuration (e.g., `local.properties`):
   `gemini.api.key=YOUR_API_KEY`
4. **Build**: Sync Gradle and run the app on an emulator (API 26+) or physical device.

## 🧪 Testing

The codebase is designed for high testability:
- **Unit Tests**: Using **MockK** to validate `HubSnapUiModelImpl` logic without Android dependencies.

`./gradlew test`

---
*Developed with a focus on performance, clean code, and AI scalability.*
