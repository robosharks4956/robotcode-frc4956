
package org.usfirst.frc.team4956.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team4956.robot.commands.AutoDriveStraight;
import org.usfirst.frc.team4956.robot.commands.AutoLowbar;
import org.usfirst.frc.team4956.robot.commands.AutoLowbar2;
import org.usfirst.frc.team4956.robot.subsystems.BallLift;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	//subsystems
	public static DriveTrain drivetrain;
	public static BallLift balllift;
	public static Solenoid door;
	public static Solenoid ejector;
	public static OI oi;
	
    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		
		drivetrain = new DriveTrain();
		balllift = new BallLift();
		
		ejector = new Solenoid(RobotMap.ejectorSolenoid);
		ejector.set(false);
		
		door = new Solenoid(RobotMap.doorSolenoid);
		door.set(true);
		
		// OI is last since it needs the other subsystems
		oi = new OI();
        
        SmartDashboard.putString("DB/String 1", "");
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any
     *  subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * Initialize auton. Runs once when auton starts.
	 * Reads String 0 from the default dashboard and picks an auton mode based on what the string says.
	 */
    public void autonomousInit()
    {
		String autoSelected = SmartDashboard.getString("DB/String 0", "Default");
		
		SmartDashboard.putString("DB/String 1", "You picked:"+autoSelected);
		
		
		switch(autoSelected) {
		case "lowbar":
			autonomousCommand = new AutoLowbar();
			break;
		case "lowbar2": // Goes backward when finished
			autonomousCommand = new AutoLowbar2();
			break;
		case "nothing":
			autonomousCommand = new AutoDriveStraight(0, 0);
			break;
			
		case "strong":
			autonomousCommand = new AutoDriveStraight(-.80, 3.5);
			break;
			
		default:
			autonomousCommand = new AutoDriveStraight(-0.65,4);
			break;
		}
		
    	drivetrain.reset();
		
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        drivetrain.reset();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //log(); // Comment this line out when on wireless
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    /**
	 * The log method puts interesting information to the SmartDashboard.
	 */
    private void log() {
        drivetrain.log();
        balllift.log();
    }
}
