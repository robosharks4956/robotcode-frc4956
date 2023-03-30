/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class PnuematicLiftWithController extends Command {

  boolean toggleOn = false;

  public PnuematicLiftWithController() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.pneumaticlift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    if (toggleOn && !(Robot.oi.supportStick.getRawButton(RobotMap.rightBumper) || Robot.oi.supportStick.getRawButton(RobotMap.leftBumper))) {
        toggleOn = false;
    }
    else if (toggleOn && (Robot.oi.supportStick.getRawButton(RobotMap.rightBumper) || Robot.oi.supportStick.getRawButton(RobotMap.leftBumper))) {
      return;
    }

    if (Robot.pneumaticlift.front.get() == Value.kReverse && Robot.oi.supportStick.getRawButton(RobotMap.rightBumper)) {
        Robot.pneumaticlift.front.set(Value.kForward);
        toggleOn = true;
    } else if (Robot.pneumaticlift.front.get()  == Value.kForward && Robot.oi.supportStick.getRawButton(RobotMap.rightBumper)) {
        Robot.pneumaticlift.front.set(Value.kReverse);
        toggleOn = true;
    }

    if (Robot.pneumaticlift.back.get() == Value.kReverse && Robot.oi.supportStick.getRawButton(RobotMap.leftBumper)) {
        Robot.pneumaticlift.back.set(Value.kForward);
        toggleOn = true;
    } else if (Robot.pneumaticlift.back.get() == Value.kForward && Robot.oi.supportStick.getRawButton(RobotMap.leftBumper)) {
        Robot.pneumaticlift.back.set(Value.kReverse);
        toggleOn = true;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
