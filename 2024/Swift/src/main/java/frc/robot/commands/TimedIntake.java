// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class TimedIntake extends Command {
  private final Intake intake;
  private final Timer timer = new Timer();
  double time = .05;
  /** Creates a new TimedIntake. */
  public TimedIntake(Intake intake, double time){
    addRequirements(intake);
    this.intake = intake;
    this.time = time;
  }
    // Use addRequirements() here to declare subsystem dependencies.

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.restart();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
     if (timer.get()<time){
      intake.setVelocity(1);
    }
    else{
      intake.setVelocity(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get()>time;
  }
}
