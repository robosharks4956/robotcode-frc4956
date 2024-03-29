// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DefaultClimb;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.DefaultIntake;
import frc.robot.commands.DefaultShoot;
import frc.robot.commands.DriveToNote;
import frc.robot.commands.MobilityCommand;
import frc.robot.commands.Shoot;
import frc.robot.commands.TimedDelay;
import frc.robot.commands.TimedIntake;
import frc.robot.commands.VibrateController;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.NoteCamera;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.*;

import java.util.Map;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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
  private final Climber climber = new Climber();
  private final LEDs leds = new LEDs();
 // private final AprilTagCamera aprilTagCamera = new AprilTagCamera();
  private final NoteCamera noteCamera = new NoteCamera();
  private final CommandXboxController driveController =
    new CommandXboxController(DRIVE_CONTROLLER_PORT);
  private final CommandXboxController supportController =
    new CommandXboxController(SUPPORT_CONTROLLER_PORT);
  private ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

  private GenericEntry maxspeed = driveTab
    .add("Max Speed", 1)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 0, "max", 2))
    .getEntry();
  private GenericEntry fieldrelative2 = driveTab
    .add("Field Relative", true)
    .withWidget(BuiltInWidgets.kToggleSwitch)
    .getEntry();
  private GenericEntry delay = driveTab
    .add("Autonomous Delay", 0)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 0, "max", 10))
    .getEntry();

  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    intake.setDefaultCommand(
      new DefaultIntake(supportController, intake));
    shooter.setDefaultCommand(new DefaultShoot(shooter, supportController));
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
    m_chooser.addOption("#2 Leave", new MobilityCommand(drivetrain, 1.5, 70, 0));
    m_chooser.addOption("#3 Shoot, Drive to Note", new TimedDelay(() -> delay.getDouble(0))
      .andThen (new Shoot(intake, shooter, 1))
      .andThen(new DriveToNote(drivetrain, noteCamera, intake)));
    m_chooser.addOption("#4 Shoot and Leave", new TimedDelay(() -> delay.getDouble(0))
      .andThen(new Shoot(intake, shooter,1))
      .andThen(new MobilityCommand(drivetrain, 1.5, 70, 0)));
    m_chooser.addOption("#5 Shoot, Drive to Note, Shoot", new TimedDelay(() -> delay.getDouble(0))
      .andThen(new Shoot(intake, shooter, 1))
      .andThen(new DriveToNote(drivetrain, noteCamera, intake))
      .andThen(new TimedIntake(intake, .05))
      .andThen(new MobilityCommand(drivetrain, 1, 70, 0))
      .andThen(new MobilityCommand(drivetrain, 3.2, -70, 0))
      .andThen(new Shoot(intake, shooter, 1)));
    m_chooser.addOption("#6 Amp (Blue)", new MobilityCommand(drivetrain, 0.75, 0, -30)
      .andThen(new Shoot(intake, shooter, 0.275))
      .andThen(new WaitCommand(0.5)
      .andThen(new MobilityCommand(drivetrain, 5, 0, -30))));
    m_chooser.addOption("#6 Amp (Red)", new MobilityCommand(drivetrain, 0.75, 0, 30)
      .andThen(new Shoot(intake, shooter, 0.275))
      .andThen(new WaitCommand(0.5)
      .andThen(new MobilityCommand(drivetrain, 5, 0, 30))));
    m_chooser.addOption("#7 Shoot and Leave Diagonal Source Side", new TimedDelay(() -> delay.getDouble(0))
      .andThen(new Shoot(intake, shooter, 1))
      .andThen(new MobilityCommand(drivetrain, 2.5, 70, 0)));
    m_chooser.addOption("#8 Shoot and Leave Diagonal Amp Side RED", new TimedDelay(() -> delay.getDouble(0))
      .andThen(new Shoot(intake, shooter, 1))
      .andThen(new MobilityCommand(drivetrain, 2.5, 70, -40)));
    m_chooser.addOption("#9 Shoot and Leave Diagonal Amp Side BLUE", new TimedDelay(() -> delay.getDouble(0))
      .andThen(new Shoot(intake, shooter, 1))
      .andThen(new MobilityCommand(drivetrain, 2.5, 70, 40)));
    m_chooser.addOption("#10 Shoot", new TimedDelay(() -> delay.getDouble(0))
    .andThen(new Shoot(intake, shooter, 1)));
    //m_chooser.addOption("#10 Drive to Speaker", new DriveToAprilTag(drivetrain, aprilTagCamera, true, 0.1));
    //m_chooser.addOption("#11 Drive to Amp", new DriveToAprilTag(drivetrain, aprilTagCamera, false, 0.1));
    //m_chooser.addOption("#11 Turn 90 Degrees", new TurnToAngle(drivetrain, 90));
    
    

    driveTab.add("Autonomous", m_chooser);

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
    supportYButton.whileTrue(new Shoot(intake, shooter, 1));

    //final Trigger supportBButton = supportController.b();
    //supportBButton.whileTrue(new AmpShoot(shooter));

    // Reset gyro on driver back button press
    final Trigger driverBackButton = driveController.back();
    driverBackButton.onTrue(new InstantCommand(()->drivetrain.zeroGyroscope()));

    final Trigger driverYButton = driveController.y();
    driverYButton.whileTrue(new DriveToNote(drivetrain, noteCamera, intake));

    // Vibrate controllers when color sensor detects a note
    final Trigger colorSensorTrigger = new Trigger(() -> intake.getColorSensor());
    colorSensorTrigger.onTrue(new VibrateController(0.55, 1, driveController, supportController));

    final Trigger foundTargetTrigger = new Trigger(() -> noteCamera.hasTarget);
    foundTargetTrigger.whileTrue(
      new VibrateController(0.1, 0.5, driveController, supportController).andThen(new WaitCommand(0.15)).repeatedly());
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
    if (DriverStation.getAlliance().isPresent()){
      if (DriverStation.getAlliance().get() == Alliance.Blue) {
      leds.setColor(LEDs.Color.blue);
    }
     if (DriverStation.getAlliance().get() == Alliance.Red) {
      leds.setColor(LEDs.Color.red);
    }
   }
  }
}
