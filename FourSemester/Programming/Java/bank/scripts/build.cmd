@echo off
call javac --class-path .. Server.java Client.java BankWebServer.java Person.java RemotePerson.java LocalPerson.java BasePerson.java BankTest.java
rem call rmic -d .. examples.rmi.RemoteAccount examples.rmi.RemoteBank
