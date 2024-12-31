@echo off
set CURRENT_DIR=%CD%
echo Demarrage du serveur...
start java -jar "%CURRENT_DIR%\Server.jar"
timeout /t 5 /nobreak
echo Demarrage du client...
java -jar "%CURRENT_DIR%\Client.jar"