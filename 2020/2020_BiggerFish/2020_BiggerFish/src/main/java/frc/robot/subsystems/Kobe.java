/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Kobe extends SubsystemBase {
  TalonSRX larry_bird = new TalonSRX(2);
  TalonSRX lebron_james = new TalonSRX(8);
  Servo servo = new Servo(0);
  public double servohome = 1;
  public double servolow = .334;
  public double servomid = 0.2;
  public double servohigh = 0.01;
  /**
   * Creates a new Kobe.
   */
  public Kobe() {
      moveServo(servohome);
      
  }

  public void shootDemBalls(double power) {
    larry_bird.set(ControlMode.PercentOutput, power);
    lebron_james.set(ControlMode.PercentOutput, power);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("unicorn", servo.getPosition());
    // This method will be called once per scheduler run
  }
  public void moveServo(double angle) {
        servo.setPosition(angle);
        
      }

  public void adjustServo(double amount) {
    double angle = servo.getPosition()+ amount;
    servo.setPosition(angle);
    //SmartDashboard.putNumber("unicorn", angle);
  }
}
