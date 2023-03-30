package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveDistance extends Command {
	Timer driveTimer = new Timer();
	double speed;
	double distance;

    public AutoDriveDistance(double speed, double distance) {
        requires(Robot.drivetrain);
        
        this.speed = speed;
        this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	driveTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.tankDrive(speed, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (Robot.drivetrain.left_encoder.getDistance() <= this.distance || driveTimer.get() > 7) {
    		return true;
    	} else if (Robot.drivetrain.right_encoder.getDistance() <= this.distance) {
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
