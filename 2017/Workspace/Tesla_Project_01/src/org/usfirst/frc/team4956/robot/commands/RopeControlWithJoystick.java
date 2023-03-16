package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RopeControlWithJoystick extends Command {

    public RopeControlWithJoystick() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.ropeclimber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = Robot.oi.supportStick.getY();
    	
    	if (speed > 0) {
    		speed = 0;
    	}

    	if (Robot.oi.fastRope.get()) {
        	Robot.ropeclimber.setSpeed(speed * -1);
    	}
    	else {
    		Robot.ropeclimber.setSpeed(speed * -0.30);
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
