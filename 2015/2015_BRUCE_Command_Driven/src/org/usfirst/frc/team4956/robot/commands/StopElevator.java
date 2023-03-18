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

public class StopElevator extends Command 
{   
	
    Elevator elevator;
    
    public StopElevator() 
    {
        requires(Robot.elevator);
        elevator = Robot.elevator;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
//    	elevator.stop();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	return true;
    }
    
  
    // Called once after isFinished returns true
    protected void end() 
    {
//    	elevator.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
