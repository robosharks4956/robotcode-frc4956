package frc.robot;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import choreo.auto.AutoFactory;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.ShootAndFeed;
import frc.robot.subsystems.Shooter;

/*
* This class is where the bulk of the robot should be declared.  Since Command-based is a
* "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
* periodic methods (other than the scheduler calls).  Instead, the structure of the robot
* (including subsystems, commands, and button mappings) should be declared here.
*/
public class RobotContainer {

  // The robot's subsystems
  private final DriveSubsystem robotDrive = new DriveSubsystem();
  private final Shooter shooter = new Shooter();
  private final Agitator agitator = new Agitator();
  private final Intake intake = new Intake();
  private final Arm arm = new Arm();
  private final Climber climber = new Climber();
  private final Feeder feeder = new Feeder();
  private final AprilTagCamera camera = new AprilTagCamera();
  private final AutoFactory autoFactory;

  CommandXboxController driverController = new CommandXboxController(OIConstants.kDriverControllerPort);
  CommandXboxController supportController = new CommandXboxController(OIConstants.kSupportControllerPort);

  boolean fieldRelative = true;

  private final SendableChooser<Command> chooser = new SendableChooser<Command>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // Send subsystem data to dashboard
    SmartDashboard.putData("Arm", arm);
    SmartDashboard.putData("Shooter", shooter);
    SmartDashboard.putData("Drivetrain", robotDrive);

    // Setup Choreo AutoFactory
    autoFactory = new AutoFactory(
        robotDrive::getPose, // A function that returns the current robot pose
        robotDrive::resetOdometry, // A function that resets the current robot pose to the
                                   // provided Pose2d
        robotDrive::followTrajectory, // The drive subsystem trajectory follower
        true, // If alliance flipping should be enabled
        robotDrive // The drive subsystem
    );

    // Populate auton choices on dashboard
    SmartDashboard.putData("Autonomous Chooser", chooser);

    chooser.setDefaultOption("#1 Nothing", new InstantCommand());

    // Shoot pre-loaded balls immediately
    chooser.addOption("#2 Shoot", shootCommand());

    // Wait 5 seconds then shoot all pre-loaded balls
    chooser.addOption("#3 ShootDelay5sec", Commands.sequence(
        arm.setSpeedCmd(0).withTimeout(5), // 5s delay to start
        shootCommand()));

    chooser.addOption("#4 ShootPickupDepot", Commands.sequence(
        shootCommand(),
        robotDrive.driveCmd(0, 0, 0, fieldRelative).withTimeout(0.5),
        Commands.parallel(
            robotDrive.driveCmd(-0.5, 0, 0, fieldRelative),
            intake.intakeCmd(1))));

    chooser.addOption("Trajectory test", getTrajectoryCommand());

