package org.usfirst.frc.team4956.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.ArcadeDriveWithJoystick;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to
 * the robots chassis. These include four drive motors, a left and right encoder
 * and a gyro.
 */
public class DriveTrain extends Subsystem {
	private SpeedController front_left_motor, back_left_motor,
							front_right_motor, back_right_motor;
	private RobotDrive drive;
	
	private AnalogInput rangefinder;
	
	private Gyro gyro;

	public DriveTrain() {
		super();
		front_left_motor = new Victor(RobotMap.frontLeftMotor);
		back_left_motor = new Victor(RobotMap.rearLeftMotor);
		front_right_motor = new Victor(RobotMap.frontRightMotor);
		back_right_motor = new Victor(RobotMap.rearRightMotor);
		drive = new RobotDrive(front_left_motor, back_left_motor,
							   front_right_motor, back_right_motor);
		
		// This is sensitivity.
		drive.setMaxOutput(0.5);
		
    	//inverted all motors because joystick was backwards 
		drive.setInvertedMotor(MotorType.kFrontLeft, true);
		drive.setInvertedMotor(MotorType.kFrontRight, true);
		drive.setInvertedMotor(MotorType.kRearLeft, true);
		drive.setInvertedMotor(MotorType.kRearRight, true);

		rangefinder = new AnalogInput(RobotMap.rangeFinder);
		
		gyro = new Gyro(RobotMap.gyro);
	}

	/**
	 * When no other command is running let the operator drive around
	 * using the right joystick.
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoystick());
	}

	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	public void log() {
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		SmartDashboard.putNumber("Obstacle Range (cm)", getDistanceToObstacle());
	}

	/**
	 * Tank style driving for the DriveTrain. 
	 * @param left Speed in range [-1,1]
	 * @param right Speed in range [-1,1]
	 */
	public void tankDrive(double left, double right) {
		drive.tankDrive(left, right);
	}
	
	/**
	 * @param joy The joystick to use to drive tank style.
	 */
	public void tankDrive(Joystick left, Joystick right) {
		drive.tankDrive(left, right);
	}
	
	/**
	 * Arcade style driving for the DriveTrain. 
	 * @param speed Speed in range [-1,1]
	 * @param rotation Value to use for left/right rotation [-1,1]
	 */
	public void arcadeDrive(double speed, double rotation) {
		drive.arcadeDrive(speed, rotation);
		
	}
	
	public void drive(double speed, double rotation) {
		drive.drive(speed, rotation);
		
	}

	/**
	 * @param stick The joystick to use to drive tank style.
	 */
	public void arcadeDrive(Joystick stick) {
		drive.arcadeDrive(stick);
	}
	
	/**
	 * Stops the robot drive motors.
	 */
	public void stop()
	{
		drive.stopMotor();
	}

	/**
	 * @return The robots heading in degrees.
	 */
	public double getHeading() {
		synchronized(this){
			return gyro.getAngle();
		}
	}

	/**
	 * Reset the robots sensors to the zero states.
	 */
	public void reset() {
		gyro.reset();
	}
	
	/**
	 * @return The distance to the obstacle detected by the rangefinder. 
	 */
	public double getDistanceToObstacle() {
		
    	double toVolts = 5.0/4096.0;
    	double cmPerVolts = 1.0/0.0049;
    	double ultraSonic = rangefinder.getValue();
    	return ultraSonic*toVolts*cmPerVolts;
	}
}
