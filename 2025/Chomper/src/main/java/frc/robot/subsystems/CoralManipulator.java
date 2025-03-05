// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class CoralManipulator extends SubsystemBase {
  private final Servo angleActuator = new Servo(ANGLE_ACTUATOR_PWM_CHANNEL);
  private final Servo latchActuator = new Servo(LATCH_ACTUATOR_PWM_CHANNEL);  

  /** Creates a new CoralManipulator. */
  public CoralManipulator() {
    angleActuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
    latchActuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
  }

  @Override
  public void periodic() {}

  public void latch() {
    latchActuator.setPosition(1);
  }

  public void unlatch() {
    latchActuator.setPosition(0.05);
  }

  public void goUp() {
    angleActuator.setPosition(1);
  }

  public void goDown() {
    angleActuator.setPosition(0.05);
  }
}
