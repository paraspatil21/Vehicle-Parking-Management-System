@echo off
set "BIN_DIR=bin"
set "SRC_DIR=src"
set "LIB_DIR=lib"

if not exist %BIN_DIR% mkdir %BIN_DIR%

echo Compiling VPMS...
javac -d %BIN_DIR% -cp "%LIB_DIR%\*;%SRC_DIR%" %SRC_DIR%\com\vpms\Main.java %SRC_DIR%\com\vpms\view\*.java %SRC_DIR%\com\vpms\model\*.java %SRC_DIR%\com\vpms\dao\*.java %SRC_DIR%\com\vpms\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b
)

echo Running VPMS...
java -cp "%BIN_DIR%;%LIB_DIR%\*" com.vpms.Main
pause
