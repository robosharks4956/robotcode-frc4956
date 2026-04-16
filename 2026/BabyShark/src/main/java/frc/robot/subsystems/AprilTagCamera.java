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

public class AprilTagCamera extends SubsystemBase {

  private PhotonCamera camera = new PhotonCamera("SquareCamera");

  // TODO: Update these with the camera's position on the robot
  public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.0254, 0.0381, 0.6985),
      new Rotation3d(0, 0, 0));
  public static final Transform3d kCameraToRobot = new Transform3d(new Translation3d(-0.0254, -0.0381, -0.6985),
      new Rotation3d(0, 0, 0));

  public int cameraWidth = 1280;
  public int cameraHeight = 720;
  public List<PhotonPipelineResult> lastTargets;
  public double currentPitch = 0;
  public double currentYaw = 0;
  public int currentId = 1;

  @Override
  public void periodic() {
    calculateTargetRPM();
  }

  public boolean isHubTag(int id) {
    int[] hubTags = {2,5,8,9,10,11};
    for(byte i = 0; i < 6; i++) {
      if((id - 1)%16 + 1 == hubTags[i]) {
        return true;
      }
    }
    return false;
  }

  public void calculateTargetRPM() {
    var res = camera.getAllUnreadResults();

    // Maybe this will help, version check looks expensive so maybe stop doing it after first iteration
    PhotonCamera.setVersionCheckEnabled(false);

    if (res.size() > 0) {
      lastTargets = res;
    } else {
      lastTargets = null;
      return;
    }

    // TODO: Are we reading these results in descing order by timestamp? If so, just need first good result then break,
    // because otherwise older results take priority, plus no need to calculate targetRPM twice

    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          currentId = bestTarget.getFiducialId();
          if(isHubTag(currentId)) {
            currentPitch = Math.toRadians(bestTarget.getPitch());
            currentYaw = Math.toRadians(bestTarget.getYaw());
          }
        }
      }
    }

    SmartDashboard.putNumber("targetRPM", getRPM_A());
    SmartDashboard.putNumber("TagDistance", 35.875/Math.tan(34*Math.PI/180 + currentPitch));
    SmartDashboard.putNumber("distance", getDistance(currentPitch, currentYaw, currentId));
    SmartDashboard.putNumber("currentId", currentId);
    SmartDashboard.putNumber("Pitch", currentPitch);

    // TODO: Speed the above code up somehow, we're overrunning the loop, this is the longest periodic function

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
  /*
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
  */


  public double getDistance(double pitch, double yaw, int id) {
    final double[] tagOffsets = {200, 23.5, 200, 200, 23.5, 200, 200, 27.418, 27.418, 23.5, 27.418, 200, 200, 200, 200, 200}; // NEEDS FIXING
    final double robotOffset = 3.5;

    final double cameraAngle = Math.toRadians(34);
    final double yCamera = 35.875;
    final double xCamera = yCamera / Math.tan(cameraAngle + pitch);

    return xCamera*Math.cos(yaw) + Math.sqrt(Math.pow(tagOffsets[(id - 1)%16], 2) - Math.pow(xCamera*Math.sin(yaw), 2)) + robotOffset;
  }

  // No Air Resistance
  public double getRPM_NA(double pitch) {
  
    final double distance = getDistance(currentPitch, currentYaw, currentId);
    final double g = 386.089; // in squared
    final double shootingAngle = Math.toRadians(73.5); // deg, likely needs minor adjustments WAS AT 75
    final double y = 62; // in 
    final double velocity = Math.sqrt((g * Math.pow(distance, 2)) / (distance * Math.sin(2 * shootingAngle) - y * (Math.cos(2 * shootingAngle) + 1)));
    
    final double e = 1.25; // error multiplier, accounts for air resistance, forward topspin, etc.
    
    return 2 * velocity * (15 / Math.PI) * e; // rpm
  }

  // With Air Resistance
  public double getRPM_A() {
    
    final double g = 386.089;
    final double mu = 0.1754; // Needs Tuning

    final double y = 58.5; // Needs Measurement
    final double x = getDistance(currentPitch, currentYaw, currentId);
    
    final double shootingAngle = Math.toRadians(73); // needs measurement

    final double C = Math.exp( mu*mu/g * (y - x*Math.tan(shootingAngle)));

    double z = mu*x/Math.cos(shootingAngle)/Math.sqrt(g*x*x/(x*Math.sin(2*shootingAngle) - y*(Math.cos(2*shootingAngle) + 1)));

    for(byte i = 0; i < 2; i++) {
      z = z - (C*Math.exp(-z) - 1)/z - 1;
    }

    final double velocity = mu*x/z/Math.cos(shootingAngle);

    return 30/Math.PI * velocity * 1.19; // final number is the error constant, needs tuning

    /*final double h = 36.75; // in
    final double cameraAngle = 34; // deg
    final double offset = 25; // in - may need further testing
    final double distance = (h / (Math.tan(Math.toRadians(cameraAngle + pitch)))) + offset;
    final double g = 386.089; // in squared
    final double shootingAngle = Math.toRadians(73.5); // deg, likely needs minor adjustments WAS AT 75
    final double y = 62; // in 
    final double mu = 0.1;
    byte step = 0;

    double velocity = Math.sqrt((g * Math.pow(distance, 2)) / (distance * Math.sin(2 * shootingAngle) - y * (Math.cos(2 * shootingAngle) + 1)));

    while(step < 6) {
      velocity = velocity - (Math.pow(velocity, 3)*Math.pow(Math.cos(shootingAngle), 2) - Math.pow(velocity, 2)*mu*distance*Math.cos(shootingAngle))*((distance*Math.tan(shootingAngle) - y)/(g*(distance*distance)) + 1/(mu*distance*velocity*Math.cos(shootingAngle)) + Math.log(1 - mu*distance/(velocity*Math.cos(shootingAngle)))/(mu*mu*distance*distance));
      step += 1;
    }


    return 30*velocity/Math.PI;*/
  }
}
