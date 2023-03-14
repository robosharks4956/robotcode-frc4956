// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Latch extends SubsystemBase {
  /** Creates a new Latch. */
  VictorSPX motor=new VictorSPX(56);
    public void set(double Power){
      motor.set(VictorSPXControlMode.PercentOutput, Power);
    }
    public Encoder encoder = new Encoder(0,1);
    public void ResetEncoder(){
      encoder.reset();
    }
  public Latch() {
    encoder.setDistancePerPulse(360.0/44.4);
    SmartDashboard.putData("Slide Encoder", encoder);
    motor.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
