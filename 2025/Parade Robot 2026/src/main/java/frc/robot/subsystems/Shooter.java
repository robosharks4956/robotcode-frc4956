// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

  private final PWMSparkMax shooterSpark = new PWMSparkMax(4);

  /** Creates a new Shooter. */
  public Shooter() {
    shooterSpark.set(0);
  }

  public void on() {
    shooterSpark.set(1);
  }

  public void off() {
    shooterSpark.set(0);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
