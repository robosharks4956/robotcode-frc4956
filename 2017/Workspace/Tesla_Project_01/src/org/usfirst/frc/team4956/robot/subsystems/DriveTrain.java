package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.ArcadeDriveWithJoystick;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
/**
 *
 */
public class DriveTrain extends Subsystem {
    
	CANTalon front_left_motor, front_right_motor, back_left_motor, back_right_motor;
	public Encoder left_encoder, right_encoder;
	RobotDrive drive;
	boolean fast = false;

	public DriveTrain() {
		super();	
		initMotors();
		initEncoders();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoystick()); 
    }
	
	public void initMotors() {
		front_left_motor = new CANTalon(RobotMap.frontLeftMotor);
		front_right_motor = new CANTalon(RobotMap.frontRightMotor);
		back_left_motor = new CANTalon(RobotMap.backLeftMotor);
		back_right_motor = new CANTalon(RobotMap.backRightMotor);
		drive = new RobotDrive(front_left_motor,back_left_motor,front_right_motor,back_right_motor);
		
		// This is sensitivity.
		drive.setMaxOutput(RobotMap.normalSpeedPercent);
	}
	
	public void initEncoders() {
		//wheel circumference is 22.75 inches
		//left_encoder = new Encoder(RobotMap.leftEncOne, RobotMap.leftEncTwo, false, EncodingType.k4X);
		//right_encoder = new Encoder(RobotMap.rightEncOne, RobotMap.rightEncTwo, false, EncodingType.k4X);
		
    	// Formula to convert encoder pulses to distance and set that. This will make the encoder give us numbers in distance.
    	//double inchesPerRevolution = 22.75;
    	//double revolutionsPerPulse = 1.0 / 250.0;
    	//left_encoder.setDistancePerPulse(-1*inchesPerRevolution*revolutionsPerPulse);
    	//right_encoder.setDistancePerPulse(1*inchesPerRevolution*revolutionsPerPulse);
	}
    
    public void arcadeDrive(Joystick stick) {
    	drive.arcadeDrive(stick);
    }
    
    public void arcadeDrive(double speed, double rotation) {
    	
    	drive.arcadeDrive(speed, rotation);
    	
    }
    
    public void tankDrive(double left, double right) {
    	drive.tankDrive(left, right);
    }
    
    public void stop() {
	   drive.stopMotor();
    }
    
    /**
	 * Reset the robots sensors to the zero states.
	 */
	public void reset() {
		//gyro.reset();
		//left_encoder.reset();
		//right_encoder.reset();
	}
	
	public void setSensitivity(double sensitivity){
		
		drive.setMaxOutput(sensitivity);
	}
	
	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	public void log() {
		SmartDashboard.putNumber("Left Distance", left_encoder.getDistance());
		SmartDashboard.putNumber("Right Distance", right_encoder.getDistance());
		SmartDashboard.putNumber("Left Speed", left_encoder.getRate());
		SmartDashboard.putNumber("Right Speed", right_encoder.getRate());

		//SmartDashboard.putNumber("Gyro", gyro.getAngle());
		
		//System.out.println("Left Distance "+ left_encoder.getDistance());
		//System.out.println("Right Distance "+ right_encoder.getDistance());
		//System.out.println("Left Speed "+ left_encoder.getRate());
		//System.out.println("Right Speed "+ right_encoder.getRate());
	}
	
}