/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class MecanumDriveWithController extends Command {
  double sensitivity = RobotMap.normalSpeedPercent;
  double reverseMultiplier = -1;
  boolean speedToggled = false;
  boolean speedToggling = false;
  boolean reverseToggled = true;
  boolean reverseToggling = false;

  public MecanumDriveWithController() {
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    // Set robot to slow speed or fast speed. It toggles on and off (on is slow).
    if (!speedToggled && Robot.oi.driverStick.getRawButton(RobotMap.minSpeedBtn) && !speedToggling) {
      speedToggled = true;
      sensitivity = RobotMap.minSpeedPercent;
      speedToggling = true;
    } else if (speedToggled && Robot.oi.driverStick.getRawButton(RobotMap.minSpeedBtn) && !speedToggling) {
      speedToggled = false;
      sensitivity = RobotMap.normalSpeedPercent;
      speedToggling = true;
    } else if (!Robot.oi.driverStick.getRawButton(RobotMap.minSpeedBtn) && speedToggling) {
      speedToggling = false;
    }

    // Set robot to forward or reverse mode. It toggles on and off (on is reverse).
    SmartDashboard.putBoolean("Reverse", reverseToggled);
    if (!reverseToggled && Robot.oi.driverStick.getRawButton(RobotMap.invertDirection) && !reverseToggling) {
      reverseToggled = true;
      reverseMultiplier = -1;
      reverseToggling = true;
    } else if (reverseToggled && Robot.oi.driverStick.getRawButton(RobotMap.invertDirection) && !reverseToggling) {
      reverseToggled = false;
      reverseMultiplier = 1;
      reverseToggling = true;
    } else if (!Robot.oi.driverStick.getRawButton(RobotMap.invertDirection) && reverseToggling) {
      reverseToggling = false;
    }
 
    double x = Robot.oi.driverStick.getRawAxis(RobotMap.leftStickX) * sensitivity * reverseMultiplier;
    double y = (Robot.oi.driverStick.getRawAxis(RobotMap.rightTrigger) - Robot.oi.driverStick.getRawAxis(RobotMap.leftTrigger)) * sensitivity * reverseMultiplier;
    double z = Robot.oi.driverStick.getRawAxis(RobotMap.rightStickX) * sensitivity;

    Robot.drivetrain.MecanumDrive(y, x, z);
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
