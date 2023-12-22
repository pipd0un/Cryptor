@echo off

setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
set "PROJECT_DIR=!SCRIPT_DIR!"
set CRYPTOR_PATH=%PROJECT_DIR%\py\Cryptor.exe
set PYINSTALLER=%PROJECT_DIR%\pyinstaller.exe

cd /D %PROJECT_DIR%

call mvnw package

%PYINSTALLER% --noconfirm --onefile --windowed --icon "py/oregon.ico" --name "Cryptor-GUI" --add-data "py/Cryptor.exe;." --add-data "py/gui.py;." --add-data "py/main.py;." --add-data "py/oregon.ico;." --add-data "py/utils.py;." --add-data "py/cert;cert/" --add-data "py;py/" --add-data "py/cert/Halcom.pem;."  "py/main.py"

move dist\Cryptor-GUI.exe Cryptor-GUI.exe

rmdir /s /q dist
rmdir /s /q build
del *.spec

call mvnw clean


echo "Build and cleanup completed."
