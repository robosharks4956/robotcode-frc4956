package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TimeDrive extends Command {
	
	DriveTrain drivetrain;
	double speed;
	double seconds;
	
	Timer timer = new Timer();
	

    public TimeDrive(double speed, double seconds) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	requires(Robot.drivetrain);
    	drivetrain = Robot.drivetrain;
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
    	
    	drivetrain.arcadeDrive(speed, 0);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        
    	if (timer.hasPeriodPassed(seconds))
    		return true;
    	else
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
