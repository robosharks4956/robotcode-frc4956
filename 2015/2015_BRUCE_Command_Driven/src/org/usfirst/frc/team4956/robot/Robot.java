package org.usfirst.frc.team4956.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import org.usfirst.frc.team4956.robot.commands.Autonomous;
import org.usfirst.frc.team4956.robot.subsystems.Jaw;
import org.usfirst.frc.team4956.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4956.robot.subsystems.Elevator;

public class Robot extends IterativeRobot 
{
	public static DriveTrain drivetrain;
	public static OI oi;
	public static Jaw jaw;
	public static Elevator elevator;
	CameraServer server;
    Command autonomousCommand;

    
    public void robotInit()  // This is called when robot starts up
    {    	
    	// Initialize all subsystems
        drivetrain = new DriveTrain();
        elevator = new Elevator();
		jaw = new Jaw();
        oi = new OI();
        
        
        server = CameraServer.getInstance();
        server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam0");
        
        autonomousCommand = new Autonomous();
    }	
    
	public void disabledPeriodic() 
	{
		Scheduler.getInstance().run();
	}

    public void autonomousInit() 
    {
    	drivetrain.reset();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    } 

    public void autonomousPeriodic()   // Periodically calls through autonomous
    {
        Scheduler.getInstance().run();
    }

    public void teleopInit() 
    {
		/*This makes sure that the autonomous stops running when
        teleop starts running. If you want the autonomous to 
        continue until interrupted by another command, remove
        this line or comment it out.*/
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
    }
    
}
