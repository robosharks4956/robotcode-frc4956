package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.ArcadeDriveWithController;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 *
 */
public class DriveTrain extends Subsystem {
	
	public Spark front_left_motor, front_right_motor;
	Spark back_left_motor, back_right_motor;
	public Encoder left_encoder, right_encoder;
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
	
	public void initEncoders() {
		
		//wheel circumference is 22.75 inches
		left_encoder = new Encoder(RobotMap.leftEncOne, RobotMap.leftEncTwo, false, EncodingType.k4X);
		right_encoder = new Encoder(RobotMap.rightEncOne, RobotMap.rightEncTwo, false, EncodingType.k4X);
		this.left_encoder.reset();
		this.right_encoder.reset();
		
    	// Formula to convert encoder pulses to distance and set that. This will make the encoder give us numbers in distance.
    	double inchesPerRevolution = 6 * Math.PI;
    	double revolutionsPerPulse = 1.0 / 250.0;
    	left_encoder.setDistancePerPulse(-1*inchesPerRevolution*revolutionsPerPulse);
    	right_encoder.setDistancePerPulse(1*inchesPerRevolution*revolutionsPerPulse);

	}
	
	public void initMotors() {
		
		front_left_motor = new Spark(RobotMap.frontLeftMotor);
		front_right_motor = new Spark(RobotMap.frontRightMotor);
		back_left_motor = new Spark(RobotMap.backLeftMotor);
		back_right_motor = new Spark(RobotMap.backRightMotor);
	
		drive = new DifferentialDrive(new SpeedControllerGroup(front_left_motor, back_left_motor), new SpeedControllerGroup(front_right_motor, back_right_motor)); 
			
		drive.setSubsystem("DriveTrain");
	}
	
	
	    public void initDefaultCommand() {
	    	setDefaultCommand(new ArcadeDriveWithController());
	    }
	    
	    public void arcadeDrive(double speed, double rotation) {	    
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



