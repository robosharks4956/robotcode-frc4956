// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

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

public class AprilTag extends Command {
  public Drivetrain drive;
  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  NetworkTable table;
  DoubleArraySubscriber xSub;
  DoubleArraySubscriber ySub;
  DoubleArraySubscriber idSub;
  StringPublisher sSub;
  double lastTargetX = 0;
  double lastTargetY = 0;


  public AprilTag(Drivetrain drive) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
    this.drive = drive;
    table = inst.getTable("Vision");
     xSub = table.getDoubleArrayTopic("target_x").subscribe(new double[0]);
     ySub = table.getDoubleArrayTopic("target_y").subscribe(new double[0]);
     idSub = table.getDoubleArrayTopic("target_id").subscribe(new double[1]);
     sSub = table.getStringTopic("string").publish();
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    lookForTarget(7);
    if (Math.abs(lastTargetX)>.02){
      double rotationRate = 25;
      if (lastTargetX < 0){
        rotationRate *= -1;
      }
       drive.drive(
          new ChassisSpeeds(
              0,
              0,
              rotationRate));
    }
    else {
      drive.drive(
        new ChassisSpeeds(
          0, 0, 0)
        );
    }
    }

  public void lookForTarget(double targetID) {
    double[] array = idSub.get();
    if (array.length >0){
      sSub.set(""+idSub.get());
       if (array[0] == targetID){
        System.out.println("Found Target 9");
       sSub.set("Target 7");
       double target_x = xSub.get()[0];
       double target_y = ySub.get()[0];
       double distance = (Math.sqrt(((target_x)*(target_x))+((target_y)*(target_y))));
       System.out.println("Distance to target:" + distance);
      lastTargetX = target_x + 0.75;
      lastTargetY = -1 * (target_y + 0.5);
      System.out.println("Last Target X = " + lastTargetX);
      System.out.println("Last Target Y = " + lastTargetY);
      SmartDashboard.putNumber("Last Target X = ", lastTargetX);
      }
    }
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
