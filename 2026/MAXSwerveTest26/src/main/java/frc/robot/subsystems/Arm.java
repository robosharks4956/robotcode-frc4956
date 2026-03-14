// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utils;

public class Arm extends SubsystemBase {
  SparkMax armMotor = new SparkMax(28, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();


  
  private final SparkClosedLoopController motorClosedLoopController = armMotor.getClosedLoopController();
  
  private boolean isUpper = false;
  private double upperPosition = 246.5;
  private double lowerPosition = 0;
  private double targetPosition = upperPosition;


   /** Creates a new Angle. */
  public Arm() {
    
    //SmartDashboard.putD("Arm Motor", armMotor::get);
    motorConfig.inverted(false).idleMode(IdleMode.kBrake).closedLoopRampRate(0.1);
    motorConfig.closedLoop.pid(0.5/upperPosition, 0, 0).outputRange(-0.03, 0.05);
    motorConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder).positionWrappingEnabled(true);

    armMotor.configure(motorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    
  }
  @Override
  public void periodic() {
      // motorClosedLoopController.setSetpoint(isUpper ? SmartDashboard.getNumber("Angle Upper Position", 0.225) : SmartDashboard.getNumber("Angle Lower Position", 0.1), ControlType.kPosition);
  }
    // This method will be called once per scheduler run
  

   /**
   * Creates a command that moves the coral manipulator to the upper position.
   * @return The command that moves the coral manipulator to the upper position.
   */


  // Upper Position: 246.5
  // Lower Position: 0

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

  public Command manualControl(DoubleSupplier speedSupplier) {
    return run(() -> {
      armMotor.set(Utils.modifyAxis(speedSupplier.getAsDouble() * 1,1, 0.05, 3)) ;
      //System.out.println("Arm Input: " + speedSupplier.getAsDouble());
    }
    );
  }

  
  public Command manualControlPID(DoubleSupplier speedSupplier) {
    return run(() -> {
      targetPosition += speedSupplier.getAsDouble();
      motorClosedLoopController.setSetpoint(targetPosition, ControlType.kPosition, ClosedLoopSlot.fromInt(0), 0);
    }).finallyDo(() -> armMotor.set(0));
  }


  public Command setSpeed(double speed) {
      return run(() -> armMotor.set(-speed)).finallyDo(() -> armMotor.set(0));
  }
}
