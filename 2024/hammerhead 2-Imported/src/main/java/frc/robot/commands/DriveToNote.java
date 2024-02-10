// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringSubscriber;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.NoteCamera;

public class DriveToNote extends Command {
  public Drivetrain drive;
  public NoteCamera noteCamera;
  double lastTargetX = 0;
  double lastTargetY = 0;
  PIDController turnPID = new PIDController(0.22, 3, 0.07);
  PIDController speedPID = new PIDController(0.13, 2, 0.013);
  boolean hasTarget = false;
  double turnOutput = 0;
  double speedOutput = 0;


  public DriveToNote(Drivetrain drive, NoteCamera noteCamera){
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive, noteCamera);
    this.drive = drive;
    this.noteCamera = noteCamera;
    SmartDashboard.putData("Turn PID", turnPID);
    SmartDashboard.putData("Speed PID", speedPID);
    turnPID.setIntegratorRange(-15, 15);
    turnPID.setIntegratorRange(-15, 15);

  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    lookForTarget(7);
    if (noteCamera.hasTarget){
      if (!hasTarget){
        hasTarget = true;
        turnPID.reset();
        speedPID.reset();
        System.out.println("Note Seen");
      }
      turnOutput = turnPID.calculate(noteCamera.lastNoteX*-1, 0);
      speedOutput = speedPID.calculate(noteCamera.lastNoteY, 0);
      SmartDashboard.putNumber("Turn Output", turnOutput);
      SmartDashboard.putNumber("Speed Output", speedOutput);
      drive.drive(
          new ChassisSpeeds(
              speedOutput,
              0,
              turnOutput
        ));
    }
    else {
      hasTarget = false;
      drive.drive(
        new ChassisSpeeds(
          0, 0, 0)
        );
    }
    }

  public void lookForTarget(double targetID) {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}