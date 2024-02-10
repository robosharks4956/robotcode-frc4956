package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DistanceDrive extends Command {
  public Drivetrain drive;
  public double targetXDistance;
  public double targetYDistance;
  public double startingXDistance;
  public double startingYDistance;

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
    startingXDistance = drive.getPoseX();
    startingYDistance = drive.getPoseY();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xSpeed = isXDistanceReached() ? 0 : 50;
    double ySpeed = isYDistanceReached() ? 0 : 50;
    drive.drive(
      new ChassisSpeeds(
      xSpeed,
      ySpeed,
      0
      )
    );
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }
  public boolean isXDistanceReached() {
    if (drive.getPoseX() - startingXDistance > targetXDistance) {
      return true;
    }
    return false;
  }
  public boolean isYDistanceReached() {
    if (drive.getPoseY() - startingYDistance > targetYDistance) {
      return true;
    }
    return false;
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isXDistanceReached() && isYDistanceReached();
  }
}
