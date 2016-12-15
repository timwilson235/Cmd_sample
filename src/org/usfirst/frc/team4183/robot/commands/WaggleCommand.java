package org.usfirst.frc.team4183.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class WaggleCommand extends CommandGroup {

	public WaggleCommand() {
		addSequential(new CurveLeftCommand(2.0));
		addSequential(new CurveRightCommand(2.0));
	}
}
