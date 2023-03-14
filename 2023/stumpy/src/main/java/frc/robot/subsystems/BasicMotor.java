// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BasicMotor extends SubsystemBase {
  Spark spark = new Spark(0);
  /** Creates a new BasicMotor. */
  public BasicMotor() {
  }

  public void set (double speed) {
    spark.set(speed);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
