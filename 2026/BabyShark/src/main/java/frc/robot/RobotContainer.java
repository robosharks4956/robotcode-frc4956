// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import org.photonvision.PhotonCamera;

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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.ShootAndFeed;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    // The robot's subsystems
    private final DriveSubsystem m_robotDrive = new DriveSubsystem();
    private final Shooter m_shooter = new Shooter();
    private final Agitator m_agitator = new Agitator();
    private final Intake m_intake = new Intake();
    private final Arm m_arm = new Arm();
    private final Climber m_climber = new Climber();
    private final Feeder m_feeder = new Feeder();
    private final AprilTagCamera m_camera = new AprilTagCamera();

    // The driver's controller
    CommandXboxController m_driverController = new CommandXboxController(OIConstants.kDriverControllerPort);
    CommandXboxController m_supportController = new CommandXboxController(OIConstants.kSupportControllerPort);

    boolean fieldRelative = true;

    private final SendableChooser<Command> chooser = new SendableChooser<Command>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {

        // Populate auton choices on dashboard
        SmartDashboard.putString("Message for drive team", "WE LOVE YOU DRIVE TEAM!");
        SmartDashboard.putData("Autonomous Chooser", chooser);

        chooser.setDefaultOption("#1 Nothing", new InstantCommand());

        chooser.addOption("#2 Shoot", Commands.sequence(
                m_arm.setSpeed(-0.5).withTimeout(0.8),
                m_shooter.chargeCommand(.55).withTimeout(2),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.setVelocityCommand(2940),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5),
                Commands.parallel(
                        m_arm.setSpeed(0.5),
                        m_intake.intakeCommand(0.65)).withTimeout(0.5),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.setVelocityCommand(2940),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5)

        ));

        chooser.addOption("#3 ShootDelay5sec", Commands.sequence(
                m_arm.setSpeed(-0.5).withTimeout(0.8),
                m_arm.setSpeed(0).withTimeout(5),
                m_shooter.chargeCommand(.55).withTimeout(2),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.setVelocityCommand(2940),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5),
                Commands.parallel(
                        m_arm.setSpeed(0.5),
                        m_intake.intakeCommand(0.65)).withTimeout(0.5),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.setVelocityCommand(2940),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5)

        ));

        chooser.addOption("#4 ShootPickupDepot", Commands.sequence(
                m_arm.setSpeed(-0.5).withTimeout(0.8),
                m_shooter.chargeCommand(.55).withTimeout(2),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.setVelocityCommand(2940),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5),
                Commands.parallel(
                        m_arm.setSpeed(0.5),
                        m_intake.intakeCommand(0.65)).withTimeout(0.5),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.setVelocityCommand(2940),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5),
                m_robotDrive.driveCommand(0, 0, 0, fieldRelative).withTimeout(0.5),
                Commands.parallel(
                        m_robotDrive.driveCommand(-0.5, 0, 0, fieldRelative),
                        m_intake.intakeCommand(1)
                )
        ));

        chooser.addOption("TESTING PURPOSES ONLY", Commands.sequence(
                m_robotDrive.driveCommand(-0.1, -0.2, 0.6, fieldRelative).withTimeout(0.5),
                Commands.parallel(
                        m_arm.setSpeed(-0.5).withTimeout(0.5),
                        m_robotDrive.driveCommand(0, -0.1, 0, fieldRelative).withTimeout(2.5),
                        m_intake.intakeCommand(1)
                ).withTimeout(3),
                m_robotDrive.driveCommand(0, 0.1, -0.3, fieldRelative).withTimeout(0.3),
                m_shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)).withTimeout(1.5),
                Commands.parallel(
                        m_shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)),
                        m_feeder.shootCommand(0.5),
                        m_agitator.agitatorCommand(0.3)
                        
                )/*,
                m_robotDrive.driveCommand(1, 0, 0, fieldRelative).withTimeout(1),
                m_shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)).withTimeout(2),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5),
                Commands.parallel(
                        m_arm.setSpeed(0.5),
                        m_intake.intakeCommand(0.65)).withTimeout(0.5),
                Commands.parallel(
                        //m_shooter.chargeCommand(.55),
                        m_shooter.chargeCommandPID(SmartDashboard.getNumber("targetRPM", 0)),
                        m_agitator.agitatorCommand(.3),
                        m_feeder.shootCommand(.5)).withTimeout(5)
                */
        ));
   
        chooser.addOption("Trajectory test", getTrajectoryCommand());

        // Configure the button bindings
        configureButtonBindings();

        m_arm.setDefaultCommand(m_arm.manualControl(m_supportController::getLeftY));
        m_climber.setDefaultCommand(m_climber.manualControl(m_supportController::getRightY));

        // Configure default commands
        m_robotDrive.setDefaultCommand(
                // The left stick controls translation of the robot.
                // Turning is controlled by the X axis of the right stick.
                new RunCommand(
                        () -> m_robotDrive.drive(
                                -MathUtil.applyDeadband(
                                        -m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                                -MathUtil.applyDeadband(
                                        -m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                                -MathUtil.applyDeadband(
                                        m_driverController.getRightX(), OIConstants.kDriveDeadband),
                                fieldRelative),
                        m_robotDrive));

        // m_shooter.setDefaultCommand(m_shooter.shooterCommand(m_supportController::getRightTriggerAxis));
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
        m_driverController
                .rightBumper()
                .whileTrue(new RunCommand(() -> m_robotDrive.setX(), m_robotDrive));

        m_driverController
                .start()
                .onTrue(new InstantCommand(() -> m_robotDrive.zeroHeading(), m_robotDrive));

        m_driverController
                .back()
                .onTrue(new InstantCommand(() -> fieldRelative = !fieldRelative, m_robotDrive));

        SmartDashboard.getNumber("targetPitch", 0);

        
        // THIS IS CURRENTLY BROKEN; FOR SOME REASON, THE PITCH DEFAULTS TO ZERO
        m_supportController.a().whileTrue(new ShootAndFeed(m_shooter, m_feeder, 0.5, () -> SmartDashboard.getNumber("targetRPM", 0), m_supportController));

        // PID Style
        m_supportController.x().whileTrue(new ShootAndFeed(m_shooter, m_feeder, 0.5, () -> 2940, m_supportController));
        m_supportController.y().whileTrue(new ShootAndFeed(m_shooter, m_feeder, 0.5, () -> 3530, m_supportController));
        m_supportController.b().whileTrue(new ShootAndFeed(m_shooter, m_feeder, 0.5, () -> 5500, m_supportController));
        m_supportController.povLeft().whileTrue(m_shooter.chargeCommandPID(3000));

        // Separate shooter command just for testing purposes
        m_supportController.povUp().whileTrue(m_feeder.shootCommand(0.55));

        // Old Style
        //m_supportController.x().whileTrue(m_shooter.chargeCommand(0.55));
        //m_supportController.y().whileTrue(m_shooter.chargeCommand(0.65));
        //m_supportController.b().whileTrue(m_shooter.chargeCommand(1.0));
        
       // m_supportController.rightTrigger().whileTrue(m_feeder.shootCommand(0.5));   
        m_supportController.leftTrigger().whileTrue(m_feeder.shootCommand(-0.5));

        // Change feeder motor speeds to be different if needed
        m_supportController.rightBumper().whileTrue(m_agitator.agitatorCommand(0.3));
        m_supportController.leftBumper().whileTrue(m_intake.intakeCommand(1));
        m_supportController.povDown().whileTrue(m_intake.intakeCommand(-1));

 
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
          List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
          // End 3 meters straight ahead of where we started, facing forward
          new Pose2d(3, 0, new Rotation2d(0)),
          config);
          
          var thetaController =
          new ProfiledPIDController(
          AutoConstants.kPThetaController, 0, 0,
          AutoConstants.kThetaControllerConstraints);
          thetaController.enableContinuousInput(-Math.PI, Math.PI);
          
          SwerveControllerCommand swerveControllerCommand =
          new SwerveControllerCommand(
          exampleTrajectory,
          m_robotDrive::getPose, // Functional interface to feed supplier
          DriveConstants.kDriveKinematics,
          
          // Position controllers
          new PIDController(AutoConstants.kPXController, 0, 0),
          new PIDController(AutoConstants.kPYController, 0, 0),
          thetaController,
          m_robotDrive::setModuleStates,
          m_robotDrive);
          
          // Reset odometry to the starting pose of the trajectory.
          m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());
          
          // Run path following command, then stop at the end.
          return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0,
          false));
    }
}
