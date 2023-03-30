package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TurnBabyTurn extends CommandGroup {

    public TurnBabyTurn() {
        addSequential(new AutoDriveTurn(-83));
        
    }
}
