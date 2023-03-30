/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class DistanceDrive extends CommandBase {
  
  DriveTrain drivetrain;
  double distance;
  double speed;

  public DistanceDrive(DriveTrain drivetrain, double distance, double speed) {
    this.drivetrain = drivetrain;
    this.distance = distance;
    this.speed = speed;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetEncoders();
  }

  
  @Override
  public void execute() {
    drivetrain.tankDrive(speed, speed);
    System.out.println("Left pos: " + drivetrain.getLeftEncoder() + " Right pos: " + drivetrain.getRightEncoder());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return drivetrain.getLeftEncoder() > distance && drivetrain.getRightEncoder() > distance;
  }
}
