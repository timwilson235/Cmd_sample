package org.usfirst.frc.team4183.robot.commands;

import org.usfirst.frc.team4183.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TeleopDriveCommand extends Command {
		
		public TeleopDriveCommand() {
			
			requires( Robot.driveSubsystem);
		}

	    // Called just before this Command runs the first time
	    protected void initialize() {
	    	System.out.println( getName() + " initialize");
	    }

	    // Called repeatedly when this Command is scheduled to run
	    protected void execute() {
	    	
	        double fwd = Robot.oi.getFwdStick();
	        double turn = Robot.oi.getTurnStick();
	        Robot.driveSubsystem.arcadeDrive( fwd, turn);
			
	    }

	    // Make this return true when this Command no longer needs to run execute()
	    protected boolean isFinished() {
	        return false;
	    }

	    // Called once after isFinished returns true
	    protected void end() {
	    	System.out.println( getName() + " end");
	    }

	    // Called when another command which requires one or more of the same
	    // subsystems is scheduled to run
	    protected void interrupted() {
	    	System.out.println( getName() + " interrupted");
	    }
	    
}