package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetDriveSensitivity extends Command {

	double sensitivity;
	
    public SetDriveSensitivity(double sensitivity) {
    	this.sensitivity = sensitivity;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.setSensitivity(sensitivity);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
