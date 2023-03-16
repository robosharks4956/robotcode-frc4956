package org.usfirst.frc.team4956.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	// PWM Outputs
	public static int frontLeftMotor = 9;
	public static int frontRightMotor = 8;

	// CAN Outputs
	//public static int spinner = 3;
	public static int ballLiftMotor1 = 4;
	public static int ballLiftMotor2 = 2; 
	
	// Solenoids
	public static int ejectorSolenoid = 0;
	public static int doorSolenoid = 7;

	// Buttons
	public static int shooterBtn = 1;
	public static int fastBtn = 1;
	public static int ejectorBtn = 2;
	public static int doorBtn = 3;
	
	// Digital IO
	public static int rightEncOne = 2;
	public static int rightEncTwo = 3;
	public static int leftEncOne = 4;
	public static int leftEncTwo = 5;
	
	// Sensitivities 
	public static double fastSensitivity = 1;
	public static double slowSensitivity = 0.70;
	
	// Cameras
	public static String autonCameraName = "cam0";
}