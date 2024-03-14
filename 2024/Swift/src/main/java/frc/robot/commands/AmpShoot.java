// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class AmpShoot extends Command {
  /** Creates a new AmpShoot. */
  PIDController shootPID = new PIDController(.00035, 0.0004, 0.0004);
  Shooter shooter;
  double TargetRPM = 2000;

  public AmpShoot(Shooter shooter) {
    shootPID.setIntegratorRange(-5, 5);
    addRequirements(shooter);
    this.shooter = shooter;
    SmartDashboard.putData("Amp PID", shootPID);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double Output = (shootPID.calculate(shooter.getRPM(), TargetRPM));
    shooter.set(Output);
    SmartDashboard.putNumber("Amp PID Output", Output);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
