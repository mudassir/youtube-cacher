# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/mudassir.chughtai/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:

# The following are commented out ProGuard code snippets found on
# StackOverflow. Leave them here in case they are needed

#-keepclassmembers class io.github.mudassir.youtubecacher.util.YoutubeScraper {
#    <methods>;
#}
#-keepclassmembers class io.github.mudassir.youtubecacher.util.YoutubeScraper {
#	public *;
#}
#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
#-keep public class io.github.mudassir.youtubecacher.util.YoutubeScraper
#-keep public class * implements io.github.mudassir.youtubecacher.util.YoutubeScraper
#-keepattributes *Annotation*
#-keepattributes JavascriptInterface
#-keepattributes *JavascriptInterface*
