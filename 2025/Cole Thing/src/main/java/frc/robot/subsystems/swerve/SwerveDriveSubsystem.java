// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDriveSubsystem extends SubsystemBase {
  private static final double kTrackWidth = 24.5;
  private static final double kWheelbase = 19.5;
  private static final double kMaxSpeed = 0.25;
  private static final double kWheelCircumference = 0.05;
  private static final double kDriveGearRatio = 6.75;
  private static final double kSteerGearRatio = 12.8;

  private final SparkFlex[] m_driveMotors = new SparkFlex[] {
    new SparkFlex(21, MotorType.kBrushless),
    new SparkFlex(23, MotorType.kBrushless),
    new SparkFlex(25, MotorType.kBrushless),
    new SparkFlex(27, MotorType.kBrushless)
  };

  private final SparkMax[] m_steerMotors = new SparkMax[] {
    new SparkMax(22, MotorType.kBrushless),
    new SparkMax(24, MotorType.kBrushless),
    new SparkMax(26, MotorType.kBrushless),
    new SparkMax(28, MotorType.kBrushless),
  };

  private final AHRS m_gyro = new AHRS(NavXComType.kMXP_SPI);

  private final SwerveChassis m_swerveChassis = new SwerveChassis(
    (int index) -> new SwerveModule(
      m_driveMotors[index]::setVoltage,
      () -> m_driveMotors[index].getEncoder().getPosition() / kDriveGearRatio * kWheelCircumference,
      () -> m_driveMotors[index].getEncoder().getVelocity() / kDriveGearRatio * kWheelCircumference,
      m_steerMotors[index]::setVoltage,
      () -> new Rotation2d(m_steerMotors[index].getAbsoluteEncoder().getPosition() / 57.3),
      200.0,
      0.0,
      3.0,
      0.0
    ),
    () -> Rotation2d.fromDegrees(m_gyro.getAngle()),
    kTrackWidth,
    kWheelbase,
    kMaxSpeed
  );

  public SwerveDriveSubsystem() {
    SmartDashboard.putBoolean("Field Relative", false);
  }

  @Override
  public void periodic() {
    m_swerveChassis.update();

    SmartDashboard.putNumber("Swerve Chassis/Heading", m_swerveChassis.getHeading().getDegrees());
    SmartDashboard.putNumber("Swerve Chassis/Translation/X", m_swerveChassis.getPose().getX());
    SmartDashboard.putNumber("Swerve Chassis/Translation/Y", m_swerveChassis.getPose().getY());

    final String[] moduleNames = new String[] {"Front Left", "Front Right", "Back Left", "Back Right"};
    
    for (int i = 0; i < 4; i++) {
      SmartDashboard.putNumber(String.format(
        "Swerve Chassis/%s Module/Drive Velocity", moduleNames[i]
      ), m_swerveChassis.getStates()[i].speedMetersPerSecond);

      SmartDashboard.putNumber(String.format(
        "Swerve Chassis/%s Module/Steer Rotation", moduleNames[i]
      ), MathUtil.inputModulus(m_swerveChassis.getStates()[i].angle.getDegrees(), -180.0, 180.0));

      SmartDashboard.putNumber(String.format(
        "Swerve Chassis/%s Module/Desired Drive Velocity", moduleNames[i]
      ), m_swerveChassis.getDesiredStates()[i].speedMetersPerSecond);

      SmartDashboard.putNumber(String.format(
        "Swerve Chassis/%s Module/Desired Steer Rotation", moduleNames[i]
      ), MathUtil.inputModulus(m_swerveChassis.getDesiredStates()[i].angle.getDegrees(), -180.0, 180.0));
    }
  }

  public void drive(double xVelocity, double yVelocity, double angularVelocity, boolean fieldRelative) {
    m_swerveChassis.drive(-xVelocity, yVelocity, angularVelocity, fieldRelative);
  }

  public void stop() {
    m_swerveChassis.stop();
  }

  public void steerWheels(Rotation2d angle) {
    m_swerveChassis.aimWheels(angle);
  }

  public void lockPose() {
    m_swerveChassis.lockPose();
  }

  public void resetHeading() {
    m_swerveChassis.resetHeading();
  }

  public void setCurrentPosition(double xPosition, double yPosition, double heading) {
    m_swerveChassis.setPose(new Pose2d(xPosition, yPosition, Rotation2d.fromDegrees(heading)));
  }

  private static final double kControllerDefaultMaxSpeed = 0.05;
  private static final double kControllerDefaultMaxAngularSpeed = 0.03;
  private static final double kControllerSmoothing = 3;
  private static final double kControllerDeadband = 0.05;
  private static final double kSlowModeConversionFactor = 0.2;
  private static final double kGoblinModeConversionFactor = 2;

  public Command controllerDriveCommand(
    DoubleSupplier xInputSupplier,
    DoubleSupplier yInputSupplier,
    DoubleSupplier angularInputSupplier,
    BooleanSupplier slowMode,
    BooleanSupplier goblinMode
  ) {
    return runEnd(() -> {
      final double GearRatio = slowMode.getAsBoolean() ?
        kSlowModeConversionFactor : goblinMode.getAsBoolean() ? kGoblinModeConversionFactor : 1;

      drive(
        modifyAxis(
          yInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing
        ) * kControllerDefaultMaxSpeed * GearRatio,
        modifyAxis(
          xInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing
        ) * kControllerDefaultMaxSpeed * GearRatio,
        modifyAxis(
          angularInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing
        ) * kControllerDefaultMaxAngularSpeed * GearRatio,
        SmartDashboard.getBoolean("Field Relative", false)
      );
    }, this::stop);
  }

  private double modifyAxis(double input, double deadband, double smoothing) {
    return MathUtil.clamp(Math.copySign(Math.pow(MathUtil.applyDeadband(input, deadband), smoothing), input), -1, 1);
  }

  public Command aimWheelsCommand(DoubleSupplier xInputSupplier, DoubleSupplier yInputSupplier) {
    return run(() -> steerWheels(new Rotation2d(Math.atan2(
      modifyAxis(xInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing),
      modifyAxis(yInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing)
    ))));
  }

  public Command lockPoseCommand() {
    return run(this::lockPose);
  }
}
