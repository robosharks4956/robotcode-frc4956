package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AimAndFire extends Command {

	double setPoint = 35;
	
    public AimAndFire() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	requires(Robot.balllift);
    	setTimeout(3);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.balllift.setDegrees(setPoint);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	double currentDegrees = Robot.balllift.getDegrees();
    	
    	if (currentDegrees >= setPoint-2 || currentDegrees <= setPoint+2) {
    		
    		//Code for Firing Ball
    	}
    		
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    	Robot.balllift.setDegrees(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
