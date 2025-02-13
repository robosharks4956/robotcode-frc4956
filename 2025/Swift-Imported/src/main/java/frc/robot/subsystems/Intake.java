// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.NoteMechanismIDs.*;

public class Intake extends SubsystemBase {
  private final DigitalInput colorSensor = new DigitalInput(COLOR_SENSOR);
  private final SparkMax motor = new SparkMax(INTAKE_MOTOR, MotorType.kBrushless);
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  // private SparkPIDController m_pidController;
   private RelativeEncoder m_encoder;

  /** Creates a new Intake. */
  public Intake() {
    // m_pidController = motor.getPIDController();
     m_encoder = motor.getEncoder();
     motorConfig.idleMode(IdleMode.kCoast);
     motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // kP = 0.0035; 
    // kI = 0;
    // kD = 0.0244; 
    // kIz = 0; 
    // kFF = 0; 
    // kMaxOutput = 1; 
    // kMinOutput = -1;
    // maxRPM = 3500;

    // // set PID coefficients
    // m_pidController.setP(kP);
    // m_pidController.setI(kI);
    // m_pidController.setD(kD);
    // m_pidController.setIZone(kIz);
    // m_pidController.setFF(kFF);
    // m_pidController.setOutputRange(kMinOutput, kMaxOutput);

    //SmartDashboard.putData(colorSensor);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public boolean getColorSensor() {
    return colorSensor.get();
  }

  public void setVelocity(double velocity) {
    motor.set(velocity);
    SmartDashboard.putNumber("Intake Velocity", m_encoder.getVelocity());

    // Set max output to 0 when velocity is 0, to fix oscillation at 0
    /*if (velocity == 0) {
      m_pidController.setOutputRange(0,0);
    } else {
      m_pidController.setOutputRange(-1, 1);
    }*/

    //double setPoint = velocity*maxRPM;
    //m_pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    //SmartDashboard.putNumber("Intake SetPoint", setPoint);
    //SmartDashboard.putNumber("Intake Velocity", m_encoder.getVelocity());
  }
}
