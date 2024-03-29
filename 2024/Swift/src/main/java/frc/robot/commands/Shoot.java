// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class Shoot extends Command {
  private final Intake intake;
  private final Shooter shooter;
  private final double power;

  public Shoot(Intake intake, Shooter shooter, double power) {
    this.intake = intake;
    this.shooter = shooter;
    this.power = power;

    addRequirements(intake, shooter);
  }

  private final Timer timer = new Timer();

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.restart();
    shooter.set(power);
  }

  private final double spinTime = .5;
  private final double shootTime = .9;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (timer.hasElapsed(spinTime)) {
      intake.setVelocity(-1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.set(0);
    intake.setVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(spinTime + shootTime);
  }
}
