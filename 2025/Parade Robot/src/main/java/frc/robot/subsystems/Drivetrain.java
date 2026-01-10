// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

  private final TalonSRX frontLeftMotor = new TalonSRX(2);
  private final TalonSRX backLeftMotor = new TalonSRX(7);
  private final TalonSRX frontRightMotor = new TalonSRX(3);
  private final TalonSRX backRightMotor = new TalonSRX(8);

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    frontLeftMotor.setInverted(false);
    backLeftMotor.setInverted(false);
    frontRightMotor.setInverted(true);
    backRightMotor.setInverted(true);
  }

  @Override
  public void periodic() {}

  /**
   * Drives the robot with arcade style steering.
   * @param driveVelocity The desired percent of the maximum velocity to drive. Negative values drive backwards.
   * @param turnVelocity The desired percent of the maximum angular velocity to rotate. Negative values are clockwise.
   */
  public void drive(double driveVelocity, double turnVelocity) {

    driveVelocity *= 01;
    turnVelocity *= 01;

    // Arcade drive calculation
    double left_out = driveVelocity + turnVelocity;
    double right_out = driveVelocity - turnVelocity;

    frontLeftMotor.set(TalonSRXControlMode.PercentOutput, left_out);
    backLeftMotor.set(TalonSRXControlMode.PercentOutput, left_out);

    frontRightMotor.set(TalonSRXControlMode.PercentOutput, right_out);
    backRightMotor.set(TalonSRXControlMode.PercentOutput, right_out);
  }
}
