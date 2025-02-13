// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.NoteMechanismIDs.*;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;

public class Shooter extends SubsystemBase { 
  private final SparkMax leftMotor = new SparkMax(SHOOTING_MOTOR, MotorType.kBrushless);
  private final SparkMax rightMotor = new SparkMax(SHOOTER_CORNER_MOTOR, MotorType.kBrushless);

  public Shooter() {
    SparkMaxConfig invertedMotor = new SparkMaxConfig();
    invertedMotor.inverted(true);
    leftMotor.configure(invertedMotor, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
