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

public class Drivetrain extends SubsystemBase {
  private final SwerveDrive swerveDrive;

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), SWERVE_DIRECTORY);
    try {
      swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(MAX_SPEED);
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
  private void drive(double percentXVelocity, double percentYVelocity, double percentAngularVelocity, boolean fieldRelative) {
    swerveDrive.setHeadingCorrection(true);
      swerveDrive.drive(
        new Translation2d(
          percentYVelocity * swerveDrive.getMaximumChassisVelocity(),
          percentXVelocity * swerveDrive.getMaximumChassisVelocity()
        ),
        percentAngularVelocity * swerveDrive.getMaximumChassisAngularVelocity(),
        fieldRelative,
        false
      );
  }

  /**
   * Creates a command that drives the robot using input suppliers.
   * @param percentXVelocitySupplier The supplier for the desired percent of the max velocity of the robot in the X
   * direction.
   * @param percentYVelocitySupplier The supplier for the desired percent of the max velocity of the robot in the Y
   * direction.
   * @param percentAngularVelocitySupplier The supplier for the desired percent of the max angular velocity of the
   * robot.
   * @param fieldRelativeSupplier The supplier for whether or not the robot should be driving in field relative or
   * robot relative.
   * @return The command that drives the robot using input suppliers.
   */
  public Command controllerDriveCommand(
    DoubleSupplier percentXVelocitySupplier,
    DoubleSupplier percentYVelocitySupplier,
    DoubleSupplier percentAngularVelocitySupplier,
    BooleanSupplier fieldRelativeSupplier
  ) {
    return run(() -> {
      drive(
        -percentXVelocitySupplier.getAsDouble(),
        -percentYVelocitySupplier.getAsDouble(),
        -percentAngularVelocitySupplier.getAsDouble(),
        false//fieldRelativeSupplier.getAsBoolean()
      );
    });
  }

  /**
   * Creates a command that drives the robot a set distance.
   * @param distanceX The desired distance for the robot to travel in the X direction.
   * @param distanceY The desired distance for the robot to travel in the Y direction.
   * @param percentVelocity The desired percent of the max velocity of the robot.
   * @return The command that drives the robot a set distance.
   */
  public Command distanceDriveCommand(double distanceX, double distanceY, double percentVelocity) { 
    final double distance = Math.hypot(distanceX, distanceY);
    final double percentVelocityX = percentVelocity * distanceX / distance;
    final double percentVelocityY = percentVelocity * distanceY / distance;

    class Translation { public Translation2d translation = new Translation2d(); }
    final Translation initialTranslation = new Translation();

    return runOnce(() -> initialTranslation.translation = new Translation2d()).andThen(run(
      () -> drive(percentVelocityX, percentVelocityY, 0, false)
    )).until(() -> swerveDrive.getPose().getTranslation().getDistance(new Translation2d()) > distance);
  }
  
  public Command timeDriveCommand(double percentXVelocity, double percentYVelocity, double percentAngularVelocity, double time) {
    return runEnd(() -> drive(percentXVelocity, percentYVelocity, percentAngularVelocity, false), () -> drive(0, 0, 0, true)).withTimeout(time);
  }

  /**
   * Creates a command that resets the gyro to 0.
   * @return The command that resets the gyro to 0.
   */
  public Command resetGyroCommand() {
    return run(() -> swerveDrive.zeroGyro());
  }
}
