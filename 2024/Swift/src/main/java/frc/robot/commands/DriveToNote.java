// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.NoteCamera;

public class DriveToNote extends Command {
  private final Drivetrain drive;
  private final NoteCamera noteCamera;
  private final Intake intake;

  private final Timer timer = new Timer();

  private PIDController turnPID = new PIDController(0.22, 0.1, 0.011);
  private PIDController speedPID = new PIDController(0.13, 0.5, 0.013);

  public DriveToNote(Drivetrain drive, NoteCamera noteCamera, Intake intake){
    this.drive = drive;
    this.noteCamera = noteCamera;
    this.intake = intake;
    SmartDashboard.putData("Turn PID", turnPID);
    SmartDashboard.putData("Speed PID", speedPID);
    turnPID.setIntegratorRange(-15, 15);
    turnPID.setIntegratorRange(-15, 15);
    timer.start();

    addRequirements(drive, noteCamera, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intake.setVelocity(-1);
  }

  private double turnOutput = 0;
  private double speedOutput = 0;
  private boolean hasTarget = false;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (noteCamera.hasTarget){
      if (!hasTarget){
        hasTarget = true;
        turnPID.reset();
        speedPID.reset();
        System.out.println("Note Seen " + timer.get());
      }
      turnOutput = turnPID.calculate(noteCamera.lastNoteX*-1, 0);
      speedOutput = speedPID.calculate(noteCamera.lastNoteY, 0);
      SmartDashboard.putNumber("Turn Output", turnOutput);
      SmartDashboard.putNumber("Speed Output", speedOutput);
      drive.drive(new ChassisSpeeds(speedOutput, 0, turnOutput));
    }
    else {
      if (hasTarget) {
        timer.reset();
      }
      if (timer.hasElapsed(0.5) || noteCamera.lastNoteY < 30) {
        drive.stop();

      }
      hasTarget = false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.stop();
    intake.setVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.getColorSensor();
  }
}