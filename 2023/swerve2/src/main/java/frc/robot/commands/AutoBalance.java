package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class AutoBalance extends CommandBase {
  /** Creates a new AutoBalance. */
  public Drivetrain drive;

  public AutoBalance(Drivetrain drive) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
    this.drive = drive;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  static final double kOffBalanceAngleThresholdDegrees = 2;
  static final double kOonBalanceAngleThresholdDegrees = 5;
  boolean autoBalanceXMode = false;
  boolean autoBalanceYMode = true;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xAxisRate = 1;
    double yAxisRate = 1;
    double pitchAngleDegrees = drive.getPitch();
    double rollAngleDegrees = drive.getRoll();
    SmartDashboard.putNumber("Pitch Angle", pitchAngleDegrees);
    SmartDashboard.putNumber("Roll Angle", rollAngleDegrees);

    if (!autoBalanceXMode && (Math.abs(pitchAngleDegrees) >= Math.abs(kOffBalanceAngleThresholdDegrees))) {
      autoBalanceXMode = true;
    } else if (autoBalanceXMode && (Math.abs(pitchAngleDegrees) <= Math.abs(kOonBalanceAngleThresholdDegrees))) {
      autoBalanceXMode = false;
    }
    if (!autoBalanceYMode && (Math.abs(pitchAngleDegrees) >= Math.abs(kOffBalanceAngleThresholdDegrees))) {
      autoBalanceYMode = true;
    } else if (autoBalanceYMode && (Math.abs(pitchAngleDegrees) <= Math.abs(kOonBalanceAngleThresholdDegrees))) {
      autoBalanceYMode = false;
    }
    if (autoBalanceXMode) {
      double pitchAngleRadians = pitchAngleDegrees * (Math.PI / 180.0);
      xAxisRate = Math.sin(pitchAngleRadians) * -1;
    }
    if (autoBalanceYMode) {
      double rollAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
      yAxisRate = Math.sin(rollAngleRadians) * -1;
    }

    try {
      drive.drive(
          new ChassisSpeeds(
              xAxisRate * 40,
              yAxisRate * 40,
              0));
    } catch (RuntimeException ex) {
      String err_string = "Drive system error:  " + ex.getMessage();
      DriverStation.reportError(err_string, true);
    }
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
