// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.CoralManipulatorConstants.*;

public class Latch extends SubsystemBase {
  private final Servo actuator = new Servo(LATCH_ACTUATOR_PWM_CHANNEL);

  /** Creates a new Latch. */
  public Latch() {
    actuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
    actuator.setPosition(0.5);
  }

  @Override
  public void periodic() {}

  /**
   * Creates a command that closes the latch.
   * @return The command that closes the latch.
   */
  public Command latchCommand() {
    return runOnce(() -> actuator.setPosition(0.5));
  }

  /**
   * Creates a command that opens the latch.
   * @return The command that opens the latch.
   */
  public Command unlatchCommand() {
    return runOnce(() -> actuator.setPosition(0));
  }
}
