// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SwerveConstants;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerveDrive;

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
    try {
      swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(SwerveConstants.MAX_SPEED);
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override 
  public void periodic() {}

  /**
   * Drives the robot.
   * @param translationX Desired X translation of the robot.
   * @param translationY Desired Y translation of the robot.
   * @param angularVelocity Desired angular velocity or the robot.
   */
  public void drive(double translationX, double translationY, double angularVelocity) {
    swerveDrive.setHeadingCorrection(true);
    swerveDrive.drive(
      new Translation2d(
        translationX * swerveDrive.getMaximumChassisVelocity(),
        translationY * swerveDrive.getMaximumChassisVelocity()),
      angularVelocity * swerveDrive.getMaximumChassisAngularVelocity(),
      true,
      false);
  }
}
