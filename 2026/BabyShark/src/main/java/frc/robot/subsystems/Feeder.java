// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

  public static final double kFeedSpeed = 0.5;

  /** Creates a new Feeder. */
  public Feeder() {}

  SparkMax feederMotor = new SparkMax(24, MotorType.kBrushless);

  public Command shootCommand(double speed) {
    return run(() -> feederMotor.set(speed)).finallyDo(() -> feederMotor.set(0));
  }

  public void set(double speed) {
    feederMotor.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
