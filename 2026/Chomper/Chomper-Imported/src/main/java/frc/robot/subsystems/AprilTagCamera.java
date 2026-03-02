// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class AprilTagCamera extends SubsystemBase {
  /** Creates a new Vision. */
  private PhotonCamera camera = new PhotonCamera("SquareCamera");
  public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.0254, 0.0381, 0.6985),
      new Rotation3d(0, 0, 0));
  public static final Transform3d kCameraToRobot = new Transform3d(new Translation3d(-0.0254, -0.0381, -0.6985),
      new Rotation3d(0, 0, 0));

  public int cameraWidth = 1280;
  public int cameraHeight = 720;
  public List<PhotonPipelineResult> lastTargets;

  @Override
  public void periodic() {
    var res = camera.getAllUnreadResults();
    if (res.size() > 0) {
      lastTargets = res;
    } else {
      lastTargets = null;
    }
  }

  public boolean hasTarget(int id) {
    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          if (bestTarget != null && id == bestTarget.getFiducialId()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public double getYaw(int id) {
    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          if (bestTarget != null && id == bestTarget.getFiducialId()) {
            return bestTarget.getYaw();
          }
        }
      }
    }
    return 0;
  }

  public double getDistance(int id) {
    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          if (bestTarget != null && id == bestTarget.getFiducialId()) {
            return bestTarget.getBestCameraToTarget().getX();
          }
        }

      }
    }
    return 0;
  }
}
