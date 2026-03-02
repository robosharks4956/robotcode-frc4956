package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Shooter extends SubsystemBase {
  SparkMax shooterMotor = new SparkMax(22, MotorType.kBrushless);
  SparkMax feederMotor = new SparkMax(24, MotorType.kBrushless);

  public Shooter() {
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    // globalConfig.inverted(true);
    shooterMotor.configure(
        globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // not used currently
  public void set(double speed) {
    shooterMotor.set(speed);
  }

  /**
   * Creates a command that opens and closes the tail fin using input suppliers.
   *
   * @param inputSupplier The supplier for the input on the controller.
   * @return The command that opens and closes the tail fin using input suppliers.
   */
  public Command shooterCommand(DoubleSupplier inputSupplier) {
    return run(() -> set(inputSupplier.getAsDouble())).finallyDo(() -> set(0));
  }

  public Command chargeCommand(double speed) {
    return run(() -> shooterMotor.set(speed)).finallyDo(() -> shooterMotor.set(0));
  }

  public Command shootCommand(double speed) {
    return run(() -> feederMotor.set(speed)).finallyDo(() -> feederMotor.set(0));
  }

}
