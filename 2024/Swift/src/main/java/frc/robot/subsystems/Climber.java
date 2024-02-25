// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.CLIMBER_LEFT_MOTOR;
import static frc.robot.Constants.CLIMBER_RIGHT_MOTOR;
import static frc.robot.Constants.LATCH_LEFT_SERVO;
import static frc.robot.Constants.LATCH_RIGHT_SERVO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  private final VictorSPX leftMotor = new VictorSPX(CLIMBER_LEFT_MOTOR);
  private final VictorSPX rightMotor = new VictorSPX(CLIMBER_RIGHT_MOTOR);
  private final Servo leftLatch = new Servo(LATCH_LEFT_SERVO);
  private final Servo rightLatch = new Servo(LATCH_RIGHT_SERVO);
  double leftOpen = 0;
  double leftClosed = 1;
  double rightOpen = 0;
  double rightClosed = 1;
  boolean latchOpen = false;

  public void set(double power){
    leftMotor.set(ControlMode.PercentOutput, power * 0.5);
    rightMotor.set(ControlMode.PercentOutput, power * 0.5);
  }

  public Climber() {
    leftLatch.set(leftClosed);
    rightLatch.set(rightClosed);
  }

  public void setLatch(boolean latchOpen) {
    if (latchOpen) {
      leftLatch.set(leftOpen);
      rightLatch.set(rightOpen);
    }
    else {
      leftLatch.set(leftClosed);
      rightLatch.set(rightClosed);
    }
    this.latchOpen = latchOpen;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
