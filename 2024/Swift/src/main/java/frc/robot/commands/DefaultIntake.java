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
  private final VibrateController vibrateController;

  /** Creates a new DefaultIntake. */
  public DefaultIntake(CommandXboxController supportController, Intake intake, VibrateController vibrateController) {
    this.intake = intake;
    this.supportController = supportController;
    this.vibrateController = vibrateController;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  private boolean colorSensorOutput;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (supportController.getHID().getLeftBumper()){
      intake.setVelocity(1);
    }
    else if(supportController.getHID().getRightBumper()){
      intake.setVelocity(-1);
    }
    else {
      intake.setVelocity(0);
    }

    if (intake.getColorSensor() &&! colorSensorOutput) {
      //vibrateController.schedule();
    }
    //colorSensorOutput = intake.getColorSensor();
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
