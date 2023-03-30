/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4956.robot;

import org.usfirst.frc.team4956.robot.commands.AutoInCrossLine;
import org.usfirst.frc.team4956.robot.commands.AutoInCurvesDrop;
import org.usfirst.frc.team4956.robot.commands.AutoInStraightDrop;
import org.usfirst.frc.team4956.robot.commands.AutoOutCrossLine;
import org.usfirst.frc.team4956.robot.commands.AutoOutSwitchDrop;
import org.usfirst.frc.team4956.robot.subsystems.Arm;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4956.robot.subsystems.Grabber;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	public static OI oi;
	public static DriveTrain drivetrain;
	public static Grabber grabber;
	public static Arm arm;
	Command m_autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		drivetrain = new DriveTrain();
		grabber = new Grabber();
		arm = new Arm();
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		String autoPosition = SmartDashboard.getString("DB/String 0", "nothing").toLowerCase();
		String strategy = SmartDashboard.getString("DB/String 1", "primary").toLowerCase();
		if (strategy == null || strategy.length() == 0) strategy = "primary";
		
		SmartDashboard.putString("DB/String 5", "You picked: " + autoPosition);
		
		String Colors = gameData.substring(0, 1);
		
		System.out.println("autoPosition:" + autoPosition + ", strat:" + strategy);
		
		switch (autoPosition) {
		case "nothing":
		case "":
			System.out.println("nothing is chosen");
			m_autonomousCommand = new AutoOutCrossLine();
			break;
		case "l":
			SmartDashboard.putString("DB/String 6", "Runnning here strat 2: " + strategy);
			switch (Colors) {
			case "L": 
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutSwitchDrop(-1) : new AutoOutCrossLine();
				break;
			case "R":
				m_autonomousCommand = new AutoOutCrossLine();
				break;
			}

			break;
		case "c":
			
			switch (Colors) {
			case "L":
				SmartDashboard.putString("DB/String 6", "Runnning here strat: " + strategy);
				m_autonomousCommand = strategy.equals("primary") ? new AutoInCurvesDrop() : new AutoInCrossLine();
				
				break;
			case "R":
				m_autonomousCommand = strategy.equals("primary") ? new AutoInStraightDrop() : new AutoInCrossLine();
				break;
			}
			break;
		case "r":
			switch (Colors) {
			case "R":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutSwitchDrop(1) : new AutoOutCrossLine();
				break;
			case "L":
				m_autonomousCommand = new AutoOutCrossLine();
				break;
			}
			break;
		}
		
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
