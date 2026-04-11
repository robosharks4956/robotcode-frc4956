package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

  public static final double kFeedSpeed = 0.5;

  private final SparkMax feederMotor = new SparkMax(24, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();

  /** Creates a new Feeder. */
  public Feeder() {
    motorConfig.voltageCompensation(12);
    feederMotor.configure(
        motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void set(double speed) {
    feederMotor.set(speed);
  }

  /**
   * Return a Command to feed fuel into the shooter.
   */
  public Command shootCmd() {
    return run(() -> set(kFeedSpeed)).finallyDo(() -> set(0));
  }

  /**
   * Return a Command to reverse the feeder, sending fuel back into the hopper.
   */
  public Command reverseCmd() {
    return run(() -> set(-kFeedSpeed)).finallyDo(() -> set(0));
  }
}
