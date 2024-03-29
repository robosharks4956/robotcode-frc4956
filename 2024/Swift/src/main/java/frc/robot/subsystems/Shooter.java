// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.NoteMechanismIDs.*;

public class Shooter extends SubsystemBase { 
  private final CANSparkMax leftMotor = new CANSparkMax(SHOOTING_MOTOR, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(SHOOTER_CORNER_MOTOR, MotorType.kBrushless);

  public Shooter() {
    leftMotor.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void set(double power) {
    leftMotor.set(power);
    rightMotor.set(power * 0.95);
    SmartDashboard.putNumber("Shooter Velocity Left", leftMotor.getEncoder().getVelocity());
  }

  public double getRPM() {
    return leftMotor.getEncoder().getVelocity();
  }
}
