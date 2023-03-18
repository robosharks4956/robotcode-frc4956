// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Crane;
import frc.robot.subsystems.Latch;

public class CranePositionControl extends CommandBase {

  Crane crane;
  PIDController pid = new PIDController(0.001, 0, 0.000);
  CommandXboxController controller; 
  double maxsetpoint=3350;
  double minsetpoint=2550;
  double setpoint=maxsetpoint; 
  double setpointincrement=5;
  Latch latch;

  /** Creates a new CranePositionControl. */
  public CranePositionControl(Crane crane, Latch latch, CommandXboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(crane, latch);
    this.crane = crane;
    this.controller = controller;
    this.latch = latch;
    SmartDashboard.putData("Crane PID", pid);
    pid.setTolerance(100);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pid.reset();
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    setpoint = setpoint + controller.getLeftY();
    setpoint = Math.max(Math.min(setpoint, maxsetpoint), minsetpoint);

    // Open latch if we aren't considered as being atSetpoint
    if (pid.atSetpoint()) {
      latch.set(0);
    } else {
      latch.set(0.4);
    }

    double pidOutput = pid.calculate(crane.getPosition(), setpoint);
    SmartDashboard.putNumber("pidoutput", pidOutput);
    if (latch.encoder.getDistance() >= 5) {
      //crane.set(pidOutput);
    } else {
      crane.set(0);
    }
    
    SmartDashboard.putBoolean("At Setpoint", pid.atSetpoint());
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
