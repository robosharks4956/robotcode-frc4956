// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Latch;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LatchPID extends PIDCommand {
  /** Creates a new SliderHoldPosition. */
  public LatchPID(Latch latch, double setpoint) {
    super(
        // The controller that the command will use
        new PIDController(.2, 0, 0),
        // This should return the measurement
        () -> latch.encoder.getDistance(),
        // This should return the setpoint (can also be a constant)
        () -> setpoint,
        // This uses the output
        output -> { 
          latch.set(output);
        // SmartDashboard.putNumber("Latch Output", output);
        // SmartDashboard.putNumber("Latch Setpoint", setpoint);
        });

      addRequirements(latch);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}