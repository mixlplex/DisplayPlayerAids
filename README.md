This (currently) requires JDK to run (I'm not a programmer by trade so this is a bit of a kludge, the upside is that it's not compiled code so you can see exactly what it's doing and all the stuff I have in there for future enhancements that aren't working right currently).

This utility is composed of several parts:
1) DisplayPlayerAids.bat - the batch file which starts all of this (it's called from your front end)
2) DisplayPlayerAids.java - A Java program that displays the graphics for which elements of your control panel are used in the game and for what purpose 
3) RotateMagStik.java - A Java program that will display how to switch the MagStik Plus to 4-way for games that need it (and back again as the game exits)
4) 4-way-joysticks.txt - A list of games that use a 4 way joystick (instead of 8 way or other), update this list as needed to match the games you play
5) DisplayPlayerAids.properties - A configuration file for the name of the pictures directory, etc. Right now not all features are implemented but are included for future enhancements.
6) The control panel pictures - There is a directory that contains each of the pictures for the how the control panel is used along with a PowerPoint file that's used to generate the pictures.

All of these are in the DisplayPlayerAids directory, which should be located in your Front End's main directory.

1) Batch file:
This is called from within your Front End with the two parameters: "Filename Of the Game" "Path to the directory that the DisplayPlayerAids directory sits in"

How you do this will vary based on the Front End. For RetroFe edit the mame.conf file (found in the launchers directory) so it reads:
executable = %RETROFE_PATH%\DisplayPlayerAids\DisplayPlayerAids.bat
arguments = "%ITEM_NAME%" "%RETROFE_PATH%"

For example, if you're running RetroFE from "C:\My Arcade Games\" check the log file after trying to launch a program and you should see a call to: "C:\My Arcade Games\DisplayPlayerAids\DisplayPlayerAids.bat" "bublboblr" "C:\My Arcade Games"

The batch file will check to see if you held down a key while launching (future feature not yet implemented - mainly because it wasn't reliable) then launch the Java program to display the graphic for the game you're launching. Once that's been displayed it will check a file called "4-way-joysticks.txt" which contains a list a number of games that use a 4 way joystick instead of an 8-way joystick. If the game you're launching matches one of those it will display a graphic of how to rotate the MagStick Plus. If you don't need/want to worry about doing anything with a 4 way joystick you can delete all the contents of this file (but the file still needs to exist or the batch file will throw an error). If you want to do something other than display a graphic (such as to have the servos fire to rotate the joystick for you, change the following lines:
java RotateMagStik.java Rotate4way.png
java RotateMagStik.java Rotate8way.png

To run whatever you want to change your joystick to 4 way and back to 8 way.

You may also need to update the batch file with the path to the Mame emulator, by default it's set to:
cd "%arg2%\emulators\mame\"

2) DisplayPlayerAids.java:
This will scan the Control Panel Picts directory for a file with the same name as the game you're playing and then display it until you hit the exit key (which is currently hard coded - I hope to one day get this to read in from the configuration file)

3) RotateMagStik.java:
This will look in the Control Panel Picts directory for a file with the same name as what was passed to it (either Rotate4way.png or Rotate8way.png based on the batch file) then display it until you hit the exit key (which is currently hard coded to 6 - I hope to one day get this to read in from the configuration file)
