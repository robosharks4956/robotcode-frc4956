/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.subsystems.Elevator;

/**
 * Move the elevator to a given location. This command finishes when it is within
 * the tolerance, but leaves the PID loop running to maintain the position. Other
 * commands using the elevator should make sure they disable PID!
 */
public class HoldElevatorSetpoint extends Command 
{
    private double setpoint;
    
    double k = 0.4;
    double targetDifference = 2;
    Elevator elevator;
    Timer timer = new Timer();
    double seconds;
    
    public HoldElevatorSetpoint(double setpoint, double seconds) 
    {
        this.setpoint = setpoint;
        this.seconds = seconds;
        requires(Robot.elevator);
        elevator = Robot.elevator;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	if(seconds > 0)
    	{
        	timer.reset();
        	timer.start();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	if (isClose())
    	{
    		elevator.stop();
    	}
    	else
    	{
	    	// Calculate a target speed based on how far we are from the elevator
	    	double speed = (setpoint - elevator.getDistance())*k;
	    	
	    	// Cap speed magnitude at 0.5
	    	speed = Math.min(speed, 0.9);
	    	speed = Math.max(speed, -0.9);
	    	
	    	// Set the elevator to that speed
	    	elevator.setSpeed(speed);
	    	
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
       	// If time has elapsed and we have a valid time, end.   	
    	if (timer.hasPeriodPassed(seconds) && (seconds > 0))
    		return true;
    	else
    		return false;
    }

    protected boolean isClose()
    {
    	if(Math.abs(setpoint - elevator.getDistance()) < targetDifference){
    		return true;
    	}
    	else return false;
    }
    
    
    // Called once after isFinished returns true
    protected void end() 
    {
    	elevator.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
