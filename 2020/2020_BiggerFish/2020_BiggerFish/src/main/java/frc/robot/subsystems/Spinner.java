/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Spinner extends SubsystemBase {

  DoubleSolenoid solenoid = new DoubleSolenoid(2, 3);
  Spark spinnerMotor = new Spark(1);

  // Constructor
  public Spinner() {
    solenoid.set(Value.kReverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  

  public void set(boolean on) {
    if (on) {
      solenoid.set(Value.kForward);
      spinnerMotor.setSpeed(0.37);
    }

    else {
      solenoid.set(Value.kReverse);
      spinnerMotor.setSpeed(0.0);
    }
  }
}
