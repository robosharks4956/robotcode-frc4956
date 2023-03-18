/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.subsystems.Elevator;

/**
 * Move the elevator to a given location. This command finishes when it is within
 * the tolerance, but leaves the PID loop running to maintain the position. Other
 * commands using the elevator should make sure they disable PID!
 */
public class SetElevatorSetpoint extends Command 
{
    private double setpoint;
    
    double k = 0.3;
    double targetDifference = 1;
    Elevator elevator;
    
    public SetElevatorSetpoint(double setpoint) 
    {
        this.setpoint = setpoint;
        requires(Robot.elevator);
        elevator = Robot.elevator;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	double speed = (setpoint - elevator.getDistance())*k;
    	speed = Math.min(speed, 0.9);
    	speed = Math.max(speed, -0.9);		
    	elevator.setSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
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
