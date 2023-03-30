// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Winch extends SubsystemBase {

  Spark winchmotor1 = new Spark(3);
  Spark winchmotor2 = new Spark(4);

  /** Creates a new Intake. */
  public Winch() {
    winchmotor1.setInverted(true);;
    winchmotor2.setInverted(true);
  }

  public void setSpeed(double speed) {
    winchmotor1.set(speed);
    winchmotor2.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
