// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DefaultAim;
import frc.robot.commands.DefaultClimb;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.DefaultIntake;
import frc.robot.commands.DefaultShoot;
import frc.robot.commands.DriveToAprilTag;
import frc.robot.commands.DriveToNote;
import frc.robot.commands.Shoot;
import frc.robot.commands.TimedIntake;
import frc.robot.commands.VibrateController;
import frc.robot.subsystems.Aimer;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.NoteCamera;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.*;

import java.util.Map;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final Aimer aimer = new Aimer();
  private final Climber climber = new Climber();
  private final LEDs leds = new LEDs();
  //private final AprilTagCamera aprilTagCamera = new AprilTagCamera();
  private final NoteCamera noteCamera = new NoteCamera();
  private final CommandXboxController driveController =
    new CommandXboxController(DRIVE_CONTROLLER_PORT);
  private final CommandXboxController supportController =
    new CommandXboxController(SUPPORT_CONTROLLER_PORT);
  private ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
  
  private GenericEntry maxspeed = driveTab
    .add("Max Speed", 1)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 0, "max", 1))
    .getEntry();
  private GenericEntry fieldrelative2 = driveTab
    .add("Field Relative", true)
    .withWidget(BuiltInWidgets.kToggleSwitch)
    .getEntry();
  private ShuffleboardTab supportTab = Shuffleboard.getTab("Support");

  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    intake.setDefaultCommand(
      new DefaultIntake(supportController, intake));
    shooter.setDefaultCommand(new DefaultShoot(shooter, supportController));
    aimer.setDefaultCommand(new DefaultAim(aimer, () -> modifyAxis(supportController.getRightY(), 0.05, 2)));
    climber.setDefaultCommand(new DefaultClimb(climber, supportController));
    drivetrain.setDefaultCommand(new DefaultDrive(
      drivetrain,
      () -> -modifyAxis((driveController.getLeftY()), 0.05, 2) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
      () -> -modifyAxis((driveController.getLeftX()), 0.05, 2) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
      () -> -modifyAxis((driveController.getRightX()), 0.05, 2)
        * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
      () -> fieldrelative2.getBoolean(true),
      () -> maxspeed.getDouble(1)));

    driveTab.addNumber("Voltage", () -> RobotController.getBatteryVoltage())
      .withWidget(BuiltInWidgets.kVoltageView)
      .withProperties(Map.of("min", 0, "max", 13));
    putDriveControls(); 

    m_chooser.setDefaultOption("#1 Nothing", new InstantCommand());
    m_chooser.addOption("#2 Leave", getMobilityCommand(1.5, 70));
    m_chooser.addOption("#3 Shoot, Drive to Note", new Shoot(intake, shooter)
      .andThen(new DriveToNote(drivetrain, noteCamera, intake)));
    m_chooser.addOption("#4 Shoot and Leave", new Shoot(intake, shooter)
      .andThen(getMobilityCommand(1.5, 70)));
    m_chooser.addOption("#5 Shoot, Drive to Note, Shoot", new Shoot(intake, shooter)
      .andThen(new DriveToNote(drivetrain, noteCamera, intake))
      .andThen(new TimedIntake(intake, .05))
      .andThen(getMobilityCommand(1.85, -70))
      .andThen(new Shoot(intake, shooter)));
    SmartDashboard.putData(m_chooser);

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
    // Run shoot command on Y button
    final Trigger supportYButton = supportController.y();
    supportYButton.whileTrue(new Shoot(intake, shooter));

    // Open climber latch servos on support controller A button press
    final Trigger supportAButton = supportController.a();
    supportAButton.onTrue(new InstantCommand(()-> {climber.setLatch(true);}, climber));

    // Close climber latch servos on support controller B button press
    final Trigger supportBButton = supportController.b();
    supportBButton.onTrue(new InstantCommand(()-> {climber.setLatch(false);}, climber));

    // Reset gyro on driver back button press
    final Trigger driverBackButton = driveController.back();
    driverBackButton.onTrue(new InstantCommand(()->drivetrain.zeroGyroscope()));

    /*final Trigger driverXButton = driveController.x();
    driverXButton.onTrue(new InstantCommand(() -> {
      if (DriverStation.getAlliance().get() == Alliance.Blue) {
        new DriveToAprilTag(drivetrain, aprilTagCamera, AprilTagIDs.BLUE_SPEAKER).schedule();
      }
      if (DriverStation.getAlliance().get() == Alliance.Red) {
        new DriveToAprilTag(drivetrain, aprilTagCamera, AprilTagIDs.RED_SPEAKER).schedule();
      }
    }));*/

    final Trigger driverYButton = driveController.y();
    driverYButton.whileTrue(new DriveToNote(drivetrain, noteCamera, intake));

    // Vibrate controllers when color sensor detects a note
    final Trigger colorSensorTrigger = new Trigger(() -> intake.getColorSensor());
    colorSensorTrigger.onTrue(new VibrateController(0.55, 1, driveController, supportController));

    final Trigger foundTargetTrigger = new Trigger(() -> noteCamera.hasTarget);
    foundTargetTrigger.whileTrue(
      new VibrateController(0.1, 0.5, driveController, supportController).andThen(new WaitCommand(0.15)).repeatedly());
  }

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

  private void putDriveControls() {
    ShuffleboardTab tab = Shuffleboard.getTab("Controls");
    tab.add("Drive", "Driver Left Stick");
    tab.add("Rotate", "Driver Right Stick");
    tab.add("Reset Gyro", "Driver Back Button");
    tab.add("Aim", "Support Right Stick (up/down)");
    tab.add("Intake", "Support Right Bumper (in) / Left Bumper (out)");
    tab.add("Shoot", "Support Right Trigger (shoot) / Left Trigger (UNshoot)");
    tab.add("Climb Latches", "Support A Button (release) / B Button (lock)");
    tab.add("Climb", "Support Left Stick (up/down)");
  }

  private static double modifyAxis(double value, double deadband, double smoothing) {
    value = MathUtil.applyDeadband(value, deadband);
    value = Math.copySign(Math.pow(Math.abs(value), smoothing), value);
    return value;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }

  public void setAllianceLEDs() {
    if (DriverStation.getAlliance().get() == Alliance.Blue) {
      leds.setColor(LEDs.Color.blue);
    }
    if (DriverStation.getAlliance().get() == Alliance.Red) {
      leds.setColor(LEDs.Color.red);
    }
  }
}
