package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AutoBalance;
// import frc.robot.commands.CranePositionControl;
 import frc.robot.commands.DefaultDriveCommand;
// import frc.robot.subsystems.Crane;
// import frc.robot.subsystems.DoubleSolenoidSubsystem;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LEDs;
// import frc.robot.subsystems.Latch;
// import frc.robot.subsystems.Slider;

import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.*;

import java.util.Map;

/**
 * This class is where the bulk of the robot is declared, including subsystems,
 * default commands,
 * controller button bindings, etc.
 */
public class RobotContainer {

  public final Drivetrain drivetrain = new Drivetrain();
  // private final Crane crane = new Crane();
  // private final Slider slider = new Slider();
  // private final DoubleSolenoidSubsystem baseslider = new DoubleSolenoidSubsystem(BASE_SOLENOID_FORWARD,
      // BASE_SOLENOID_REVERSE, "Base Solenoid");
  // private final DoubleSolenoidSubsystem grabber = new DoubleSolenoidSubsystem(GRABBER_SOLENOID_FORWARD,
      // GRABBER_SOLENOID_REVERSE, "Grabber Solenoid");
  // private final Latch latch = new Latch();
 // private final LEDs m_leds = new LEDs();
  private final CommandXboxController driverController = new CommandXboxController(kDriverControllerPort);
  // private final CommandXboxController supportController = new CommandXboxController(kSupportControllerPort);

  private ShuffleboardTab drivetab = Shuffleboard.getTab("Drive");

  private GenericEntry maxspeed = drivetab
      .add("Max Speed", 1)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", 0, "max", 1))
      .getEntry();

  private GenericEntry fieldrelative2 = drivetab
      .add("Field Relative", false)
      .withWidget(BuiltInWidgets.kToggleSwitch)
      .getEntry();

  // private final SlewRateLimiter Xfilter = new SlewRateLimiter(10);
  // private final SlewRateLimiter Yfilter = new SlewRateLimiter(10);
  // private final SlewRateLimiter Rfilter = new SlewRateLimiter(10);
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Set up the default command for the drivetrain.
    // The controls are for field-oriented driving:
    // Left stick Y axis -> forward and backwards movement
    // Left stick X axis -> left and right movement
    // Right stick X axis -> rotation

    drivetrain.setDefaultCommand(new DefaultDriveCommand(
        drivetrain,
        () -> -modifyAxis((driverController.getLeftY())) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
        () -> -modifyAxis((driverController.getLeftX())) * Drivetrain.MAX_VELOCITY_METERS_PER_SECOND,
        () -> -modifyAxis((driverController.getRightX()))
            * Drivetrain.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
        () -> fieldrelative2.getBoolean(false),
        () -> maxspeed.getDouble(1)));
    drivetab
        .addNumber("Voltage", () -> RobotController.getBatteryVoltage())
        .withWidget(BuiltInWidgets.kVoltageView)
        .withProperties(Map.of("min", 0, "max", 13));
    putDriveControls();

    // Configure the button bindings
    configureButtonBindings();

    
    // SlewRateLimiter craneLimiter = new SlewRateLimiter(.4);
    // boolean useVelocityControl = false;
    // if (useVelocityControl) {
      // Crane with velocity control
      // crane.setDefaultCommand(new CranePositionControl(crane, latch, supportController));
     // } else {
      // Crane with normal power
    //   crane.setDefaultCommand(new RunCommand(() -> {
    //     if (Math.abs(supportController.getLeftY()) > .03) {
    //       if (supportController.getLeftY() < 0) {
    //         if (latch.encoder.getDistance() < 5) {
    //           crane.set(0);
    //         } else 
    //           crane.set(craneLimiter.calculate(supportController.getLeftY()) * 0.3);
    //       }
    //       else 
    //       {
    //         double cranespeed = 1.0;
    //         if (supportController.x().getAsBoolean()== true) {
    //           cranespeed = 1;
    //         }
    //         crane.set(craneLimiter.calculate(supportController.getLeftY()) * cranespeed);}
    //     } else
    //       crane.set(0);
    //   }, crane));
    // }

    // slider.setDefaultCommand(new RunCommand(() -> {
    //   // If we're 
    //   if (supportController.getRightY() < 0) {
    //     slider.set(supportController.getRightY() * .6);
    //   } else {
    //     slider.set(supportController.getRightY() * .4);
    //   }
      
    // }, slider));

    // latch.setDefaultCommand(new RunCommand(() -> {
    //   if (Math.abs(supportController.getLeftY()) > .05) {
    //     latch.set(.4);
    //   } else
    //     latch.set(0);
    // }, latch));

