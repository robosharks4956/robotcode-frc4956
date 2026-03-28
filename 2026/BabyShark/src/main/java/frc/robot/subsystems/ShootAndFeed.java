// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class ShootAndFeed extends Command {

  private Shooter shooter;
  private Feeder feeder;
  private double feeder_perc;
  private DoubleSupplier rpm_supplier;

  // Will not run the feeder until this is true, gives operator a way to hold off shooting until robot is in position
  // In auton, just set to true all the time so it fires when RPM target is reached
  private BooleanSupplier safetySwitch;

  public ShootAndFeed(Shooter shooter, Feeder feeder, double feeder_perc, DoubleSupplier rpm_supplier,
      BooleanSupplier safetySwitch) {
    addRequirements(shooter, feeder);
    this.shooter = shooter;
    this.feeder = feeder;
    this.feeder_perc = feeder_perc;
    this.rpm_supplier = rpm_supplier;
    this.safetySwitch = safetySwitch;
  }

  @Override
  public void initialize() {
    feeder.set(0);
  }

  @Override
  public void execute() {
    final double shooter_current_speed = shooter.getSpeed();
    double rpm = rpm_supplier.getAsDouble();
    shooter.setVelocity(rpm);
    if (safetySwitch.getAsBoolean()) {
      if (shooter_current_speed >= rpm) {
        feeder.set(feeder_perc);
        // Else leave feeder running until it falls below 90% of target, that way small
        // fluctuations don't make the feeder rapidly toggle
      } else if (shooter_current_speed < rpm * 0.965) {
        feeder.set(0);
      }
    } else {
      feeder.set(0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    shooter.set(0.0);
    feeder.set(0.0);
  }
}
