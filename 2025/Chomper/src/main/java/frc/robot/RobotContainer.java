// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DistanceDrive;
import frc.robot.commands.LiftPosition;
import frc.robot.subsystems.*;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.OperatorConstants;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final Drivetrain drivetrain = new Drivetrain();
  private final Lift lift = new Lift();
  private final CoralManipulator coralManipulator = new CoralManipulator();

  private final CommandXboxController driverController =
    new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);
  private final CommandXboxController supportController =
    new CommandXboxController(OperatorConstants.SUPPORT_CONTROLLER_PORT);

  private final SendableChooser<Command> chooser = new SendableChooser<Command>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    SmartDashboard.putBoolean("Field Relative", true);

    chooser.setDefaultOption("#1 Nothing", new InstantCommand());
    chooser.addOption("#2 Leave", new DistanceDrive(1, 0, .25, drivetrain));

    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    drivetrain.setDefaultCommand(drivetrain.controllerDriveCommand(
      () -> modifyAxis(driverController.getLeftX(), 1, 0.05, 3),
      () -> modifyAxis(driverController.getLeftY(), 1, 0.05, 3),
      () -> modifyAxis(driverController.getRightX(), 1, 0.05, 3),
      () -> SmartDashboard.getBoolean("Field Relative", false)
    ));

    // lift.setDefaultCommand(new RunCommand(() -> {
    //   //lift.setVelocity(modifyAxis(supportController.getLeftY(), 1, 0.05, 3));
    // }, lift));

    supportController.y().onTrue(new RunCommand(coralManipulator::goUp, coralManipulator));
    supportController.x().onTrue(new RunCommand(coralManipulator::goDown, coralManipulator));    
    supportController.b().onTrue(new RunCommand(coralManipulator::latch, coralManipulator));
    supportController.a().onTrue(new RunCommand(coralManipulator::unlatch, coralManipulator));
    supportController.povLeft().whileTrue(new LiftPosition(-46, lift));
    supportController.povUp().whileTrue(new LiftPosition(-25, lift));
    supportController.povRight().whileTrue(new LiftPosition(-10, lift));
  }

  /**
   * Returns a modified joystick axis for better controls.
   * @param input The unmodified joystick axis.
   * @param maxPercent The maximum distance from 0 the modified axis can be.
   * @param deadband The distance from 0 where the unmodified can be treated as 0.
   * @param smoothing The exponent used to smooth out the axis.
   * @return The modified joystick axis.
   */
  private double modifyAxis(double input, double maxPercent, double deadband, double smoothing) {
    return MathUtil.clamp(Math.copySign(
      Math.pow(Math.abs(MathUtil.applyDeadband(input, deadband)), smoothing), input), -maxPercent, maxPercent
    );
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
