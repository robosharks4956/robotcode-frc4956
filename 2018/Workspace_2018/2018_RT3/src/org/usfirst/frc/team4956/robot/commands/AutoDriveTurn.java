package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveTurn extends Command {
	public double targetPositionLeft;
	public double targetPositionRight;
	public double targetPosition;
	public double direction;
    public AutoDriveTurn(double targetDegrees) {
        // Use requires() here to declare subsystem dependencies
          requires(Robot.drivetrain);
          requires(Robot.lift);
          targetPosition=targetDegrees*(3.14/180.0)*14.0;// Convert Degrees to Radians Multiply by the Radius of the Robot 
          if (targetDegrees < 0) {
        	  direction = -1;
          }
          else
        	  direction = 1;
    }
    
    void setPosition() {
    	
    	// NOTE: Might want to change to ControlMode.Velocity instead of Position
     	Robot.drivetrain.front_left_motor.set(ControlMode.Velocity,-1.8 * direction / Robot.drivetrain.inchesPerTick); 
    	Robot.drivetrain.front_right_motor.set(ControlMode.Velocity,  1.8 * direction / Robot.drivetrain.inchesPerTick); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {
     	this.targetPositionLeft = Robot.drivetrain.getLeftPosition() - targetPosition ;
    	this.targetPositionRight = Robot.drivetrain.getRightPosition() - targetPosition * direction;
    	
    	System.out.println("turnLeftTarget: " + this.targetPositionLeft);
     	System.out.println("turnRightTarget: " +this.targetPositionRight);
     	System.out.println("turnLeftCurrent: " +Robot.drivetrain.getLeftPosition());
     	System.out.println("turnRightCurrent: " +Robot.drivetrain.getRightPosition());

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.lift.setSpeed(Robot.lift.hoverSpeed);
    	setPosition();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       
        System.out.println("turnLeft  Target: " +this.targetPositionLeft);
     	System.out.println("turnRight Target: " +this.targetPositionRight);
     	
       	System.out.println("turnLeft  Position: " +Robot.drivetrain.getLeftPosition());
     	System.out.println("turnRight Position: " +Robot.drivetrain.getRightPosition());
    
    	
        if ((direction == 1 && Robot.drivetrain.getLeftPosition() <= targetPositionLeft) || (direction == -1 && Robot.drivetrain.getLeftPosition() >= targetPositionLeft)) {
        	System.out.println("L");
        	return true; 
        }
        else if ((direction == 1 && Robot.drivetrain.getRightPosition() <= targetPositionRight) || (direction == -1 && Robot.drivetrain.getRightPosition() <= targetPositionRight)) {
        	System.out.println("R");
        	return true;
    }
        else
        	return false;
        //return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
