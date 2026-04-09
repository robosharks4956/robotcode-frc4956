package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

  public static final double kFeedSpeed = 0.5;

  SparkMax feederMotor = new SparkMax(24, MotorType.kBrushless);

  /** Creates a new Feeder. */
  public Feeder() {
    // TODO: Add voltage compensation like in Shooter subsystem
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

  public void set(double speed) {
    feederMotor.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
