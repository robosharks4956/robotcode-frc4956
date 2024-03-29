// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShootWithController extends CommandBase {
  /** Creates a new ShootWithController. */
  Shooter shooter;
  XboxController support;

  public ShootWithController(Shooter shooter, XboxController support) {
    this.shooter = shooter;
    this.support = support;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (support.getLeftBumper()) {
      shooter.setSpeed(1);
    } else if (support.getAButton()) {
      shooter.setSpeed(0.80);
    } else if (support.getBButton()) {
      shooter.setSpeed(-0.5);
    } else {
      shooter.setSpeed(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override 
  public void end(boolean interrupted) {
    shooter.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
