package frc.robot.subsystems;

import static frc.robot.Constants.ClimberIDsAndPorts.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  private final VictorSPX leftMotor = new VictorSPX(CLIMBER_LEFT_MOTOR);
  private final VictorSPX rightMotor = new VictorSPX(CLIMBER_RIGHT_MOTOR);
  private final Servo leftLatch = new Servo(LATCH_LEFT_SERVO);
  private final Servo rightLatch = new Servo(LATCH_RIGHT_SERVO);
  double leftOpen = 0;
  double leftClosed = 0.5;
  double rightOpen = 0.5;
  double rightClosed = 0;
  boolean latchOpen = false;

  public void set(double power){
    leftMotor.set(ControlMode.PercentOutput, -power * 0.55);
    rightMotor.set(ControlMode.PercentOutput, power * 0.55);
  }

  public Climber() {
    leftLatch.set(leftClosed);
    rightLatch.set(rightClosed);
  }

  public void setLatch(boolean latchOpen) {
    if (latchOpen) {
      leftLatch.set(leftOpen);
      rightLatch.set(rightOpen);
    }
    else {
      leftLatch.set(leftClosed);
      rightLatch.set(rightClosed);
    }
    this.latchOpen = latchOpen;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
