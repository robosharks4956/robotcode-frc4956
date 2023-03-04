package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Crane extends SubsystemBase {
  VictorSPX motor=new VictorSPX(0);
  public void set(double Power){
    motor.set(VictorSPXControlMode.PercentOutput, Power);

  }
  /** Creates a new Crane. */
  public Crane() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
