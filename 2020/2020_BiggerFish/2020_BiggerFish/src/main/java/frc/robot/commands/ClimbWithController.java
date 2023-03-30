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
import frc.robot.subsystems.ClimberSolenoid;
import frc.robot.subsystems.Winch;

public class ClimbWithController extends CommandBase {

  ClimberSolenoid climberSol;
  Winch winch;
  XboxController support;
  enum ClimbState
   {
    noHook, hookUp, hookDown;
   }
   ClimbState state = ClimbState.noHook;
  /**
   * Creates a new ClimbWithController.
   */
  public ClimbWithController(ClimberSolenoid climberSol, Winch winch, XboxController support) {
    this.climberSol = climberSol;
    this.support = support;
    this.winch = winch;
    addRequirements(climberSol, winch);

    // TODO: Add boolean to only allow winch after releasing the hook
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (support.getPOV() == 180) {
      climberSol.set(false);
      if(state == ClimbState.noHook) {
        state = ClimbState.hookUp;
      }
    } else if (support.getPOV() == 0) {
      climberSol.set(true);
      if(state == ClimbState.hookUp) {
        state = ClimbState.hookDown;
      }
    }
    if(state == ClimbState.hookDown)
      winch.set(support.getY(Hand.kLeft));
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
