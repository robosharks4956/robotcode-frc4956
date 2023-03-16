package org.usfirst.frc.team4956.robot;

import org.usfirst.frc.team4956.robot.commands.ForwardJaw;
import org.usfirst.frc.team4956.robot.commands.HoldElevatorSetpoint;
import org.usfirst.frc.team4956.robot.commands.ReverseJaw;
import org.usfirst.frc.team4956.robot.commands.StopElevator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{
	
	public Joystick rightStick = new Joystick(0);
	public Joystick leftStick = new Joystick(1);
	public int toteHeight = 11;
	
	public OI()
	{
		// Add triggers for pressing the RaiseArm and LowerArm buttons
		new JoystickButton(leftStick, RobotMap.forwardJawExtender).whenPressed(new ForwardJaw());
		new JoystickButton(leftStick, RobotMap.reverseJawExtender).whenPressed(new ReverseJaw());
		new JoystickButton(leftStick, RobotMap.toteHeightPoint).whenPressed(new HoldElevatorSetpoint(toteHeight, 0));
		new JoystickButton(leftStick, RobotMap.toteHeightPoint).whenReleased(new StopElevator());
	}
}

