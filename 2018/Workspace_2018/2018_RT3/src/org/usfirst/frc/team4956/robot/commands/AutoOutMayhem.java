package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Used when we have no colors on our side so we gain a better position ready for tele.
 */
public class AutoOutMayhem extends CommandGroup {

    public AutoOutMayhem(double turnDirection) {
    	addSequential(new AutoDriveStraight(218, true)); 
    	addSequential(new AutoDriveTurn(turnDirection < 0 ? RobotMap.right90 : RobotMap.left90));
    	addSequential(new AutoDriveStraight(226, false));
    }
}
