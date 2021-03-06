package org.usfirst.frc.team4183.robot.commands;

import org.usfirst.frc.team4183.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PositionCommand extends Command {

    public PositionCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires( Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println( getName() + " initialize");
   }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double position = Robot.oi.getPosStick();
        Robot.driveSubsystem.setPosition(position);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
