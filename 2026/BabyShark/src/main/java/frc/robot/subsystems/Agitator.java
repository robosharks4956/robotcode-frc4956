package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkFlex;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Agitator extends SubsystemBase {

  public static final double kAgitateSpeed = 0.3;

  SparkFlex agitatorMotor = new SparkFlex(25, MotorType.kBrushless);

  /** Creates a new Agitator. */
  public Agitator() {
    // TODO: Add voltage compensation like in Shooter subsystem
    // TODO: Should we be using velocity PID to ensure consistency in case the agitator gets bogged down with lots of fuel?
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void set(double speed) {
    agitatorMotor.set(speed);
  }

  /**
   * Returns a command that sends fuel towards the shooter.
   */
  public Command agitateCmd() {
    return agitatorCmd(kAgitateSpeed);
  }

  private Command agitatorCmd(double speed) {
    // Leaving this version with a speed parameter just in case we ever want to make
    // a reverse command
    return run(() -> set(-speed)).finallyDo(() -> set(0));
  }
}
