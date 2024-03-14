// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Intake;

public class DefaultIntake extends Command {
  private final Intake intake;
  private final CommandXboxController supportController;

  /** Creates a new DefaultIntake. */
  public DefaultIntake(CommandXboxController supportController, Intake intake) {
    this.intake = intake;
    this.supportController = supportController;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (supportController.getHID().getLeftBumper()){
      intake.setVelocity(1);
    }
    else if(supportController.getHID().getRightBumper()){
      intake.setVelocity(-1);
      //if (intake.getColorSensor() == true && !(supportController.getRightTriggerAxis() > 0.05)) {
    //    intake.setVelocity(0);
    //  }
    }
    else {
      intake.setVelocity(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.setVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
