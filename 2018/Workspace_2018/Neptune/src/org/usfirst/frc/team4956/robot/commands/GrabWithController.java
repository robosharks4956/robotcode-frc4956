package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GrabWithController extends Command {

    public GrabWithController() {
        requires(Robot.grabber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (Robot.oi.supportStick.getRawAxis(RobotMap.rightTrigger) > 0.1) {
    		Robot.grabber.solenoid.set(DoubleSolenoid.Value.kReverse);
    	} else if (Robot.oi.supportStick.getRawAxis(RobotMap.leftTrigger) > 0.1) {
    		Robot.grabber.solenoid.set(DoubleSolenoid.Value.kForward);
    	} else {
    		Robot.grabber.solenoid.set(DoubleSolenoid.Value.kOff);
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
