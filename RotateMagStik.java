package us.mixlplex.util;
// ************ TO DO *************** - Display a guide/FAQ that's text in addition to the graphic for what controls are what. Must be scrollable. 
//Config File sample:
//Note: No more than 10 pictures per ROM (titled -0 to -9), don't use extension just pass the name of the ROM, and use a Hex code for the colors, such as #FFFFFF for White or #000000 for Black. For the titles any non printable characters will be stripped (including tabs).
//FilePath = controlpicts
//NoMoreGraphics = Press the A button to exit
//MoreGraphics = Press the A button to exit, press B to display next graphic, press C to display previous graphic
//Exit = KEYCODE_A
//Next = KEYCODE_B
//Previous = KEYCODE_C
//Background = #FFFFFF

import java.awt.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import javax.imageio.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class RotateMagStik
{
	
	// added
public class KeyListenerDemo extends Frame
{
	public static int x = 0;
	public static int y = 0;
	
	public void main(String[] a) 
	{   
		KeyListenerDemo test = new KeyListenerDemo();
	}
	KeyListenerDemo()
	{		
		this.setAlwaysOnTop(true);
	    this.addKeyListener(new MyKeyListener());
	}
}

class MyKeyListener implements KeyListener 
{
	public void keyTyped(KeyEvent e) 
	{
		char c = e.getKeyChar();
	}

	public void keyPressed(KeyEvent e) 
	{
		char c = e.getKeyChar();
		
		if (c == '6')
		{
			// If they hit 6 (two player start) then exit the program
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) 
	{
		char c = e.getKeyChar();
	}
}
// ended
  public static void main(String[] args) throws Exception
  {
	Properties prop = new Properties();
	// Set default values
	String filePath = "";
	String noMoreGraphics = "";
	String moreGraphics = "";
	String exitKey = "";
	String nextKey = "";
	String previousKey = "";
	String backgroundColor = "";
	// Establish log file in case things go wrong
	File file = new File("RotateMagStick-error.log");        
    PrintStream printStreamToFile = new PrintStream(file);
    // *** TO DO **** uncomment this line to have error logged to a file instead of to the console
	System.setOut(printStreamToFile);
	// Display currrent date/time in the error log so that we know when the error occurred
	Date logDate = new Date();
	System.out.println(logDate);
	//Let's try reading the config file
        try {
            // the configuration file name
            String nameOfROM = "DisplayPlayerAids.properties";
            ClassLoader classLoader = RotateMagStik.class.getClassLoader();

            // Make sure that the configuration file exists
            URL res = Objects.requireNonNull(classLoader.getResource(nameOfROM),
                "Can't find configuration file RotateMagStik.properties");

            InputStream is = new FileInputStream(res.getFile());

            // load the properties file
            prop.load(is);
			filePath=prop.getProperty("FilePath");
			String slashChar = Character.toString((char)92);
            // get the value for the file path aka directory
			if (filePath.length() == 0) {
				//exit program since there's no path to determine where the pictures are located
				System.out.println("No FilePath found, exiting (did you remember to put "+slashChar+slashChar+" instead of "+slashChar+" for the path?)");
				System.exit(0);
			//There is a path (don't know if it's legit or not, but there's at least something) so see if it's in the right format for a directory path
			} else if (filePath.indexOf("..")!= -1) {
				// see if last character is a \ (this is needed so that we can ask for filePath\fileName 
				if (filePath.substring(filePath.length() - 1) == slashChar) {
					// It has normal paths, nothing to do
				}
				else {
					// it has a leading .. but not a trailing slash, so add one
					filePath = filePath + slashChar;
				}
			} else if (filePath.indexOf(slashChar) == -1) {
				// it has no leading .. and has no trailing slash, so add one
					filePath = filePath + slashChar;
				}
            // get the value for the title (the instructions on how exit and launch MAME) if there's only one graphic
			System.out.println(prop.getProperty("FilePath")+" provided as the file path, looking in: "+filePath);
            exitKey = prop.getProperty("2Player");
			exitKey = exitKey.replace("KEYCODE_","");
			System.out.println("Exit key:"+exitKey);
            // get the value for the Background, if there's no value set it as black
            backgroundColor = prop.getProperty("Background","Black");
			System.out.println("Background: "+backgroundColor);
			// Write to the log file what ROM pictures we're looking for


        } catch (IOException e) {
            e.printStackTrace();
        }
 
    new RotateMagStik(args[0], filePath, noMoreGraphics, moreGraphics, exitKey, previousKey, nextKey, backgroundColor);

    }

public class ControlKeys {
    public static char exit = 'x';
    public static int b;
}

    public RotateMagStik(final String nameOfROM, String filePath, String noMoreGraphics, String moreGraphics, String exitKey, String previousKey, String nextKey, String backgroundColor) throws IOException {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String file2get = filePath+nameOfROM;

		File folder = new File(filePath);
        // see if the directory exists, if not exit
			if(!folder.isDirectory()){
				System.out.println("FilePath "+filePath+" does not exist, exiting.");
				System.exit(0);
			}
//			File[] listOfFiles = folder.listFiles();
		//See if there's more than one file that matches the pattern
		int numOfPictures = 0;
		//System.out.println("searching for: "+nameOfROM);
		// Establish the array we're going to put the picture's file names into - max of 10 pictures: 0 to 9. 
		String[] arrayOfPictures = new String[9];
							numOfPictures = numOfPictures + 1;
		File file = new File(file2get);
		if (file.isFile()) {
			BufferedImage bufferedImage = ImageIO.read(file);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			// *** TO DO **** Switch from bufferedImage.getScaledInstance to g2.drawImage
			// Figure out the screen ratio vs the image ratio to see if the image is portrat and needs to be scaled appropriately
			float screenRatio = (float)screenSize.width/screenSize.height;
			float pictureRatio = (float)bufferedImage.getWidth(null)/bufferedImage.getHeight(null);
			Image image = bufferedImage.getScaledInstance(screenSize.width, -1, Image.SCALE_SMOOTH);
			if (screenRatio > pictureRatio){
				// Portrait image, have the height fit the screen instead, make sure you remove a bit because the titlebar takes up space
	//			System.out.println("Scaling by height");
				image = bufferedImage.getScaledInstance(-1, screenSize.height-30, Image.SCALE_SMOOTH);
			}
			ImageIcon icon = new ImageIcon(image);
			JFrame frame;
			//Display controls in title because if the picture is portrait instead of landscape you have no place to put it; title is best.
			if (numOfPictures == 1){
				frame = new JFrame(noMoreGraphics);
			} else {
				frame = new JFrame(moreGraphics);
			}
			frame.getContentPane().setBackground(stringToColor(backgroundColor));
			frame.setLayout(new FlowLayout());
			frame.setMaximizedBounds(env.getMaximumWindowBounds());
			frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
			frame.addKeyListener(new MyKeyListener());
			
			//repaint();

			JLabel jLabel = new JLabel();
			jLabel.setIcon(icon);
			frame.add(jLabel);
			frame.setVisible(true);
			// ***TO DO*** read mame config or settings.conf to get player1 start sequence
			// ***TO DO*** exit program when exit key is pressed (instead of hard coding the X)
			// ***TO DO*** Move to next image if right is hit (and previous image is left is hit)
			// ***TO DO*** reade mame config or settings.conf to see what the joystick settings are and use those for navigation... error trapping if no joystick, use mouse XY for spinner or ?? for steering wheel, or ?? for flight stick, otherwise go for buttons? what about game pads?
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			System.out.println("file not found: "+file2get+"\n");
		}
   }
     public static Color stringToColor(final String value) {
    if (value == null) {
      return Color.black;
    }
    try {
      // get color by hex or octal value
      return Color.decode(value);
    } catch (NumberFormatException nfe) {
      // if we can't decode lets try to get it by name
      try {
        // try to get a color by name using reflection
        final Field f = Color.class.getField(value);
        return (Color) f.get(null);
      } catch (Exception ce) {
        // if we can't get any color return black
        return Color.black;
      }
    }
  }

}
