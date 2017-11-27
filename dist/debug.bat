@echo off
set arg1=%~dp0
set arg2=%1
set arg3=%2
shift
shift
cd %arg1%
start jre\bin\java.exe -jar EasyKts.jar %arg2% %arg3% %*
