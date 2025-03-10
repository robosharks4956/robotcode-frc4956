// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Lift;

public class LiftPosition extends Command {
  private final double position;
  private final Lift lift;

  /** Creates a new LiftPosition. */
  public LiftPosition(double position, Lift lift) {
    this.position = position;
    this.lift = lift;

    addRequirements(lift);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    lift.setPositionCommand(position);
  }

  @Override
  public void end(boolean interrupted) {
    lift.setPositionCommand(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
