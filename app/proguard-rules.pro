# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


   -keepclassmembers,allowobfuscation class * {
      @com.google.gson.annotations.SerializedName <fields>;
    }

    -keepclassmembers class com.civilcam.domainLayer.** { <fields>; }
    -keep class com.civilcam.domainLayer.model.subscription.UserSubscriptionState
    -keep class * extends androidx.fragment.app.Fragment{}
    -keepnames class androidx.navigation.fragment.NavHostFragment

    -keepclassmembers class * implements java.io.Serializable {
            private static final java.io.ObjectStreamField[] serialPersistentFields;
            private void writeObject(java.io.ObjectOutputStream);
            private void readObject(java.io.ObjectInputStream);
            java.lang.Object writeReplace();
            java.lang.Object readResolve();
    }

        -keepnames class com.facebook.FacebookActivity
        -keepnames class com.facebook.CustomTabActivity

        -keep class com.google.googlesignin.** { *; }
        -keepnames class com.google.googlesignin.* { *; }

        -keep class com.google.android.gms.auth.** { *; }
        -keep class com.facebook.login.Login

        -keep class com.google.android.apps.authenticator.** {*;}

        -keep,allowoptimization class com.google.android.libraries.maps.** { *; }