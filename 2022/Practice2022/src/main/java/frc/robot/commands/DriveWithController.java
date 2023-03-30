// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class DriveWithController extends CommandBase {

  DriveTrain driveTrain;
  XboxController driver;

  public DriveWithController(DriveTrain driveTrain, XboxController driver) {
    this.driveTrain = driveTrain;
    this.driver = driver;
    addRequirements(driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double speedMultiplier = 0.6;
    if (driver.getRightBumper())
      speedMultiplier = 1;

    double z = driver.getLeftX() * speedMultiplier;
    double y = (driver.getRightTriggerAxis() - driver.getLeftTriggerAxis()) * speedMultiplier;
    double x = driver.getRightX() * speedMultiplier;

    driveTrain.MecanumDrive(x, y, z);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveTrain.MecanumDrive(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
