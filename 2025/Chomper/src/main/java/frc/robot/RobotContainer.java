// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.*;

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
  private final Lift lift = new Lift();
  private final Latch latch = new Latch();
  private final Hang hang = new Hang();
  private final Angle angle = new Angle();

  private final CommandXboxController driveController = new CommandXboxController(DRIVE_CONTROLLER_PORT);
  private final CommandXboxController supportController = new CommandXboxController(SUPPORT_CONTROLLER_PORT);

  private final SendableChooser<Command> chooser = new SendableChooser<Command>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    SmartDashboard.putString("Message for drive team", "WE LOVE YOU DRIVE TEAM!");
    SmartDashboard.putData("Autonomous", chooser);

    chooser.addOption("#1 Nothing.", new InstantCommand());

    chooser.setDefaultOption("#2 Leave.", drivetrain.timeDriveCommand(0, 0.2, 0, 2));

    chooser.addOption("#3: Timed drive and drop into L1 from middle.", Commands.sequence(
      drivetrain.resetGyroCommand(),
      angle.lowerCommand(),
      latch.latchCommand(),
      drivetrain.timeDriveCommand(0, 0.1, 0, 5.5),
      drivetrain.timeDriveCommand(0, 0.5, 0, 0.5),
      latch.unlatchCommand(),
      Commands.waitSeconds(1),
      drivetrain.timeDriveCommand(0.25, 0, 0, 0.5),
      latch.latchCommand()
    ));

    chooser.addOption("#4: Timed drive and drop into L1 from left side.", Commands.sequence(
      drivetrain.resetGyroCommand(),
      angle.lowerCommand(),
      latch.latchCommand(),
      drivetrain.timeDriveCommand(0, 0.1, 0, 8.5),
      latch.unlatchCommand(),
      Commands.waitSeconds(1),
      drivetrain.timeDriveCommand(-0.25, 0, 0, 0.5),
      latch.latchCommand()
    ));

    chooser.addOption("#5: Timed drive and drop into L1 from right side.", Commands.sequence(
      drivetrain.resetGyroCommand(),
      angle.lowerCommand(),
      latch.latchCommand(),
      drivetrain.timeDriveCommand(0, 0.1, 0, 8.5),
      latch.unlatchCommand(),
      Commands.waitSeconds(1),
      drivetrain.timeDriveCommand(0.25, 0, 0, 0.5),
      latch.latchCommand()
    ));

    chooser.addOption("#6: Timed drive and drop into L4 from middle.", Commands.sequence(
      drivetrain.resetGyroCommand(),
      angle.lowerCommand(),
      latch.latchCommand(),
      drivetrain.timeDriveCommand(0, 0.1, 0, 5.5),
      drivetrain.timeDriveCommand(0, 0.5, 0, 0.5),
      drivetrain.timeDriveCommand(0, -0.05, 0, 0.25),
      lift.toL4Command(),
      Commands.waitSeconds(3),
      drivetrain.timeDriveCommand(0, 0.05, 0, 0.25),
      latch.unlatchCommand(),
      Commands.waitSeconds(1),
      latch.latchCommand(),
      Commands.waitSeconds(1),
      drivetrain.timeDriveCommand(0, -0.05, 0, 0.25),
      lift.toL1Command()
    ));

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

    driveController.back().onTrue(drivetrain.resetGyroCommand());
    driveController.y().whileTrue(drivetrain.lockPoseCommand());

    hang.setDefaultCommand(hang.hangCommand(supportController::getLeftY));

    supportController.y().onTrue(angle.upperCommand());
    supportController.x().onTrue(angle.lowerCommand());
    supportController.b().onTrue(latch.latchCommand());
    supportController.a().onTrue(latch.unlatchCommand());

    supportController.povLeft().onTrue(lift.toL4Command());
    supportController.povUp().onTrue(lift.toL3Command());
    supportController.povRight().onTrue(lift.toL2Command());
    supportController.povDown().onTrue(lift.toL1Command());

    supportController.back().onTrue(lift.resetEncoderCommand());
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
