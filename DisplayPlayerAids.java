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

public class DisplayPlayerAids
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
		//System.out.println("Key Typed: " + c);
	}

	public void keyPressed(KeyEvent e) 
	{
		char c = e.getKeyChar();
		//System.out.println("Key Pressed: " + c);
		
		if (c == '1')
		{
			// If they hit X then exit the program
			System.exit(0);
//			System.out.println("y:"+KeyListenerDemo.y);
		}
		if (c == '2')
		{
			// If they hit the P key, show the previous image
			KeyListenerDemo.y = KeyListenerDemo.y +5;
			System.out.println("y:"+KeyListenerDemo.y);
		}
		if (c == '6')
		{
			// If they hit the N key, show the next image
			KeyListenerDemo.x = KeyListenerDemo.x -5;
			System.out.println("x:"+KeyListenerDemo.x);
		}
	}

	public void keyReleased(KeyEvent e) 
	{
		char c = e.getKeyChar();
		//System.out.println("Key Released: " + c);
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
	File file = new File("DisplayHelpers-error.log");        
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
            ClassLoader classLoader = DisplayPlayerAids.class.getClassLoader();

            // Make sure that the configuration file exists
            URL res = Objects.requireNonNull(classLoader.getResource(nameOfROM),
                "Can't find configuration file DisplayPlayerAids.properties");

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
            noMoreGraphics = prop.getProperty("NoMoreGraphics");
            noMoreGraphics = noMoreGraphics.replaceAll("\\p{C}", "");
            System.out.println(noMoreGraphics);
            // get the value for the title if there's more than one graphic (how to move between pictures and how to launch MAME)
            moreGraphics = prop.getProperty("MoreGraphics");
			moreGraphics = moreGraphics.replaceAll("\\p{C}", "");
            System.out.println(moreGraphics);
            // get the value for the Exit key, and change it to a value we can use
			// Right now this is hard coded as X
            exitKey = prop.getProperty("Exit");
			exitKey = exitKey.replace("KEYCODE_","");
			System.out.println(exitKey);
            // get the value for the Next key, and change it to a value we can use
			// Right now this is hard coded as N
            nextKey = prop.getProperty("Next");
			nextKey = nextKey.replace("KEYCODE","VK");
			System.out.println(nextKey);
            // get the value for the Previous key, and change it to a value we can use
			// Right now this is hard coded as P
            previousKey = prop.getProperty("Previous");
			previousKey = previousKey.replace("KEYCODE","VK");
			System.out.println(previousKey);
            // get the value for the Background, if there's no value set it as black
            backgroundColor = prop.getProperty("Background","Black");
			System.out.println(backgroundColor);
			// Write to the log file what ROM pictures we're looking for
			System.out.println("ROM Name: "+args[0]);


        } catch (IOException e) {
            e.printStackTrace();
        }
 
    new DisplayPlayerAids(args[0], filePath, noMoreGraphics, moreGraphics, exitKey, previousKey, nextKey, backgroundColor);

    }

public class ControlKeys {
    public static char exit = 'x';
    public static int b;
}
    public DisplayPlayerAids(final String nameOfROM, String filePath, String noMoreGraphics, String moreGraphics, String exitKey, String previousKey, String nextKey, String backgroundColor) throws IOException {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String file2get = filePath+nameOfROM;
		System.out.println(file2get+"\n");
		File folder = new File(filePath);
        // see if the directory exists, if not exit
			if(!folder.isDirectory()){
				System.out.println("FilePath "+filePath+" does not exist, exiting.");
				System.exit(0);
			}
			File[] listOfFiles = folder.listFiles();
		//See if there's more than one file that matches the pattern
		int numOfPictures = 0;
		System.out.println("searching for: "+nameOfROM);
		// Establish the array we're going to put the picture's file names into - max of 10 pictures: 0 to 9. 
		String[] arrayOfPictures = new String[9];
			for (File filelist : listOfFiles) {
				if (filelist.isFile()) {
					String check1 = filelist.getName();
					// This is the file we're currently checking
					System.out.println(check1);
					// The RegEx (?<=nameOfROM)(?:.gif|.jpg|.png|-\d.gif|-\d.jpg|-\d.png) will make sure the file name starts with the name of the ROM then check for pictures with no suffix and numbered suffixes matting -0 to -9, and will only match picture formats (.gif|.jpg|.png) and doesn't care if the ROM name is capalized or not in either the filename or the nameOfROM
					Pattern pattern = Pattern.compile("(?<="+nameOfROM+")(?:.bmp|.gif|.jpg|.png|-\\d.bmp|-\\d.gif|-\\d.jpg|-\\d.png)", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(check1);
					boolean matchFound = matcher.find();
					    if(matchFound) {
							// For diagnostic purposes write out the match we found, then add it to the array of pictures we can show and increment the array index
							System.out.println("Match found: "+check1);
							arrayOfPictures[numOfPictures] = check1;
							numOfPictures = numOfPictures + 1;
							} else {
							System.out.println("Match not found: "+check1);
						}
				}
			}
			if (numOfPictures == 0){
				File file = new File(file2get);
				//see if the file exists, if not exit
				if(!file.isFile()){
					JFrame frame;
					frame = new JFrame();
					//Get the image to be displayed
					//Get the screen dimensions so the image can be scaled up to it (nicely) to fit the screen size
//					GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
					// get rid of the title bar and make the frame full screen
					frame.setUndecorated(true);
					frame.setMaximizedBounds(env.getMaximumWindowBounds());
					frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);

					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

					ImageIcon imageIcon = new ImageIcon(new ImageIcon("controlpicts\\mame.gif").getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT));
					//label.setIcon(imageIcon);
					frame.add(new JLabel(imageIcon));
						
					//frame.addKeyListener(new MyKeyListener());
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//	getScaledImage(loading, screenSize.width, screenSize.height);
					// *** TO DO **** Switch from bufferedImage.getScaledInstance to g2.drawImage
					// Figure out the screen ratio vs the image ratio to see if the image is portrat and needs to be scaled appropriately
					//	float screenRatio = (float)screenSize.width/screenSize.height;
					//	float pictureRatio = (float)bufferedImage.getWidth(null)/bufferedImage.getHeight(null);

					frame.setVisible(true);
					int remainingTime = 2;
					long timeout = System.currentTimeMillis() + (remainingTime * 1000);
					try {
    Thread.sleep(1000);                 //1500 milliseconds is one second.
} catch(InterruptedException ex) {
    Thread.currentThread().interrupt();
}
					//exit after 2 seconds
					System.exit(0);
				}
			}
			// Get the first picture that matches the nameOfROM from the array
			File file = new File(filePath+arrayOfPictures[0]);
			System.out.println("reading: "+file);
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
   }
     public static Color stringToColor(final String value) {
		 System.out.println("Testing: "+value);
    if (value == null) {
	  System.out.println("Null, returning Black");
      return Color.black;
    }
    try {
      // get color by hex or octal value
	  System.out.println("Decoded: "+Color.decode(value));
      return Color.decode(value);
    } catch (NumberFormatException nfe) {
      // if we can't decode lets try to get it by name
      try {
        // try to get a color by name using reflection
        final Field f = Color.class.getField(value);
	  System.out.println("Reflected: "+f.get(null));
        return (Color) f.get(null);
      } catch (Exception ce) {
        // if we can't get any color return black
		System.out.println("Can't get any color, returning Black");
        return Color.black;
      }
    }
  }

}
