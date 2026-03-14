// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;

public class ShootAndFeed extends Command {
  private Shooter shooter;
  private Feeder feeder;
  private double shooter_perc;
  private double feeder_perc;
  private double rpm;

  public ShootAndFeed(Shooter shooter, Feeder feeder, double shooter_perc, double feeder_perc, double rpm) {
    addRequirements(shooter, feeder);
    this.shooter = shooter;
    this.feeder = feeder;
    this.shooter_perc = shooter_perc;
    this.feeder_perc = feeder_perc;
    this.rpm = rpm;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    final double shooter_current_speed = shooter.getSpeed();
    shooter.set(shooter_perc);
    if (shooter_current_speed >= rpm) {
      feeder.shootCommand(feeder_perc);
    } else {
      feeder.shootCommand(0.0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    shooter.set(0.0);
    feeder.shootCommand(0.0);
  }
}
