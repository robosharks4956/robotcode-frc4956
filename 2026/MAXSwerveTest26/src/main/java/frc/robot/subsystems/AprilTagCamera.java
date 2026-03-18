// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  public double currentPitch = 0;

  @Override
  public void periodic() {
    var res = camera.getAllUnreadResults();
    if (res.size() > 0) {
      lastTargets = res;
    } else {
      lastTargets = null;
    }

    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          currentPitch = bestTarget.getPitch();
        }
      }
    }

    SmartDashboard.putNumber("targetRPM", getRPM(currentPitch));

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

  public double getPitch(int id) {
    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          if (bestTarget != null && id == bestTarget.getFiducialId()) {
            return bestTarget.getPitch();
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

  public double getRPM(double pitch) {
    // private double pitch = getPitch();
    final double h = 36.75; // in
    final double cameraAngle = 34; // deg
    final double offset = 24; // in - may need further testing
    final double distance = (h / (Math.tan(Math.toRadians(cameraAngle + pitch)))) + offset;
    // The Distance Above is Correct


    final double g = 386.089; // in squared
    final double shootingAngle = Math.toRadians(75); // deg, likely needs minor adjustments
    final double y = 62; // in 
    final double velocity = Math.sqrt((g * Math.pow(distance, 2)) / (distance * Math.sin(2 * shootingAngle) - y * (Math.cos(2 * shootingAngle) + 1)));
    
    final double e = 2.5; // error multiplier, compensates if rpm is too low ... was at 2.491
    
    return velocity * (15 / Math.PI) * e; // rpm
  }
}
