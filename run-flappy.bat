@echo off
cd /d "%~dp0"
echo ==========================================
echo   Flappy Bird OpenGL - Primer Parcial
echo ==========================================
echo.
call mvn clean compile
if errorlevel 1 (
    echo.
    echo Error: no se pudo compilar el proyecto.
    pause
    exit /b 1
)
echo.
call mvn exec:exec "-DmainClass=com.graphics.AppFlappyBird"
if errorlevel 1 (
    echo.
    echo Error: no se pudo ejecutar el juego.
    pause
    exit /b 1
)
