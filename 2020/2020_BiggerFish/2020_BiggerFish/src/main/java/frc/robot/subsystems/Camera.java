/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.GripPipelineYellowBalls;

import java.util.Collections;
import java.util.Comparator;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {

  VisionThread visionThread;
  public Rect rect;
  public double centerX;
  public double pixelsOff;
  public final Object imgLock = new Object();
  UsbCamera camera;
  public void moveServo(double angle) {
//    camServo.setPosition(angle);
  }
 
    public Camera() {
      camera = CameraServer.getInstance().startAutomaticCapture(0);
      camera.setResolution(320, 240);
      moveServo(0);
      startCapture();
    }
  
  public static final Comparator<MatOfPoint> SIZE = (MatOfPoint ol, MatOfPoint o2) -> Double.compare(Imgproc.boundingRect(ol).area(), Imgproc.boundingRect(o2).area());
  public void startCapture() {
    

    visionThread = new VisionThread(camera, new GripPipelineYellowBalls(), pipeline -> {

        if(!pipeline.convexHullsOutput().isEmpty()) {
          Collections.sort(pipeline.convexHullsOutput(),SIZE);
          Collections.reverse(pipeline.convexHullsOutput());

          nContours = pipeline.findContoursOutput().size();

          
            MatOfPoint first = pipeline.convexHullsOutput().get(0);
            rect = Imgproc.boundingRect(first);
            
            synchronized (imgLock) {
            if(rect != null) {
              centerX = (rect.x + (rect.width/2));
              pixelsOff = 320/2 - centerX;
            }
          }
        }else{
          rect = null;
        }
    });
    
    visionThread.start();

  }

  int nContours = 0;
  public int getnContours() {
    return nContours;
  }  
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
