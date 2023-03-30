package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoLiftPunch extends Command {
	public double liftTime;
	public double holdTime;
	Timer timer = new Timer();
	Timer holdTimer = new Timer();
	Timer downTimer = new Timer();

    public AutoLiftPunch(double liftTimeSeconds, double holdTimeSeconds) {
        requires(Robot.lift);
        requires(Robot.hook);
        liftTime = liftTimeSeconds;
        holdTime = holdTimeSeconds;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    	holdTimer.start();
    	downTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (timer.get() < liftTime) {
			Robot.lift.setSpeed(0.39);
    	} else if (holdTimer.get() < liftTime + holdTime) {
    		Robot.lift.setSpeed(0.12);
    		Robot.hook.solenoid.set(false);
    	} else if (downTimer.get() < liftTime * 2 + holdTime) {
    		Robot.hook.solenoid.set(true);
    		Robot.lift.setSpeed(-0.1);
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
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
