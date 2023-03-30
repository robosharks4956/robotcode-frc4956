package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoInStraightDrop extends CommandGroup {

    public AutoInStraightDrop() {
    	addSequential(new TimeDrive(-0.75, 2)); // Want it to  go 110in 
    }
}
