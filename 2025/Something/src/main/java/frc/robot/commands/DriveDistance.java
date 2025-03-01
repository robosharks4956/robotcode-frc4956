// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DriveDistance extends Command {
  private final Drivetrain drivetrain;
  private final double distance, velocityX, velocityY;

  /** Creates a new DriveDistance. */
  public DriveDistance(Drivetrain drivetrain, double distanceX, double distanceY, double velocity) {
    this.drivetrain = drivetrain;
    this.distance = Math.hypot(distanceX, distanceY);
    this.velocityX = velocity * distanceX / this.distance;
    this.velocityY = velocity * distanceY / this.distance;

    addRequirements(drivetrain);
  }

  private Translation2d initialTranslation;

  @Override
  public void initialize() {
    initialTranslation = drivetrain.getTranslation();
  }

  @Override
  public void execute() {
    drivetrain.drive(velocityX, velocityY, 0);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return drivetrain.getTranslation().getDistance(initialTranslation) > distance;
  }
}
