package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Turn to robot in place to face a certain heading.
 */
public class TurnToHeading extends Command {
	// Reference to Robot.drivetrain for convenience
    DriveTrain drivetrain;
    
    // Target heading the robot will be facing when this command finishes
    double targetHeading;
    
    // Scaling constant for turning robot. Affects how fast the robot turns to try and reach target heading.
    static final double Kp = 0.05;
    
    // How close in degrees we need to be to targetHeading before this command will finish
    double targetDifference = 4;
    
    double maxSpeed = 0.75;
    
    public TurnToHeading(double targetHeading) {
        requires(Robot.drivetrain);
        drivetrain = Robot.drivetrain;
        this.targetHeading = targetHeading;
    }

    // Called just before this Command runs the first time
    protected void initialize() {}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Read current angle, calculate speed for motors to reach target setting, and set robot to drive at speed. 
    	// This will turn the robot towards the target heading.
    	double angle = drivetrain.getHeading();
    	double stickValue = (-angle+targetHeading)*Kp;
    	stickValue = Math.min(stickValue, maxSpeed); // Limit speed
    	stickValue = Math.max(stickValue, -maxSpeed);
    	drivetrain.tankDrive(-stickValue, stickValue);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	// If we're close enough to targetHeading, end command, else continue.
    	if (Math.abs(drivetrain.getHeading() - targetHeading) < targetDifference)
    		return true;
    	else
    		return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Stop robot from moving.
        Robot.drivetrain.tankDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
