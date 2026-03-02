// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Agitator extends SubsystemBase {

  //SparkMax feederMotor = new SparkMax(24, MotorType.kBrushless);
  SparkMax agitatorMotor = new SparkMax(25, MotorType.kBrushless);

  /** Creates a new Feeder. */
  public Agitator() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void set(double agitatorSpeed) {
    //feederMotor.set(feederSpeed);
    agitatorMotor.set(agitatorSpeed);
  }



  public Command agitatorCommand(double agitatorSpeed) {
    return run(() -> agitatorMotor.set(-agitatorSpeed)).finallyDo(() -> agitatorMotor.set(0));
  }

}
