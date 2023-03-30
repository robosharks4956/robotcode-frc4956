package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.LiftWithController;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Controls the two motors that move the scissor lift up and down.
 */
public class Lift extends Subsystem {
	
	public double hoverSpeed = 0.11;
	public WPI_TalonSRX left_lift_motor, right_lift_motor;
	public double liftTimer = 0;
	public Lift() {
		left_lift_motor = new WPI_TalonSRX(RobotMap.liftMotorLeft);
		right_lift_motor = new WPI_TalonSRX(RobotMap.liftMotorRight);
	}

    public void initDefaultCommand() {
        setDefaultCommand(new LiftWithController());
    }
    
    public void setSpeed(double speed) {
    	
    	left_lift_motor.set(speed < 0 ? speed * 0.7 : speed);
    	right_lift_motor.set(speed > 0 ? speed * 0.9 : speed);
    }
}


