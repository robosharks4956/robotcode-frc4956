package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Shooter extends SubsystemBase {

  // RPMS for shooter settings at near, middle, and far distances
  public static final double kNearShotRpm = 2940;
  public static final double kMidShotRpm = 3530;
  public static final double kFarShotRpm = 5500;

  SparkFlex shooterMotor = new SparkFlex(23, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();
  private final SparkClosedLoopController motorClosedLoopController = shooterMotor.getClosedLoopController();
  RelativeEncoder shooterEncoder;

  public Shooter() {
    // Set voltage compensation so it always sets percent as though motor is at 12
    // volts, compensates for voltage drop when everything is running
    motorConfig.voltageCompensation(12);
    motorConfig.closedLoop.pid(6 * 0.5 / 10000.0, 0, 0).outputRange(-1, 1);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);
    shooterMotor.configure(
        motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    shooterEncoder = shooterMotor.getEncoder();
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Shooter Velocity", shooterEncoder::getVelocity, null);
    builder.addDoubleProperty("Shooter Output", shooterMotor::getAppliedOutput, null);
  }

  @Override
  public void periodic() {
  }

  public void set(double speed) {
    shooterMotor.set(speed);
  }

  public void setVelocity(double rpms) {
    double feedForward = 12 * rpms / 6472;
    // feedForward = 0;
    motorClosedLoopController.setSetpoint(rpms, ControlType.kVelocity, ClosedLoopSlot.kSlot0, feedForward);
  }

  public Command setVelocityCmd(double rpms) {
    return run(() -> setVelocity(rpms)).finallyDo(() -> set(0));
  }

  public Command chargeCmd(double speed) {
    return run(() -> shooterMotor.set(speed)).finallyDo(() -> set(0));
  }

  public Command chargeVelocityCmd(double velocity) {
    return run(() -> setVelocity(velocity)).finallyDo(() -> set(0));
  }

  public Command chargeVelocityCmd(DoubleSupplier velocitySupplier) {
    return run(() -> setVelocity(velocitySupplier.getAsDouble())).finallyDo(() -> set(0));
  }

  public double getVelocity() {
    return shooterMotor.getEncoder().getVelocity();
  }
}
