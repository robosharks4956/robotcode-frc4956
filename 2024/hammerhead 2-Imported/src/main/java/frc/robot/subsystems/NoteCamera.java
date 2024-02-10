// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import org.photonvision.PhotonCamera;

public class NoteCamera extends SubsystemBase {
  /** Creates a new Vision. */
private PhotonCamera cam = new PhotonCamera("Ring_Camera");
private Drivetrain drivetrain;
 public static final Transform3d kRobotToCam =
     new Transform3d(new Translation3d(0.0254, 0.0381, 0.6985), new Rotation3d(0, 0, 0));
     public static final Transform3d kCameraToRobot =
     new Transform3d(new Translation3d(-0.0254, -0.0381, -0.6985), new Rotation3d(0, 0, 0));


  public double lastNoteX = 0;
  public double lastNoteY = 0;
  public int cameraWidth = 1280;
  public int cameraHeight = 720;
  public boolean hasTarget = false;
     public NoteCamera(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var res = cam.getLatestResult();
    if (res.hasTargets()) {
      hasTarget = true;
        var imageCaptureTime = res.getTimestampSeconds();
        var bestTarget = res.getBestTarget();
        var Corners = bestTarget.getMinAreaRectCorners();
        lastNoteX = (Corners.get(0).x+Corners.get(1).x)/2;
        lastNoteX = lastNoteX - (cameraWidth/2);
        lastNoteY = (Corners.get(0).y);
        //lastNoteY = ((Corners.get(0).y+Corners.get(3).y)/2);
        lastNoteY = cameraHeight - lastNoteY;
        //bestTarget.getDetectedCorners().get(0);
       // var camPose = kFarTargetPose.transformBy(camToTargetTrans.inverse());
       // drivetrain.odometry.addVisionMeasurement(
        //        camPose.transformBy(kCameraToRobot).toPose2d(), imageCaptureTime);
    }
    else{
      hasTarget = false;
    }
  }
}
