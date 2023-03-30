/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.ClimbWithController;
import frc.robot.commands.ConveyWithController;
import frc.robot.commands.DevonsOlderAndWiserAttemptAtFaceTarget;
import frc.robot.commands.DistanceDrive;
import frc.robot.commands.DriveStraightDenTurn;
import frc.robot.commands.DriveWithController;
import frc.robot.commands.ShootWithController;
import frc.robot.commands.SpinWithController;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.ClimberSolenoid;
import frc.robot.subsystems.ConveyBalls;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Kobe;
import frc.robot.subsystems.Spinner;
import frc.robot.subsystems.Winch;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public final DriveTrain drivetrain = new DriveTrain();
  private final Kobe kobe = new Kobe();
  private final ConveyBalls conveyballs = new ConveyBalls();
  //private final Winch winch = new Winch();
 // private final ClimberSolenoid climberSolenoid = new ClimberSolenoid(); // Hide because we arent using for at home
 // private final Spinner spinner = new Spinner(); // Hide because we arent using for at home

  private final XboxController driver = new XboxController(0);
  private final XboxController support = new XboxController(1);
  private final Camera camera = new Camera();

  //UsbCamera camera;
  
  private final DriveWithController drivewithcontroller = new DriveWithController(drivetrain, driver);
  private final ShootWithController shootwithcontroller = new ShootWithController(kobe, support);
  private final ConveyWithController conveywithcontroller = new ConveyWithController(conveyballs, support);
 // private final ClimbWithController climbWithController = new ClimbWithController(climberSolenoid, winch, support);
 // private final SpinWithController spinWithController = new SpinWithController(spinner, support);
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    drivetrain.setDefaultCommand(drivewithcontroller);
    kobe.setDefaultCommand(shootwithcontroller);
    conveyballs.setDefaultCommand(conveywithcontroller);
    //climberSolenoid.setDefaultCommand(climbWithController);
    //spinner.setDefaultCommand(spinWithController);

    //camera = CameraServer.getInstance().startAutomaticCapture(0);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    //return new SequentialCommandGroup(new DistanceDrive(m_drivetrain, 46, .4), new AngleTurn(m_drivetrain, 90, .4))
    
    // public low int Devon = Constants.noob;
    
   // return new DriveStraightDenTurn(drivetrain);
   /*Command shoot = new AutoShoot(this.kobe, this.conveyballs).withTimeout(3);
   return new SequentialCommandGroup(
     new DistanceDrive(this.drivetrain, 25, 0.6), shoot );*/
     return new DevonsOlderAndWiserAttemptAtFaceTarget(drivetrain, camera, true);

  }
}
