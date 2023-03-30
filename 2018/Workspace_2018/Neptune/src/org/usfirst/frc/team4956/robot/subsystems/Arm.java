package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;
import org.usfirst.frc.team4956.robot.commands.ArmWithController;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Arm extends Subsystem {
	
	public Spark arm_motor;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public Arm() {
		super();
		arm_motor = new Spark(RobotMap.armMotor);
	}
	
    public void initDefaultCommand() {
        setDefaultCommand(new ArmWithController());
    }
}

