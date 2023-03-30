package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftWithController extends Command {

    public LiftWithController() {
        requires(Robot.lift);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = 0;
    	 
    	// Use the right hand y-axis as a modifier to the speed of the buttons, so speed
    	// can go up or down from the central speed of 50%
    	
    	if (Robot.oi.holdButton.get()) {
    		speed = Robot.lift.hoverSpeed;
    	} else {
    		speed += Robot.oi.supportStick.getY(Hand.kRight) * -0.5;
    	}
    	
    	if (Math.abs(speed) < 0.1) {
    		speed = 0;
    	}
    	
    	Robot.lift.right_lift_motor.set(Robot.oi.supportStick.getY(Hand.kLeft) * 0.4);
    	
    	//System.out.println("Lift speed:" + speed);
    	
    	Robot.lift.setSpeed(speed);
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
