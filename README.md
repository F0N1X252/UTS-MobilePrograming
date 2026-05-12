# MyTravel Android Application
# Anggota Kelompok 
- Anandito Daffa Prianto - 2212501536
- Dicky Wahyu Artanto - 241250057

MyTravel is a comprehensive travel booking and management application for Android. It allows users to browse available travel destinations, book tickets, manage their profile, and keep track of their e-tickets in a single place.

## 🚀 Features

### 1. User Authentication & Security
- **Secure Login & Registration**: Persistent user sessions using `SharedPreferences`.
- **Forgot Password**: Password recovery mechanism.
- **Biometric Authentication**: Integrated fingerprint and device lock verification for sensitive actions like password resets.

### 2. Dashboard & Navigation
- **Home Dashboard**: Overview of recent activity and quick access to booking.
- **Bottom Navigation**: Seamless switching between Home, My Tickets, and Explore sections.
- **Floating Action Button (FAB)**: Quick access to start a new booking from any main screen.

### 3. Travel Booking
- **Explore Destinations**: Browse a list of available travel destinations with details.
- **Booking Form**: Comprehensive form to capture passenger information and trip details.
- **Validation**: Real-time input validation to ensure all required fields are correctly filled.
- **E-Ticket Generation**: Instant summary of booked tickets with sharing and PDF download capabilities.

### 4. Personalization & Settings
- **Profile Management**: View and update personal information such as name, email, and phone number.
- **Multilingual Support**: Switch between **English** and **Indonesian** dynamically within the app.
- **Dynamic Theming**: Support for **Light** and **Dark Mode** with smooth transitions.
- **Settings Menu**: Centralized access to profile, language settings, and theme toggling.

### 5. Ticket Management
- **My Tickets**: View a categorized list of ordered and unpaid tickets.
- **Detailed Summaries**: Access detailed E-Ticket information for every booking.

## 🛠 Technical Stack

- **Language**: Java
- **UI Framework**: Android XML with Material Design 3 (M3) components.
- **Persistence**: `SharedPreferences` for user data and application settings.
- **Authentication**: Android Biometric Library for secure identity verification.
- **Architecture**: Fragment-based UI with a `BaseActivity` for shared functionality (Localization & Theming).

## 📁 Project Structure

```text
com.mytravel/
├── AvailableTravelFragment.java  # Destination browsing logic
├── BaseActivity.java              # Shared logic for Localization/Theming
├── DashboardActivity.java         # Main host activity with BottomNav
├── DashboardHomeFragment.java     # Dashboard landing page
├── ForgotPasswordActivity.java    # Biometric-secured password recovery
├── LocaleHelper.java             # Utility for dynamic language switching
├── LoginActivity.java             # User login management
├── MainActivity.java              # Entry point for the booking process
├── MyTicketsFragment.java         # User's ticket list management
├── ProfileActivity.java           # User profile update logic
├── RegisterActivity.java          # New user account creation
├── ResetPasswordActivity.java     # Password update implementation
├── ResultActivity.java            # E-Ticket summary and actions
├── TicketAdapter.java             # Adapter for ticket list RecyclerView
├── Travel.java                    # Data model for travel destinations
└── TravelAdapter.java             # Adapter for destination RecyclerView
```

## 🌍 Localization

The app currently supports:
- 🇺🇸 **English** (Default)
- 🇮🇩 **Indonesian**

## 🌙 Theming

MyTravel respects system settings for Dark Mode but also provides a manual toggle within the app settings for a customized user experience.
