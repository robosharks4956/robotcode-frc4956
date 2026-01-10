// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {

  private PneumaticsControlModule pneumaticsControlModule;
  private DoubleSolenoid solenoid;

  /** Creates a new Pneumatics. */
  public Pneumatics(int forwardChannel, int reverseChannel) {
    solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, forwardChannel, reverseChannel);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void forward() {
    solenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void reverse() {
    solenoid.set(DoubleSolenoid.Value.kReverse);
  }
  public void off() {
    solenoid.set(DoubleSolenoid.Value.kOff);
  }
}
