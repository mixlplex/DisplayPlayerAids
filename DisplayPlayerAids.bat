@Echo off
REM Arg1 = ROM Name, Arg2 = Path to RetroFe
set arg1=%1
set arg2=%2
set launched=No
Goto :Choice[1-9]

:Launch
REM	Echo. Display the control panel picture (the player aid) for %arg1%
	java DisplayPlayerAids.java %arg1% 
	set launched=Yes
:Choice[1-9]
REM	Echo %Choice% 
	if /i %launched%==No (goto :Launch) else (Echo tried to launch with %launched%)
REM See if this game uses a 4-way joystick
findstr /x %arg1% 4-way-joysticks.txt > nul
set rotate=%ERRORLEVEL%
REM if %rotate% is 0 then Findstr did find a match; therefore this is game requires a 4 way joystick so show the graphic to roate it
if %rotate% EQU 0 java RotateMagStik.java Rotate4way.png
REM Run the game
REM Change drive here if necessary
C:
REM CD to correct location for emulator
cd "%arg2%\emulators\mame\"
REM Run mame with game
mame.exe %arg1% 
REM Change back to Display Player Aids directory to display graphic to rotate joystick back ... if needed
REM You may need to change the drive path back if DisplayPlayerAids is no longer a subfolder of RetroFe
cd "%arg2%\DisplayPlayerAids\"
REM if %rotate% is 0 then Findstr did find a match; we're done with the game so show the graphic to roate it back
if %rotate% EQU 0 (java RotateMagStik.java Rotate8way.png)
REM Exit back to RetroFe
	exit
REM	exit /B 0