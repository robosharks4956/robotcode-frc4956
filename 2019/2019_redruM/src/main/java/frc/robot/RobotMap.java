/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

  //Lift motors
  public static final int liftMotor1 = 1;
  public static final int liftMotor2 = 5;

  //Ball up and down motor
  public static final int ballMotorCan = 9;

  // Spinners
  public static final int ballSpinnerMotor = 9;
  public static final int ballSpinnerMotor2 = 10;
  
  //spark drive motors
  public static final int sparkFrontLeftMotor = 1;
  public static final int sparkBackLeftMotor = 3;
  public static final int sparkFrontRightMotor = 2;
  public static final int sparkBackRightMotor = 0;

  //compressor/pnuematics
  public static final int compressor = 0;
  public static final int front_up = 5;
  public static final int front_down = 4;
  public static final int back_up = 3;
  public static final int back_down = 2;
  public static final int panel_up = 0;
  public static final int pane_down = 1;

  //controllers
  public static final int driverStick = 0, supportStick = 1;
  public static final int leftStickX = 0, leftStickY = 1;
  public static final int rightStickX = 4, rightStickY = 5;
	public static final int rightTrigger = 3, leftTrigger = 2;
  public static final int rightBumper = 6, leftBumper = 5;
  
  // drive settings
	public static int maxSpeedBtn = 3, minSpeedBtn = 2; //max x min is b
	public static int invertDirection = 1;
	public static double normalSpeedPercent = 1;
	public static double maxSpeedPercent = 1; //0.85;
	public static double minSpeedPercent = 0.3;
}
