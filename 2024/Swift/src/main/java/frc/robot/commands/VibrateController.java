// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class VibrateController extends Command {
  CommandXboxController[] controllers;
  Timer timer = new Timer();
  double time;

  /** Creates a new VibrateController. */
  public VibrateController(double time, CommandXboxController... controllers) {
    this.controllers = controllers;
    this.time = time;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.restart();
    for (CommandXboxController controller : controllers) {
          controller.getHID().setRumble(RumbleType.kBothRumble, 0.75);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    for (CommandXboxController controller : controllers) {
          controller.getHID().setRumble(RumbleType.kBothRumble, 0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(time);
  }
}
