
set CP=bin;lib/*;config
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_25

echo ********** Sending Claims01 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim01.xml
pause;

echo ********** Sending Claims02 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim02.xml
pause;

echo ********** Sending Claims03 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim03.xml
pause;

echo ********** Sending Claims04 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim04.xml
pause;

echo ********** Sending Claims05 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim05.xml
pause;

echo ********** Sending Claims06 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim06.xml
pause;

echo ********** Sending Claims07 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim07.xml
pause;

echo ********** Sending Claims08 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim08.xml
pause;

echo ********** Sending Claims09 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim09.xml
pause;

echo ********** Sending Claims10 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim10.xml
pause;

echo ********** Sending Claims11 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim11.xml
pause;

echo ********** Sending Claims12 **********
"%JAVA_HOME%\bin\java" -cp %CP% utd.claimsProcessing.claimsInjector.ClaimsInjector claims/claim12.xml
pause;

echo ********** FINISHED **********

