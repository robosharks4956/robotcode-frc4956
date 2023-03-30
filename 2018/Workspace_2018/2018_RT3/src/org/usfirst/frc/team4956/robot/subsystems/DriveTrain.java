package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.ArcadeDriveWithController;

import com.ctre.CANTalon;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 *
 */
public class DriveTrain extends Subsystem {
	
	public WPI_TalonSRX front_left_motor, front_right_motor;
	WPI_TalonSRX back_left_motor, back_right_motor;
	DifferentialDrive drive;
	boolean fast = false;
	
	double ticksPerRevolution = 4096;
	double wheelCircumfranceInches = 6 * Math.PI;
	public double inchesPerTick = wheelCircumfranceInches / ticksPerRevolution;
	
	public DriveTrain() {
		super();
		initMotors();
		initEncoders();	
	}
	
	public void initMotors() {
		
		front_left_motor = new WPI_TalonSRX(RobotMap.frontLeftMotor);
		front_right_motor = new WPI_TalonSRX(RobotMap.frontRightMotor);
		back_left_motor = new WPI_TalonSRX(RobotMap.backLeftMotor);
		back_right_motor = new WPI_TalonSRX(RobotMap.backRightMotor);
		back_left_motor.set(ControlMode.Follower, RobotMap.frontLeftMotor);
		back_right_motor.set(ControlMode.Follower, RobotMap.frontRightMotor);
	
		drive = new DifferentialDrive(front_left_motor, front_right_motor); 
	
		// This is kind of like sensitivity
		//drive.setMaxOutput(RobotMap.normalSpeedPercent);
		
		// Add a label to the subsystem
		drive.setSubsystem("DriveTrain");
		
		// Set the ramp rate to smooth out the acceleration
		// time from neutral to 100% in seconds, timeout in ms
		//front_left_motor. configOpenloopRamp(1, 100);
		//front_right_motor.configOpenloopRamp(1, 100);

		front_left_motor.config_kF(0, .9, 10);
		front_right_motor.config_kF(0, -.8, 10);
		
		front_left_motor.config_kP(0, .09, 10);
		front_right_motor.config_kP(0, -.09, 10);
		
		front_left_motor.config_kD(0, .009, 10);
		front_right_motor.config_kD(0, -.009, 10);
	}
	
	public void initEncoders() {
		// Set motor feedback devices to the encoder
		front_left_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		front_right_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);	
		front_left_motor.setSensorPhase(true);
		front_right_motor.setSensorPhase(true);
		//front_left_motor.setSelectedSensorPosition(arg0, arg1, arg2)
	}
	
	public double getLeftPosition() {
		return Math.floor(Robot.drivetrain.front_left_motor.getSelectedSensorPosition(0) * Robot.drivetrain.inchesPerTick * 10) / 10.0;
	}
	
	public double getRightPosition() {
		return Math.floor(Robot.drivetrain.front_right_motor.getSelectedSensorPosition(0) * Robot.drivetrain.inchesPerTick * 10) / 10.0;
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new ArcadeDriveWithController());
    }
    
    public void arcadeDrive(double speed, double rotation) {
    	if (front_left_motor.getControlMode() != ControlMode.PercentOutput || front_right_motor.getControlMode() != ControlMode.PercentOutput) {
    		front_left_motor.set(ControlMode.PercentOutput, 0);
    		front_right_motor.set(ControlMode.PercentOutput, 0);
    	}
    
    	drive.arcadeDrive(speed, rotation);
    }
    public void tankDrive(double leftSpeed, double rightSpeed) {
    	drive.tankDrive(leftSpeed, rightSpeed);
    }
    public void stop() {
 	   drive.stopMotor();
     }
    
    public void setSensitivity(double sensitivity) {
		drive.setMaxOutput(sensitivity);
	}
}