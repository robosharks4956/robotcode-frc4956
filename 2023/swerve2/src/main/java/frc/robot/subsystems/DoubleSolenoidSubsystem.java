// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DoubleSolenoidSubsystem extends SubsystemBase {
  DoubleSolenoid solenoid; 
  public DoubleSolenoidSubsystem(int forward, int reverse) {
     solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, forward, reverse);
  }
  public void set(boolean forward){
    solenoid.set(forward ? Value.kForward : Value.kReverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
