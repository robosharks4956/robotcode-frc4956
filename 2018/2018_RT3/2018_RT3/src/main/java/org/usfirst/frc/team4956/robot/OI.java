/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4956.robot;

//import org.usfirst.frc.team4956.robot.commands.LiftMoveOrSomething;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public XboxController supportStick = new XboxController(RobotMap.supportStick);
	public XboxController driverStick = new XboxController(RobotMap.driverStick);
	public Button maxSpeed = new JoystickButton(driverStick, RobotMap.maxSpeedBtn);
	public Button invertDirection = new JoystickButton(driverStick, RobotMap.invertDirection);
	public Button minSpeed = new JoystickButton(driverStick, RobotMap.minSpeedBtn);
	public Button liftScale = new JoystickButton(supportStick, RobotMap.liftScaleBtn);
	public Button liftPortal = new JoystickButton(supportStick, RobotMap.liftPortalBtn);
	public Button liftSwitch = new JoystickButton(supportStick, RobotMap.liftSwitchBtn);
	public Button holdButton = new JoystickButton(supportStick, RobotMap.rightBumper);
	//public Button hookDown = new JoystickButton(supportStick, RobotMap.rightStick);
	public OI() {
		
	}
}
