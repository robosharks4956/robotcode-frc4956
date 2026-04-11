package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Agitator extends SubsystemBase {

  public static final double kAgitateSpeed = 0.35;

  private final SparkFlex agitatorMotor = new SparkFlex(25, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();

  /** Creates a new Agitator. */
  public Agitator() {
    // Set voltage compensation so it always sets percent as though motor is at 12
    // volts, compensates for voltage drop when everything is running
    motorConfig.voltageCompensation(12);
    agitatorMotor.configure(
        motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // TODO: Should we be using velocity PID to ensure consistency in case it gets bogged down with lots of fuel?
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
    return startEnd(() -> set(-speed), () -> set(0));
  }
}
