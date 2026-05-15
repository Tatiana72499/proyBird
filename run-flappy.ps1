Set-Location $PSScriptRoot
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  Flappy Bird OpenGL - Primer Parcial" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

mvn clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Error: no se pudo compilar el proyecto." -ForegroundColor Red
    Read-Host "Presiona ENTER para cerrar"
    exit 1
}

Write-Host ""
mvn exec:exec "-DmainClass=com.graphics.AppFlappyBird"
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Error: no se pudo ejecutar el juego." -ForegroundColor Red
    Read-Host "Presiona ENTER para cerrar"
    exit 1
}
