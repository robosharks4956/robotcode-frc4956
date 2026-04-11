package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class ShootAndFeed extends Command {

  private Shooter shooter;
  private Feeder feeder;
  private DoubleSupplier rpmSupplier;

  // Will not run the feeder until this is true, gives operator a way to hold off
  // shooting until robot is in position
  // In auton, just set to true all the time so it fires when ready
  private BooleanSupplier safetySwitch;

  public ShootAndFeed(Shooter shooter, Feeder feeder, DoubleSupplier rpmSupplier,
      BooleanSupplier safetySwitch) {
    addRequirements(shooter, feeder);
    this.shooter = shooter;
    this.feeder = feeder;
    this.rpmSupplier = rpmSupplier;
    this.safetySwitch = safetySwitch;
  }

  @Override
  public void initialize() {
    feeder.set(0);
  }

  @Override
  public void execute() {
    final double shooter_current_speed = shooter.getVelocity();
    double rpm = rpmSupplier.getAsDouble();
    shooter.setVelocity(rpm);
    if (safetySwitch.getAsBoolean()) {
      if (shooter_current_speed >= rpm) {
        feeder.set(Feeder.kFeedSpeed);
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
