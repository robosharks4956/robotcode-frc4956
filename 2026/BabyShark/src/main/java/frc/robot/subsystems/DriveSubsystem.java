package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import choreo.trajectory.SwerveSample;
import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;

public class DriveSubsystem extends SubsystemBase {

  // Create MAXSwerveModules
  private final MAXSwerveModule m_frontLeft = new MAXSwerveModule(
      DriveConstants.kFrontLeftDrivingCanId,
      DriveConstants.kFrontLeftTurningCanId,
      DriveConstants.kFrontLeftChassisAngularOffset);

  private final MAXSwerveModule m_frontRight = new MAXSwerveModule(
      DriveConstants.kFrontRightDrivingCanId,
      DriveConstants.kFrontRightTurningCanId,
      DriveConstants.kFrontRightChassisAngularOffset);

  private final MAXSwerveModule m_rearLeft = new MAXSwerveModule(
      DriveConstants.kRearLeftDrivingCanId,
      DriveConstants.kRearLeftTurningCanId,
      DriveConstants.kBackLeftChassisAngularOffset);

  private final MAXSwerveModule m_rearRight = new MAXSwerveModule(
      DriveConstants.kRearRightDrivingCanId,
      DriveConstants.kRearRightTurningCanId,
      DriveConstants.kBackRightChassisAngularOffset);

  PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
  PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
  ProfiledPIDController headingController = new ProfiledPIDController(
      AutoConstants.kPThetaController, 0, 0,
      AutoConstants.kThetaControllerConstraints);

  // For heading stabilized with PID, what is the target?
  private double driverHeadingTargetRadians = 0;
  private double previousTurnRate = 0;

  // Field map used to send current robot pose to a field diagram on the dashboard
  public final Field2d field = new Field2d();

  // The gyro sensor
  private final AHRS m_gyro = new AHRS(NavXComType.kMXP_SPI);

