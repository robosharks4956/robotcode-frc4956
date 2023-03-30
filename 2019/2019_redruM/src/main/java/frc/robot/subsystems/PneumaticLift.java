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
import frc.robot.commands.PnuematicLiftWithController;

/**
 * Subsystem for the pneumatics responsible for raising the robot up
 * off the floor.
 */
public class PneumaticLift extends Subsystem {
  Compressor c = new Compressor(RobotMap.compressor);
  public DoubleSolenoid front, back;
  
  public PneumaticLift() {
    super();
    c.setClosedLoopControl(true);

    front = new DoubleSolenoid(RobotMap.front_up, RobotMap.front_down);
    back = new DoubleSolenoid(RobotMap.back_up, RobotMap.back_down);
    front.set(Value.kReverse);
    back.set(Value.kReverse);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new PnuematicLiftWithController());
  }
}
