/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ConveyBalls;
import frc.robot.subsystems.Kobe;

public class AutoShoot extends CommandBase {
  Kobe kobe;
  ConveyBalls conveyballs;

  public AutoShoot(Kobe kobe, ConveyBalls conveyballs) {
    this.kobe = kobe;
    this.conveyballs = conveyballs;
    addRequirements(kobe,conveyballs);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    kobe.shootDemBalls(1);
    conveyballs.setConveyTop(1);
    conveyballs.setConveyBottom(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    kobe.shootDemBalls(0);
    conveyballs.setConveyTop(0);
    conveyballs.setConveyBottom(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
