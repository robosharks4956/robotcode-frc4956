package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Crane extends SubsystemBase {
  TalonSRX motor = new TalonSRX(8);
  public static final int kTimeoutMs = 30;
  /** Creates a new Crane. */
  public Crane() {
    motor.setInverted(false);
    motor.configFactoryDefault();
    motor.configNominalOutputForward(0, kTimeoutMs);
    motor.configNominalOutputReverse(0, kTimeoutMs);
    motor.configPeakOutputForward(1, kTimeoutMs);
    motor.configPeakOutputReverse(-1, kTimeoutMs);
    motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    motor.setSensorPhase(false);
    motor.config_kF(0, 0, kTimeoutMs);
    motor.config_kP(0, 5, kTimeoutMs);
    motor.config_kI(0, 0, kTimeoutMs);
    motor.config_kD(0, 5, kTimeoutMs);
  }

  public void set(double power) {
    motor.set(TalonSRXControlMode.PercentOutput, power);
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
