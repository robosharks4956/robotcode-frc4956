package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.GrabWithController;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Grabber extends Subsystem {
	public DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.grabberPort, RobotMap.grabberForward, RobotMap.grabberReverse);
    public void initDefaultCommand() {
    	setDefaultCommand(new GrabWithController());
    }
    
    public Grabber() {
    	super();
    	solenoid.set(DoubleSolenoid.Value.kReverse);
    }
}