    chooser.addOption("ChoreoShooter", Commands.sequence(
        autoFactory.resetOdometry("Shooter"),
        autoFactory.trajectoryCmd("Shooter")));

    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> robotDrive.drive(
                MathUtil.applyDeadband(
                    driverController.getLeftY(),
                    OIConstants.kDriveDeadband),
                MathUtil.applyDeadband(
                    driverController.getLeftX(),
                    OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(
                    driverController.getRightX(),
                    OIConstants.kDriveDeadband),
                fieldRelative),
            robotDrive));

    // TODO: Should we apply smoothing to the drive controls?

    arm.setDefaultCommand(arm.setSpeedCmd(supportController::getLeftY));
    climber.setDefaultCommand(climber.setSpeedCmd(supportController::getRightY));
  }

  /**
   * Get command to shoot the pre-loaded balls, used by a few auton modes.
   */
  public Command shootCommand() {

    // TODO: ShootAndFeed should also run the agitator, they're always used together
    // so it'd simplify the code

    return Commands.sequence(
        arm.setSpeedCmd(-0.5).withTimeout(0.8),
        // Spin up the shooter for a bit to save on running the agitator for no reason
        shooter.setVelocityCmd(Shooter.kNearShotRpm).withTimeout(1),
        // Shoot majority of the fuel
        Commands.parallel(
            new ShootAndFeed(shooter, feeder, () -> Shooter.kNearShotRpm,
                () -> true),
            agitator.agitateCmd()).withTimeout(4),
        // Run the arm up while running the intake to pull in remaining fuel, continue
        // spinning shooter to save time
        Commands.parallel(
            arm.setSpeedCmd(0.5),
            intake.intakeCmd(0.65),
            shooter.setVelocityCmd(Shooter.kNearShotRpm)).withTimeout(0.5),
        // Shoot the remaining fuel
        Commands.parallel(
            new ShootAndFeed(shooter, feeder, () -> Shooter.kNearShotRpm,
                () -> true),
            agitator.agitateCmd()).withTimeout(5));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of
   * its subclasses ({@link edu.wpi.first.wpilibj.Joystick} or
   * {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {

    // Driver right bumper sets the wheels into an X formation to prevent movement.
    driverController.rightBumper().whileTrue(robotDrive.setXCmd());

    driverController
        .start()
        .onTrue(new InstantCommand(robotDrive::zeroHeading, robotDrive));

    driverController
        .back()
        .onTrue(new InstantCommand(() -> fieldRelative = !fieldRelative, robotDrive));

    // Hold left bumper to drive with location locked onto a heading facing the goal
    driverController.leftBumper().whileTrue(robotDrive.driveOnHeadingCmd(driverController::getLeftY,
        driverController::getLeftX, () -> 0));
    // TODO: After testing, replace constant 0 with this::radiansToGoal

    SmartDashboard.getNumber("targetPitch", 0);

    BooleanSupplier shooterSafetySwitch = () -> supportController.getRightTriggerAxis() > 0.3;

    supportController.a().whileTrue(new ShootAndFeed(shooter, feeder,
        () -> SmartDashboard.getNumber("targetRPM", 0), shooterSafetySwitch));

    // Shoot and feed fuel at fixed ranges with fixed shooter velocities for each
    // range
    supportController.x().whileTrue(
        new ShootAndFeed(shooter, feeder, () -> Shooter.kNearShotRpm, shooterSafetySwitch));
    supportController.y().whileTrue(
        new ShootAndFeed(shooter, feeder, () -> Shooter.kMidShotRpm, shooterSafetySwitch));
    supportController.b().whileTrue(
        new ShootAndFeed(shooter, feeder, () -> Shooter.kFarShotRpm, shooterSafetySwitch));

    // Separate manual shooter and feeder commands just for testing purposes
    supportController.povUp().whileTrue(feeder.shootCmd());
    supportController.povLeft().whileTrue(shooter.chargeVelocityCmd(Shooter.kNearShotRpm));

    // Reverse the feeder with the left trigger
    supportController.leftTrigger().whileTrue(feeder.reverseCmd());

    // Change feeder motor speeds to be different if needed
    supportController.rightBumper().whileTrue(agitator.agitateCmd());
    supportController.leftBumper().whileTrue(intake.intakeCmd(1));
    supportController.povDown().whileTrue(intake.intakeCmd(-1));
  }

  /**
   * Get the angle in radians between the robots current position and the center
   * of the goal. Points at either red or blue based on the alliance color
   * reported by the field system or drive station.
   */
  public double radiansToGoal() {

    var alliance = getAlliance();

    // Get goal coordinates based on alliance color
    double targetX = alliance == Alliance.Blue ? Constants.Field.kBlueGoalX : Constants.Field.kRedGoalX;
    double targetY = alliance == Alliance.Blue ? Constants.Field.kBlueGoalY : Constants.Field.kRedGoalY;

    // Get current robot coordinates
    Pose2d pose = robotDrive.getPose();
    double robotX = pose.getX();
    double robotY = pose.getY();

    // Shooter is back of robot, so get angle from goal to robot so that the
    // rotation will be away from the goal, which will result in shooter facing goal
    return Math.atan2(targetY - robotY, targetX - robotX);
  }

  public Alliance getAlliance() {
    Optional<Alliance> ally = DriverStation.getAlliance();

    if (ally.isPresent())
      return ally.get();
    else
      return Alliance.Blue;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return chooser.getSelected();
  }

  public Command getTrajectoryCommand() {
    TrajectoryConfig config = new TrajectoryConfig(
        AutoConstants.kMaxSpeedMetersPerSecond,
        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
        .setKinematics(DriveConstants.kDriveKinematics);

    // An example trajectory to follow. All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(0.5, 0.5), new Translation2d(1, -0.5)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(1.5, 0, new Rotation2d(-Math.PI / 2)),
        config);

    var thetaController = new ProfiledPIDController(
        AutoConstants.kPThetaController, 0, 0,
        AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        exampleTrajectory,
        robotDrive::getPose, // Functional interface to feed supplier
        DriveConstants.kDriveKinematics,

        // Position controllers
        new PIDController(AutoConstants.kPXController, 0, 0),
        new PIDController(AutoConstants.kPYController, 0, 0),
        thetaController,
        robotDrive::setModuleStates,
        robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> robotDrive.drive(0, 0, 0,
        false));
  }
}
