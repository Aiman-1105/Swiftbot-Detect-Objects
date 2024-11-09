import swiftbot.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Random;

/*
 * DetectObjects class implements the main functionality for SwiftBot,
 * including modes like Curious Mode, Scaredy Mode, and Dubious Mode.
 * It also allows the user to interact with the SwiftBot.
 */
public class DetectObjects {
	static SwiftBotAPI swiftBot;
	
	// To read from System.in
	private static Scanner reader = new Scanner(System.in); 
	
	// Counter for images captured
	private static int imageCounter = 1;
	
	// Mode for Execution Log
	private static String Mode;
		
	// For calculating duration
	private static long curiousStartTime;
	private static long scaredyStartTime;
	
	// Constants for buffer zone
	private static int bufferDistance;
	private static int bufferZoneStart;
	
	// Constants for time durations in milliseconds
	private static final int pauseOneSec = 1000;
	private static final int pauseTwoSec = 2000;
	private static final int pauseThreeSec = 3000;
	private static final int pauseFiveSec = 5000;
    private static final int objectEncounterDistance = 100;
    private static final int reverseOneSecond = 1000;
	private static final int blinkDuration = 500;
	
	// Millisecond to second conversion constant
	private static final int divideforSecond = 1000;
	
	// Time it takes to turn n degrees
	private static final int turn45degDuration = 650;
	private static final int turn180degDuration = 2850;
	
	// Constants for speed
	private static final int lowSpeed = 35;
    private static final int reverseSpeed = -35;
    private static final int changeDirectionSpeed = 100;
    
	// ASNI escape codes to change color of UI text
	public static final String Gold = "\u001B[33m"; 
    public static final String Reset = "\u001B[0m";
    public static final String Cyan = "\u001B[36m";
    public static final String Red = "\u001B[31m";
    public static final String Green = "\u001B[32m";
    public static final String Purple = "\u001B[35m";
    
    // Main Program starts
	public static void main(String[] args) throws InterruptedException {
		try {
            swiftBot = new SwiftBotAPI();
        } 
        catch (Exception e) 
        {
            /*
             * Outputs a warning if I2C is disabled. This only needs to be turned on once,
             * so you won't need to worry about this problem again!
             */
            System.out.println("\nI2C disabled!");
            System.out.println("Run the following command:");
            System.out.println("sudo raspi-config nonint do_i2c 0\n");
            System.exit(5);
        }
        
        // Creates a Menu for User Interaction
        System.out.println(Gold + "========================================================================" + Reset);
        System.out.println(Gold + "========================================================================" + Reset);
        System.out.println(Gold + "=" + Reset + Purple + "                             SWIFTBOT MENU                            " + Cyan + Reset + Gold + "=");
       	System.out.println(Gold + "=" + Reset + Purple + "                         Task 5: DETECT OBJECTS                       " + Cyan + Reset + Gold + "=");
       	System.out.println(Gold + "========================================================================" + Reset);

        // Loops main menu after each user interaction
       	while (true) {
       	    // Menu Options
       		System.out.println(Gold + "========================================================================" + Reset);
       		System.out.println(Gold + "1. " + Reset + Cyan + "Press 'Q' to begin scanning." + Reset + Gold + "                                        =" + Reset);
       	    System.out.println(Gold + "2. " + Reset + Cyan + "Press 'X' to terminate program." + Reset + Gold + "                                     =" + Reset);
       	    System.out.println(Gold + "========================================================================" + Reset);
       	    System.out.println(Gold +">> " + Reset + Cyan + "Please Enter a key:" + Reset + Gold + "                                                 =" + Reset);

       	    // Reads user input and performs corresponding action
       	    String ans = reader.next();
       	    switch (ans) {
       	        // Q to scan QR Code
       	        case "Q" : 
       	            System.out.println("Initializing...");
       	            System.out.println();
       	            System.out.println("Initialized successfully!");
       	            System.out.println();
       	            scanQR();
       	            break;
       	        
       	     case "q" : 
    	            System.out.println("Initializing...");
    	            System.out.println();
    	            System.out.println("Initialized successfully!");
    	            System.out.println();
    	            scanQR();
    	            break;
    	            
       	        case "X" : 
       	        	// To terminate the program
       	        	System.out.println(Red + "Terminating program...");
	                System.out.println("Program successfully terminated!" + Reset);
	                System.out.println(Gold + "Thank you for utilizing this program! Goodbye." + Reset);
       	            reader.close();
       	            System.exit(0);   	            
       	        	break;
       	        	
       	     case "x" : 
    	        	// To terminate the program
    	        	System.out.println(Red + "Terminating program...");
	                System.out.println("Program successfully terminated!" + Reset);
	                System.out.println(Gold + "Thank you for utilizing this program! Goodbye." + Reset);
    	            reader.close();
    	            System.exit(0);   	            
    	        	break;

       	        // Error Handling
       	        default : 
       	            System.out.println(Red + "ERROR: Please enter a valid key." + Reset);
       	        
       	    }
       	}

	}

