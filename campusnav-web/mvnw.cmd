@echo off
rem Maven Wrapper script for CampusNav
setlocal enabledelayedexpansion

rem Check if mvn is directly available in PATH
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    mvn %*
    exit /b %ERRORLEVEL%
)

rem Check standard IntelliJ IDEA bundled maven locations
set "IDEA_MVN=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"
if exist "%IDEA_MVN%" (
    call "%IDEA_MVN%" %*
    exit /b %ERRORLEVEL%
)

rem Search for other IntelliJ versions if any
for /d %%D in ("C:\Program Files\JetBrains\IntelliJ IDEA*") do (
    if exist "%%D\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" (
        call "%%D\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" %*
        exit /b %ERRORLEVEL%
    )
)

echo [ERROR] Maven not found. Please install Maven or run within IntelliJ IDEA.
exit /b 1
