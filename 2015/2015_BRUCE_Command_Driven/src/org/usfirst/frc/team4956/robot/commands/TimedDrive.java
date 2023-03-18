package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive for a certain period of time at a certain speed
 */
public class TimedDrive extends Command 
{
	// Reference to Robot.drivetrain for convenience
    DriveTrain drivetrain;
    
    // Speed and seconds to drive.
    double speed;
    double seconds;
    
    Timer timer = new Timer();

    public TimedDrive(double speed, double seconds) 
    {
        requires(Robot.drivetrain);
        drivetrain = Robot.drivetrain;
        this.speed = speed;
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
    	// Robot will drive along at speed
        drivetrain.arcadeDrive(-speed, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	// End command if time is elapsed.
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
