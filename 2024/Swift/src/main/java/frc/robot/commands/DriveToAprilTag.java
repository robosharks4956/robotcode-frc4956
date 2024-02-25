// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Drivetrain;

public class DriveToAprilTag extends Command {
  public Drivetrain drive;
  AprilTagCamera aprilTagCamera;
  double lastTargetX = 0;
  double lastTargetY = 0;
  PIDController turnPID = new PIDController(5, 0.4, 0.2);
  PIDController speedPID = new PIDController(40, 10, 0.013);
  boolean hasTarget = false;
  double turnOutput = 0;
  double speedOutput = 0;
  Timer timer = new Timer();
  boolean targetInRange = false;


  public DriveToAprilTag(Drivetrain drive, AprilTagCamera aprilTagCamera){
    addRequirements(drive, aprilTagCamera);
    this.drive = drive;
    SmartDashboard.putData("Tag Turn PID", turnPID);
    SmartDashboard.putData("Tag Speed PID", speedPID);
    turnPID.setIntegratorRange(-15, 15);
    speedPID.setIntegratorRange(-100, 100);
    timer.start();
    this.aprilTagCamera = aprilTagCamera;
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (aprilTagCamera.hasTarget(7)){
      if (!hasTarget){
        hasTarget = true;
        turnPID.reset();
        speedPID.reset();
        System.out.println("Tag Seen " + timer.get());
      }
      turnOutput = turnPID.calculate(aprilTagCamera.getYaw(7)*-1, 0);
      speedOutput = speedPID.calculate(aprilTagCamera.getDistance(7), 2);
      if (Math.abs(aprilTagCamera.getDistance(7)-2) < .1){
        targetInRange = true;
      }
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
      if (hasTarget) {
        timer.reset();
        System.out.println("Timer Reset");
      }
      if (timer.hasElapsed(0.1)) {
        drive.stop();

      }
      hasTarget = false;
    }
  }

  public void lookForTarget(double targetID) {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return targetInRange;
  }
}