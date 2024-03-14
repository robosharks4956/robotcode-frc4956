// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.math.MathUtil;
//import edu.wpi.first.wpilibj.Servo;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import static frc.robot.Constants.NoteMechanismIDs.*;

public class Aimer extends SubsystemBase {
  //private final double minPosition = 0.125;
  private final double maxPosition = 0.400;

  double position = maxPosition;
 // private final Servo actuator = new Servo(AIMER_MOTOR);

  /** Creates a new Aimer. */
  public Aimer() {
   // actuator.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
   // actuator.setPosition(position);
  }

  @Override
  public void periodic() {
    //actuator.setPosition(position);  
  }

  public void move(double movement) {
    //position = MathUtil.clamp(position + movement * 0.01, minPosition, maxPosition);
    //SmartDashboard.putNumber("Aimer Pos.", position);
  }
}
