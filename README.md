# RideMitra

### Airport Ride Booking & Carpooling App

RideMitra is a mobile application designed to make airport transportation easier through ride booking and carpooling.

## Features
- **Book Airport Rides**: Transparent dynamic pricing for Solo and Shared Pool rides from airport terminals.
- **Share Rides with Other Passengers**: Smart carpooling with a strict N-1 passenger capacity rule to ensure passenger comfort.
- **Track Rides & Manage Bookings**: End-to-end booking journey, pickup pillar navigation, flight delay protection, and trip history.
- **Driver Management & Verification**: Multi-step driver onboarding, regulatory compliance verification (DL, RC, Insurance, PUC), and earnings ledger.
- **Administration & Payments**: Platform operations dashboard, payment breakdown (10% advance fare, driver payout, cancellation refund policy), and dispute resolution.

## Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose & Material 3
- **Navigation**: Navigation Compose
- **Architecture**: Single-Activity, Model-View-State with unidirectional data flow
- **Build System**: Gradle Kotlin DSL (AGP 8.7.2, Gradle 8.10.2)
- **IDE**: Android Studio (Target SDK 35, Min SDK 24, Java 17)
- **Libraries**: AndroidX Core, Lifecycle, Coroutines, Coil, Lottie, JUnit & Espresso

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/kesanapallibhavyasree/Ridemitra.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle dependencies.
4. Configure the required environment variables and services (ensure Gradle JDK is set to JDK 17 or 21 in Settings).
5. Build and run the application on an Android device or emulator.

## Contributing
Contributions are welcome! Fork the repository, create a new branch, make your changes, and submit a Pull Request.

## License
Refer to the repository's license for usage and distribution details.