  /**
   * Standard deviations of model states. Increase these numbers to trust your
   * model's state estimates less. This matrix is in the form [x, y, theta]ᵀ, with
   * units in meters and radians, then meters.
   */
  private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5));

  /**
   * Standard deviations of the vision measurements. Increase these numbers to
   * trust global measurements from vision less. This matrix is in the form [x, y,
   * theta]ᵀ, with units in meters and radians.
   */
  private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(10));

  // Odometry class for tracking robot pose
  SwerveDrivePoseEstimator m_odometry = new SwerveDrivePoseEstimator(
      DriveConstants.kDriveKinematics,
      getGyroscopeRotation(),
      new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_rearLeft.getPosition(),
          m_rearRight.getPosition()
      }, new Pose2d(),
      stateStdDevs,
      visionMeasurementStdDevs);

  DoubleLogEntry targetHeadingLog;
  DoubleLogEntry currentHeadingLog;
  DoubleLogEntry headingOutputLog;

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    // Usage reporting for MAXSwerve template
    HAL.report(tResourceType.kResourceType_RobotDrive, tInstances.kRobotDriveSwerve_MaxSwerve);
    SmartDashboard.putData("field", field);
    //SmartDashboard.putData("HeadingPID", headingController);

    headingController.enableContinuousInput(-Math.PI, Math.PI);
    DataLog log = DataLogManager.getLog();
    targetHeadingLog = new DoubleLogEntry(log, "/drivesubsystem/TargetHeading");
    currentHeadingLog = new DoubleLogEntry(log, "/drivesubsystem/CurrentHeading");
    headingOutputLog = new DoubleLogEntry(log, "/drivesubsystem/HeadingOutput");
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("FL Output %", m_frontLeft::getPercentOutput, null);
    builder.addDoubleProperty("FL Velocity mps", () -> m_frontLeft.getState().speedMetersPerSecond, null);
    builder.addDoubleProperty("Gyro", this::getGyroDegrees, null);
    builder.addDoubleProperty("Heading", this::getHeadingDegrees, null);
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(
        getGyroscopeRotation(),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        });

    // Send robot pose to dashboard
    field.setRobotPose(getPose());

    // Send swerve module states to the dashboard, can be used with AdvantageScope
    // to see how well the modules are keeping up with targets
    SmartDashboard.putNumberArray("swerve/measuredStates", getModuleStates());
    SmartDashboard.putNumberArray("swerve/desiredStates", getDesiredModuleStates());
    
    ChassisSpeeds measuredChassisSpeeds = getRobotVelocity();
    double[] measuredSpeeds = new double[3]; 
    measuredSpeeds[1] = measuredChassisSpeeds.vyMetersPerSecond;
    measuredSpeeds[0] = measuredChassisSpeeds.vxMetersPerSecond;
    measuredSpeeds[2] = Math.toDegrees(measuredChassisSpeeds.omegaRadiansPerSecond);
    SmartDashboard.putNumberArray("swerve/measuredChassisSpeeds", measuredSpeeds);

    ChassisSpeeds desiredChassisSpeeds = getDesiredVelocity();
    double[] desiredSpeeds = new double[3]; 
    desiredSpeeds[1] = measuredChassisSpeeds.vyMetersPerSecond;
    desiredSpeeds[0] = measuredChassisSpeeds.vxMetersPerSecond;
    desiredSpeeds[2] = Math.toDegrees(measuredChassisSpeeds.omegaRadiansPerSecond);
    SmartDashboard.putNumberArray("swerve/desiredChassisSpeeds", desiredSpeeds);

    SmartDashboard.putNumber("swerve/robotRotation", getHeadingDegrees());
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getEstimatedPosition();
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    m_gyro.reset();
    driverHeadingTargetRadians = getHeadingRadians();
  }

  public Rotation2d getGyroscopeRotation() {
    return Rotation2d.fromDegrees(-m_gyro.getAngle());
  }

  /**
   * Returns the heading of the robot according to the gyro.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getGyroDegrees() {
    return getGyroscopeRotation().getDegrees();
  }

  /**
   * Returns the heading of the robot according to the pose estimator.
   * 
   * @return Heading in degrees.
   */
  public double getHeadingDegrees() {
    return getPose().getRotation().getDegrees();
  }

  /**
   * Returns the heading of the robot according to the pose estimator.
   * 
   * @return Heading in radians.
   */
  public double getHeadingRadians() {
    return getPose().getRotation().getRadians();
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return -m_gyro.getRate();
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        getGyroscopeRotation(),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        },
        pose);
    driverHeadingTargetRadians = getHeadingRadians();
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed        Speed of the robot in the x direction (forward).
   * @param ySpeed        Speed of the robot in the y direction (sideways).
   * @param rot           Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the
   *                      field.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    // Convert the commanded speeds into the correct units for the drivetrain
    double xSpeedDelivered = xSpeed * DriveConstants.kMaxSpeedMetersPerSecond;
    double ySpeedDelivered = ySpeed * DriveConstants.kMaxSpeedMetersPerSecond;
    double rotDelivered = rot * DriveConstants.kMaxAngularSpeed;

    var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(
                xSpeedDelivered, ySpeedDelivered, rotDelivered, getGyroscopeRotation())
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));

    setModuleStates(swerveModuleStates);
  }

  public void driveFieldRelative(ChassisSpeeds speeds) {
    var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(speeds);
    setModuleStates(swerveModuleStates);
  }

  /**
   * Return a command to drive at fixed speeds.
   */
  public Command driveCmd(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    return run(() -> drive(xSpeed, ySpeed, rot, fieldRelative));
  }

  /**
   * Method to drive the robot using speeds from a controller info.
   *
   * @param xSpeed        Speed in the x direction (forward).
   * @param ySpeed        Speed in the y direction (sideways).
   * @param rotation      Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative
   *                      to the field.
   */
  public Command driveCmd(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier rotation,
      BooleanSupplier fieldRelative) {
    return run(() -> drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), rotation.getAsDouble(),
        fieldRelative.getAsBoolean()));
  }

  /** Sets the wheels into an X formation to prevent movement. */
  public Command setXCmd() {
    return run(() -> {
      m_frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
      m_frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
      m_rearLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
      m_rearRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    });
  }

    /**
   * Gets the current robot-relative velocity (x, y and omega) of the robot
   *
   * @return A ChassisSpeeds object of the current robot-relative velocity
   */
  public ChassisSpeeds getRobotVelocity()
  {
    return DriveConstants.kDriveKinematics.toChassisSpeeds(getModuleStates());
  }

    public ChassisSpeeds getDesiredVelocity()
  {
    return DriveConstants.kDriveKinematics.toChassisSpeeds(getDesiredModuleStates());
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DriveConstants.kMaxSpeedMetersPerSecond);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_rearLeft.setDesiredState(desiredStates[2]);
    m_rearRight.setDesiredState(desiredStates[3]);
  }

  /**
   * Gets the current swerve module states.
   */
  public SwerveModuleState[] getModuleStates() {
    return new SwerveModuleState[] {
        m_frontLeft.getState(),
        m_frontRight.getState(),
        m_rearLeft.getState(),
        m_rearRight.getState(),
    };
  }

  /**
   * Gets the desired swerve module states.
   */
  public SwerveModuleState[] getDesiredModuleStates() {
    return new SwerveModuleState[] {
        m_frontLeft.getDesiredState(),
        m_frontRight.getDesiredState(),
        m_rearLeft.getDesiredState(),
        m_rearRight.getDesiredState(),
    };
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    m_frontLeft.resetEncoders();
    m_rearLeft.resetEncoders();
    m_frontRight.resetEncoders();
    m_rearRight.resetEncoders();
  }

  public void stop() {
    drive(0, 0, 0, false);
  }

  public void followTrajectory(SwerveSample sample) {
    // Get the current pose of the robot
    Pose2d pose = getPose();

    var vx = sample.vx + xController.calculate(pose.getX(), sample.x);
    var vy = sample.vy + yController.calculate(pose.getY(), sample.y);
    var omega = sample.omega + headingController.calculate(pose.getRotation().getRadians(), sample.heading);

    SmartDashboard.putNumber("vx", vx);
    SmartDashboard.putNumber("vy", vy);
    SmartDashboard.putNumber("omega", omega);

    currentHeadingLog.log(pose.getRotation().getDegrees());
    targetHeadingLog.log(Units.radiansToDegrees(sample.heading));
    headingOutputLog.log(omega);

    ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
        vx,
        vy,
        omega,
        pose.getRotation()); // Drive relative to field based on where robot thinks it is

    // Apply the generated speeds
    driveFieldRelative(speeds);
  }

  public Command driveOnHeadingCmd(DoubleSupplier xSupplier, DoubleSupplier ySupplier,
      DoubleSupplier headingRadiansSupplier) {

    return run(() -> {

      var targetHeading = headingRadiansSupplier.getAsDouble();
      var currentHeading = getHeadingRadians();

      SmartDashboard.putNumber("Current Heading rad", currentHeading);
      SmartDashboard.putNumber("Target Heading rad", targetHeading);

      drive(
          xSupplier.getAsDouble(),
          ySupplier.getAsDouble(),
          // Set turn speed based on difference between target and current heading
          headingController.calculate(currentHeading, targetHeading),
          false);
    });
  }

  public Command driveWithPIDStabilization(DoubleSupplier xSupplier, DoubleSupplier ySupplier,
      DoubleSupplier rotationSupplier, BooleanSupplier fieldRelativeSupplier) {

    final double maxDifference = Math.PI / 4;

    return run(() -> {

      var requestedTurnRate = rotationSupplier.getAsDouble();
      var currentHeading = getHeadingRadians();

      // To avoid any weirdness stemming from getting stuck or being pinned by a
      // defence bot, ensure target is never more than 45 degrees from current
      driverHeadingTargetRadians = MathUtil.clamp(driverHeadingTargetRadians, currentHeading - maxDifference,
          currentHeading + maxDifference);

      // SmartDashboard.putNumber("Current Heading rad", currentHeading);
      // SmartDashboard.putNumber("Target Heading rad", driverHeadingTargetRadians);

      double turnSpeed = requestedTurnRate;

      if (requestedTurnRate == 0) {

        // Any time the driver lets off the stick, save current heading as the heading
        // to lock to
        if (previousTurnRate != 0) {
          driverHeadingTargetRadians = currentHeading;
        }

        // Set turn speed based on difference between target and current heading
        turnSpeed = headingController.calculate(currentHeading, driverHeadingTargetRadians);
      }

      previousTurnRate = requestedTurnRate;

      drive(
          xSupplier.getAsDouble(),
          ySupplier.getAsDouble(),
          turnSpeed,
          fieldRelativeSupplier.getAsBoolean());
    })
        // Any time this command starts, save current rotation in radians as a
        // starting point, and reset the timer
        .beforeStarting(() -> {
          // Save current rotation in radians as a starting point
          driverHeadingTargetRadians = getHeadingRadians();
          previousTurnRate = 0;
        });
  }

  /**
   * See {@link SwerveDrivePoseEstimator#addVisionMeasurement(Pose2d, double)}.
   */
  public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds) {
    m_odometry.addVisionMeasurement(visionMeasurement, timestampSeconds);
  }

  /**
   * See
   * {@link SwerveDrivePoseEstimator#addVisionMeasurement(Pose2d, double, Matrix)}.
   */
  public void addVisionMeasurement(
      Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> stdDevs) {
    m_odometry.addVisionMeasurement(visionMeasurement, timestampSeconds, stdDevs);
  }
}
