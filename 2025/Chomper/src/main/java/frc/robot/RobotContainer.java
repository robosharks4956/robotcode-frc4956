// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.*;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.OperatorConstants.*;

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
  private final TailFin tailFin = new TailFin();
  private final AngleMotor angleMotor = new AngleMotor();

  private final CommandXboxController driveController =
    new CommandXboxController(DRIVE_CONTROLLER_PORT);
  private final CommandXboxController supportController =
    new CommandXboxController(SUPPORT_CONTROLLER_PORT);

  

  private final SendableChooser<Command> chooser = new SendableChooser<Command>();

  private final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

  private final GenericEntry fieldRelative = driveTab
    .add("Field Relative", true)
    .withWidget(BuiltInWidgets.kToggleSwitch)
    .getEntry();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    driveTab.add("Autonomous", chooser);
    SmartDashboard.putData("Autonomous", chooser);

    chooser.addOption("#1 Nothing.", new InstantCommand());

    chooser.setDefaultOption("#2 Leave.", drivetrain.distanceDriveCommand(0, 1, 0.3));

    chooser.addOption("#3: Timed drive and drop into L1.", Commands.sequence(
      drivetrain.timeDriveCommand(0, 0.2, 0, 1.5),
      drivetrain.timeDriveCommand(0, 0.1, 0, 2.5),
      coralManipulator.unlatchCommand(),
      Commands.waitSeconds(1),
      coralManipulator.latchCommand(),
      drivetrain.timeDriveCommand(0.05, 0, 0, 1.5)
    ));

    chooser.addOption("#4: Timed drive and drop into L4.", Commands.sequence(
      drivetrain.timeDriveCommand(0, 0.2, 0, 1.5),
      drivetrain.timeDriveCommand(0, 0.1, 0, 2.5),
      drivetrain.timeDriveCommand(0, -0.05, 0, 0.25),
      lift.setPositionCommand(-46),
      Commands.waitSeconds(3),
      drivetrain.timeDriveCommand(0, 0.05, 0, 0.25),
      coralManipulator.unlatchCommand(),
      Commands.waitSeconds(1),
      coralManipulator.latchCommand(),
      Commands.waitSeconds(1),
      drivetrain.timeDriveCommand(0, -0.05, 0, 0.25),
      lift.setPositionCommand(0),
      Commands.waitSeconds(3)
    ));

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
    double speed = 1;
    if(driveController.rightBumper().getAsBoolean()){
      speed=.1;
    }
    if(driveController.leftBumper().getAsBoolean()){
      speed=.3;
    }
    drivetrain.setDefaultCommand(drivetrain.controllerDriveCommand(
      () -> (modifyAxis(driveController.getLeftX(), driveController.rightBumper().getAsBoolean() ? 0.1 : driveController.leftBumper().getAsBoolean() ? 1 : 0.5, 0.05, 3)),
      () -> (modifyAxis(driveController.getLeftY(), driveController.rightBumper().getAsBoolean() ? 0.1 : driveController.leftBumper().getAsBoolean() ? 1 : 0.5, 0.05, 3)),
      () -> (modifyAxis(driveController.getRightX(), driveController.rightBumper().getAsBoolean() ? 0.15 : 0.75, 0.05, 3)),
      () -> fieldRelative.getBoolean(false)
    )); 

    driveController.back().onTrue(drivetrain.resetGyroCommand());

    angleMotor.setDefaultCommand(angleMotor.lowerCommand());

    tailFin.setDefaultCommand(tailFin.hangCommand(() -> modifyAxis(supportController.getLeftY(), 0.5, 0.05, 3)));    

    supportController.y().onTrue(angleMotor.upperCommand());
    supportController.x().onTrue(angleMotor.lowerCommand());    
    supportController.b().onTrue(coralManipulator.latchCommand());
    supportController.a().onTrue(coralManipulator.unlatchCommand());

    supportController.povLeft().onTrue(lift.setPositionCommand(-46));
    supportController.povUp().onTrue(lift.setPositionCommand(-23));
    supportController.povRight().onTrue(lift.setPositionCommand(-10));
    supportController.povDown().onTrue(lift.setPositionCommand(0));

    supportController.back().onTrue(lift.resetEncoderCommand());
  }

  /**
   * 
   * Returns a modified joystick axis for better controls.
   * @param input The unmodified joystick axis.
   * @param maxPercent The maximum distance from 0 the modified axis can be.
   * @param deadband The distance from 0 where the unmodified axis can be treated as 0.
   * @param smoothing The exponent used to smooth out the axis.
   * @return The modified joystick axis.
   */
  private double modifyAxis(double input, double maxPercent, double deadband, double smoothing) {
    return MathUtil.clamp(
      Math.copySign(Math.pow(Math.abs(MathUtil.applyDeadband(input, deadband)), smoothing), input) * maxPercent,
      -maxPercent,
      maxPercent
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
