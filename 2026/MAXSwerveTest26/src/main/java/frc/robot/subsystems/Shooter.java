package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;
import frc.robot.subsystems.AprilTagCamera;

public class Shooter extends SubsystemBase {
  SparkMax shooterMotor = new SparkMax(22, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();
  private final SparkClosedLoopController motorClosedLoopController = shooterMotor.getClosedLoopController();


  public Shooter() {
    SparkMaxConfig globalConfig = new SparkMaxConfig();

    motorConfig.closedLoop.pid(0, 0, 0).outputRange(-1, 1);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);
    // globalConfig.inverted(true);
    shooterMotor.configure(
        globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

   @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Velocity", shooterMotor.getEncoder().getVelocity());
    //motorClosedLoopController.setSetpoint(6, ControlType.kPosition, 0, );
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

  // RPMS for shooter settings
  // 2940 - Low setting (0.55)
  // 3470 - Medium (0.65)
  // 5500 - High (1)

  public Command chargeCommandPID(double speed) {
    return run(() -> motorClosedLoopController.setSetpoint(speed, ControlType.kVelocity, ClosedLoopSlot.fromInt(0), 12.5*speed/5500
     )).finallyDo(() -> shooterMotor.set(0));
  }
}
