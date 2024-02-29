package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class DefaultDrive extends Command {
  private final Drivetrain drivetrainSubsystem;

  private final DoubleSupplier translationXSupplier;
  private final DoubleSupplier translationYSupplier;
  private final DoubleSupplier rotationSupplier;
  private final BooleanSupplier fieldrelativeSupplier;
  private final DoubleSupplier maxspeedSupplier;

  public DefaultDrive(Drivetrain drivetrainSubsystem,
      DoubleSupplier translationXSupplier,
      DoubleSupplier translationYSupplier,
      DoubleSupplier rotationSupplier,
      BooleanSupplier fieldrelativeSupplier,
      DoubleSupplier maxspeedSupplier) {
    this.drivetrainSubsystem = drivetrainSubsystem;
    this.translationXSupplier = translationXSupplier;
    this.translationYSupplier = translationYSupplier;
    this.rotationSupplier = rotationSupplier;
    this.fieldrelativeSupplier = fieldrelativeSupplier;
    this.maxspeedSupplier = maxspeedSupplier;

    addRequirements(drivetrainSubsystem);
  }

  @Override
  public void execute() {

    double speedmultiplier = -maxspeedSupplier.getAsDouble();

    // Added extra multiplier to slow rotation because with lag it would
    // rotate a little bit too long at high speed and throw off the driver
    double rotatespeedmultiplier = speedmultiplier * 0.5;

    if (fieldrelativeSupplier.getAsBoolean()) {
      // Field-relative drive
      drivetrainSubsystem.drive(
          ChassisSpeeds.fromFieldRelativeSpeeds(
              translationXSupplier.getAsDouble() * speedmultiplier,
              translationYSupplier.getAsDouble() * speedmultiplier,
              rotationSupplier.getAsDouble() * rotatespeedmultiplier,
              drivetrainSubsystem.getGyroscopeRotation()));
    } else {
      // Robot-relative drive
      drivetrainSubsystem.drive(
          new ChassisSpeeds(
              translationXSupplier.getAsDouble() * speedmultiplier,
              translationYSupplier.getAsDouble() * speedmultiplier,
              rotationSupplier.getAsDouble() * rotatespeedmultiplier));
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
  }
}
