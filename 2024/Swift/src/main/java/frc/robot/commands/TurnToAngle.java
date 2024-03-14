package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

/**
 * Auton command to turn until the robot is oriented towards an angle on the gyro.
 */
public class TurnToAngle extends Command {

  PIDController turnPID = new PIDController(10, 0.1, 2);

  Drivetrain drivetrain;
  double targetAngle;

  /** Creates a new TurnToAngle. */
  public TurnToAngle(Drivetrain drivetrain, double targetAngle) {
    this.drivetrain = drivetrain;
    this.targetAngle = targetAngle;

    addRequirements(drivetrain);

    turnPID.setIntegratorRange(-15, 15);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      double turnOutput = turnPID.calculate(drivetrain.getGyroAngle(), targetAngle);
      SmartDashboard.putNumber("Turn Output", turnOutput);
      drivetrain.drive(
          new ChassisSpeeds(
              0,
              0,
              -turnOutput
        ));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Stop command when current gyro angle is withing 0.1 of the target angle
    return targetAngle < drivetrain.getGyroAngle() + 0.1 && targetAngle > drivetrain.getGyroAngle() - 0.1;
  }
}
