package frc.robot.subsystems;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AprilTagCamera;
import frc.robot.subsystems.DriveSubsystem;

public class LockOn extends Command {

  private AprilTagCamera camera;
  private DriveSubsystem drivetrain;
  private PIDController turnPID = new PIDController(0.0135, 0.0, 0.0011);

  public LockOn(AprilTagCamera camera, DriveSubsystem drivetrain) {
    addRequirements(drivetrain);
    this.camera = camera;
    this.drivetrain = drivetrain;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.restart();
    hasTarget = false;
    turnOutput = 0;
    speedOutput = 0;
    SmartDashboard.putData("Turn PID", turnPID);
    //SmartDashboard.putData("Speed PID", speedPID);
    System.out.println("Initialization.");
  }

  private double turnOutput = 0;
  private double speedOutput = 0;
  private boolean hasTarget = false;
  private boolean targetInRange = false;
  private final Timer timer = new Timer();
  int id = 16;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (camera.hasTarget(id)) {

      // If we didn't have target on last loop, this is new target aquisition, reset PID and timer
      if (!hasTarget) {
        hasTarget = true;
        //turnPID.reset();
        System.out.println("Tag Seen " + timer.get());
      }

      // Note: right turn negative, left turn positive
      var targetAngle = camera.getYaw(id);
     // System.out.println("targetAngle: " + targetAngle);
      turnOutput = turnPID.calculate(targetAngle * 1, 0);
      turnOutput = Math.min(Math.max(turnOutput, -0.3),0.3);
      //System.out.printf("Turn oputput: %.2f\n",turnOutput);
      SmartDashboard.putNumber("Turn Output", turnOutput);
      SmartDashboard.putNumber("Speed Output", speedOutput);
      drivetrain.drive(
          -speedOutput,
          0.0,
          turnOutput,
          false);
    } else {
      if (hasTarget) {
        timer.reset();
      }
      if (timer.hasElapsed(0.05)) {
        turnPID.reset();
        drivetrain.stop();
        System.out.println("Time stop");
      }
      hasTarget = false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
    System.out.println("Stop");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return targetInRange;
  }
}
