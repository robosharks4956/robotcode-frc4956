// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Angle extends SubsystemBase {
  private final SparkMax motorController = new SparkMax(14, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();
  
  private final SparkClosedLoopController motorClosedLoopController = motorController.getClosedLoopController();

  private boolean isUpper = false;
 
  /** Creates a new Angle. */
  public Angle() {
    motorConfig.inverted(false).idleMode(IdleMode.kBrake).closedLoopRampRate(0.1);
    motorConfig.closedLoop.pid(1.5, 0, 0.15).outputRange(-0.1, 0.1);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder).positionWrappingEnabled(false);

    motorController.configure(motorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Angle Encoder Position", motorController.getAbsoluteEncoder().getPosition());
    SmartDashboard.putNumber("Angle Output", motorController.getAppliedOutput());

    if (!SmartDashboard.containsKey("Angle Upper Position")) {
      SmartDashboard.putNumber("Angle Upper Position", 0.225);
    }
    if (!SmartDashboard.containsKey("Angle Lower Position")) {
      SmartDashboard.putNumber("Angle Lower Position", 0.1);
    }

    motorClosedLoopController.setReference(isUpper ? SmartDashboard.getNumber("Angle Upper Position", 0.225) : SmartDashboard.getNumber("Angle Lower Position", 0.1), ControlType.kPosition);
  }

  /**
   * Creates a command that moves the coral manipulator to the upper position.
   * @return The command that moves the coral manipulator to the upper position.
   */
  public Command upperCommand() {
    return runOnce(() -> isUpper = true);
  }

  /**
   * Creates a command that moves the coral manipulator to the lower position.
   * @return The command that moves the coral manipulator to the lower position.
   */
  public Command lowerCommand() {
    return runOnce(() -> isUpper = false);
  }
}
