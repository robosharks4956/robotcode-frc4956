package org.usfirst.frc.team4956.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap 
{
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	// PWM Outputs
	public static int frontLeftMotor = 0;
	public static int rearLeftMotor = 1;
	public static int frontRightMotor = 2;
	public static int rearRightMotor = 3;
	public static int elevatorMotor1 = 4;
	public static int elevatorMotor2 = 5;
	
	// Analog Inputs
	public static int gyro = 0;
	public static int rangeFinder = 1;
	
	// Buttons
	public static int toteHeightPoint = 5;
	public static int forwardJawExtender = 3;
	public static int reverseJawExtender = 2;
	
	// PCM ports
	public static int forwardJawSolenoid = 5;
	public static int reverseJawSolenoid= 6;
	
	// Camera settings
	public static String autonCameraName = "cam0";
}
