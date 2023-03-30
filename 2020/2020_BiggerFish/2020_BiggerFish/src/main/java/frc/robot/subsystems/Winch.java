/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Winch extends SubsystemBase {

  CANSparkMax winch_motor = new CANSparkMax(Constants.WINCH, MotorType.kBrushless);

  /**
   * Creates a new Winch.
   */
  public Winch() {
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void set(double speed) {
    if (speed > -0.1) // Winch only goes one way and has a 0.1 deadband
      winch_motor.set(0);
    else
      winch_motor.set(speed);
  }
}
