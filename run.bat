@echo off
echo.
echo ========================================
echo   Campus Cafe Management System
echo ========================================
echo.

REM Check if MySQL connector exists
if not exist "mysql-connector-j-8.0.33.jar" (
    echo ERROR: MySQL connector not found!
    echo Please ensure mysql-connector-j-8.0.33.jar is in this directory
    pause
    exit /b 1
)

echo [1/3] Checking src folder...
if not exist "src" (
    echo ERROR: src folder not found!
    pause
    exit /b 1
)

echo [2/3] Compiling Java files from src folder...
if not exist "src\*.java" (
    echo ERROR: No Java files found in src folder!
    pause
    exit /b 1
)

javac -cp ".;mysql-connector-j-8.0.33.jar" -d . src\*.java
if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the errors above.
    pause
    exit /b 1
)

echo [3/3] Compilation successful!
echo.
echo Starting Campus Cafe Management System...
echo.

java -cp ".;mysql-connector-j-8.0.33.jar" Main
if errorlevel 1 (
    echo.
    echo ERROR: Application failed to start!
    echo Please check your database connection settings in DatabaseManager.java
    pause
)

