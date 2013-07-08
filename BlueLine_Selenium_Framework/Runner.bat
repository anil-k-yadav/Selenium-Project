echo off
set str=JARs\jxl-2.6.jar;JARs\mail-1.3.3.jar;JARs\mailapi.jar;JARs\mysql-connector-java-5.1.2.jar;JARs\selenium-server-standalone-2.33.0.jar;JARs\smtp.jar
echo on
echo %Selenium_JARs%
java -cp bin;%str% DriverScript