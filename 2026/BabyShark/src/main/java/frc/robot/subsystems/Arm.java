// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utils;

public class Arm extends SubsystemBase {
  SparkMax armMotor = new SparkMax(28, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();

  private final SparkClosedLoopController motorClosedLoopController = armMotor.getClosedLoopController();

  private double upperPosition = 246.5;
  private double lowerPosition = 0;
  private double targetPosition = upperPosition;

  private SparkAbsoluteEncoder encoder;

  public Arm() {

    // TODO: Add voltage compensation like in Shooter subsystem

    motorConfig.inverted(false).idleMode(IdleMode.kBrake).closedLoopRampRate(0.1);
    motorConfig.closedLoop.pid(0.5 / upperPosition, 0, 0).outputRange(-0.03, 0.05);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder).positionWrappingEnabled(true);
    armMotor.configure(motorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

    encoder = armMotor.getAbsoluteEncoder();
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Position", encoder::getPosition, null);
    builder.addDoubleProperty("Applied Output", armMotor::getAppliedOutput, null);

    // Allows setting arm position setpoints from dashboard
    builder.addDoubleProperty("Upper Position", () -> upperPosition, (value) -> upperPosition = value);
    builder.addDoubleProperty("Lower Position", () -> lowerPosition, (value) -> upperPosition = value);
  }

  @Override
  public void periodic() {
  }

  /**
   * Set speed from double supplier. Meant to be used with joystick controller input, smooths the input.
   * @param speedSupplier Joystick axis.
   * @return The command that will set speed to joystick input.
   */
  public Command setSpeedCmd(DoubleSupplier speedSupplier) {
    return run(() -> {
      armMotor.set(Utils.modifyAxis(speedSupplier.getAsDouble() * 1, 1, 0.05, 3));
    });
  }

  /** Sets speed to a constant value. Sets speed to zero when command finishes. */
  public Command setSpeedCmd(double speed) {
    return run(() -> armMotor.set(-speed)).finallyDo(() -> armMotor.set(0));
  }

  public Command manualControlPID(DoubleSupplier speedSupplier) {
    return run(() -> {
      targetPosition += speedSupplier.getAsDouble();
      motorClosedLoopController.setSetpoint(targetPosition, ControlType.kPosition, ClosedLoopSlot.fromInt(0), 0);
    }).finallyDo(() -> armMotor.set(0));
  }
}
