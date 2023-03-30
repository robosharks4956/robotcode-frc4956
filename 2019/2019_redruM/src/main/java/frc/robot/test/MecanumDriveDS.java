/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.test;

import java.util.StringJoiner;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Copy of MecanumDrive class from WIPLib for testing on our robot. Basically just
 * adds separate weights for each motor.
 */
public class MecanumDriveDS extends RobotDriveBase {
  private static int instances;

  private final SpeedController m_frontLeftMotor;
  private final SpeedController m_rearLeftMotor;
  private final SpeedController m_frontRightMotor;
  private final SpeedController m_rearRightMotor;

  public double m_maxOutput_frontLeft = 1;
  public double m_maxOutput_rearLeft = 1;
  public double m_maxOutput_frontRight = 1;
  public double m_maxOutput_rearRight = 1;

  private double m_rightSideInvertMultiplier = -1.0;
  private boolean m_reported;

  /**
   * Construct a MecanumDrive.
   *
   * <p>If a motor needs to be inverted, do so before passing it in.
   */
  public MecanumDriveDS(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
                      SpeedController frontRightMotor, SpeedController rearRightMotor) {
    verify(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    m_frontLeftMotor = frontLeftMotor;
    m_rearLeftMotor = rearLeftMotor;
    m_frontRightMotor = frontRightMotor;
    m_rearRightMotor = rearRightMotor;
    addChild(m_frontLeftMotor);
    addChild(m_rearLeftMotor);
    addChild(m_frontRightMotor);
    addChild(m_rearRightMotor);
    instances++;
    setName("MecanumDrive", instances);
  }

  /**
   * Verifies that all motors are nonnull, throwing a NullPointerException if any of them are.
   * The exception's error message will specify all null motors, e.g. {@code
   * NullPointerException("frontLeftMotor, rearRightMotor")}, to give as much information as
   * possible to the programmer.
   *
   * @throws NullPointerException if any of the given motors are null
   */
  private void verify(SpeedController frontLeft, SpeedController rearLeft,
                      SpeedController frontRight, SpeedController rearRightMotor) {
    if (frontLeft != null && rearLeft != null && frontRight != null && rearRightMotor != null) {
      return;
    }
    StringJoiner joiner = new StringJoiner(", ");
    if (frontLeft == null) {
      joiner.add("frontLeftMotor");
    }
    if (rearLeft == null) {
      joiner.add("rearLeftMotor");
    }
    if (frontRight == null) {
      joiner.add("frontRightMotor");
    }
    if (rearRightMotor == null) {
      joiner.add("rearRightMotor");
    }
    throw new NullPointerException(joiner.toString());
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
   * from its angle or rotation rate.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *                  positive.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
    driveCartesian(ySpeed, xSpeed, zRotation, 0.0);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
   * from its angle or rotation rate.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *                  positive.
   * @param gyroAngle The current angle reading from the gyro in degrees around the Z axis. Use
   *                  this to implement field-oriented controls.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
    if (!m_reported) {
      HAL.report(tResourceType.kResourceType_RobotDrive, 4,
                 tInstances.kRobotDrive2_MecanumCartesian);
      m_reported = true;
    }

    ySpeed = limit(ySpeed);
    ySpeed = applyDeadband(ySpeed, m_deadband);

    xSpeed = limit(xSpeed);
    xSpeed = applyDeadband(xSpeed, m_deadband);

    // Compensate for gyro angle.
    Vector2d input = new Vector2d(ySpeed, xSpeed);
    input.rotate(-gyroAngle);

    double[] wheelSpeeds = new double[4];
    wheelSpeeds[MotorType.kFrontLeft.value] = input.x + input.y + zRotation;
    wheelSpeeds[MotorType.kFrontRight.value] = -input.x + input.y - zRotation;
    wheelSpeeds[MotorType.kRearLeft.value] = -input.x + input.y + zRotation;
    wheelSpeeds[MotorType.kRearRight.value] = input.x + input.y - zRotation;

    normalize(wheelSpeeds);

    m_frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft.value] * m_maxOutput_frontLeft);
    m_frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight.value] * m_maxOutput_frontRight
        * m_rightSideInvertMultiplier);
    m_rearLeftMotor.set(wheelSpeeds[MotorType.kRearLeft.value] * m_maxOutput_rearLeft);
    m_rearRightMotor.set(wheelSpeeds[MotorType.kRearRight.value] * m_maxOutput_rearRight
        * m_rightSideInvertMultiplier);

    feed();
  }

  /**
   * Gets if the power sent to the right side of the drivetrain is multipled by -1.
   *
   * @return true if the right side is inverted
   */
  public boolean isRightSideInverted() {
    return m_rightSideInvertMultiplier == -1.0;
  }

  /**
   * Sets if the power sent to the right side of the drivetrain should be multipled by -1.
   *
   * @param rightSideInverted true if right side power should be multipled by -1
   */
  public void setRightSideInverted(boolean rightSideInverted) {
    m_rightSideInvertMultiplier = rightSideInverted ? -1.0 : 1.0;
  }

  @Override
  public void stopMotor() {
    m_frontLeftMotor.stopMotor();
    m_frontRightMotor.stopMotor();
    m_rearLeftMotor.stopMotor();
    m_rearRightMotor.stopMotor();
    feed();
  }

  @Override
  public String getDescription() {
    return "MecanumDrive";
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("MecanumDrive");
    builder.setActuator(true);
    builder.setSafeState(this::stopMotor);
    builder.addDoubleProperty("Front Left Motor Speed",
        m_frontLeftMotor::get,
        m_frontLeftMotor::set);
    builder.addDoubleProperty("Front Right Motor Speed",
        () -> m_frontRightMotor.get() * m_rightSideInvertMultiplier,
        value -> m_frontRightMotor.set(value * m_rightSideInvertMultiplier));
    builder.addDoubleProperty("Rear Left Motor Speed",
        m_rearLeftMotor::get,
        m_rearLeftMotor::set);
    builder.addDoubleProperty("Rear Right Motor Speed",
        () -> m_rearRightMotor.get() * m_rightSideInvertMultiplier,
        value -> m_rearRightMotor.set(value * m_rightSideInvertMultiplier));
  }
}
