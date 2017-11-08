# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\SDK\AndroidToolKit\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#remove debugging Log API Calls.
-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5

-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

#As i said remove all library files from config.
-keep class com.facebook.** { *; }
-keep class com.androidquery.** { *; }
-keep class com.google.** { *; }
-keep class org.acra.** { *; }
-keep class org.apache.** { *; }
-keep class com.mobileapptracker.** { *; }
-keep class com.nostra13.** { *; }
-keep class net.simonvt.** { *; }
-keep class android.support.** { *; }
-keep class com.nnacres.app.model.** { *; }


# Suppress warnings if you are NOT using IAP:
 -dontwarn com.nnacres.app.**
 -dontwarn com.androidquery.**
 -dontwarn com.google.**
 -dontwarn org.acra.**
 -dontwarn org.apache.**
 -dontwarn com.mobileapptracker.**
 -dontwarn com.nostra13.**
 -dontwarn net.simonvt.**
 -dontwarn android.support.**


-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


# The official support library.
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

#  Library JARs.
-keep class de.greenrobot.dao.** { *; }
-keep interface de.greenrobot.dao.** { *; }

# Library projects.
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }

# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn com.fasterxml.**

# Okhttp
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Firebase
-keep class com.firebase.** { *; }
-keep class org.shaded.apache.** { *; }
-keepnames class com.shaded.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**