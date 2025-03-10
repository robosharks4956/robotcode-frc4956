// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AngleMotor extends SubsystemBase {
  private final SparkMax angleMotorController = new SparkMax(14, MotorType.kBrushless);
  private final SparkMaxConfig angleConfig = new SparkMaxConfig();
  private final SparkClosedLoopController angleClosedLoopController = angleMotorController.getClosedLoopController();
 
  /** Creates a new AngleMotor. */
  public AngleMotor() {
    angleConfig.inverted(false);
    angleConfig.idleMode(IdleMode.kBrake);
    angleConfig.closedLoop.p(0.8).i(0.000).d(0.09).outputRange(-0.4, 0.4);
    angleConfig.closedLoopRampRate(0.1);

    angleMotorController.configure(angleConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    angleMotorController.getEncoder().setPosition(0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Coral Angle Encoder Position", angleMotorController.getEncoder().getPosition());
    SmartDashboard.putNumber("Coral Angle Output", angleMotorController.getAppliedOutput());
  }

  /**
   * Creates a command that moves the coral manipulator to the upper position.
   * @return The command that moves the coral manipulator to the upper position.
   */
  public Command upperCommand() {
    return run(() -> angleClosedLoopController.setReference(0.6, ControlType.kPosition, ClosedLoopSlot.kSlot0, 2.4));
  }

  /**
   * Creates a command that moves the coral manipulator to the lower position.
   * @return The command that moves the coral manipulator to the lower position.
   */
  public Command lowerCommand() {
    return run(() -> angleClosedLoopController.setReference(0.1, ControlType.kPosition, ClosedLoopSlot.kSlot0, -1));
  }
}
