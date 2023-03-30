package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoOutSwitchDrop extends CommandGroup {

    public AutoOutSwitchDrop(double turnDirection) {
    	addSequential(new TimeDrive(-0.7, 2));// Straight Long Enough To Turn At 153in
    	addSequential(new TimeDrive(-0.6, 1));
        addSequential(new AutoTurn(turnDirection < 0 ? 0.6 : -0.6, 1.3)); //left turn is weaker, right turn has to be a higher angle to get 90
        addSequential(new TimeDrive(-0.8, 1.5)); // Drive To Switch 
    }
}
