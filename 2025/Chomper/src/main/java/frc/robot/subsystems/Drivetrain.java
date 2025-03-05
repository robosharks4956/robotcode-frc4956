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

import static frc.robot.Constants.SwerveConstants;

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerveDrive;

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), SwerveConstants.SWERVE_DIRECTORY);
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
   * @param percentXVelocity Desired percent of the max velocity of the robot in the X direction.
   * @param percentYVelocity Desired percent of the max velocity of the robot in the Y direction.
   * @param percentAngularVelocity Desired percent of the max angular velocity or the robot.
   */
  public void drive(double percentXVelocity, double percentYVelocity, double percentAngularVelocity, boolean fieldRelative) {
    swerveDrive.setHeadingCorrection(true);
      swerveDrive.drive(
        new Translation2d(
          percentYVelocity * swerveDrive.getMaximumChassisVelocity(),
          percentXVelocity * swerveDrive.getMaximumChassisVelocity()
        ),
        percentAngularVelocity * swerveDrive.getMaximumChassisAngularVelocity(),
        false, //fieldRelative,
        false
      );
  }

  /**
   * Creates a command that drives the robot using input suppliers.
   * @param percentXVelocitySupplier The supplier for the desired percent of the max velocity of the robot in the X direction.
   * @param percentYVelocitySupplier The supplier for the desired percent of the max velocity of the robot in the Y direction.
   * @param percentAngularVelocitySupplier The supplier for the desired percent of the max angular velocity of the robot.
   * @return The command that drives the robot using input suppliers.
   */
  public Command controllerDriveCommand(
    DoubleSupplier percentXVelocitySupplier,
    DoubleSupplier percentYVelocitySupplier,
    DoubleSupplier angularVelocitySupplier,
    BooleanSupplier fieldRelativeSupplier
  ) {
    return run(() -> {
      drive(
        percentXVelocitySupplier.getAsDouble(),
        percentYVelocitySupplier.getAsDouble(),
        angularVelocitySupplier.getAsDouble(),
        fieldRelativeSupplier.getAsBoolean()
      );
    });
  }

  /**
   * Gets the translation of the robot.
   * @return The translation of the robot.
   */
  public Translation2d getTranslation() {
    return swerveDrive.getPose().getTranslation();
  }
}
