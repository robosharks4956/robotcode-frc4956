// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

import static frc.robot.Constants.SwerveConstants.*;
import static frc.robot.Utils.*;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerveDrive;

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), SWERVE_DIRECTORY);
    try {
      swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(MAX_SPEED);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    swerveDrive.setCosineCompensator(true);
  }

  @Override
  public void periodic() {
  }

  /**
   * Drives the robot.
   * 
   * @param percentXVelocity       Desired percent of the max velocity of the
   *                               robot in the X direction.
   * @param percentYVelocity       Desired percent of the max velocity of the
   *                               robot in the Y direction.
   * @param percentAngularVelocity Desired percent of the max angular velocity or
   *                               the robot.
   */
  public void drive(double percentXVelocity, double percentYVelocity, double percentAngularVelocity,
      boolean fieldRelative) {
    // System.out.println("Setting turn translation drive to: " +
    // (percentAngularVelocity * swerveDrive.getMaximumChassisAngularVelocity()));
    swerveDrive.drive(
        new Translation2d(
            percentYVelocity * swerveDrive.getMaximumChassisVelocity(),
            percentXVelocity * swerveDrive.getMaximumChassisVelocity()),
        percentAngularVelocity * swerveDrive.getMaximumChassisAngularVelocity(),
        fieldRelative,
        false);
  }

  public void stop() {
    drive(0, 0, 0, false);
  }

  /**
   * Creates a command that drives the robot using input suppliers.
   * 
   * @param xInputSupplier     The supplier for the X input on the controller.
   * @param yInputSupplier     The supplier for the Y input on the controller.
   * @param angleInputSupplier The supplier for the angular input on the
   *                           controller.
   * @param slowModeSupplier   The supplier for whether or not the robot should be
   *                           driving in slow mode.
   * @param goblinModeSupplier The supplier for whether or not the robot should be
   *                           driving in goblin mode.
   * @return The command that drives the robot using input suppliers.
   */
  public Command controllerDriveCommand(
      DoubleSupplier xInputSupplier,
      DoubleSupplier yInputSupplier,
      DoubleSupplier angleInputSupplier,
      BooleanSupplier slowModeSupplier,
      BooleanSupplier goblinModeSupplier) {
    return run(() -> {
      drive(
          -modifyAxis(
              xInputSupplier.getAsDouble(),
              slowModeSupplier.getAsBoolean() ? 0.1 : goblinModeSupplier.getAsBoolean() ? 1 : 0.5,
              0.05,
              3),
          -modifyAxis(
              yInputSupplier.getAsDouble(),
              slowModeSupplier.getAsBoolean() ? 0.1 : goblinModeSupplier.getAsBoolean() ? 1 : 0.5,
              0.05,
              3),
          -modifyAxis(angleInputSupplier.getAsDouble(), slowModeSupplier.getAsBoolean() ? 0.15 : 0.75, 0.05, 3),
          false);
    });
  }

  public Command lockPoseCommand() {
    return run(() -> swerveDrive.lockPose());
  }

  public Command timeDriveCommand(double percentXVelocity, double percentYVelocity, double percentAngularVelocity,
      double time) {
    return runEnd(() -> drive(percentXVelocity, percentYVelocity, percentAngularVelocity, false),
        () -> drive(0, 0, 0, true)).withTimeout(time);
  }

  /**
   * Creates a command that resets the gyro to 0.
   * 
   * @return The command that resets the gyro to 0.
   */
  public Command resetGyroCommand() {
    return runOnce(() -> swerveDrive.zeroGyro());
  }
}
