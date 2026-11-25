# =====================================================================================================
# Macrivo - R8 / ProGuard rules (release)
# CHANGED: complete file. Used together with proguard-android-optimize.txt (see the Gradle block you
# already have: getDefaultProguardFile("proguard-android-optimize.txt") + "proguard-rules.pro").
#
# Principle: every rule below exists because the code does something R8 cannot see (reflection,
# runtime JSON <-> field-name mapping, runtime class lookup). Everything else (Compose, AndroidX,
# Hilt, Room, WorkManager, Firebase, AdMob, ML Kit, CameraX, Credential Manager, OkHttp, DataStore)
# ships its own consumer rules inside its AAR and is deliberately NOT repeated here.
# =====================================================================================================

# --- Crash reports / stack traces -----------------------------------------------------------------
# Keep file names + line numbers so Crashlytics / Play Console stack traces can be de-obfuscated with
# mapping.txt. Source file names are hidden behind a constant.
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

# --- Generic signatures + annotations -------------------------------------------------------------
# Gson (TypeToken<List<...>>), Retrofit (return types such as Response<FoodDetailResponseDto>) and
# kotlinx.serialization read generic signatures and annotations at runtime.
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

# =====================================================================================================
# 1. GSON  (Retrofit converter + Room TypeConverters + FoodLogEntityMapper)
# =====================================================================================================

# 1a. FatSecret API DTOs (Retrofit -> Gson). Gson maps JSON keys to FIELD NAMES (or @SerializedName).
# Obfuscated field names would silently turn every response into nulls/zeros. Fields and the
# constructors Gson/Unsafe fall back to are kept; the class names themselves may still be renamed.
-keepclassmembers class com.macrivo.app.data.remote.dto.fatsecret.** {
    <init>(...);
    <fields>;
}

# 1b. ServingsContainerDto uses @JsonAdapter(ServingsContainerDto.Deserializer::class). Gson creates
# the adapter with reflection (no-arg constructor), so R8 would otherwise see it as unused.
-keep class com.macrivo.app.data.remote.dto.fatsecret.detail.ServingsContainerDto$Deserializer {
    <init>();
}

# 1c. Types that are WRITTEN TO ROOM AS JSON by Gson (no @SerializedName, so the JSON keys are the
# Kotlin field names):
#   - MealIngredient (IngredientConverters, FoodLogEntityMapper -> ingredientsJson)
#   - NutritionInfo  (NutritionInfoConverter)
#   - ServingEntity  (ServingConverter, inside FoodDetailEntity)
# If these were renamed, JSON saved by an older build could not be read back (data loss for existing
# users on update) and JSON from release/debug builds would differ. Field names MUST stay stable.
-keepclassmembers class com.macrivo.app.domain.model.MealIngredient { <init>(...); <fields>; }
-keepclassmembers class com.macrivo.app.domain.model.NutritionInfo  { <init>(...); <fields>; }
-keepclassmembers class com.macrivo.app.data.local.entity.ServingEntity { <init>(...); <fields>; }

# 1d. Anonymous TypeToken subclasses (object : TypeToken<List<MealIngredient>>() {}) - R8 full mode
# would otherwise strip/rename the generic superclass signature Gson reads. (Newer Gson versions ship
# this too; it is harmless to keep it explicit.)
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# 1e. Fields annotated with @SerializedName anywhere (safety net for DTOs outside the package above).
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# =====================================================================================================
# 2. FIRESTORE  (toObject(Dto::class.java) and .set(dto))
# =====================================================================================================
# Firestore's CustomClassMapper maps document fields <-> Kotlin properties by NAME using reflection
# (no-arg constructor produced by the default values, fields, getters/setters). Renaming them would
# write/read different field names than the documents already stored in users' Firestore.
# Includes the nested domain types used inside the DTOs (NutritionTargets, NutritionProgress,
# NutritionInfo, MealIngredient).
-keepclassmembers class com.macrivo.app.data.remote.dto.firebase.** {
    <init>(...);
    <fields>;
    <methods>;
}
-keepclassmembers class com.macrivo.app.domain.model.NutritionTargets  { <init>(...); <fields>; <methods>; }
-keepclassmembers class com.macrivo.app.domain.model.NutritionProgress { <init>(...); <fields>; <methods>; }
-keepclassmembers class com.macrivo.app.domain.model.NutritionInfo     { <init>(...); <fields>; <methods>; }
-keepclassmembers class com.macrivo.app.domain.model.MealIngredient    { <init>(...); <fields>; <methods>; }

# =====================================================================================================
# 3. RETROFIT
# =====================================================================================================
# Retrofit reads the annotations (@GET/@POST/@Query/@Field) and the generic return types of
# FatSecretApiService / FatSecretAuthApiService through reflection/dynamic proxies. Retrofit 2.10+
# and 3.x ship these rules in the AAR; they are listed so older 2.9.x builds also work. Remove them
# only after confirming your Retrofit version (see "Retrofit version" in the notes).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# =====================================================================================================
# 4. NAVIGATION 3 + kotlinx.serialization  (core/navigation/Routes.kt)
# =====================================================================================================
# rememberNavBackStack(...) saves the back stack as polymorphic JSON keyed by the route CLASS NAME and
# recreates it after process death. The route classes must keep their names and the generated
# serializer members. (kotlinx.serialization's own consumer rules cover $serializer/Companion for
# normal classes; these routes are objects + a sealed interface, so they are kept explicitly.)
-keepnames class com.macrivo.app.core.navigation.Routes
-keepnames class com.macrivo.app.core.navigation.Routes$*
-keepclassmembers class com.macrivo.app.core.navigation.Routes$* {
    public static ** INSTANCE;
    public static ** Companion;
    public static ** serializer(...);
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}

# =====================================================================================================
# 5. LOG STRIPPING (requires proguard-android-optimize.txt)
# =====================================================================================================
# Removes Log.v / Log.d / Log.i calls from the release build (argument strings are removed too).
# Warnings and errors (Log.w / Log.e) are kept for diagnostics.
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# =====================================================================================================
# Intentionally NOT included (and why)
#  - Room / Hilt / Dagger / WorkManager / HiltWorker / Compose: consumer rules + generated code are
#    reachable by R8 without help. SyncWorker is created by HiltWorkerFactory (generated), the
#    WorkManagerInitializer is removed in the manifest.
#  - Firebase Auth/Firestore, Play Services Ads, UMP, ML Kit barcode, CameraX, Credential Manager /
#    Google ID: each ships consumer rules.
#  - Enums (Gender, ActivityLevel, Goal ...): stored by .name; R8 never changes the value returned by
#    name(), and the default Android rules keep values()/valueOf().
#  - No JNI/native methods in the app code, no Class.forName / manual reflection.
#  - No "-dontwarn" lines: add one only for a specific missing class reported in
#    app/build/outputs/mapping/release/missing_rules.txt, after checking why it is missing.
# =====================================================================================================
