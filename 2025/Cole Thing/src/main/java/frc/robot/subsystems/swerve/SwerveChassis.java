// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swerve;

import java.util.function.IntFunction;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveChassis  {
  private final SwerveModule[] m_modules = new SwerveModule[4];
  private final SwerveDriveKinematics m_kinematics;
  private final SwerveDriveOdometry m_odometry;

  private final Supplier<Rotation2d> m_headingSupplier;

  private final double m_maxWheelSpeed;

  private SwerveModuleState[] m_desiredStates = new SwerveModuleState[4];

  public SwerveChassis(
    IntFunction<SwerveModule> swerveModuleFactory,
    Supplier<Rotation2d> headingSupplier,
    double maxWheelSpeed,
    double wheelbase,
    double trackWidth
  ) {
    for (int i = 0; i < 4; i++) {
      m_modules[i] = swerveModuleFactory.apply(i);
    }

    m_kinematics = new SwerveDriveKinematics(
      new Translation2d(wheelbase / 2.0, trackWidth / 2.0),
      new Translation2d(wheelbase / 2.0, -trackWidth / 2.0),
      new Translation2d(-wheelbase / 2.0, trackWidth / 2.0),
      new Translation2d(-wheelbase / 2.0, -trackWidth / 2.0)
    );

    m_odometry = new SwerveDriveOdometry(
      m_kinematics,
      headingSupplier.get(),
      getPositions()
    );

    m_headingSupplier = headingSupplier;

    m_maxWheelSpeed = maxWheelSpeed;

    aimWheels(Rotation2d.kZero);
  }

  public void update() {
    for (int i = 0; i < 4; i++) {
      m_modules[i].setDesiredState(m_desiredStates[i]);
    }

    m_odometry.update(m_headingSupplier.get(), getPositions());
  }

  public ChassisSpeeds getCurrentChassisSpeeds() {
    return m_kinematics.toChassisSpeeds(getStates());
  }

  public void drive(ChassisSpeeds chassisSpeeds) {
    m_desiredStates = m_kinematics.toSwerveModuleStates(chassisSpeeds);
    
    SwerveDriveKinematics.desaturateWheelSpeeds(m_desiredStates, m_maxWheelSpeed);
  }

  public void drive(double xVelocity, double yVelocity, double angularVelocity, boolean fieldRelative) {
    drive(fieldRelative ?
      ChassisSpeeds.fromFieldRelativeSpeeds(xVelocity, yVelocity, angularVelocity, m_headingSupplier.get()) :
      new ChassisSpeeds(xVelocity, yVelocity, angularVelocity)
    );
  }

  public SwerveModuleState[] getDesiredStates() {
    return m_desiredStates;
  }

  public SwerveModulePosition[] getPositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for (int i = 0; i < 4; i++) {
      positions[i] = m_modules[i].getPosition();
    }
    return positions;
  }

  public SwerveModuleState[] getStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (int i = 0; i < 4; i++) {
      states[i] = m_modules[i].getState();
    }
    return states;
  }

  public void setStates(double velocity, Rotation2d angle) {
    for (int i = 0; i < 4; i++) {
      m_desiredStates[i] = new SwerveModuleState(velocity, angle);
    }
  }

  public void stop() {
    for (int i = 0; i < 4; i++) {
      m_desiredStates[i] = new SwerveModuleState(0, m_modules[i].getPosition().angle);
    }
  }

  public void aimWheels(Rotation2d angle) {
    setStates(0, angle);
  }

  public void lockPose() {
    m_desiredStates = new SwerveModuleState[] {
      new SwerveModuleState(0, new Rotation2d(Math.PI / 4)),
      new SwerveModuleState(0, new Rotation2d(-Math.PI / 4)),
      new SwerveModuleState(0, new Rotation2d(-Math.PI / 4)),
      new SwerveModuleState(0, new Rotation2d(Math.PI / 4))
    };
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public void setPose(Pose2d pose) {
    m_odometry.resetPosition(m_headingSupplier.get(), getPositions(), pose); 
  }

  public Rotation2d getHeading() {
    return getPose().getRotation();
  }

  public void resetHeading() {
    m_odometry.resetPosition(
      m_headingSupplier.get(),
      getPositions(),
      new Pose2d(m_odometry.getPoseMeters().getTranslation(), Rotation2d.kZero)
    );
  }
}
