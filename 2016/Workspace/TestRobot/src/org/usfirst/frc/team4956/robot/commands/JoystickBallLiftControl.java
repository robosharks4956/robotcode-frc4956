package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickBallLiftControl extends Command {

	double forwardLimit = 150; // According to Dennis, if 0 is where the arm starts, 150 is the ground.
	   // 35 is the ideal firing angle by the way.
	
	double backwardLimit = 0;
	
    public JoystickBallLiftControl() {
    	requires(Robot.balllift);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	moveBySpeed();
    	//moveByEncoder();
    }
    
    void moveBySpeed() {
    	double joystickVal = Robot.oi.leftStick.getY();
    	
    	if (joystickVal > 0) { // Want it to be faster going up
    		Robot.balllift.setSpeed(joystickVal* 45);
    	}
    	else {
    		Robot.balllift.setSpeed(joystickVal* 35);
    	}
    }
    
    void moveByVelocity() {
    	
    }
    
    void moveByEncoder() {
    	double newSetpoint = Robot.balllift.getDegreeSetpoint() + Robot.oi.leftStick.getY() * 15f;
    	
    	if (newSetpoint > forwardLimit) {
    		newSetpoint = forwardLimit;
    	} else if (newSetpoint < backwardLimit) {
    		newSetpoint = backwardLimit;
    	}
    	
    	Robot.balllift.setDegrees(newSetpoint);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.balllift.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
