// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
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
   * @param xVelocity Desired X velocity of the robot.
   * @param yVelocity Desired Y velocity of the robot.
   * @param angularVelocity Desired angular velocity or the robot..
   */
  public void drive(double xVelocity, double yVelocity, double angularVelocity) {
    swerveDrive.setHeadingCorrection(true);
      swerveDrive.drive(
        new Translation2d(
          xVelocity * swerveDrive.getMaximumChassisVelocity(),
          yVelocity * swerveDrive.getMaximumChassisVelocity()
        ),
        angularVelocity * swerveDrive.getMaximumChassisAngularVelocity(),
        true,
        false
      );
  }

  /**
   * Returns a command that drives the robot.
   * @param xVelocity Desired X velocity of the robot.
   * @param yVelocity Desired Y velocity of the robot.
   * @param angularVelocity Desired angular velocity or the robot.
   * @return
   * The command that drives the robot.
   */
  public Command driveCommand(double xVelocity, double yVelocity, double angularVelocity) {
    return runOnce(() -> {
      drive(xVelocity, yVelocity, angularVelocity);
    });
  }

  /**
   * Returns a command that drives the robot using input suppliers.
   * @param xVelocitySupplier The supplier for the desired X velocity of the robot.
   * @param yVelocitySupplier The supplier for the desired Y velocity of the robot.
   * @param angularVelocitySupplier The supplier for the desired angular velocity of the robot.
   * @return
   * The command that drives the robot using the input suppliers.
   */
  public Command driveCommand(DoubleSupplier xVelocitySupplier, DoubleSupplier yVelocitySupplier, DoubleSupplier angularVelocitySupplier) {
    return run(() -> {
      drive(xVelocitySupplier.getAsDouble(), yVelocitySupplier.getAsDouble(), angularVelocitySupplier.getAsDouble());
    });
  }

  /**
   * Returns the current translation of the robot.
   * @return
   * The current translation of the robot.
   */
  public Translation2d getTranslation() {
    return swerveDrive.getPose().getTranslation();
  }
}
