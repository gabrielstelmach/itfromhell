@ECHO OFF
REM ==================================================
REM List all transaction' entries.
REM The hook is called only when adding or updating the file
REM to validate its contents.
REM ==================================================
@SETLOCAL EnableExtensions EnableDelayedExpansion 

SET SVNRepository=%1
SET SVNTransaction=%2
SET hookClassPath=%SVNRepository%\hooks\

REM Loads the list of changes
FOR /F "delims=" %%g IN ('svnlook.exe changed %SVNRepository% -t %SVNTransaction%') DO (
  SET changePath=%%g
  SET actionFlag=!changePath:~0,1!
  SET filePath=!changePath:~4, 255!

  REM Adding...
  IF !actionFlag! == A (FOR /f "delims=" %%v IN ('java -cp %hookClassPath%JamesHookFat.jar net.itfromhell.howit.jameshook.precommit.PreCommitInterdiction net.itfromhell.howit.BadLanguageHook "%SVNRepository%" %SVNTransaction% !actionFlag! "!filePath!"') DO SET result=%%v)
  REM Updating...
  IF !actionFlag! == U (FOR /f "delims=" %%v IN ('java -cp %hookClassPath%JamesHookFat.jar net.itfromhell.howit.jameshook.precommit.PreCommitInterdiction net.itfromhell.howit.BadLanguageHook "%SVNRepository%" %SVNTransaction% !actionFlag! "!filePath!"') DO SET result=%%v)
  
  REM If the first char is 1, something got wrong
  SET resultCode=!result:~0,1!
  if !resultCode! == 1 (GOTO ERROR_EXIT)
)

REM Success exit
GOTO GET_OUT

REM Fail exit
:ERROR_EXIT
ECHO ============================================================== >&2
ECHO.>&2
ECHO Your commit is blocked since something in file contents is not >&2
ECHo allowed.                                                       >&2
ECHO File: !filePath! >&2
ECHO Reason: !result:~2,255! >&2
ECHO You should fix it to try again.                                >&2
ECHO.>&2
ECHO ============================================================== >&2
EXIT 1

:GET_OUT
REM No code must be printed allowing an eventual next script to execute