package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive for a certain period of time along specified heading
 */
public class TimedWait extends Command 
{
	
	double seconds;
	
    Timer timer = new Timer();
    
    public TimedWait(double seconds) 
    {
        requires(Robot.drivetrain);
        this.seconds = seconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if (timer.hasPeriodPassed(seconds))
    		return true;
    	else
    		return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	// Stop robot from moving.
        Robot.drivetrain.tankDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
        end();
    }
}
