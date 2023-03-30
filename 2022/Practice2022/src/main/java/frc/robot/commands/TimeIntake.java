package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class TimeIntake extends CommandBase {

  Intake intake;
  double timeS;

  Timer timer = new Timer();

  /** Creates a new TimeIntake. */
  public TimeIntake(Intake intake, double timeS) {
    addRequirements(intake);
    this.intake = intake;
    this.timeS = timeS;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setSpeed(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= timeS;
  }
}
