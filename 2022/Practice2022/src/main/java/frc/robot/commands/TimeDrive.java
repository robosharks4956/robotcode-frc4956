// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class TimeDrive extends CommandBase {

  DriveTrain driveTrain;
  double timeS, x, y, z;
  Timer driveTimer = new Timer();

  /** Creates a new TimeDrive. */
  public TimeDrive(DriveTrain driveTrain, double timeS, double x, double y, double z) {
    addRequirements(driveTrain);
    this.driveTrain = driveTrain;
    this.timeS = timeS;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveTimer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveTrain.MecanumDrive(x, y, z);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveTrain.MecanumDrive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    // Quit when time in seconds has elapsed
    return driveTimer.get() >= timeS;
  }
}
