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
import frc.robot.subsystems.Kobe;

public class ShootWithController extends CommandBase {
  
  Kobe kobe;
  XboxController support; 

  public ShootWithController(Kobe kobe, XboxController support) {
    this.kobe = kobe;
    this.support = support;
    addRequirements(kobe);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Shoot when A button is pressed
    if(support.getAButton())
      kobe.shootDemBalls(1);
    else
      kobe.shootDemBalls(0);

    if(support.getPOV(0) == 0)
      kobe.moveServo(kobe.servohigh);
    else if (support.getPOV(0) == 180)
      kobe.moveServo(kobe.servolow);
    else if (support.getPOV(0) == 270)
      kobe.moveServo(kobe.servomid);
    else if (support.getPOV(0) == 90)
      kobe.moveServo(kobe.servohome);
    else {
      if (support.getY(Hand.kLeft) > 0.5)
      kobe.adjustServo(0.005);
      else if (support.getY(Hand.kLeft) < -0.5)
      kobe.adjustServo(-0.005);
    }
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
