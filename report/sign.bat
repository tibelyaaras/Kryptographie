"C:\Program Files\Java\jdk-17.0.2\bin\jarsigner" -keystore keystore.jks -storepass dhbw2022* build\libs\report-1.0-SNAPSHOT.jar server
"C:\Program Files\Java\jdk-17.0.2\bin\jarsigner" -verify -keystore keystore.jks -storepass dhbw2022* jar\report-1.0-SNAPSHOT.jar server
pause