/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.LiftControlWithController;

/**
 * The ladder lift mechanism for raising balls and hatch panels high
 * up in the air. Controlled with two electric motors.
 */
public class Lift extends Subsystem {
  
  public WPI_TalonSRX motor, motor2;

  public Lift(){
    super();
    motor = new WPI_TalonSRX(RobotMap.liftMotor2);
    motor2 = new WPI_TalonSRX(RobotMap.liftMotor1);

    // Set a deadband to fix lift continuing to run when controls stick
    motor.configNeutralDeadband(0.1);
    motor2.configNeutralDeadband(0.1);

    // Slow down rate of change of motor speed to decrease jerkiness of lift control
    motor.configClosedloopRamp(1.2);
    motor2.configClosedloopRamp(1.2);
  }
  
  public void setSpeed(double speed){
    // Botto=-2950
    motor.set(ControlMode.PercentOutput, speed);
    motor2.set(ControlMode.PercentOutput, speed);
    //System.out.println("Motor 1: " + motor.getMotorOutputPercent() + " : " + motor.getMotorOutputVoltage());
    //System.out.println("Motor 2: " + motor2.getMotorOutputPercent() + " : " + motor2.getMotorOutputVoltage());
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new LiftControlWithController());
  }
}
