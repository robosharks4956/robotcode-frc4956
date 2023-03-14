// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SimpleSparkMAX extends SubsystemBase {
  private CANSparkMax m_motor = new CANSparkMax(26, MotorType.kBrushless);
  /** Creates a new SimpleSparkMAX. */
  public void set (double speed) {
    m_motor.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
