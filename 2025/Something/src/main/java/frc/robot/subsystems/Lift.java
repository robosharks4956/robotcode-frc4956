// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lift extends SubsystemBase {
  private final SparkMax leftMotor = new SparkMax(10, MotorType.kBrushless);
  private final SparkMax rightMotor = new SparkMax(11, MotorType.kBrushless);

  private final SparkMaxConfig leftConfig = new SparkMaxConfig();
  private final SparkMaxConfig rightConfig = new SparkMaxConfig();

  private final SparkClosedLoopController closedLoopController = leftMotor.getClosedLoopController();

  /** Creates a new Lift. */
  public Lift() {
    leftConfig.inverted(false);
    leftConfig.idleMode(IdleMode.kBrake);
    leftConfig.closedLoop.p(0).i(0).d(0).outputRange(0, 0);
    
    rightConfig.inverted(true);
    rightConfig.idleMode(IdleMode.kBrake);
    rightConfig.follow(leftMotor);

    leftMotor.configure(
      leftConfig,
      ResetMode.kNoResetSafeParameters,
      PersistMode.kPersistParameters);

    rightMotor.configure(
      rightConfig,
      ResetMode.kNoResetSafeParameters,
      PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Lift Encoder Position", leftMotor.getEncoder().getPosition());
  }

  /**
   * Sets the velocity of the lift.
   * @param velocity The desired velocity of the lift.
   */
  public void setVelocity(double velocity) {
    closedLoopController.setReference(velocity, ControlType.kVelocity);
  }

  /**
   * Sets the position of the lift.
   * @param position The desired position of the lift.
   */
  public void setPosition(double position) {
    closedLoopController.setReference(position, ControlType.kPosition);
  }
}
