/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4956.robot;

import org.usfirst.frc.team4956.robot.commands.AutoDriveStraight;
import org.usfirst.frc.team4956.robot.commands.AutoInCrossLine;
import org.usfirst.frc.team4956.robot.commands.AutoInCurvesDrop;
import org.usfirst.frc.team4956.robot.commands.AutoInStraightDrop;
import org.usfirst.frc.team4956.robot.commands.AutoOutCrossLine;
import org.usfirst.frc.team4956.robot.commands.AutoOutMayhem;
import org.usfirst.frc.team4956.robot.commands.AutoOutScaleDrop;
import org.usfirst.frc.team4956.robot.commands.AutoOutSwitchDrop;
import org.usfirst.frc.team4956.robot.commands.TurnBabyTurn;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4956.robot.subsystems.Hooker;
import org.usfirst.frc.team4956.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
	public static Lift lift;
	public static Hooker hook;

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		drivetrain = new DriveTrain();
		lift = new Lift();
		hook = new Hooker();
		oi = new OI();
	}
	
	public void diveTrainInit() {
		drivetrain = new DriveTrain();
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
	 * You can add additional auto modes by adding additional commands to the
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
		String Colors = gameData.substring(0, 2);
		
		switch (autoPosition) {
		case "nothing":
		case "":
			System.out.println("nothing is chosen");
			m_autonomousCommand = new AutoDriveStraight(200, true);
			break;
		case "turnbaby":
			m_autonomousCommand = new TurnBabyTurn();
			break;
		case "l":
			switch (Colors) {
			case "LL": 
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutSwitchDrop(-1) : new AutoOutScaleDrop(-1);
				break;
			case "LR":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutSwitchDrop(-1) : new AutoOutCrossLine();
				break;
			case "RL":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutScaleDrop(-1) : new AutoOutCrossLine();
				break;
			case "RR":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutMayhem(-1) : new AutoOutCrossLine();
				break;
			}

			break;
		case "c":
			switch (Colors) {
			case "LL":
			case "LR":
				m_autonomousCommand = strategy.equals("primary") ? new AutoInCurvesDrop() : new AutoInCrossLine();
				SmartDashboard.putString("DB/String 6", "Runnning here strat: " + strategy);
				break;
			case "RL":
			case "RR":
				m_autonomousCommand = strategy.equals("primary") ? new AutoInStraightDrop() : new AutoInCrossLine();
				break;
			}
			break;
		case "r":
			switch (Colors) {
			case "RR":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutSwitchDrop(1) : new AutoOutScaleDrop(1);
				break;
			case "LR":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutScaleDrop(1) : new AutoOutScaleDrop(1);
				break;
			case "RL":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutSwitchDrop(1) : new AutoOutCrossLine();
				break;
			case "LL":
				m_autonomousCommand = strategy.equals("primary") ? new AutoOutMayhem(1) : new AutoOutCrossLine();
				break;
			}
			break;
		}
	

		//m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		double currentLeftReading = Robot.drivetrain.getLeftPosition();
		double currentRightReading = Robot.drivetrain.getRightPosition();
		if (leftLastReading != currentLeftReading || rightLastReading != currentRightReading) {
			leftLastReading = currentLeftReading; 
			rightLastReading = currentRightReading;
			System.out.println("Left Encoder: "+currentLeftReading);
			System.out.println("Right Encoder: "+currentRightReading);	
		}
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}
	double rightCurrentMax = 0;
	double leftCurrentMax = 0;
	double leftLastReading = 0;
	double rightLastReading = 0;
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		double currentLeftReading = Robot.drivetrain.getLeftPosition();
		double currentRightReading = Robot.drivetrain.getRightPosition();
		if (leftLastReading != currentLeftReading || rightLastReading != currentRightReading) {
			leftLastReading = currentLeftReading; 
			rightLastReading = currentRightReading;
			System.out.println("Left Encoder: "+currentLeftReading);
			System.out.println("Right Encoder: "+currentRightReading);	
		}
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
