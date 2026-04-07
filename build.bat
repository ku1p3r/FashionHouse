@echo off
:: build.bat — Compile and run the Merchandise Catalog app
:: Requires: Java 17+

set SRC_DIR=src\main\java
set OUT_DIR=out
set MAIN_CLASS=Main

echo ^ Compiling...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

:: Collect all .java files and compile them
for /r "%SRC_DIR%" %%f in (*.java) do (
    set SOURCES=!SOURCES! "%%f"
)

:: Enable delayed expansion for SOURCES variable
setlocal enabledelayedexpansion
set SOURCES=
for /r "%SRC_DIR%" %%f in (*.java) do (
    set SOURCES=!SOURCES! "%%f"
)

if "!SOURCES!"=="" (
    echo X No .java files found in %SRC_DIR% 1>&2
    exit /b 1
)

javac -d "%OUT_DIR%" !SOURCES!

if %errorlevel% neq 0 (
    echo X Compilation failed.
    exit /b %errorlevel%
)

echo ^ Done. Starting application...
echo.

java -cp "%OUT_DIR%" "%MAIN_CLASS%"