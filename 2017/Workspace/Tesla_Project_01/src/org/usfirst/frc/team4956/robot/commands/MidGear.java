package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MidGear extends CommandGroup {

    public MidGear() {
    	//Goes to target,  drops gear,  drives back
    	addSequential(new AutoDriveStraight(0.6, 1.30));
    	addSequential(new FaceTarget());
    	//addSequential(new AutoTurn(.4, 0.2));
    	addSequential(new AutoDriveStraight(0.5,1.4));
    }
}
