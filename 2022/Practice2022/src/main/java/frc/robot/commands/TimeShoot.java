package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class TimeShoot extends CommandBase {
  Shooter shooter;
  double timeS;

  Timer timer = new Timer();

  /** Creates a new TimeShoot. */
  public TimeShoot(Shooter shooter, double timeS) {
    addRequirements(shooter);
    this.shooter = shooter;
    this.timeS = timeS;

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setSpeed(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= timeS;
  }
}
