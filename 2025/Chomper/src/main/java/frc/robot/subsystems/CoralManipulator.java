// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralManipulator extends SubsystemBase {
  private final Servo angleActuator = new Servo(4);
  private final Relay latchActuator = new Relay(0);  

  /** Creates a new CoralManipulator. */
  public CoralManipulator() {
    angleActuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
    latchActuator.set(Value.kOn);
  }

  @Override
  public void periodic() {}

  public void latch() {
    latchActuator.setDirection(Direction.kForward);
  }

  public void unlatch() {
    latchActuator.setDirection(Direction.kReverse);
  }

  public void goUp() {
    angleActuator.setPosition(1);
  }

  public void goDown() {
    angleActuator.setPosition(0.05);
  }
}
