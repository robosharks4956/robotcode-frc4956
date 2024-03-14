// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.Drivetrain;
import static frc.robot.Constants.AprilTagIDs.*;

public class DriveToAprilTag extends Command {
  private final Drivetrain drive;
  private final AprilTagCamera aprilTagCamera;
  private final boolean toSpeaker;
  private final double distanceBuffer;

  private final Timer timer = new Timer();

  private PIDController turnPID = new PIDController(5, 0.4, 0.2);
  private PIDController speedPID = new PIDController(40, 10, 0.013);



  private int id;

  public DriveToAprilTag(Drivetrain drive, AprilTagCamera aprilTagCamera, boolean toSpeaker, double distanceBuffer) {
    this.drive = drive;
    this.aprilTagCamera = aprilTagCamera;
    this.toSpeaker = toSpeaker;
    this.distanceBuffer = distanceBuffer;

    SmartDashboard.putData("Tag Turn PID", turnPID);
    SmartDashboard.putData("Tag Speed PID", speedPID);
    turnPID.setIntegratorRange(-15, 15);
    speedPID.setIntegratorRange(-100, 100);

    addRequirements(drive, aprilTagCamera);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.restart();

    switch (DriverStation.getAlliance().get()) {
      case Blue:
        this.id = toSpeaker ? BLUE_SPEAKER : BLUE_AMP;
        break;
      
      case Red:
        this.id = toSpeaker ? RED_SPEAKER : RED_AMP;
        break;
    
      default:
        break;
    }
  }

  private double turnOutput = 0;
  private double speedOutput = 0;
  private boolean hasTarget = false;
  private boolean targetInRange = false;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (aprilTagCamera.hasTarget(id)){
      if (!hasTarget){
        hasTarget = true;
        turnPID.reset();
        speedPID.reset();
        System.out.println("Tag Seen " + timer.get());
      }
      turnOutput = turnPID.calculate(aprilTagCamera.getYaw(id) * -1, 0);
      speedOutput = speedPID.calculate(aprilTagCamera.getDistance(id), .25);
      if (Math.abs(aprilTagCamera.getDistance(id) - 2) < distanceBuffer){
        targetInRange = true;
      }
      SmartDashboard.putNumber("Turn Output", turnOutput);
      SmartDashboard.putNumber("Speed Output", speedOutput);
      drive.drive(
          new ChassisSpeeds(
              -speedOutput,
              0,
              turnOutput
        ));
    }
    else {
      if (hasTarget) {
        timer.reset();
      }
      if (timer.hasElapsed(0.1)) {
        drive.stop();
      }
      hasTarget = false;
    }
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