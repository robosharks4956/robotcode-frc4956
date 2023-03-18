package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveStraight extends CommandGroup {

    public DriveStraight() {
    	//Goes to target,  drops gear,  drives back
    	addSequential(new AutoDriveStraight(0.7, 1.9));
    	//addSequential(new AutoTurn(.4, 0.2));
    	addSequential(new AutoDriveStraight(0.5, 0.9));
    	addSequential(new AutoDriveStraight(-0.5, 1.75));
    }
}
