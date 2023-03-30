package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftMoveOrSomething extends Command {

    public double time;
    Button button;
    Timer timer = new Timer();
    boolean released = false;
    double lastTime = 0;
	public LiftMoveOrSomething(double timeout, Button button) {
        requires(Robot.lift);
        this.time = timeout;
        this.button = button;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//calculates elapsed time and adds it to lift timer
    	double elapsedTime = timer.get() - lastTime;
    	lastTime = timer.get();
    	Robot.lift.liftTimer += elapsedTime;
    
    	if (button.get()) {
    		if (timer.get() < time) {
    			Robot.lift.setSpeed(0.25);
    		} else if (timer.get() >= time) {
    			Robot.lift.setSpeed(0.12);
    		}
    	} else if (!released) {
    		released = true;
    		time = timer.get() < time ? timer.get() : time;
    		timer.reset();
    		Robot.lift.setSpeed(-0.1);
    		
    	} else {
    		Robot.lift.setSpeed(-0.1);
    	}
    	
    	
    }
    
    protected boolean isFinished() {
        if (released && timer.get() > time) {
        	return true;
        }
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.lift.setSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.lift.setSpeed(0);
    }
}
