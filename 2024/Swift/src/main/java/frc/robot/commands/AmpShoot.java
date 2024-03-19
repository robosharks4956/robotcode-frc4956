// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class AmpShoot extends Command {

  PIDController shootPID = new PIDController(.00003, 0.0001, 0.00003);
  double TargetRPM = 1950;
  double kF = 0.320 / TargetRPM;

  Shooter shooter;
  
  public AmpShoot(Shooter shooter) {
    shootPID.setIntegratorRange(-0.1, 0.1);
    shootPID.setTolerance(5);
    this.shooter = shooter;
    SmartDashboard.putData("Amp PID", shootPID);

    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //shooter.setSetpoint(TargetRPM);
    //double output = (shootPID.calculate(shooter.getRPM(), TargetRPM));
    // SmartDashboard.putNumber("Amp PID Output", output);
    // output = output + (kF * TargetRPM);
    // shooter.set(output);
    // SmartDashboard.putNumber("Amp PIDF Output", output);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
