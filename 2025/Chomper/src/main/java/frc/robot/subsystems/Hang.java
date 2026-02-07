// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.TailFinConstants.MOTOR_CONTROLLER_ID;
import static frc.robot.Utils.modifyAxis;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hang extends SubsystemBase {
  private final SparkMax motorController = new SparkMax(MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  private final SparkMaxConfig config = new SparkMaxConfig();

  /** Creates a new Hang. */
  public Hang() {
    config.inverted(false);
    config.idleMode(IdleMode.kBrake);
  }

  @Override
  public void periodic() {}

  /**
   * Creates a command that opens and closes the tail fin using input suppliers.
   * @param inputSupplier The supplier for the input on the controller.
   * @return The command that opens and closes the tail fin using input suppliers.
   */
  public Command hangCommand(DoubleSupplier inputSupplier) {
    return run(() -> motorController.set(modifyAxis(inputSupplier.getAsDouble(), 0.5, 0.05, 3)));
  }
}
