// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  private final PWMSparkMax frontLeftMotor = new PWMSparkMax(0);
  private final PWMSparkMax backLeftMotor = new PWMSparkMax(1);
  private final PWMSparkMax frontRightMotor = new PWMSparkMax(3);
  private final PWMSparkMax backRightMotor = new PWMSparkMax(2);

  private final DifferentialDrive drivebase = new DifferentialDrive(frontLeftMotor, frontRightMotor);

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    frontLeftMotor.addFollower(backLeftMotor);
    frontRightMotor.addFollower(backRightMotor);

    frontLeftMotor.setInverted(false);
    frontRightMotor.setInverted(true);
  }

  @Override
  public void periodic() {}

  /**
   * Drives the robot with arcade style steering.
   * @param driveVelocity The desired percent of the maximum velocity to drive. Negative values drive backwards.
   * @param turnVelocity The desired percent of the maximum angular velocity to rotate. Negative values are clockwise.
   */
  public void drive(double driveVelocity, double turnVelocity) {
    drivebase.arcadeDrive(driveVelocity, turnVelocity);
  }
}
