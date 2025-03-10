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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.LiftConstants.*;

public class Lift extends SubsystemBase {
  private final SparkMax motorController = new SparkMax(MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  private final SparkMaxConfig config = new SparkMaxConfig();
  private final SparkClosedLoopController closedLoopController = motorController.getClosedLoopController();

  /** Creates a new Lift. */
  public Lift() {
    config.inverted(false);
    config.idleMode(IdleMode.kBrake);
    config.closedLoop.p(0.1).i(0).d(0).outputRange(-0.3, 0.25);
    config.closedLoopRampRate(1);

    motorController.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    motorController.getEncoder().setPosition(0);
    //Hi Code Team! -build team.
    System.out.print("Hello Code Team!");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Lift Encoder Position", motorController.getEncoder().getPosition());
  }

  /**
   * Creates a command that sets the position of the lift.
   * @param position The desired position of the lift.
   * @return The command that sets the position of the lift.
   */
  public Command setPositionCommand(double position) {
    return runOnce(() -> closedLoopController.setReference(position, ControlType.kPosition));
  }

  /**
   * Creates a command that resets the encoder to 0.
   * @return The command that resets the encoder to 0.
   */
  public Command resetEncoderCommand() {
    return runOnce(() -> motorController.getEncoder().setPosition(0));
  }
}
