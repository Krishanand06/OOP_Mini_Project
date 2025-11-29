@echo off
REM ============================================
REM Campus Cafe - Quick Run (No Recompile)
REM ============================================
REM Note: Assumes Java files have been compiled

echo Checking for MySQL connector...
if not exist "mysql-connector-j-8.0.33.jar" (
    echo ERROR: MySQL connector not found!
    echo Please ensure mysql-connector-j-8.0.33.jar is in this directory
    pause
    exit /b 1
)

echo Starting Campus Cafe Management System...
echo.

REM Run the application (assumes already compiled)
java -cp ".;mysql-connector-j-8.0.33.jar" Main

if errorlevel 1 (
    echo.
    echo ERROR: Application failed to start!
    echo Please check your database connection settings in DatabaseManager.java
    pause
)
