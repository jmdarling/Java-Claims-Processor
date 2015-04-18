
set CP=bin;lib/*;config
REM set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_25
"%JAVA_HOME%/bin/java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector %1
