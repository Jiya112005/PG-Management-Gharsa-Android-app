# GharSa PG Management App - Implementation Plan

This plan outlines the implementation of the **GharSa PG Management App** in Jetpack Compose, strictly following the Material 3 design system, color palette, typography, and interactive behaviors defined in `DESIGN.md` and `code.html`.

## User Review Required

> [!IMPORTANT]
> - **Jetpack Compose & Material 3**: The app will be built using modern Jetpack Compose (`androidx.compose`) and Material 3 (`androidx.compose.material3`), implementing custom color roles (Deep Navy Primary `#00253e`, Vibrant Teal Secondary `#006a63`, Canvas Background `#f9f9ff`, etc.) as specified in the design doc.
> - **Feature Scope**: We will implement the Auth / Sign In screen (with role switcher for Resident vs Property Owner, outlined inputs, password visibility toggle, biometric prompt simulation, OTP login simulation) and transition to a fully functional Dashboard screen tailored to the active role.

## Open Questions

- None. The design guidelines and HTML prototype provide exact UI specifications, color hex codes, and interactions.

## Proposed Changes

### Core & Theme
#### [NEW] [Theme.kt](file:///D:/MAD%20Project/app/src/main/java/com/example/gharsa/ui/theme/Theme.kt)
- Define custom Material 3 color schemes (`LightColors`), typography, and shapes based on `DESIGN.md`.

### Data Models & ViewModel
#### [NEW] [AuthViewModel.kt](file:///D:/MAD%20Project/app/src/main/java/com/example/gharsa/viewmodel/AuthViewModel.kt)
- Manages authentication state (role: Resident/Manager, username/email, password, password visibility, login loading state, authentication success, toast messages, biometric/OTP dialog states).

### UI Screens & Components
#### [NEW] [LoginScreen.kt](file:///D:/MAD%20Project/app/src/main/java/com/example/gharsa/ui/screens/LoginScreen.kt)
- Implements the exact Sign In screen from `code.html`:
  - Top status bar & app header
  - GharSa logo mark with glowing teal gradient & version badge
  - Role switcher chip ("Resident / Tenant" vs "Property Owner")
  - Outlined email/phone input with verified status badge
  - Password input with visibility toggle, "Remember login", and "Forgot Password"
  - "Login to GharSa" button with loading/success micro-interaction
  - Alternative login options ("Login with OTP", "Fingerprint ID")
  - Resident community showcase card & 256-bit encryption security seal
  - Toast notification bar

#### [NEW] [DashboardScreen.kt](file:///D:/MAD%20Project/app/src/main/java/com/example/gharsa/ui/screens/DashboardScreen.kt)
- Implements the post-login dashboard for Resident or Property Owner:
  - **Resident View**: Room 304 details, rent payment status, maintenance request tracker, meal menu.
  - **Owner View**: Occupancy rate, total revenue collection, tenant directory, pending dues, FAB for quick actions.

#### [MODIFY] [MainActivity.kt](file:///D:/MAD%20Project/app/src/main/java/com/example/gharsa/MainActivity.kt)
- Wire up `AuthViewModel`, theme, and navigation between `LoginScreen` and `DashboardScreen`.

## Verification Plan

### Automated Tests
- Build verification via `gradle_build` (`app:assembleDebug`).

### Manual Verification
- Deploy and run app on emulator/device using `deploy` or observe Jetpack Compose preview / UI state.
