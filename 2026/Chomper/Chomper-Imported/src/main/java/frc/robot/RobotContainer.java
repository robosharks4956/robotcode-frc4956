// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.LockOn;
import frc.robot.subsystems.*;
import org.photonvision.PhotonCamera;

import static frc.robot.Constants.OperatorConstants.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final Drivetrain drivetrain = new Drivetrain();

  private final CommandXboxController driveController = new CommandXboxController(DRIVE_CONTROLLER_PORT);
  //private final CommandXboxController supportController = new CommandXboxController(SUPPORT_CONTROLLER_PORT);

  private final SendableChooser<Command> chooser = new SendableChooser<Command>();

  //AprilTagCamera apriltagcamera = new AprilTagCamera();


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    SmartDashboard.putData("Autonomous", chooser);

    chooser.addOption("#1 Nothing.", new InstantCommand());

    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    drivetrain.setDefaultCommand(drivetrain.controllerDriveCommand(
        driveController::getLeftX,
        driveController::getLeftY,
        driveController::getRightX,
        driveController.rightBumper(),
        driveController.leftBumper()));
      /**
       * LockOn(apriltagcamera, drivetrain)
       */
    driveController.back().onTrue(drivetrain.resetGyroCommand());
    driveController.y().whileTrue(drivetrain.lockPoseCommand());
    //driveController.x().whileTrue(new LockOn(apriltagcamera, drivetrain));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return chooser.getSelected();
  }
}
