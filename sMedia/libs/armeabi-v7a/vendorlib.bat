cd C:\Users\Administrator
adb remount
adb push C:\Users\Administrator\Desktop\pushsystem\armeabi-v7a\lib\. /vendor/lib
adb shell chmod 755 vendor/lib/*
pause