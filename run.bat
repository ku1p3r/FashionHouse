@echo off
REM build.bat — Compile and run the Merchandise Catalog app
REM Requires: Java 17+

set SRC_DIR=src\main\java
set OUT_DIR=out
set MAIN_CLASS=Main

echo ▸ Compiling...

REM Create output directory if it doesn't exist
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

REM Collect all .java files into a temp list
dir /s /b "%SRC_DIR%\*.java" > sources.txt

REM Compile all files at once, with source root on classpath
javac -d "%OUT_DIR%" -cp "%SRC_DIR%" @sources.txt
if errorlevel 1 (
    del sources.txt
    exit /b 1
)

del sources.txt

echo ▸ Done. Starting application...
echo.

java -cp "%OUT_DIR%" %MAIN_CLASS%