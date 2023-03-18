package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.ElevatorControlWithJoystick;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem 
{
	
	Encoder liftEncoder;
	int maxHeight = 36;
    int minHeight = 0;
	
	Talon elevatorMotor1;
	Talon elevatorMotor2;
	
	
	public Elevator()
	{
		super();
		
		// Initialize encoder mounted on motor box
    	liftEncoder = new Encoder(8, 9, false, Encoder.EncodingType.k4X);
    	liftEncoder.setMaxPeriod(.1);
    	liftEncoder.setMinRate(10);
    	//double inchesPerRevolution = 1.85; // Approximate distance from Matt
    	double inchesPerRevolution = 2.8; // Need new distance
    	double revolutionsPerPulse = 1.0 / 250.0;
    	liftEncoder.setDistancePerPulse(-1*inchesPerRevolution*revolutionsPerPulse);
    	liftEncoder.setSamplesToAverage(7);
    	
    	// Initialize motors
    	elevatorMotor1 = new Talon(RobotMap.elevatorMotor1);
    	elevatorMotor2 = new Talon(RobotMap.elevatorMotor2);
	}
	
	public void initDefaultCommand() 
	{
		setDefaultCommand(new ElevatorControlWithJoystick());
	}
	
	public double getDistance()
	{
		return liftEncoder.getDistance();
	}
	
	
	/**
	 * Tells the elevator motors not to move.
	 */
	public void stop()
	{
		setSpeed(0);
	}
	
	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
    public void log() 
    {
        SmartDashboard.putNumber("Encoder", liftEncoder.getDistance());
    }
    
    public void setSpeed (double speed)
    {
    	if((liftEncoder.getDistance()>=maxHeight && (speed<=0)) || 
        		(liftEncoder.getDistance()<=minHeight && (speed>=0)) ||
        			(liftEncoder.getDistance()<=maxHeight) && (liftEncoder.getDistance()>=minHeight))
        {
            elevatorMotor1.set(speed);
            elevatorMotor2.set(speed);
        }
    	else
        {
        	elevatorMotor1.set(0);
        	elevatorMotor2.set(0);
        }
    }
}
