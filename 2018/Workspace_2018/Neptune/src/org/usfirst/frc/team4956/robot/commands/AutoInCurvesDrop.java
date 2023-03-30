package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoInCurvesDrop extends CommandGroup {

    public AutoInCurvesDrop() {
    	addSequential(new TimeDrive(-0.63, 1)); // Straight Long Enough To Turn
        addSequential(new AutoTurn(-0.6, .95));
        addSequential(new TimeDrive(-0.65, 2.76)); // Straight Long Enough To Turn
        addSequential(new AutoTurn(0.6, 1.1));
    	addSequential(new TimeDrive(-0.8, 2)); // Straight Long Enough To Turn
    }
    
}

