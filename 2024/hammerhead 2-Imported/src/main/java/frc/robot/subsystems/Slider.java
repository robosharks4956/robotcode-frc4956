package frc.robot.subsystems;

//import com.ctre.phoenix6.motorcontrol.VictorSPXControlMode;
//import com.ctre.phoenix6.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Slider extends SubsystemBase {
  //VictorSPX motor=new VictorSPX(55);
  public void set(double Power){
   // motor.set(VictorSPXControlMode.PercentOutput, Power);
  }
  /** Creates a new Slider. */
  public Slider() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
