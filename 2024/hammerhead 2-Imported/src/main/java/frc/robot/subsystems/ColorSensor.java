// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {

  DigitalInput sensor = new DigitalInput(9);

  /** Creates a new ColorSensor. */
  public ColorSensor() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public boolean get() {
    return sensor.get();
  }
}
