// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

  private final TalonSRX frontLeftMotor = new TalonSRX(2);
  private final TalonSRX backLeftMotor = new TalonSRX(7);
  private final TalonSRX frontRightMotor = new TalonSRX(3);
  private final TalonSRX backRightMotor = new TalonSRX(8);

  // private final MecanumDrive mecanumDrive = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);

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
   * @param forward The desired percent of the maximum velocity to drive. Negative values drive backwards.
   * @param turn The desired percent of the maximum angular velocity to rotate. Negative values are clockwise.
   */
  public void drive(double forward, double turn, double strafe) {

    forward *= 01;
    turn *= 01;

    // MecanumDrive calculation

    frontLeftMotor.set(TalonSRXControlMode.PercentOutput, forward + strafe + turn);
    backLeftMotor.set(TalonSRXControlMode.PercentOutput, forward - strafe + turn);

    frontRightMotor.set(TalonSRXControlMode.PercentOutput, forward - strafe - turn);
    backRightMotor.set(TalonSRXControlMode.PercentOutput, forward + strafe - turn);
  }
}
