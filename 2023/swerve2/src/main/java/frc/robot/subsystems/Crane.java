package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Crane extends SubsystemBase {
  TalonSRX motor=new TalonSRX(0);
  public void set(double Power){
    motor.set(TalonSRXControlMode.PercentOutput, Power);

  motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    motor.setSensorPhase(false);

  }
  /** Creates a new Crane. */
  public Crane() {}

  @Override
  public void periodic() {
    SmartDashboard.putNumber ("craneEncoder", motor.getSelectedSensorPosition(0));
    // This method will be called once per scheduler run
  }
}
