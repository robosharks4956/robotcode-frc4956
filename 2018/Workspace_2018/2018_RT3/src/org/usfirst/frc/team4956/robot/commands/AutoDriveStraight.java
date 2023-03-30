package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDriveStraight extends Command {
	public double targetPositionLeft;
	public double targetPositionRight;
	public double targetPosition;
	public boolean autoHover;
    Timer liftTimer = new Timer();
    Timer driveTimer = new Timer();
	public AutoDriveStraight(double targetPosition, boolean autoHover) {
        
    	// Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        requires(Robot.lift);
        this.targetPosition = targetPosition;
        this.autoHover = autoHover;

    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	liftTimer.start();
    	driveTimer.start();
    	this.targetPositionLeft = Robot.drivetrain.getLeftPosition() + targetPosition;
    	this.targetPositionRight = Robot.drivetrain.getRightPosition() - targetPosition;
    	
    	System.out.println("Current Left: " + Robot.drivetrain.getLeftPosition());
     	System.out.println("Current right: " + Robot.drivetrain.getRightPosition());
    	System.out.println("Target Left: " + this.targetPositionLeft);
     	System.out.println("Target Right: " +this.targetPositionRight);	
    }
    
    void setPosition(double velocity) {
    	
    	// NOTE: Might want to change to ControlMode.Velocity instead of Position
     	Robot.drivetrain.front_left_motor.set(ControlMode.Velocity, velocity); //Robot takes ticks / 100ms
    	Robot.drivetrain.front_right_motor.set(ControlMode.Velocity, velocity); 
    	//System.out.println(Robot.drivetrain.front_left_motor.getSelectedSensorVelocity(0));
    	//System.out.println(Robot.drivetrain.front_right_motor.getSelectedSensorVelocity(0));
    }

    // Gary come home
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//hover lift so that we don't drag the box holder
    	if (liftTimer.get() < 3.4 && autoHover) {
    		Robot.lift.setSpeed(0.39);
    	} else {
    		Robot.lift.setSpeed(Robot.lift.hoverSpeed);
    	}
    	
    	double deltaLeft = Math.abs(Robot.drivetrain.getLeftPosition()- targetPositionLeft);
    	double deltaRight = Math.abs(Robot.drivetrain.getRightPosition() - targetPositionRight);
    	double maxSpeed = 1.8;
    	double minSpeed = 1.3;
    	double slowRange = 20;
    	if (deltaLeft < slowRange || deltaRight < slowRange) {
    		setPosition((minSpeed + (deltaLeft/slowRange) * (maxSpeed - minSpeed)) / Robot.drivetrain.inchesPerTick);
    	}
    	else {
    		setPosition(maxSpeed / Robot.drivetrain.inchesPerTick);
    	}
    	
    	    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	/*
       	System.out.println("Left  Target: " +this.targetPositionLeft);
     	System.out.println("Right Target: " +this.targetPositionRight);
     	
       	System.out.println("Left  Position: " +Robot.drivetrain.getLeftPosition());
     	System.out.println("Right Position: " +Robot.drivetrain.getRightPosition());
    	*/
 
        if (Robot.drivetrain.getLeftPosition() >= targetPositionLeft || driveTimer.get() > 7) {
        	System.out.println("Left  Position L: " +Robot.drivetrain.getLeftPosition());
        	System.out.println("Right Position L: " +Robot.drivetrain.getRightPosition());
        	Robot.drivetrain.arcadeDrive(0, 0);
        	return true;
        }
        else if (Robot.drivetrain.getRightPosition() <= targetPositionRight ) {
        	System.out.println("Left  Position R: " +Robot.drivetrain.getLeftPosition());
        	System.out.println("Right Position R: " +Robot.drivetrain.getRightPosition());
        	Robot.drivetrain.arcadeDrive(0, 0);
        	return true;
        }
        else
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
