

package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;
import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class ArcadeDriveWithJoystick extends Command {
    public ArcadeDriveWithJoystick() {
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }
    boolean isInverted = false;
    boolean buttonTriggered = false;
    //loop
    protected void execute() {
    	
    	if (Robot.oi.maxSpeed.get()){
    		Robot.drivetrain.setSensitivity(RobotMap.maxSpeedPercent);
    	} 
    	else if (Robot.oi.minSpeed.get()){
    		Robot.drivetrain.setSensitivity(RobotMap.minSpeedPercent);	
    	}
    	else {
    		Robot.drivetrain.setSensitivity(RobotMap.normalSpeedPercent);
    	}
    	
    	// Toggles inverted direction on/off
    	if (Robot.oi.invertDirection.get() && buttonTriggered == false) {
    		buttonTriggered = true;
    		if (isInverted == true) {
    			isInverted = false;
    		}
    		else {
    			isInverted = true;
    		}
    	}
    	else if (Robot.oi.invertDirection.get() == false && buttonTriggered == true) {
    		buttonTriggered = false;
    	}
    	
    	// Right Trigger = Forward ; Left Trigger = Reverse ; Total Speed = Left + right together.
    	double speed = Robot.oi.driverStick.getRawAxis(RobotMap.rightTrigger) + Robot.oi.driverStick.getRawAxis(RobotMap.leftTrigger) * -1; 	 
    	double turnDirection = Robot.oi.driverStick.getRawAxis(RobotMap.leftStickX) * -1;
    	if (isInverted == true) {
    		speed = speed * -1;
    	}
    	
    	Robot.drivetrain.arcadeDrive(speed, turnDirection);
    }
    
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
