package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Slider extends SubsystemBase {
  VictorSPX motor=new VictorSPX(55);
  public void set(double Power){
    motor.set(VictorSPXControlMode.PercentOutput, Power);
  }
  public Encoder encoder = new Encoder(0,1);
  /** Creates a new Slider. */
  public Slider() {
    encoder.setDistancePerPulse(360.0/44.4);
    SmartDashboard.putData("Slide Encoder", encoder);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
