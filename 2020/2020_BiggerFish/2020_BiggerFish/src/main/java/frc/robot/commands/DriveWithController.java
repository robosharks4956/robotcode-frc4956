/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class DriveWithController extends CommandBase {

  DriveTrain drivetrain;
  XboxController driver;

  public DriveWithController(DriveTrain drivetrain, XboxController driver) {
    this.drivetrain = drivetrain;
    this.driver = driver;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = 1;
    if(driver.getAButton()){
      speed = 0.8;
    } else if (driver.getBButton())
      speed = 0.25;
      else {
      speed = 0.45;
      }



      drivetrain.arcadeDrive(
        (driver.getTriggerAxis(Hand.kRight) - driver.getTriggerAxis(Hand.kLeft))* speed,
        driver.getX(Hand.kLeft));
    }
    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
