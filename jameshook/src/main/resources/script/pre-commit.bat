@ECHO OFF
REM ==================================================
REM Executes each pre-commit script in order
REM ==================================================
@SETLOCAL EnableExtensions EnableDelayedExpansion 

SET V_REPOSITORY=%1
SET V_TXN=%2
SET HOOKS_PATH=%V_REPOSITORY%\hooks\

REM Java coded hook to check filename
CALL %HOOKS_PATH%TickingClockHook.bat %V_REPOSITORY% %V_TXN%

REM Java coded hook to load file contents
CALL %HOOKS_PATH%BadLanguageHook.bat %V_REPOSITORY% %V_TXN%

REM When everything went fine, the flow exits with success code
EXIT 0