package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Crane extends SubsystemBase {
  TalonSRX motor = new TalonSRX(8);

  /** Creates a new Crane. */
  public Crane() {
    motor.setInverted(true);
  }

  public void set(double power) {
    motor.set(TalonSRXControlMode.PercentOutput, power);

    motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    motor.setSensorPhase(false);
  }

  public void setVelocity(double velocity) {
    motor.set(TalonSRXControlMode.Velocity, velocity);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Crane Encoder", motor.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Crane Sensor Velocity", motor.getSelectedSensorVelocity());
  }
}
