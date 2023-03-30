package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoOutSwitchDrop extends CommandGroup {

    public AutoOutSwitchDrop(double turnDirection) {
    	addSequential(new AutoDriveStraight(153, true)); // Straight Long Enough To Turn At 153in
        addSequential(new AutoDriveTurn(turnDirection < 0 ? RobotMap.right90 : RobotMap.left90)); //left turn is weaker, right turn has to be a higher angle to get 90
        addSequential(new AutoDriveStraight(17, false)); // Drive To Switch 
        addSequential(new AutoLiftPunch(6, 3));
    }
}
