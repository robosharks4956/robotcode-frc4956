// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  Spark shootermotor1;
  Spark shootermotor2;

  /** Creates a new Intake. */
  public Shooter() {
    shootermotor1 = new Spark(0);
    shootermotor2 = new Spark(2);
  }

  public void setSpeed(double speed) {
    shootermotor1.set(speed);
    shootermotor2.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
