// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.commands.DefaultIntake;
import frc.robot.commands.DefaultShoot;
import frc.robot.commands.Shoot;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NoteCamera;
import frc.robot.subsystems.Shooter;

import java.util.Map;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
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
  // The robot's subsystems and commands are defined here...

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final Drivetrain drivetrain = new Drivetrain();
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final AprilTagCamera aprilTagCamera = new AprilTagCamera();
  private final NoteCamera noteCamera = new NoteCamera();
  private final CommandXboxController driveController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final CommandXboxController supportController =
      new CommandXboxController(OperatorConstants.kSupportControllerPort);
        private ShuffleboardTab drivetab = Shuffleboard.getTab("Drive");
  private ShuffleboardTab supporttab = Shuffleboard.getTab("Support");
   private GenericEntry maxspeed = drivetab
      .add("Max Speed", 1)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", 0, "max", 1))
      .getEntry();

  private GenericEntry fieldrelative2 = drivetab
      .add("Field Relative", true)
      .withWidget(BuiltInWidgets.kToggleSwitch)
      .getEntry();
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    intake.setDefaultCommand(new DefaultIntake(supportController, intake));
    shooter.setDefaultCommand(new DefaultShoot(supportController, shooter));
    drivetrain.setDefaultCommand(new DefaultDriveCommand(
        drivetrain,
        () -> -modifyAxis((driveController.getLeftY())) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
        () -> -modifyAxis((driveController.getLeftX())) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
        () -> -modifyAxis((driveController.getRightX()))
            * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
        () -> fieldrelative2.getBoolean(true),
        () -> maxspeed.getDouble(1)));
    drivetab
       .addNumber("Voltage", () -> RobotController.getBatteryVoltage())
       .withWidget(BuiltInWidgets.kVoltageView)
       .withProperties(Map.of("min", 0, "max", 13));
      putDriveControls(); 

    m_chooser.setDefaultOption("#1 Nothing", new InstantCommand());
    m_chooser.addOption("#2 Mobility", getMobilityCommand(4.5, 50));
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
    private Command getMobilityCommand(double timeout, double speed) {
    return new RunCommand(() -> {
      drivetrain.drive(
        ChassisSpeeds.fromFieldRelativeSpeeds(
          -speed,
              0,
              0,
              drivetrain.getGyroscopeRotation()));
    }, drivetrain).repeatedly().withTimeout(timeout);
  }

   private static double deadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  private void putDriveControls() {
    ShuffleboardTab tab = Shuffleboard.getTab("Controls");
    tab.add("Reset Gyro", "Driver Back Button");
  }

   private static double modifyAxis(double value) {
    // Deadband
    value = deadband(value, 0.05);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }
  private void configureBindings() {
    Trigger ybutton = supportController.y();
    ybutton.whileTrue(new Shoot(intake, shooter));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto();
  }
}
