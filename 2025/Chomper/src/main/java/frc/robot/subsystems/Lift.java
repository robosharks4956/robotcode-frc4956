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

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lift extends SubsystemBase {
  private final SparkMax motorController = new SparkMax(10, MotorType.kBrushless);

  private final SparkMaxConfig config = new SparkMaxConfig();

  //private final SparkClosedLoopController closedLoopController = motorController.getClosedLoopController();

  /** Creates a new Lift. */
  public Lift() {
    config.inverted(false);
    config.idleMode(IdleMode.kBrake);
    //config.closedLoop.p(0).i(0).d(0).outputRange(0, 0);

    motorController.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    //Hi Code Team! -build team.
    System.out.print("Hello Code Team!");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Lift Encoder Position", motorController.getEncoder().getPosition());
  }

  /**
   * Sets the velocity of the lift.
   * @param velocity The desired velocity of the lift.
   */
  public void setVelocity(double velocity) {
    motorController.set(MathUtil.applyDeadband(velocity, 0.05));//closedLoopController.setReference(velocity, ControlType.kVelocity);
  }

  /**
   * Sets the position of the lift.
   * @param position The desired position of the lift.
   */
  public void setPosition(double position) {
    //closedLoopController.setReference(position, ControlType.kPosition);
  }
}
