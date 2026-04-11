package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

  SparkFlex intakeMotor = new SparkFlex(26, MotorType.kBrushless);

  // Creates a new Intake.
  public Intake() {
    // TODO: Add voltage compensation like in Shooter subsystem
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command intakeCmd(double speed) {
    return startEnd(() -> intakeMotor.set(speed), () -> intakeMotor.set(0));
  }
}
