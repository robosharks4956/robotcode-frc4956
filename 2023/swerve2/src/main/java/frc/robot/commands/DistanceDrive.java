package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DistanceDrive extends CommandBase {
  public Drivetrain drive;
  public double targetXDistance;
  public double targetYDistance;

  /** Creates a new DistanceDrive. */
  public DistanceDrive(Drivetrain drive, double targetXDistance, double targetYDistance) {
    addRequirements(drive);

    this.drive = drive;
    this.targetXDistance = targetXDistance;
    this.targetYDistance = targetYDistance;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
