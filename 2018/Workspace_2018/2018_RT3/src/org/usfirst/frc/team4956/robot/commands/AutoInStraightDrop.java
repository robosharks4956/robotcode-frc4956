package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoInStraightDrop extends CommandGroup {

    public AutoInStraightDrop() {
    	addSequential(new AutoDriveStraight(90, true)); // Want it to  go 110in 
    	addSequential(new AutoLiftPunch(1, 10));
    }
}
