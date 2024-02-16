// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.ArrayList;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

public class AprilTagCamera extends SubsystemBase {
  /** Creates a new Vision. */
private PhotonCamera cam = new PhotonCamera("Main_Camera");
 public static final Transform3d kRobotToCam =
     new Transform3d(new Translation3d(0.0254, 0.0381, 0.6985), new Rotation3d(0, 0, 0));
     public static final Transform3d kCameraToRobot =
     new Transform3d(new Translation3d(-0.0254, -0.0381, -0.6985), new Rotation3d(0, 0, 0));


  public double lastNoteX = 0;
  public double lastNoteY = 0;
  public int cameraWidth = 1280;
  public int cameraHeight = 720;
  public List <PhotonTrackedTarget> lastTargets;

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var res = cam.getLatestResult();
    if (res.hasTargets()) {
        //var imageCaptureTime = res.getTimestampSeconds();
        var bestTarget = res.getBestTarget();
        var id = bestTarget.getFiducialId();
        var bestCameraToTarget = bestTarget.getBestCameraToTarget();
        var yaw = bestTarget.getYaw();
        lastTargets = res.targets;
        var Corners = bestTarget.getMinAreaRectCorners();
        //lastNoteX = (Corners.get(0).x+Corners.get(1).x)/2;
        //lastNoteX = lastNoteX - (cameraWidth/2);
        //lastNoteY = (Corners.get(0).y);
        //lastNoteY = ((Corners.get(0).y+Corners.get(3).y)/2);
        //lastNoteY = cameraHeight - lastNoteY;
        //bestTarget.getDetectedCorners().get(0);
       // var camPose = kFarTargetPose.transformBy(camToTargetTrans.inverse());
       // drivetrain.odometry.addVisionMeasurement(
        //        camPose.transformBy(kCameraToRobot).toPose2d(), imageCaptureTime);
    }
    else{
      lastTargets = null;
    }
  }
  public boolean hasTarget(int id){
    if (lastTargets != null){
      for (PhotonTrackedTarget target : lastTargets) {
         if(id == target.getFiducialId()){
          return true;
         }
      }
    }
    return false;
  }
   public double getYaw(int id){
    if (lastTargets != null){
      for (PhotonTrackedTarget target : lastTargets) {
         if(id == target.getFiducialId()){
          return target.getYaw();
         }
      }
    }
    return 0;
  }
  public double getDistance(int id){
    if (lastTargets != null){
      for (PhotonTrackedTarget target : lastTargets){
        if(id == target.getFiducialId()){
          return target.getBestCameraToTarget().getX();
        }
      }
    }
    return 0;
  }
}
