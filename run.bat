@echo off
REM build.bat — Compile and run the Merchandise Catalog app
REM Requires: Java 17+

set SRC_DIR=src\main\java
set OUT_DIR=out
set MAIN_CLASS=Main

echo ▸ Compiling...

REM Create output directory if it doesn't exist
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

REM Find and compile all Java files
for /r "%SRC_DIR%" %%f in (*.java) do (
    javac -d "%OUT_DIR%" "%%f"
    if errorlevel 1 exit /b 1
)

echo ▸ Done. Starting application...
echo.

java -cp "%OUT_DIR%" %MAIN_CLASS%