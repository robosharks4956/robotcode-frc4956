// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import static frc.robot.Constants.CoralManipulatorConstants.*;

public class CoralManipulator extends SubsystemBase {
  private final Servo latchActuator = new Servo(LATCH_ACTUATOR_PWM_CHANNEL);
  //private final Servo angleActuator = new Servo(5);

  /** Creates a new CoralManipulator. */
  public CoralManipulator() {
    latchActuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
    latchActuator.setPosition(0.5);

    //angleActuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
    //angleActuator.setPosition(0.2);
  }

  @Override
  public void periodic() {}

  /**
   * Creates a command that closes the latch.
   * @return The command that closes the latch.
   */
  public Command latchCommand() {
    return runOnce(() -> latchActuator.setPosition(0.5));
  }

  /**
   * Creates a command that opens the latch.
   * @return The command that opens the latch.
   */
  public Command unlatchCommand() {
    return runOnce(() -> latchActuator.setPosition(0));
  }

  /**
   * Creates a command that moves the coral manipulator to the upper position.
   * @return The command that moves the coral manipulator to the upper position.
   */
  // public Command upperCommand() {
  //   return runOnce(() -> angleActuator.setPosition(0.9));
  //   //return run(() -> angleClosedLoopController.setReference(0.6, ControlType.kPosition, ClosedLoopSlot.kSlot0, 2.4));
  // }

  /**
   * Creates a command that moves the coral manipulator to the lower position.
   * @return The command that moves the coral manipulator to the lower position.
   */
  // public Command lowerCommand() {
  //   return runOnce(() -> angleActuator.setPosition(0.2));
  //   //return run(() -> angleClosedLoopController.setReference(0.1, ControlType.kPosition, ClosedLoopSlot.kSlot0, -1));
  // }
}
