/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4956.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// drive MainBot
	public static int backLeftMotor = 4, backRightMotor = 7;
	public static int frontLeftMotor = 5, frontRightMotor = 6;
	
	// drive settings
	public static int maxSpeedBtn = 3, minSpeedBtn = 2; //max x min is b
	public static int invertDirection = 1;
	public static double normalSpeedPercent = 0.6;
	public static double maxSpeedPercent = 1; //0.85;
	public static double minSpeedPercent = 0.3;
	
	// lift
	public static int liftMotorLeft = 8, liftMotorRight = 1;
	public static int liftScaleBtn = 4, liftSwitchBtn = 1, liftPortalBtn = 2; // Scale is Y, Switch is A, Portal is B
																																																												
	//Hooker
	public static int hooker = 0;
	
	
	// controllers
	public static int driverStick = 1, supportStick = 2;
	public static int leftStickX = 0, leftStickY = 1;
	public static int rightTrigger = 3, leftTrigger = 2;
	public static int rightBumper = 6;

	//belt spinners
	//public static int spinForwardBtn = 5, spinBackwardBtn = 6;
	//public static int spinMotor2 = 2, spinMotor1 = 1; 
	public static double left90=93, right90=-90;
}
