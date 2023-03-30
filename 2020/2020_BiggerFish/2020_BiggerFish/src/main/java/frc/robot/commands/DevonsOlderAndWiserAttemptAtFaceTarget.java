/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.DriveTrain;

public class DevonsOlderAndWiserAttemptAtFaceTarget extends CommandBase {
  public final Camera camera;
  public final DriveTrain driveTrain;
  public final Boolean far;

  public DevonsOlderAndWiserAttemptAtFaceTarget(DriveTrain driveTrain, Camera camera, Boolean far) {
    this.driveTrain = driveTrain;
    this.camera = camera;
    this.far = far;
    addRequirements(camera, driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //camera.moveServo(far ? 100 : 140);

    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    System.out.println("Running Devon");
    double speed = 0;
    
    //synchronized (camera.imgLock) 
    {
      if(camera.rect != null) {
         speed = camera.pixelsOff + -0.003221;
         //turns at a fixed speed based on position off
         speed = 0.30 * speed * 1;
        System.out.println("pixels off " + camera.pixelsOff);
      } else {
         speed = 0.3;
      }
      System.out.println("speed " + speed);
      driveTrain.arcadeDrive(0, speed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
   
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println("finished");
    return Math.abs(camera.pixelsOff) <= 1 && camera.rect != null;
  }
}
