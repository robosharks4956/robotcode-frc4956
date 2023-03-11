// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Slider;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SliderHoldPosition extends PIDCommand {
  /** Creates a new SliderHoldPosition. */
  public SliderHoldPosition(Slider slider) {
    super(
        // The controller that the command will use
        new PIDController(1, 0, 0),
        // This should return the measurement
        () -> slider.encoder.getDistance(),
        // This should return the setpoint (can also be a constant)
        () -> slider.encoder.getDistance(),
        // This uses the output
        output -> { 
          slider.set(output);
        });
      addRequirements(slider);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
