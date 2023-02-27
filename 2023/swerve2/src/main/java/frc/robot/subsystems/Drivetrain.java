package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.swervedrivespecialties.swervelib.MkSwerveModuleBuilder;
import com.swervedrivespecialties.swervelib.MotorType;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import com.swervedrivespecialties.swervelib.SwerveModule;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Drivetrain extends SubsystemBase {
  /**
   * The maximum voltage that will be delivered to the drive motors.
   * <p>
   * This can be reduced to cap the robot's maximum speed. Typically, this is useful during initial testing of the robot.
   */
  public static final double MAX_VOLTAGE = 12.0;

  //  The formula for calculating the theoretical maximum velocity is:
  //   <Motor free speed RPM> / 60 * <Drive reduction> * <Wheel diameter meters> * pi
  //  By default this value is setup for a Mk3 standard module using Falcon500s to drive.
  //  An example of this constant for a Mk4 L2 module with NEOs to drive is:
  //   5880.0 / 60.0 / SdsModuleConfigurations.MK4_L2.getDriveReduction() * SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI
  /**
   * The maximum velocity of the robot in meters per second.
   * <p>
   * This is a measure of how fast the robot should be able to drive in a straight line.
   */
  public static final double MAX_VELOCITY_METERS_PER_SECOND = 5880.0 / 60.0 / SdsModuleConfigurations.MK4_L2.getDriveReduction() * SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI;

  /**
   * The maximum angular velocity of the robot in radians per second.
   * <p>
   * This is a measure of how fast the robot can rotate in place.
   */
  // Here we calculate the theoretical maximum angular velocity. You can also replace this with a measured amount.
  public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = MAX_VELOCITY_METERS_PER_SECOND /
          Math.hypot(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0);

  private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
          // Front left
          new Translation2d(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Front right
          new Translation2d(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Back left
          new Translation2d(-DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Back right
          new Translation2d(-DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DRIVETRAIN_WHEELBASE_METERS / 2.0)
  );

  // These are our modules. We initialize them in the constructor.
  private final SwerveModule m_frontLeftModule;
  private final SwerveModule m_frontRightModule;
  private final SwerveModule m_backLeftModule;
  private final SwerveModule m_backRightModule;
  private final SwerveDriveOdometry odometry;

  // The important thing about how you configure your gyroscope is that rotating the robot counter-clockwise should
  // cause the angle reading to increase until it wraps back over to zero.
  private AHRS gyro;
  private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);
  //private ShuffleboardTab drivetab = Shuffleboard.getTab("Drive");
        //private GenericEntry gyroentry =
         //drivetab
        // .add("Gyro", 0)
         //.withWidget(BuiltInWidgets.kGyro)
         //.getEntry();

  public Drivetrain() {
    ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

    // TODO: Confirm that rotating the robot counter-clockwise causes the angle reading to increase until it wraps back over to zero
    gyro = new AHRS(Port.kMXP);
    
    SmartDashboard.putData("gyro",gyro);

    m_frontLeftModule = new MkSwerveModuleBuilder()
    .withLayout(tab.getLayout("Front Left Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(0, 0))
    .withGearRatio(SdsModuleConfigurations.MK4_L2)
    .withDriveMotor(MotorType.NEO, FRONT_LEFT_MODULE_DRIVE_MOTOR)
    .withSteerMotor(MotorType.NEO, FRONT_LEFT_MODULE_STEER_MOTOR)
    .withSteerEncoderAnalogChannel(FRONT_LEFT_MODULE_STEER_ENCODER)
    .withSteerOffset(FRONT_LEFT_MODULE_STEER_OFFSET)
    .build();

    m_frontRightModule = new MkSwerveModuleBuilder()
    .withLayout(tab.getLayout("Front Right Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(2, 0))
    .withGearRatio(SdsModuleConfigurations.MK4_L2)
    .withDriveMotor(MotorType.NEO, FRONT_RIGHT_MODULE_DRIVE_MOTOR)
    .withSteerMotor(MotorType.NEO, FRONT_RIGHT_MODULE_STEER_MOTOR)
    .withSteerEncoderAnalogChannel(FRONT_RIGHT_MODULE_STEER_ENCODER)
    .withSteerOffset(FRONT_RIGHT_MODULE_STEER_OFFSET)
    .build();

    // Invert front right because it's backwards for some reason
    var frontRightDrive = m_frontRightModule.getDriveMotor();
    frontRightDrive.setInverted(!frontRightDrive.getInverted());

    m_backLeftModule = new MkSwerveModuleBuilder()
    .withLayout(tab.getLayout("Back Left Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(4, 0))
    .withGearRatio(SdsModuleConfigurations.MK4_L2)
    .withDriveMotor(MotorType.NEO, BACK_LEFT_MODULE_DRIVE_MOTOR)
    .withSteerMotor(MotorType.NEO, BACK_LEFT_MODULE_STEER_MOTOR)
    .withSteerEncoderAnalogChannel(BACK_LEFT_MODULE_STEER_ENCODER)
    .withSteerOffset(BACK_LEFT_MODULE_STEER_OFFSET)
    .build();

    m_backRightModule = new MkSwerveModuleBuilder()
    .withLayout(tab.getLayout("Back Right Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(6, 0))
    .withGearRatio(SdsModuleConfigurations.MK4_L2)
    .withDriveMotor(MotorType.NEO, BACK_RIGHT_MODULE_DRIVE_MOTOR)
    .withSteerMotor(MotorType.NEO, BACK_RIGHT_MODULE_STEER_MOTOR)
    .withSteerEncoderAnalogChannel(BACK_RIGHT_MODULE_STEER_ENCODER)
    .withSteerOffset(BACK_RIGHT_MODULE_STEER_OFFSET)
    .build();

    odometry = new SwerveDriveOdometry(
        m_kinematics,
        getGyroscopeRotation(),
        getModulePositions());

        var layout = tab.getLayout("Odometry", BuiltInLayouts.kList)
                .withSize(2, 4)
                .withPosition(8, 0);
    
                layout.addDouble("Pose Y", this::getPoseY);
                layout.addDouble("Pose X", this::getPoseX);
                layout.addDouble("Pose Rotation", this::getPoseRotation);
  }

  /**
   * Sets the gyroscope angle to zero. This can be used to set the direction the robot is currently facing to the
   * 'forwards' direction.
   */
  public void zeroGyroscope() {
    gyro.reset();
  }

  public Rotation2d getGyroscopeRotation() {
    return Rotation2d.fromDegrees(-gyro.getAngle());
  }

  /**
   * Get current position of each of the swerve modules in
   * front left, front right, back left, back right order.
   * These will be in meters because the swerve module code
   * calculates everything in meters.
   */
  private SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {
                m_frontLeftModule.getPosition(), 
                m_frontRightModule.getPosition(), 
                m_backLeftModule.getPosition(), 
                m_backRightModule.getPosition()
        };
  }

  public void drive(ChassisSpeeds chassisSpeeds) {
    m_chassisSpeeds = chassisSpeeds;
  }

  @Override
  public void periodic() {
    SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(m_chassisSpeeds);
    SwerveDriveKinematics.desaturateWheelSpeeds(states, MAX_VELOCITY_METERS_PER_SECOND);

    m_frontLeftModule.set(states[0].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE, states[0].angle.getRadians());
    m_frontRightModule.set(states[1].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE, states[1].angle.getRadians());
    m_backLeftModule.set(states[2].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE, states[2].angle.getRadians());
    m_backRightModule.set(states[3].speedMetersPerSecond / MAX_VELOCITY_METERS_PER_SECOND * MAX_VOLTAGE, states[3].angle.getRadians());
  
    // Udpate the odometry for the current position of the robot
    odometry.update(getGyroscopeRotation(), getModulePositions());
}

private double getPoseX() {
        return odometry.getPoseMeters().getX();    
}
private double getPoseY() {
        return odometry.getPoseMeters().getY();    
}
private double getPoseRotation() {
        return odometry.getPoseMeters().getRotation().getDegrees();    
}
}
