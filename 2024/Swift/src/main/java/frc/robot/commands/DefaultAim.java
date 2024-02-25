// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Aimer;

public class DefaultAim extends Command {
  private final Aimer aimer;

  private final DoubleSupplier movementSupplier;

  /** Creates a new DefaultAim. */
  public DefaultAim(Aimer aimer, DoubleSupplier positionSupplier) {
    this.aimer = aimer;
    this.movementSupplier = positionSupplier;
    addRequirements(aimer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    aimer.move(movementSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
