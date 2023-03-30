package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurn extends Command {
	Timer timer = new Timer();
    double speed = 0;
    double seconds = 0;
	
    public AutoTurn(double speed, double seconds) {
    	requires (Robot.drivetrain);
    	this.speed = speed; 
    	this.seconds = seconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.tankDrive(speed, -speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (timer.hasPeriodPassed(seconds))
    		return true;
    	else
    		return false;
    }
}