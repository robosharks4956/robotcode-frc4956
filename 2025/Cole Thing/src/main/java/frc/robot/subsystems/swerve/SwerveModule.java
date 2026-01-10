// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swerve;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModule {
  private final DoubleConsumer m_driveOutputConsumer, m_steerOutputConsumer;
  private final DoubleSupplier m_drivePositionSupplier, m_driveVelocitySupplier;
  private final Supplier<Rotation2d> m_steerRotationSupplier;
  private final PIDController m_driveController, m_steerController;

  public SwerveModule(
    DoubleConsumer driveOutputConsumer,
    DoubleSupplier drivePositionSupplier,
    DoubleSupplier driveVelocitySupplier,
    DoubleConsumer steerOutputConsumer,
    Supplier<Rotation2d> steerRotationSupplier,
    double driveP,
    double driveD,
    double steerP,
    double steerD
  ) {
    m_driveOutputConsumer = driveOutputConsumer;
    m_drivePositionSupplier = drivePositionSupplier;
    m_driveVelocitySupplier = driveVelocitySupplier;
    m_steerOutputConsumer = steerOutputConsumer;
    m_steerRotationSupplier = steerRotationSupplier;

    m_driveController = new PIDController(driveP, 0, steerD);
    m_steerController = new PIDController(steerP, 0, steerD);
    
    m_steerController.enableContinuousInput(-Math.PI, Math.PI);
  }

  public void setDesiredState(SwerveModuleState desiredState) {
    desiredState.optimize(m_steerRotationSupplier.get());

    m_driveOutputConsumer.accept(m_driveController.calculate(
      m_driveVelocitySupplier.getAsDouble(),
      desiredState.speedMetersPerSecond
    ));

    m_steerOutputConsumer.accept(m_steerController.calculate(
      m_steerRotationSupplier.get().getRadians(),
      desiredState.angle.getRadians()
    ));
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(m_drivePositionSupplier.getAsDouble(), m_steerRotationSupplier.get());
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(m_driveVelocitySupplier.getAsDouble(), m_steerRotationSupplier.get());
  }
}
