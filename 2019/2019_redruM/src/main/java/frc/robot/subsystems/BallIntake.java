/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.SuckWithController;

/**
 * Subsystem for the one spinning wheel that sucks the ball in and shoots it.
 */
public class BallIntake extends Subsystem {

  public WPI_VictorSPX spinner1, spinner2;
  public Spark upNDown;

  public BallIntake() {
    super();
    spinner1 = new WPI_VictorSPX(RobotMap.ballSpinnerMotor);
    spinner2 = new WPI_VictorSPX(RobotMap.ballSpinnerMotor2);
    upNDown = new Spark(RobotMap.ballMotorCan);
    
  }
  
  public void setSpeed(double speed, double ballDirection) {
    spinner1.set(ControlMode.PercentOutput, speed * 1);
    spinner2.set(ControlMode.PercentOutput, speed * 1);
    upNDown.set(ballDirection * -.7);
  }
  
  public void initDefaultCommand() {
    setDefaultCommand(new SuckWithController());
  }
}