    m_chooser.setDefaultOption("#1 Nothing", new InstantCommand());

    m_chooser.addOption("#2 Mobility", getMobilityCommand(4.5, 50));

    m_chooser.addOption("#3 Autobalance",
        new SequentialCommandGroup(getMobilityCommand(2, 50),
            getMobilityCommand(1, 30),
            new AutoBalance(drivetrain)));

    m_chooser.addOption("#4 Cube",
        new SequentialCommandGroup(getMobilityCommand(.25, -70),
            getMobilityCommand(.25, 70)));

    m_chooser.addOption("#5 Cube & Mobility",
        new SequentialCommandGroup(getMobilityCommand(.25, -70),
            getMobilityCommand(.25, 70),
            getMobilityCommand(4.5, 50)));

    m_chooser.addOption("#6 Autobalance & Cube",
        new SequentialCommandGroup(getMobilityCommand(.25, -70),
            getMobilityCommand(.25, 70),
            getMobilityCommand(1.5, 50),
            new AutoBalance(drivetrain)));

    m_chooser.addOption("#7 Autobalance & Mobility",
        new SequentialCommandGroup(getMobilityCommand(3, 50),
            getMobilityCommand(2.25, 30),
            getMobilityCommand(2.25, -50),
            getMobilityCommand(.5, -30),
            new AutoBalance(drivetrain)));

    m_chooser.addOption("#8 Autobalance, Cube, Mobility",
        new SequentialCommandGroup(getMobilityCommand(.25, -70),
            getMobilityCommand(.25, 70),
            getMobilityCommand(3, 50),
            getMobilityCommand(2.5, 30),
            getMobilityCommand(2, -50),
            getMobilityCommand(.5, -30),
            new AutoBalance(drivetrain)));
    SmartDashboard.putData(m_chooser);

    // baseslider.set(false);
    // grabber.set(false);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Driver back button zeros the gyroscope
    Trigger backButton = driverController.back();
    backButton.onTrue(new InstantCommand(drivetrain::zeroGyroscope));
    // Trigger leftbumper = supportController.leftBumper();
    // leftbumper.onTrue(new InstantCommand(() -> grabber.set(false)));
    // Trigger rightbumper = supportController.rightBumper();
    // rightbumper.onTrue(new InstantCommand(() -> grabber.set(true)));
    // Trigger ybutton = supportController.y();
    // ybutton.onTrue(new InstantCommand(() -> baseslider.set(true)));
    // Trigger abutton = supportController.a();
    // abutton.onTrue(new InstantCommand(() -> baseslider.set(false)));
    // Trigger backButton2 = supportController.back();
    // backButton2.onTrue(new InstantCommand(latch::ResetEncoder));
    Trigger bbutton = driverController.b();
    bbutton.onTrue(new AutoBalance(drivetrain));
    bbutton.onFalse(drivetrain.getDefaultCommand());
    Trigger rTrigger = driverController.rightTrigger();
    rTrigger.onTrue(new InstantCommand(()  -> maxspeed.setDouble(.25)));
    rTrigger.onFalse(new InstantCommand(() -> maxspeed.setDouble(1)));
  }

  /**
   * Add a list of controls to the SmartDashboard
   */
  private void putDriveControls() {
    ShuffleboardTab tab = Shuffleboard.getTab("Controls");
    tab.add("Reset Gyro", "Driver Back Button");
    // tab.add("Grabber Open", "Left Bumper");
    // tab.add("Grabber Close", "Right Bumper");
    // tab.add("Base Cylinder Forward", "Y Button");
    // tab.add("Base Cylinder Backward", "A Button");
    // tab.add("Raise Arm", "Left Stick Up");
    // tab.add("Lower Arm", "Left Stick Down");
    // tab.add("Extend Arm Slider", "Right Stick Up");
    // tab.add("Retract Arm Slider", "Right Stick Down");
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   * 
   */
  public Command getAutonomousCommand() {
    // Return the selected Autonomous from Shuffleboard
    return m_chooser.getSelected();
  }

  private Command getMobilityCommand(double timeout, double speed) {
    return new RunCommand(() -> {
      drivetrain.drive(
          new ChassisSpeeds(
              -speed,
              0,
              0));
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

  private static double modifyAxis(double value) {
    // Deadband
    value = deadband(value, 0.05);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }

  public void setAllianceLEDs() {
    if (DriverStation.getAlliance() == Alliance.Red) {
       //m_leds.setRed();
    }
    if (DriverStation.getAlliance() == Alliance.Blue) {
      // m_leds.setBlue();
    }
  }

  public void setRainbow() {
    //m_leds.rainbow();
  }
}