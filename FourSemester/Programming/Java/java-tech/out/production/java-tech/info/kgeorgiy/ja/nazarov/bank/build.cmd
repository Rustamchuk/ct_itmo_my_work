@echo off
set CLASSPATH=..;.
javac -d .. -cp %CLASSPATH% internal/*.java Server.java Client.java BankWebServer.java