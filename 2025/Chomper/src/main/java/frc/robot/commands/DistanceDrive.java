// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DistanceDrive extends Command {
  private final double distance;
  private final double percentVelocityX;
  private final double percentVelocityY;
  private final Drivetrain drivetrain;
  
    /** Creates a new DistanceDrive. */
  public DistanceDrive(double distanceX, double distanceY, double percentVelocity, Drivetrain drivetrain) {
    distance = Math.hypot(distanceX, distanceY);
    percentVelocityX = percentVelocity * distanceX / this.distance;
    percentVelocityY = percentVelocity * distanceY / this.distance;

    this.drivetrain = drivetrain;

    addRequirements(drivetrain);
  }

  private Translation2d initialTranslation;

  @Override
  public void initialize() {
    initialTranslation = drivetrain.getTranslation();
  }

  @Override
  public void execute() {
    drivetrain.drive(percentVelocityX, percentVelocityY, 0, true);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return drivetrain.getTranslation().getDistance(initialTranslation) > distance;
  }
}