package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Shooter extends SubsystemBase {
  SparkMax shooterMotor = new SparkMax(22, MotorType.kBrushless);

  XboxController joystick;

  public Shooter() {
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    // globalConfig.inverted(true);
    shooterMotor.configure(
        globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // When is this used?
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

  public Command shooterCommand(double speed) {
    return run(() -> set(speed)).finallyDo(() -> set(0));
  }
}
