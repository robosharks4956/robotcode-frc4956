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

  @Override
  public void periodic() {
    var res = camera.getAllUnreadResults();
    if (res.size() > 0) {
      lastTargets = res;
    } else {
      lastTargets = null;
      return;
    }

    if (lastTargets != null) {
      for (PhotonPipelineResult target : lastTargets) {
        if (target.hasTargets()) {
          var bestTarget = target.getBestTarget();
          currentPitch = bestTarget.getPitch();
        }
      }
    }

    SmartDashboard.putNumber("targetRPM", getRPMWithAirResistance(currentPitch));

    // TODO: Speed this up somehow, we're overrunning the loop, this is the longest periodic function
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
    final double offset = 25; // in - may need further testing
    final double distance = (h / (Math.tan(Math.toRadians(cameraAngle + pitch)))) + offset;
    // The Distance Above is Correct


    final double g = 386.089; // in squared
    final double shootingAngle = Math.toRadians(73.5); // deg, likely needs minor adjustments WAS AT 75
    final double y = 62; // in 
    final double velocity = Math.sqrt((g * Math.pow(distance, 2)) / (distance * Math.sin(2 * shootingAngle) - y * (Math.cos(2 * shootingAngle) + 1)));
    
    final double e = 1.25; // error multiplier, accounts for air resistance, forward topspin, etc.
    
    return 2 * velocity * (15 / Math.PI) * e; // rpm
  }

  public double getRPMWithAirResistance(double pitch) {
    final double h = 36.75; // in
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


    return 30*velocity/Math.PI;
  }
}
