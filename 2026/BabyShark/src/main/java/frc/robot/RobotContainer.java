// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;
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
         autoFactory = new AutoFactory(
            robotDrive::getPose, // A function that returns the current robot pose
            robotDrive::resetOdometry, // A function that resets the current robot pose to the provided Pose2d
            robotDrive::followTrajectory, // The drive subsystem trajectory follower 
            true, // If alliance flipping should be enabled 
            robotDrive // The drive subsystem
        );

        SmartDashboard.putData("Arm", arm);
        SmartDashboard.putData("Shooter", shooter);
        SmartDashboard.putData("Drivetrain", robotDrive);

        // Populate auton choices on dashboard
        SmartDashboard.putString("Message for drive team", "WE LOVE YOU DRIVE TEAM!");
        SmartDashboard.putData("Autonomous Chooser", chooser);

        chooser.setDefaultOption("#1 Nothing", new InstantCommand());

        // Main command to shoot the pre-loaded balls, used by a few auton modes
        var shootCommand = Commands.sequence(
                arm.setSpeed(-0.5).withTimeout(0.8),
                shooter.setVelocityCommand(2940).withTimeout(1),
                Commands.parallel(
                        new ShootAndFeed(shooter, feeder, Feeder.kFeedSpeed, () -> 2940, () -> true),
                        agitator.agitatorCommand(.3)).withTimeout(4),
                Commands.parallel(
                        arm.setSpeed(0.5),
                        intake.intakeCommand(0.65)).withTimeout(0.5),
                Commands.parallel(
                        new ShootAndFeed(shooter, feeder, Feeder.kFeedSpeed, () -> 2940, () -> true),
                        agitator.agitatorCommand(.3)).withTimeout(5)
        );

        chooser.addOption("#2 Shoot", Commands.sequence(
                shootCommand
        ));

        /*chooser.addOption("#3 ShootDelay5sec", Commands.sequence(
                arm.setSpeed(0).withTimeout(5), // 5s delay to start
                shootCommand
        ));

        chooser.addOption("#4 ShootPickupDepot", Commands.sequence(
                shootCommand,
                robotDrive.driveCommand(0, 0, 0, fieldRelative).withTimeout(0.5),
                Commands.parallel(
                        robotDrive.driveCommand(-0.5, 0, 0, fieldRelative),
                        intake.intakeCommand(1)
                )
        ));/* */

        chooser.addOption("TESTING PURPOSES ONLY", Commands.sequence(
                robotDrive.driveCommand(-0.1, -0.2, 0.6, fieldRelative).withTimeout(0.5),
                Commands.parallel(
                        arm.setSpeed(-0.5).withTimeout(0.5),
                        robotDrive.driveCommand(0, -0.1, 0, fieldRelative).withTimeout(2.5),
                        intake.intakeCommand(1)
                ).withTimeout(3),
                robotDrive.driveCommand(0, 0.1, -0.3, fieldRelative).withTimeout(0.3),
                shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)).withTimeout(1.5),
                Commands.parallel(
                        shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)),
                        feeder.shootCommand(0.5),
                        agitator.agitatorCommand(0.3)
                )
        ));
   
        chooser.addOption("Trajectory test", getTrajectoryCommand());
        Command shooterTrajectory = autoFactory.trajectoryCmd("Shooter");
        chooser.addOption("ChoreoShooter", shooterTrajectory);

        // Configure the button bindings
        configureButtonBindings();

        arm.setDefaultCommand(arm.setSpeed(supportController::getLeftY));
        climber.setDefaultCommand(climber.manualControl(supportController::getRightY));

        // Configure default commands
        robotDrive.setDefaultCommand(
                // The left stick controls translation of the robot.
                // Turning is controlled by the X axis of the right stick.
                new RunCommand(
                        () -> robotDrive.drive(
                                MathUtil.applyDeadband(
                                        driverController.getLeftY(), OIConstants.kDriveDeadband),
                                MathUtil.applyDeadband(
                                        driverController.getLeftX(), OIConstants.kDriveDeadband),
                                -MathUtil.applyDeadband(
                                        driverController.getRightX(), OIConstants.kDriveDeadband),
                                fieldRelative),
                        robotDrive));
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
     * subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
     * passing it to a
     * {@link JoystickButton}.
     */
    private void configureButtonBindings() {
        driverController
                .rightBumper()
                .whileTrue(new RunCommand(() -> robotDrive.setX(), robotDrive));

        driverController
                .start()
                .onTrue(new InstantCommand(() -> robotDrive.zeroHeading(), robotDrive));

        driverController
                .back()
                .onTrue(new InstantCommand(() -> fieldRelative = !fieldRelative, robotDrive));

        SmartDashboard.getNumber("targetPitch", 0);

        BooleanSupplier shooterSafetySwitch = () -> supportController.getRightTriggerAxis() > 0.3;
        
        supportController.a().whileTrue(new ShootAndFeed(shooter, feeder, Feeder.kFeedSpeed, () -> SmartDashboard.getNumber("targetRPM", 0), shooterSafetySwitch));

        // PID Style
        supportController.x().whileTrue(new ShootAndFeed(shooter, feeder, Feeder.kFeedSpeed, () -> 2940, shooterSafetySwitch));
        supportController.y().whileTrue(new ShootAndFeed(shooter, feeder, Feeder.kFeedSpeed, () -> 3530, shooterSafetySwitch));
        supportController.b().whileTrue(new ShootAndFeed(shooter, feeder, Feeder.kFeedSpeed, () -> 5500, shooterSafetySwitch));
        supportController.povLeft().whileTrue(shooter.chargeCommandPID(3000));

        // Separate feeder command just for testing purposes
        supportController.povUp().whileTrue(feeder.shootCommand(0.55));
        
        supportController.leftTrigger().whileTrue(feeder.shootCommand(-0.5));

        // Change feeder motor speeds to be different if needed
        supportController.rightBumper().whileTrue(agitator.agitatorCommand(0.3));
        supportController.leftBumper().whileTrue(intake.intakeCommand(1));
        supportController.povDown().whileTrue(intake.intakeCommand(-1));

        // Arm commands
        // m_supportController.povUp().onTrue(m_arm.upperCommand());
        // m_supportController.povDown().onTrue(m_arm.lowerCommand());
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
        TrajectoryConfig config =
            new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared).setKinematics(DriveConstants.kDriveKinematics);
         
          // An example trajectory to follow. All units in meters.
          Trajectory exampleTrajectory =
          TrajectoryGenerator.generateTrajectory(
          // Start at the origin facing the +X direction
          new Pose2d(0, 0, new Rotation2d(0)),
          // Pass through these two interior waypoints, making an 's' curve path
          List.of(new Translation2d(0.5, 0.5), new Translation2d(1, -0.5)),
          // End 3 meters straight ahead of where we started, facing forward
          new Pose2d(1.5, 0, new Rotation2d(-Math.PI/2)),
          config);
          
          var thetaController =
          new ProfiledPIDController(
          AutoConstants.kPThetaController, 0, 0,
          AutoConstants.kThetaControllerConstraints);
          thetaController.enableContinuousInput(-Math.PI, Math.PI);
          
          SwerveControllerCommand swerveControllerCommand =
          new SwerveControllerCommand(
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
