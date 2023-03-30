/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class HatchPanelLiftWithController extends Command {
  public HatchPanelLiftWithController() {
    requires(Robot.hatchPanelLift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // D-pad up to raise hatch panel, D-pad down to lower it
    if (Robot.oi.supportStick.getPOV() == 0) {
      Robot.hatchPanelLift.raise();
    } else if (Robot.oi.supportStick.getPOV() == 180) {
      Robot.hatchPanelLift.lower();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
}
