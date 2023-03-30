/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.HatchPanelLiftWithController;

/**
 * Small piece of metal that goes up and down for picking up and releasing
 * hatch panels.
 */
public class HatchPanelLift extends Subsystem {
  Compressor c = new Compressor(RobotMap.compressor);
  DoubleSolenoid solenoid;

  public HatchPanelLift() {
    super();
    c.setClosedLoopControl(true);
    solenoid = new DoubleSolenoid(RobotMap.panel_up, RobotMap.pane_down);
    raise();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new HatchPanelLiftWithController());
  }

  public void raise() {
    solenoid.set(Value.kForward);
  }

  public void lower() {
    solenoid.set(Value.kReverse);
  }
}
