/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;

public class DriveTrain extends SubsystemBase {
  //Servo servo = new Servo(2);
  
  //left motors
  CANSparkMax left_front = new CANSparkMax(Constants.DRIVE_LEFT_FRONT, MotorType.kBrushless);
  CANSparkMax left_back = new CANSparkMax(Constants.DRIVE_LEFT_BACK, MotorType.kBrushless); 
  SpeedControllerGroup left_group = new SpeedControllerGroup(left_front, left_back);
 
  //right motors
  CANSparkMax right_front = new CANSparkMax(Constants.DRIVE_RIGHT_FRONT, MotorType.kBrushless);
  CANSparkMax right_back = new CANSparkMax(Constants.DRIVE_RIGHT_BACK, MotorType.kBrushless);
  SpeedControllerGroup right_group = new SpeedControllerGroup(right_front, right_back);

  DifferentialDrive drive = new DifferentialDrive(left_group, right_group);

  //Gyro
  ADXRS450_Gyro gUnit = new ADXRS450_Gyro();

  /**
   * Creates a new Drivetrain.
   */
  public DriveTrain() {
    double rate = 0.0; // Changed after drive tryouts, need to see if it's OK. Was too fast during tryouts.
    left_front.setOpenLoopRampRate(rate);
    left_back.setOpenLoopRampRate(rate);
    right_front.setOpenLoopRampRate(rate);
    right_back.setOpenLoopRampRate(rate);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void arcadeDrive(final double xSpeed, final double zRotation) {
    drive.arcadeDrive(xSpeed, zRotation);
  }

  public void tankDrive(final double leftSpeed, final double rightSpeed) {
    drive.tankDrive(leftSpeed, rightSpeed);
  }

  public double getLeftEncoder() {
    System.out.println("Left ticks: " + left_front.getEncoder().getPosition() + ", Per rev: " + left_front.getEncoder().getCountsPerRevolution());
    return (left_front.getEncoder().getPosition()/10.71) * (6 * Math.PI); 
  }

  public double getRightEncoder() {
    System.out.println("Right ticks: " + right_back.getEncoder().getPosition());
    return (-right_back.getEncoder().getPosition()/10.71) * (6 * Math.PI); 
  }

  public void resetEncoders() {
    right_back.getEncoder().setPosition(0);
    left_front.getEncoder().setPosition(0);
  }

  
  public void resetGyro() {
    gUnit.reset();
  }
  public double getGyro() {
    return gUnit.getAngle();
  }

  public void motorIdle(IdleMode idleMode) {
    left_front.setIdleMode(idleMode);
    left_back.setIdleMode(idleMode);
    right_front.setIdleMode(idleMode);
    right_back.setIdleMode(idleMode);
  }

  
}
