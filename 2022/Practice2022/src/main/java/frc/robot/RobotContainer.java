// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.AutoDriveAndShoot;
import frc.robot.commands.DriveWithController;
import frc.robot.commands.IntakeWithController;
import frc.robot.commands.ShootWithController;
import frc.robot.commands.TimeDrive;
import frc.robot.commands.WinchWithController;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Winch;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final XboxController driver = new XboxController(0);
  private final XboxController support = new XboxController(1);

  // The robot's subsystems and commands are defined here...

  private final DriveTrain driveTrain = new DriveTrain();
  private final Intake  intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final Winch winch= new Winch(); 
  private final DriveWithController driveCommand = new DriveWithController(driveTrain, driver);
  private final IntakeWithController intakeCommand = new IntakeWithController(intake, support);
  private final ShootWithController shootCommand = new ShootWithController(shooter, support);
  private final WinchWithController winchCommand= new WinchWithController(winch, support);

  // Auton commands
  Command driveAndShoot = new AutoDriveAndShoot(driveTrain, shooter, intake);
  Command justReverse = new SequentialCommandGroup(
    new TimeDrive(driveTrain, 2, 0, -0.25, 0));
    Command waitandshoot= new SequentialCommandGroup(
       new TimeDrive(driveTrain, 5, 0, 0, 0),
    new AutoDriveAndShoot(driveTrain, shooter, intake)); 

  // Auton command chooser
  SendableChooser<Command> chooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    driveTrain.setDefaultCommand(driveCommand);
    intake.setDefaultCommand(intakeCommand);
    shooter.setDefaultCommand(shootCommand);
    winch.setDefaultCommand(winchCommand);
    chooser.setDefaultOption("Drive and Shoot", driveAndShoot);
    chooser.addOption("wait and shoot 5seconds", waitandshoot);
    chooser.addOption("Just Reverse", 
    new SequentialCommandGroup(
      new TimeDrive(driveTrain, 2, 0, -0.5, 0)));
  
    SmartDashboard.putData(chooser);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return chooser.getSelected();
    //return new AutoDriveAndShoot(driveTrain, shooter, intake);
  }
}
