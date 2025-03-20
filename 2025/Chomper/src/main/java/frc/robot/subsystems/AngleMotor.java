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

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AngleMotor extends SubsystemBase {
  private final SparkMax motorController = new SparkMax(14, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();
  
  private final SparkClosedLoopController motorClosedLoopController = motorController.getClosedLoopController();
 
  /** Creates a new AngleMotor. */
  public AngleMotor() {
    motorConfig.inverted(false).idleMode(IdleMode.kBrake).closedLoopRampRate(0.1);
    motorConfig.closedLoop.pid(8.5, 0, 4.5).outputRange(0.0, 0.1);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder).positionWrappingEnabled(false);

    motorController.configure(motorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    
    SmartDashboard.putNumber("Coral P", 8.5);
    SmartDashboard.putNumber("Coral I", 0);
    SmartDashboard.putNumber("Coral D", 4.5);
    SmartDashboard.putNumber("Coral Min", 0);
    SmartDashboard.putNumber("Coral Max", 0.1);
    SmartDashboard.putNumber("Coral Ramp Rate", 0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Coral Angle Encoder Position", motorController.getAbsoluteEncoder().getPosition());
    SmartDashboard.putNumber("Coral Angle Output", motorController.getAppliedOutput());

    // motorConfig.closedLoopRampRate(SmartDashboard.getNumber("Coral Ramp Rate", 0)).closedLoop
    //   .pid(SmartDashboard.getNumber("Coral P", 0), SmartDashboard.getNumber("Coral I", 0), SmartDashboard.getNumber("Coral D", 0))
    //   .outputRange(SmartDashboard.getNumber("Coral Min", 0), SmartDashboard.getNumber("Coral Max", 0));

    // SmartDashboard.putNumber("Coral P", SmartDashboard.getNumber("Coral I", 21));
  }

  /**
   * Creates a command that moves the coral manipulator to the upper position.
   * @return The command that moves the coral manipulator to the upper position.
   */
  public Command upperCommand() {
    return run(() -> motorClosedLoopController.setReference(0.295, ControlType.kPosition));
  }

  /**
   * Creates a command that moves the coral manipulator to the lower position.
   * @return The command that moves the coral manipulator to the lower position.
   */
  public Command lowerCommand() {
    return run(() -> motorClosedLoopController.setReference(0.1, ControlType.kPosition));
  }
}
