// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * Rumbles a controller to create feedback.
 */
public class Rumble extends SubsystemBase {

  XboxController controller;

  public Rumble(CommandXboxController controller) {
    this.controller = controller.getHID();
  }

  public void stop() {
    controller.setRumble(RumbleType.kBothRumble, 0);
  }

  public void full() {
    controller.setRumble(RumbleType.kBothRumble, 1);
  }

  private Command timeCmd(double seconds) {
    return run(this::full).withTimeout(seconds).finallyDo(this::stop);
  }

  public Command longCmd() {
    return timeCmd(1);
  }

  public Command shortCmd() {
    return timeCmd(0.5);
  }

  private Command gapCmd() {
    return run(this::stop).withTimeout(0.4);
  }

  public Command shortLongCmd() {
    return shortCmd().andThen(gapCmd()).andThen(longCmd());
  }

  public Command longShortCmd() {
    return shortCmd().andThen(gapCmd()).andThen(longCmd());
  }
}