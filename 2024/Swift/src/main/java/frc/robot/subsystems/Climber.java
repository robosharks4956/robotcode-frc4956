package frc.robot.subsystems;

import static frc.robot.Constants.ClimberIDsAndPorts.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  private final VictorSPX leftMotor = new VictorSPX(CLIMBER_LEFT_MOTOR);
  private final VictorSPX rightMotor = new VictorSPX(CLIMBER_RIGHT_MOTOR);

  public void set(double power){
    leftMotor.set(ControlMode.PercentOutput, -power * .7);
    rightMotor.set(ControlMode.PercentOutput, power * .7);
  }

  public Climber() {
    leftMotor.setNeutralMode(NeutralMode.Brake);
    rightMotor.setNeutralMode(NeutralMode.Brake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
