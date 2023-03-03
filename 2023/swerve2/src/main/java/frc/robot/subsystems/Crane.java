// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Crane extends SubsystemBase {
  TalonSRX motor=new TalonSRX(0);
  public void set(double Power){
    motor.set(TalonSRXControlMode.PercentOutput, Power);

  }
  /** Creates a new Crane. */
  public Crane() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
