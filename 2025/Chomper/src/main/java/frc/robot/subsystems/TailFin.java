// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import static frc.robot.Constants.TailFinConstants.*;

import java.util.function.DoubleSupplier;

public class TailFin extends SubsystemBase {
  private final SparkMax motorController = new SparkMax(MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  private final SparkMaxConfig config = new SparkMaxConfig();

  /** Creates a new TailFin. */
  public TailFin() {
    config.inverted(false);
    config.idleMode(IdleMode.kBrake);
  }

  @Override
  public void periodic() {}

  /**
   * Creates a command that opens and closes the tail fin using input suppliers.
   * @param percentVelocitySupplier The supplier for the desired percent of the max velocity of the motor controller.
   * @return The command that opens and closes the tail fin using input suppliers.
   */
  public Command hangCommand(DoubleSupplier percentVelocitySupplier) {
    return run(() -> motorController.set(percentVelocitySupplier.getAsDouble()));
  }
}
