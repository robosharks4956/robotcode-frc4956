// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utils;

public class Climber extends SubsystemBase {

  SparkMax climberMotor = new SparkMax(30, MotorType.kBrushless);

  public Climber() {
  }

  public Command setSpeedCmd(DoubleSupplier speedSupplier) {
     return run(() -> climberMotor.set(Utils.modifyAxis(speedSupplier.getAsDouble() * 1, 1, 0.05, 3)));
  }

  @Override
  public void periodic() {
  }
}
