// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class SwerveSubsystem extends SubsystemBase {
  private static final double kMaxSpeed = 5;
  
  private static final double kControllerSmoothing = 3.0;
  private static final double kControllerDeadband = 0.05;

  private final SwerveDrive m_swerveDrive;

  private final GenericEntry m_fieldRelativeEntry =
    Shuffleboard.getTab("Drive").add("Field Relative", true).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
  
  public SwerveSubsystem() {
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
    try {
      m_swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(kMaxSpeed);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    m_swerveDrive.setCosineCompensator(true);
  }

  public void drive(
    double percentXVelocity,
    double percentYVelocity,
    double percentAngularVelocity,
    boolean fieldRelative
  ) {
    m_swerveDrive.drive(
      new Translation2d(
        percentYVelocity * m_swerveDrive.getMaximumChassisVelocity(),
        percentXVelocity * m_swerveDrive.getMaximumChassisVelocity()
      ),
      percentAngularVelocity * m_swerveDrive.getMaximumChassisAngularVelocity(),
      fieldRelative,
      false
    );
  }

  public void stop() {
    drive(0, 0, 0, false);
  }
  
  public Command controllerDriveCommand(
      DoubleSupplier xInputSupplier,
      DoubleSupplier yInputSupplier,
      DoubleSupplier angularInputSupplier,
      BooleanSupplier slowMode,
      BooleanSupplier goblinMode
    ) {
    return runEnd(() -> {
      final double percentMultiplier = slowMode.getAsBoolean() ? 0.1 : goblinMode.getAsBoolean() ? 1.0 : 0.5;
      drive(
        -modifyAxis(xInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing) * percentMultiplier,
        -modifyAxis(yInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing) * percentMultiplier,
        -modifyAxis(angularInputSupplier.getAsDouble(), kControllerDeadband, kControllerSmoothing) * percentMultiplier,
        m_fieldRelativeEntry.getBoolean(false)
      );
      System.out.println(m_fieldRelativeEntry.getBoolean(true));
    }, this::stop);
  }


  private double modifyAxis(double input, double deadband, double smoothing) {
    return MathUtil.clamp(Math.copySign(Math.pow(MathUtil.applyDeadband(input, deadband), smoothing), input), -1, 1);
  }
}
