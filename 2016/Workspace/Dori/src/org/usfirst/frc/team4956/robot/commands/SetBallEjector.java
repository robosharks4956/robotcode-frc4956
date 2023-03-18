package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetBallEjector extends Command {

	boolean value;
    public SetBallEjector(boolean value) {
        // Use requires() here to declare subsystem dependencies
    	//requires(Robot.ejector);
    	this.value = value;
    	setTimeout(1);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if (value) Robot.ejector.set(true);
    	else Robot.ejector.set(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return isTimedOut();    }

    // Called once after isFinished returns true
    protected void end() {
    	//Robot.ejector.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//end();
    }
}