// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class MobilityCommand extends Command {
  private final Drivetrain drivetrain;

  private final Timer timer = new Timer();

  private final double timeout, xSpeed, ySpeed;

  /** Creates a new MobilityCommand. */
  public MobilityCommand(Drivetrain drivetrain, double timeout, double xSpeed, double ySpeed) {
    this.drivetrain = drivetrain;
    this.timeout = timeout;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.restart();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
      -xSpeed,
      ySpeed,
      0,
      drivetrain.getGyroscopeRotation()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
      0,
      0,
      0,
      drivetrain.getGyroscopeRotation()));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(timeout);
  }
}
