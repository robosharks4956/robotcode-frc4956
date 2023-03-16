package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.JoystickBallLiftControl;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BallLift extends Subsystem {
  
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	CANTalon motor1;
	CANTalon motor2;
	Encoder liftEncoder;
	
	TalonControlMode defaultMode;
	
	public BallLift() {
		super();
		motor1 = new CANTalon(RobotMap.ballLiftMotor1);
		motor2 = new CANTalon(RobotMap.ballLiftMotor2);
	
		defaultMode = motor1.getControlMode();
		
		// Set motor2 to follow motor 1
		motor2.changeControlMode(TalonControlMode.Follower);
		motor2.set(RobotMap.ballLiftMotor1);
		
		// Set to velocity mode
		startSpeedMode();
	}
	
	
    public void initDefaultCommand() {
    	setDefaultCommand(new JoystickBallLiftControl());
    }
   
    public void setSpeed(double speed) {
    	if (getControlMode() != TalonControlMode.Speed) {
    		startSpeedMode();
    	}
    	motor1.set(speed*-1);
       
       // Note that if you set speed in TalonControlMode.Speed it moves in units per 100ms. 
       // To get a unit in degrees per second, 
	}
    
    public void setByPercent(double percent) {
    	if (getControlMode() != TalonControlMode.PercentVbus) {
    		startPercentVBusMode();
    	}
    	motor1.set(percent);
    }
    
    public void setDegrees(double degrees) {
    	if (getControlMode() != TalonControlMode.Position) {
    		startPositionMode();
    	}
    	motor1.set(degrees/360.0f);
    }
    
    TalonControlMode getControlMode() {
    	 return motor1.getControlMode();
    }
   
    /* Puts the Talon in Position mode where the 
     * motor controller tries to maintain a constant position
     * on the arm. 
     */
    void startPositionMode() {
    	motor1.changeControlMode(TalonControlMode.Position);
		motor1.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		motor1.setPID(0.5, 0.0, 0.0);
		motor1.reverseSensor(false);
		
		// Tells the motor to lock at whatever position it's at
		setDegrees(getDegrees());
    }
    
    void startSpeedMode() {
    	motor1.changeControlMode(TalonControlMode.Speed);
		motor1.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		motor1.setPID(0.6, 0.1, 0.1);
		motor1.configPeakOutputVoltage(11, -11);
    }
    
    void startPercentVBusMode() {
    	motor1.changeControlMode(TalonControlMode.PercentVbus);
    }
    
    public void log() {
    	SmartDashboard.putNumber("Lift Degrees", getDegrees());
    	SmartDashboard.putNumber("Lift Motor Output", motor1.get());
    }
    
    public double getDegrees() {
    	double degreesPerTick = 360.0 / 4096.0;
    	return (motor1.getEncPosition() * degreesPerTick); 
    }
    
    public double getDegreeSetpoint() {
    	return (motor1.getSetpoint() * 360f); 
    }
    
    public double degreesPerSecToTicksPer100ms(double degreesPerSec) {
    	// 4096 ticks per revolution
    	// Default speed units is ticks per 100ms.
    	double degreesPerTick = 360f / 4096f;
    	return degreesPerSec / degreesPerTick / 10f;
    }
    
    public void stop() {
    	motor1.set(0);
    }
}