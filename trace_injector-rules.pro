-injars 'C:\flowtrace\trace_injector\out\production\test'(**.class)
-outjars 'c:\flowtrace\trace_injector\out\proguard'

-verbose
-dontwarn
-dontshrink
-dontoptimize
-dontpreverify
-dontobfuscate

#-addconfigurationdebugging

#-dontinjectflowtraces

#-skipflowtraces !android/**

-keep class *.* {}

-flowtracesfilter !android/**, !java/**

