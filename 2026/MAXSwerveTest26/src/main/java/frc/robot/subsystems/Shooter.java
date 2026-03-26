package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;
import frc.robot.subsystems.AprilTagCamera;

public class Shooter extends SubsystemBase {
  SparkFlex shooterMotor = new SparkFlex(23, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();
  private final SparkClosedLoopController motorClosedLoopController = shooterMotor.getClosedLoopController();


  public Shooter() {
    // Set voltage compensation so it always sets percent as though motor is at 12 volts, compensates for voltage drop when everything is running
    motorConfig.voltageCompensation(12);
    motorConfig.closedLoop.pid(6 * 0.5 / 10000.0, 0, 0).outputRange(-1, 1);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);
    shooterMotor.configure(
        motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

   @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Output", shooterMotor.getAppliedOutput());
    SmartDashboard.putNumber("Shooter Velocity", shooterMotor.getEncoder().getVelocity());
  }

  public void set(double speed) {
    shooterMotor.set(speed);
  }

  public void setVelocity(double rpms) {
    double feedForward = 12*rpms/6472;
    // feedForward = 0;
    motorClosedLoopController.setSetpoint(rpms, ControlType.kVelocity, ClosedLoopSlot.kSlot0, feedForward);
  }

  public Command setVelocityCommand(double rpms) {
    return run(() -> setVelocity(rpms)).finallyDo(() -> set(0));
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
  // 3530 - Medium (0.65)
  // 5500 - High (1)

  public Command chargeCommandPID(double speed) {
    return run(() -> setVelocity(speed)).finallyDo(() -> shooterMotor.set(0));
  }

  public Command chargeCommandPID(DoubleSupplier inputSupplier) {
    return run(() -> setVelocity(inputSupplier.getAsDouble())).finallyDo(() -> shooterMotor.set(0));
  }

  public double getSpeed() {
    return shooterMotor.getEncoder().getVelocity();
  }
}
