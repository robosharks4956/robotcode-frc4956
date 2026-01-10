// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final Drivetrain drivetrain = new Drivetrain();
  private final Pneumatics pneumatics = new Pneumatics(2, 3);
  //private final Solenoid fireSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
  private final Shooter shooter = new Shooter();

  private final CommandXboxController driverController =
    new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);
  //private final CommandXboxController supportController =
  //  new CommandXboxController(OperatorConstants.SUPPORT_CONTROLLER_PORT);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    drivetrain.setDefaultCommand(new RunCommand(() -> {
      drivetrain.drive(
        driverController.getRightTriggerAxis() - driverController.getLeftTriggerAxis(),
        driverController.getLeftX());
    }, drivetrain));

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
    //driverController.a().onTrue(new RunCommand(pneumatics::forward, pneumatics));
    //driverController.b().onTrue(new RunCommand(pneumatics::off, pneumatics));
    //driverController.rightBumper().onTrue(new InstantCommand(fireSolenoid::toggle).andThen(Commands.waitSeconds(2)).andThen(fireSolenoid::toggle));

    // Charge the firing cylinder for a few seconds
    driverController.a().onTrue(new InstantCommand(pneumatics::forward).andThen(Commands.waitSeconds(3)).andThen(pneumatics::off));
    
    // Trigger firing solenoid
    driverController.rightBumper().onTrue(new InstantCommand(shooter::on).andThen(Commands.waitSeconds(0.6)).andThen(shooter::off));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}