	public static void scanQR() {
	    // Scans the qrCode, decodes it and runs the appropriate mode
		try {
	        System.out.println(Purple + "Taking a capture in 3 seconds..." + Reset);
	        Thread.sleep(pauseThreeSec);

	        BufferedImage img = swiftBot.getQRImage();
	        String decodedMessage = swiftBot.decodeQRImage(img);

	        if (decodedMessage.isEmpty()) {
	            System.out.println(Red + "No QR Code was found. Try adjusting the distance of the SwiftBot's Camera from the QR code, or try another." + Reset);
	        	} 
	        
	        else {
	        	System.out.println();
	        	System.out.println(Purple + "SUCCESS: QR code found");
	        	System.out.println();
	            System.out.println("Decoded message: " + Reset + Green + decodedMessage + Reset);
	            switch (decodedMessage.toLowerCase()) {
	                case "curious swiftbot" :
	                    System.out.println(Purple + "Processing QR Code...");  // Process the QR code
	                    Thread.sleep(pauseThreeSec); // Waits for 3 seconds
	                    System.out.println("QR Code processing complete." + Reset);
	                    // Additional Functionality     	                    
	                    System.out.println(Cyan + "Enter the distance of the Buffer Zone (in cm)" + Reset);
	                    bufferDistance = reader.nextInt();
	                    System.out.println(Cyan + "Enter the distance of detecting the Buffer Zone (in cm)" + Reset);
	                    bufferZoneStart = reader.nextInt();
	                    curiousMode(bufferDistance, bufferZoneStart);
	                    break;
	                
	                case "scaredy swiftbot" : 
	                    System.out.println(Purple + "Processing QR Code...");  // Process the QR code
	                    Thread.sleep(pauseThreeSec); // Waits for 3 seconds
	                    System.out.println("QR Code processing complete." + Reset);	              
	                    scaredyMode();
	                    break;
	                
	                case "dubious swiftbot" : 
	                    System.out.println(Purple + "Processing QR Code...");  // Process the QR code
	                    Thread.sleep(pauseThreeSec); // Waits for 3 seconds
	                    System.out.println("QR Code processing complete." + Reset);	              
	                    dubiousMode();
	                    break;
	                
	                default : 
	                    System.out.println(Red + "ERROR: Unknown mode detected." + Reset);
	                    System.exit(5);
	                
	            }
	        }
	    } 
	    
		catch (Exception e) { // Handles exceptions
	    	System.out.println(Red + "ERROR: QR Scan failed! QR code is not valid. Please try again"  + Reset);
	        e.printStackTrace();
	        System.exit(5);
	    }
	}

	
	public static void curiousMode(int bufferDistance, int bufferZoneStart) { // Passes bufferDistance and bufferZoneStart into curiousMode()
		System.out.println(Green + "Curious SwiftBot Detected.\n");
		
		Mode = "Curious Swiftbot"; // Updates the mode for execution logs
		
		curiousStartTime = System.currentTimeMillis(); // Records the start time
        
		System.out.println("Activating Curious Mode...");
		
		System.out.println();
		
        System.out.println("Curious Mode Activated!" + Reset);
        
        try {
        	buttonX(); // To close the program when SwiftBot is in Curious Mode
        }
        
        catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Red + "Button X is not functioning!" + Reset);
        }
		
        while (true) { 
	        try {
	            wanderingMode();
	            Thread.sleep(pauseFiveSec);
	            noEncounter(bufferDistance, bufferZoneStart);
	        } 
	        catch (InterruptedException e) {
	           e.printStackTrace();
	           System.out.println(Red + "Curious Mode Failed!" + Reset);
	           break;
	        }
	    }
	}

	public static void scaredyMode() { 
	    System.out.println(Red + "Scaredy SwiftBot Detected.");
	    
	    Mode = "Scaredy Swiftbot"; // Updates the mode for execution logs
	    
	    scaredyStartTime = System.currentTimeMillis(); // Records the start time
	    
	    System.out.println("Activating Scaredy Mode...");
	    
	    System.out.println();
	    
	    System.out.println("Scaredy Mode Activated!" + Reset);
	    
	    try {
	        swiftBot.startMove(lowSpeed, lowSpeed);
	        buttonX(); // To close the program when SwiftBot is in Scaredy Mode

	        while (true) {
	            double distance = swiftBot.useUltrasound();
	            System.out.println("Current distance is " + distance);
	            if (distance > objectEncounterDistance) { // If the object is beyond 1 m
	               blueUL(); // Sets the Underlights to Blue
	               swiftBot.startMove(lowSpeed, lowSpeed);
	            } 
	            else {
	            	redUL(); // Sets the Underlights to Red 
	            	swiftBot.stopMove();
	                Thread.sleep(pauseOneSec); // SwiftBot freezes due to fear for 1 second
	                System.out.println(Red + "OBJECT ENCOUNTERED!" + Reset);
	                captureImage();
	                blinkRed(); // Underlights start blinking
	                // Moves in reverse for 1 second
	                swiftBot.startMove(reverseSpeed, reverseSpeed);
	                redUL(); 
	                Thread.sleep(reverseOneSecond);
	                swiftBot.stopMove();
	                blueUL();
	                // SwiftBot does a 180 degree
	                swiftBot.startMove(0, lowSpeed);
	                Thread.sleep(turn180degDuration);
	                swiftBot.stopMove();
	                // Move in the other direction for 3 seconds
	                swiftBot.startMove(lowSpeed, lowSpeed);
	                Thread.sleep(reverseOneSecond);
	                swiftBot.stopMove();
	                System.out.println();
	                System.out.println(Gold + "Object avoided.." + Reset);
	            }
	            Thread.sleep(pauseTwoSec); // Small delay to reduce resource usage
	        }
	    } 
	    
	    catch (Exception e) {
	        e.printStackTrace(); // Error Handling
	        System.out.println(Red + "Scaredy Mode Failed!" + Reset);
	    }
	    
	}

	public static void dubiousMode() { // Dubious Mode randomizes between Curious Mode and Scaredy Mode and will run that specific mode
	    System.out.println("Dubious SwiftBot Detected.");
	    System.out.println();
	    Mode = "Dubious Swiftbot"; // Updates the mode for execution logs
	    System.out.println(Gold + "Activating Dubious Mode...");
	    System.out.println();
	    System.out.println("Dubious Mode Activated!" + Reset);
	    System.out.println();

	    try {
	        // Generate a random number and assign it to different modes
	        int randomMode = new Random().nextInt(2);

	        if (randomMode == 0) {
	            curiousMode(bufferDistance, bufferZoneStart);
	        } else {
	            scaredyMode();
	        }
	    } 
	    
	    catch (Exception e) {
	        e.printStackTrace(); // Error Handling
	        System.out.println(Red + "Dubious Mode Failed!" + Reset);
	    }
	}
	
	public static void wanderingMode() {
	    try {
	        double distance;
	        
	        swiftBot.startMove(lowSpeed, lowSpeed); // SwiftBot starts wandering at low speeds

	        while (true) {
	            distance = swiftBot.useUltrasound();
	            blueUL();

	            if (distance > bufferZoneStart) {
	                System.out.println(Red + "No object detected. Currently wandering..." + Reset);
	                System.out.println();
	                // Continue wandering
	            } 
	            else {
	                System.out.println(Green + ">> Object detected! Distance: " + distance + Reset);
	                System.out.println();
	                bufferZone(bufferDistance,bufferZoneStart);
	                break; 
	            }
	        }

	        // Stop SwiftBot after bufferZone
	        swiftBot.stopMove();
	    } 
	    catch (Exception e) { // Error Handling
	    	System.out.println(Red + "An error occurred in wandering mode:" + Reset);
	        e.printStackTrace(); 
	    }
	}
	
	public static void bufferZone(int bufferDistance, int bufferZoneStart) {
		boolean bufferZoneReached = false;
		
		while (!bufferZoneReached) {
			try {
				double distance = swiftBot.useUltrasound();
				
				if (distance == bufferDistance) {
					swiftBot.stopMove();
					System.out.println(Green + "Current Distance between the object and SwiftBot is " + distance + "." + "\n The SwiftBot has reached the buffer zone!" + Reset);
					System.out.println();
					blinkGreen();
					captureImage();
					bufferZoneReached = true; //while loop stops
				}
				
				else if (distance > bufferDistance && distance < bufferZoneStart) {
					System.out.println("Object Detected!");
					System.out.println();
					System.out.println("The current distance is " + distance + "." + "\n Accelerating to reach the buffer zone." );
					System.out.println();
					swiftBot.startMove(lowSpeed, lowSpeed);
					blueUL();
				}
				
				else if (distance < bufferDistance) {
					System.out.println();
					System.out.println("The current distance between the object and SwiftBot is " + distance + "." + "\n Reversing to reach the buffer zone." );
					swiftBot.startMove(reverseSpeed, reverseSpeed);
					greenUL();
				}
	        } 
			
			catch (Exception e) { // Error Handling
	            e.printStackTrace();
	            System.out.println(Red + "Error occured in Buffer Zone: " + Reset); 
	        }
	    }
	}
	
	public static void captureImage() { // Captures Image and saves it to the directory
        
	 	try {
	 		
	 		System.out.println();
	 		
            System.out.println(Purple + "Capturing image of the object.." + Reset);
            
            Thread.sleep(pauseOneSec);
            
            BufferedImage image = swiftBot.takeStill(ImageSize.SQUARE_720x720);
            ImageIO.write(image, "jpg", new File("/home/pi/Documents/img" + imageCounter + ".jpg")); //save file using image counter
            
            System.out.println(Green + "Success!" + Reset);
            
            System.out.println(Gold + "Please transfer the photo from the SwiftBot using WinSCP." + Reset);
            
            Thread.sleep(pauseTwoSec); // Improves efficiency by reducing CPU usage
            
            imageCounter++; // Increment the counter for the next image
        } 
	 	
	 	catch (Exception e) {
            System.out.println(Red + "\nCamera not enabled!");
            System.out.println("Try running the following command: ");
            System.out.println("sudo raspi-config nonint do_camera 0\n");
            System.out.println("Then reboot using the following command: ");
            System.out.println("sudo reboot\n" + Reset);
            System.exit(5);
        }
    }
	
	public static void noEncounter(int bufferDistance, int bufferZoneStart) { //Code for action to be performed when No object is encountered.
		 
		 try {
			 
			 System.out.println();
			 
			 System.out.println(Red + "No object encountered for 5 seconds." + Reset + Gold + "Changing direction...");
			 
			 Thread.sleep(pauseOneSec);
			 swiftBot.startMove(changeDirectionSpeed, 0);
			 Thread.sleep(turn45degDuration);
			 swiftBot.stopMove();
			 
			 System.out.println();
			 
			 System.out.println("Direction changed!" + Reset);
			 
			 wanderingMode(); // Resumes wandering after changing direction
		 }
		 
		 catch (InterruptedException e) { // Error handling
			 e.printStackTrace();
			 System.out.println(Red + "Error encountered whilst changing direction." + Reset);
		 }
	 }
	
	public static void redUL() {
        int[] colourToLightUp = {255, 0, 0}; // RBG values for Red
        try {
            swiftBot.fillUnderlights(colourToLightUp);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Error Handling
            System.out.println(Red + "Red failed to light up" + Reset);
        }
    }
	
	public static void blinkRed() { // Code snippet for Underlights blinking red
	    try {
	    	
	    	long blinkstartTime = System.currentTimeMillis();
	    	
	        while (System.currentTimeMillis() - blinkstartTime < 5000) { //Blinks for 5 seconds.
	            redUL();
	            Thread.sleep(blinkDuration);
	            swiftBot.disableUnderlights();
	            Thread.sleep(blinkDuration);
	        }
	        swiftBot.disableUnderlights();
	    } 
	    
	    catch (InterruptedException | IllegalArgumentException e) {
	        e.printStackTrace(); // Error Handling
	        System.out.println("Exception occurred.");
	    	}
	}
	
	public static void blueUL() { // Underlights set to Blue
        int[] colourToLightUp = {0, 255, 0}; // RBG values for Blue
        
        try {
            swiftBot.fillUnderlights(colourToLightUp);
        	} 
        
        catch (IllegalArgumentException e) {
            e.printStackTrace(); // Error Handling
            System.out.println(Red + "Blue failed to light up" + Reset);
        	}
    }
	
	public static void greenUL() { // Underlights set to Green
        int[] colourToLightUp = {0, 0, 255}; // RBG values for Green
        
        try {
            swiftBot.fillUnderlights(colourToLightUp);
        	}
        
        catch (IllegalArgumentException e) {
            e.printStackTrace(); // Error Handling
            System.out.println(Red + "Green failed to light up" + Reset);
        	}
    	}
	
	public static void blinkGreen() { // Underlights start blinking green
	    try {
	    	long blinkstartTime = System.currentTimeMillis();
	    	
	        while (System.currentTimeMillis() - blinkstartTime < 5000) { //Blinks for 5 seconds.
	            greenUL();
	            Thread.sleep(blinkDuration);
	            swiftBot.disableUnderlights();
	            Thread.sleep(blinkDuration);
	        }
	        
	        swiftBot.disableUnderlights();
	    
	    	} 
	    
	    catch (InterruptedException | IllegalArgumentException e) {
	        e.printStackTrace(); // Error Handling
	        System.out.println(Red + "Exception occurred." + Reset);
	    	}
		}
	 
	 public static void executionLogs() {
		 	System.out.println(Gold + "========================================================================" + Reset);
	        System.out.println(Gold + "========================================================================" + Reset);
	        System.out.println(Gold + "=" + Reset + Purple + "                             EXECUTION LOGS                           " + Cyan + Reset + Gold + "=");
	        System.out.println(Gold + "========================================================================" + Reset);
	        System.out.println(Gold + "========================================================================" + Reset);
	        System.out.println(Gold + "1. " + Cyan + "Mode: " + Mode + Reset + Gold + "                                              =" + Reset);
		    duration();
		    System.out.println(Gold + "3. " + Reset + Cyan + "Objects Encountered: " + imageCounter + Reset + Gold + "                                              =" + Reset);
		    System.out.println(Gold + "4. " + Reset + Cyan + "Images Captured: " + imageCounter + Reset + Gold + "                                                  =" + Reset);
		    System.out.println(Gold + "========================================================================" + Reset);
	        System.out.println(Gold + "========================================================================" + Reset);
	 		}

	 
	 public static void duration() {
		    
		 	long endTimeMillis = System.currentTimeMillis();
		    
		 	if (Mode.equals("Curious Swiftbot")) {
			 	long durationMillis = endTimeMillis - curiousStartTime;
			    long seconds = durationMillis / divideforSecond;
			    System.out.println(Gold + "2. " + Reset + Cyan + "Duration: " + seconds + " seconds" + Reset + Gold + "                                                =" + Reset);
		 		}
		 	
		 	else if (Mode.equals("Scaredy Swiftbot")) {
		 		long durationMillis = endTimeMillis - scaredyStartTime;
			    long seconds = durationMillis / divideforSecond;
			    System.out.println(Gold + "2. " + Reset + Cyan + "Duration: " + seconds + " seconds" + Reset + Gold + "                                                =" + Reset);
		 	}
	 }
	 
	 public static void buttonX() throws InterruptedException {
		    try {
		        swiftBot.enableButton(Button.X, () -> { 
		        	swiftBot.disableUnderlights();
		        	swiftBot.stopMove();
		        	
		        	System.out.println("Do you want to view Execution logs?"); /*When Button X on SwiftBot is pressed, it prompts this to the user 
		        																*if they want to view execution log or not.
		        																*/
		            swiftBot.disableButton(Button.X);
		            swiftBot.enableButton(Button.Y, () -> { //If Button Y on SwiftBot is pressed, It will view execution logs 
		            	swiftBot.disableUnderlights();
		            	executionLogs();
		            	System.out.println(Red + "Terminating program...");
		                System.out.println("Program successfully terminated!" + Reset);
		                System.out.println(Gold + "Thank you for utilizing this program! Goodbye." + Reset);		                
		                System.exit(0); // Terminates after viewing the execution logs
		            });
		            swiftBot.enableButton(Button.X, () -> { // If button X is pressed however, it would terminate without showing the execution log
		                swiftBot.disableUnderlights(); 
		                System.out.println(Red + "Terminating program...");
		                System.out.println("Program successfully terminated!" + Reset);
		                System.out.println(Gold + "Thank you for utilizing this program! Goodbye." + Reset);		                
		                System.exit(0);
		            });
		        });
		    } 
		    catch (Exception e) { // Error Handling
		        System.out.println(Red + "ERROR occurred in Button X" + Reset);
		        e.printStackTrace();
		        System.exit(5);
		    }
		}
}