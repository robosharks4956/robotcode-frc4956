// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Autos;
import frc.robot.commands.DefaultAim;
import frc.robot.commands.DefaultClimb;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.commands.DefaultIntake;
import frc.robot.commands.DefaultShoot;
import frc.robot.commands.DriveToNote;
import frc.robot.commands.Shoot;
import frc.robot.subsystems.Aimer;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NoteCamera;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.*;

import java.util.Map;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  private final Drivetrain drivetrain = new Drivetrain();
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final Aimer aimer = new Aimer();
  private final Climber climber = new Climber();
  //private final AprilTagCamera aprilTagCamera = new AprilTagCamera();
  //private final NoteCamera noteCamera = new NoteCamera();
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
    intake.setDefaultCommand(new DefaultIntake(supportController, intake));
    shooter.setDefaultCommand(new DefaultShoot(supportController, shooter));
    aimer.setDefaultCommand(new DefaultAim(aimer, () -> MathUtil.applyDeadband(supportController.getRightY(), 0.05)));
    climber.setDefaultCommand(new DefaultClimb(supportController, climber));
    drivetrain.setDefaultCommand(new DefaultDriveCommand(
      drivetrain,
      () -> -modifyAxis((driveController.getLeftY())) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
      () -> -modifyAxis((driveController.getLeftX())) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
      () -> -modifyAxis((driveController.getRightX()))
        * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
      () -> fieldrelative2.getBoolean(true),
      () -> maxspeed.getDouble(1)));
      
    driveTab.addNumber("Voltage", () -> RobotController.getBatteryVoltage())
      .withWidget(BuiltInWidgets.kVoltageView)
      .withProperties(Map.of("min", 0, "max", 13));
    putDriveControls(); 

    m_chooser.setDefaultOption("#1 Nothing", new InstantCommand());
    m_chooser.addOption("#2 Leave", getMobilityCommand(1.5, 50));
    //m_chooser.addOption("#3 Drive to Note", new DriveToNote(drivetrain, noteCamera, driveController, intake));
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
    Trigger yButton = supportController.y();
    yButton.whileTrue(new Shoot(intake, shooter));

    // Open climber latch servos on support controller A button press
    Trigger aButton = supportController.a();
    aButton.onTrue(new InstantCommand(()-> {climber.setLatch(true);}, climber));

    // Close climber latch servos on support controller B button press
    Trigger bButton = supportController.b();
    bButton.onTrue(new InstantCommand(()-> {climber.setLatch(false);}, climber));
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

  private static double modifyAxis(double value) {
    value = MathUtil.applyDeadband(value, 0.05);
    value = Math.copySign(value * value, value);
    return value;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_chooser.getSelected();
  }
}
