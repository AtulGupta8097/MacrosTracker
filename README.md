# 🥗 MacrosTracker

A modern Android nutrition tracking app built with **Jetpack Compose**, **Clean Architecture**, and the **FatSecret API**. Search millions of foods, view full nutrition breakdowns, switch between servings, and log your meals — all with a responsive UI that adapts to phones, tablets, and desktops.

---

## ✨ Features

- 🔍 **Food Search** — Search the FatSecret database with debounced live queries and local caching
- 📊 **Full Nutrition Detail** — Complete nutrition panel including macros, vitamins, and minerals
- 🍽️ **Serving Switcher** — Switch between all available serving sizes (e.g. "1 cup", "100g", "1 scoop")
- ⚖️ **Quantity Scaling** — Tap any macro or calorie to set a target and auto-scale all nutrition proportionally
- 📝 **Food Logging** — Log meals with serving size, quantity, and timestamps; synced to Firebase
- 📱 **Responsive Layout** — Adaptive UI for mobile (bottom nav), tablet, and desktop (side nav rail)
- 🔐 **Authentication** — Firebase Auth with sign in / sign up flows
- 👤 **User Profile & Onboarding** — Set age, weight, height, gender, activity level, and goal
- 🎯 **Macro Goals** — Calculated daily macro targets based on your profile
- 💾 **Offline Caching** — Room database caches search results and food details for offline access

---

## 🏗️ Architecture

Clean Architecture with three layers:

```
presentation/          # Jetpack Compose UI + ViewModels
domain/                # Use cases, domain models, repository interfaces
data/                  # Repository implementations, Room, Retrofit, Firebase
```

### Key Patterns
- **Repository pattern** with two separate caches: `FoodItemDao` (search list) and `FoodDetailDao` (full detail)
- **`Result<T>`** for error handling throughout the data layer
- **Hilt** for dependency injection
- **StateFlow + collectAsStateWithLifecycle** for reactive UI state
- **Navigation3** for in-app navigation with animated transitions

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose, Material3 |
| Navigation | AndroidX Navigation3 |
| DI | Hilt |
| Networking | Retrofit + Gson |
| Local DB | Room |
| Auth & Sync | Firebase Auth + Firestore |
| Image Loading | — |
| Architecture | MVVM + Clean Architecture |

---

## 🌐 API

This app uses the **FatSecret Platform REST API v2**.

- `foods.search` — Search foods by name
- `food.get.v2` — Get full nutrition detail for a food by ID

Authentication uses OAuth 2.0 client credentials flow. The token is refreshed automatically via `FatSecretAuthenticator`.

> **Note:** FatSecret sometimes returns a single `serving` object instead of an array when a food has only one serving. This is handled via a custom Gson deserializer on `ServingsContainerDto`.

---

## 🗄️ Database Schema

| Table | Purpose |
|---|---|
| `food_items` | Lightweight search cache — flat columns, no JSON |
| `food_details` | Full nutrition cache — servings stored as JSON blob |
| `food_logs` | User meal log entries |

Database version: **3**  
`fallbackToDestructiveMigration()` is used — clearing app data or reinstalling resets the cache cleanly.

---

## 📦 Module Structure

```
app/
├── core/
│   ├── di/                    # AppModule, DatabaseModule, NetworkModule
│   ├── navigation/            # Routes
│   └── utils/                 # Resource sealed class
├── data/
│   ├── local/
│   │   ├── dao/               # FoodItemDao, FoodDetailDao, FoodLogDao
│   │   ├── database/          # MacrosTrackerDatabase
│   │   ├── entity/            # FoodItemEntity, FoodDetailEntity, FoodLogEntity
│   │   └── converter/         # Gson TypeConverters for Room
│   ├── mapper/                # DTO ↔ Domain ↔ Entity mappers
│   ├── remote/
│   │   ├── api/               # FatSecretApiService
│   │   ├── auth/              # OAuth interceptor, authenticator, token manager
│   │   └── dto/               # FatSecret + Firebase DTOs
│   └── repository/            # FoodRepositoryImpl, FoodLogRepositoryImpl, etc.
├── domain/
│   ├── model/                 # FoodItem, FoodDetail, Serving, NutritionInfo, FoodLog, ...
│   ├── repository/            # Repository interfaces
│   └── use_case/              # SearchFoodsUseCase, GetFoodDetailUseCase, LogFoodUseCase, ...
└── presentation/
    ├── food_browse/           # Search list + food detail screen
    ├── food_database_screen/  # Entry point for food browsing
    ├── main_screen/           # Scaffold, bottom nav, side nav, FAB
    ├── signin_screen/
    ├── signup_screen/
    ├── splash_screen/
    ├── user_setup_screens/    # Onboarding flow
    └── ui/theme/              # Color, Typography, Spacing, DeviceConfiguration
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17+
- A [FatSecret Platform](https://platform.fatsecret.com/) API account
- A Firebase project with Auth and Firestore enabled

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/yourusername/macrostracker.git
   cd macrostracker
   ```

2. **Add FatSecret credentials**

   In `local.properties`:
   ```properties
   FATSECRET_CLIENT_ID=your_client_id
   FATSECRET_CLIENT_SECRET=your_client_secret
   ```

3. **Add Firebase config**

   Download `google-services.json` from your Firebase console and place it in `app/`.

4. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

---

## 📐 Responsive Design

The app adapts its layout based on `DeviceConfiguration`:

| Device | Navigation | FAB |
|---|---|---|
| Mobile (portrait/landscape) | Bottom nav with cutout | Centered, half above nav bar |
| Tablet | Side nav rail | Button inside side nav |
| Desktop | Wide side nav | Button inside side nav |

Window insets are handled manually — the bottom nav `Surface` extends behind the system nav bar using a `Column` with a `navigationBarsPadding()` `Spacer` below the item row.

---
