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
import frc.robot.subsystems.ConveyBalls;

public class ConveyWithController extends CommandBase {
  ConveyBalls conveyballs;
  XboxController support; 

  public ConveyWithController(ConveyBalls conveyballs, XboxController support) {
    this.conveyballs = conveyballs;
    this.support = support;
    addRequirements(conveyballs);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    if(support.getBumper(Hand.kRight))
      conveyballs.setConveyTop(1);
    else if(support.getBumper(Hand.kLeft))
      conveyballs.setConveyTop(-1);
    else
      conveyballs.setConveyTop(0);

    // set bottom conveyer
    conveyballs.setConveyBottom(support.getTriggerAxis(Hand.kRight) - support.getTriggerAxis(Hand.kLeft));


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
