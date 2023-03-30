/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ConveyBalls extends SubsystemBase {

  TalonSRX mid_ConveyBalls = new TalonSRX(Constants.CONVEYER_BOTTOM);
  TalonSRX bottom_ConveyBalls = new TalonSRX(Constants.CONVEYOR_MID);
  TalonSRX top_ConveyBalls = new TalonSRX(Constants.CONVEYOR_TOP);

  // Constructor
  public ConveyBalls() {
    mid_ConveyBalls.setInverted(true);
    bottom_ConveyBalls.setInverted(true);
  }

  public void setConveyTop(double speed) {
    mid_ConveyBalls.set(ControlMode.PercentOutput, speed * 1);
    top_ConveyBalls.set(ControlMode.PercentOutput, speed * 1);
  }
  public void setConveyBottom(double speed) {
    bottom_ConveyBalls.set(ControlMode.PercentOutput, speed * 1);
    
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
