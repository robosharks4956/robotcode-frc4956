
package org.usfirst.frc.team4956.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4956.robot.commands.AutoDriveStraight;
import org.usfirst.frc.team4956.robot.commands.AutoInCrossLine;
import org.usfirst.frc.team4956.robot.commands.AutoInCurvesDrop;
import org.usfirst.frc.team4956.robot.commands.AutoInStraightDrop;
import org.usfirst.frc.team4956.robot.commands.AutoOutCrossLine;
import org.usfirst.frc.team4956.robot.commands.AutoOutMayhem;
import org.usfirst.frc.team4956.robot.commands.AutoOutScaleDrop;
import org.usfirst.frc.team4956.robot.commands.AutoOutSwitchDrop;
import org.usfirst.frc.team4956.robot.commands.DriveStraight;
import org.usfirst.frc.team4956.robot.commands.FaceTarget;
import org.usfirst.frc.team4956.robot.commands.LeftGear;
import org.usfirst.frc.team4956.robot.commands.MidGear;
import org.usfirst.frc.team4956.robot.commands.RightGear;
import org.usfirst.frc.team4956.robot.commands.TurnBabyTurn;
import org.usfirst.frc.team4956.robot.subsystems.Camera;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4956.robot.subsystems.EdDriveTrain;
import org.usfirst.frc.team4956.robot.subsystems.RopeClimber;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	boolean driveTrainInt = false;
	public static OI oi;
	public static DriveTrain drivetrain;
	public static RopeClimber ropeclimber;
	public static Camera camera;
	
	Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override	
	public void robotInit() {
		
		// Erase the You Picked box so drivers don't get confused
		SmartDashboard.putString("DB/String 4", "^Type Edison or Tesla");
		
		oi = new OI();
		camera = new Camera();
		camera.startCapture();

	}
	
	private void driveTrainInit() {
		if (driveTrainInt == false) {
			//Chooses which robot being used
			String robotSelected = SmartDashboard.getString("DB/String 3", "Default");
			if (robotSelected.equalsIgnoreCase("edison")) {
				SmartDashboard.putString("DB/String 4", "You picked: Edison");
				drivetrain = new EdDriveTrain();
				}	
			else {
				SmartDashboard.putString("DB/String 4", "You picked: Tesla");
				drivetrain = new DriveTrain();
				ropeclimber = new RopeClimber();
			}
			driveTrainInt = true;
		}
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
			
		default:
			//autonomousCommand = new AutoDriveStraight(0.65,2);
			autonomousCommand = new MidGear();
			break;
		}

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
		
		//camera.visionStart();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		
		driveTrainInit();
		camera.setCameraBright();
		
		// Don't enable this line when testing
		// camera.visionStop();
		
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
