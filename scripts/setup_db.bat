@echo off
set "DB_NAME=vpms_db"
set "DB_USER=root"
set "DB_PASS=@paraspatil7777777"
set "MYSQL_PATH=mysql"

echo Setting up VPMS Database...
%MYSQL_PATH% -u %DB_USER% "-p%DB_PASS%" -e "source db/schema.sql"

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to setup database. Please check MySQL path and credentials.
    pause
    exit /b
)

echo [SUCCESS] Database setup complete.
pause
