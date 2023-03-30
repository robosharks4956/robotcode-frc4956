/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class AngleTurn extends CommandBase {
  
  DriveTrain drivetrain;
  double angle;
  double speed;
  public AngleTurn(DriveTrain drivetrain, double angle, double speed) {
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
    this.angle = angle;
    this.speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetGyro();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    speed = speed * (angle > 0 ? 1 : -1);
    drivetrain.tankDrive(speed, -speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(angle) - 3) < Math.abs(drivetrain.getGyro());
  }
}
