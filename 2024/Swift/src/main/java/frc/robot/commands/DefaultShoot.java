// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter;

public class DefaultShoot extends Command {
  private Shooter shooter;
  CommandXboxController supportController;
  /** Creates a new DefaultShoot. */
  public DefaultShoot(CommandXboxController supportController, Shooter shooter) {
    this.supportController = supportController;
    this.shooter = shooter;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double leftTrigger = MathUtil.applyDeadband(supportController.getLeftTriggerAxis(), 0.05);
    double rightTrigger = MathUtil.applyDeadband(supportController.getRightTriggerAxis(), 0.05);
    shooter.set((rightTrigger-leftTrigger) * 2);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
