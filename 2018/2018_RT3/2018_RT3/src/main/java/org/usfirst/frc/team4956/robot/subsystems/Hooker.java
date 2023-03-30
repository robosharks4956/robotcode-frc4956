package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.HookWithController;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hooker extends Subsystem {
	public Solenoid solenoid = new Solenoid(RobotMap.hooker);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    	setDefaultCommand(new HookWithController());
    	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

