package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.RopeControlWithJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class RopeClimber extends Subsystem {
	CANTalon ropeClimberMotor1;
	CANTalon ropeClimberMotor2;
	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new RopeControlWithJoystick());
    }
    
    public RopeClimber() {
    	ropeClimberMotor1 = new CANTalon(RobotMap.ropeClimberMotor1);
    	ropeClimberMotor2 = new CANTalon(RobotMap.ropeClimberMotor2);
    }
    public void setSpeed(double speed) {
    	ropeClimberMotor1.set(speed); 
    	ropeClimberMotor2.set(speed);
    	 
    }
}

