package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive for a certain period of time along specified heading
 */
public class DriveOnHeading extends Command 
{
	// Reference to Robot.drivetrain for convenience
    DriveTrain drivetrain;
    
    // Direction to drive and speed to drive at
    double targetHeading;
    double speed;
    double seconds;
    
    Timer timer = new Timer();
    
    // Scaling constant for turning robot. Affects how fast the robot turns to try and reach target heading.
    static final double Kp = 0.03;
    
    public DriveOnHeading(double speed, double targetHeading, double seconds) 
    {
        requires(Robot.drivetrain);
        drivetrain = Robot.drivetrain;
        this.targetHeading = targetHeading;
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
    	// Robot will drive along targetheading at speed
    	double angle = drivetrain.getHeading();
        drivetrain.arcadeDrive(-speed, (-angle+targetHeading)*Kp);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	// If we're close enough to targetHeading, end command, else continue.
    	if (timer.hasPeriodPassed(seconds))
    		return true;
    	else
    		return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	// Stop robot from moving.
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
        end();
    }
}
