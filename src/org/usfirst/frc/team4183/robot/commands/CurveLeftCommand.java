
package org.usfirst.frc.team4183.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4183.robot.Robot;

/**
 *
 */
public class CurveLeftCommand extends Command {

    public CurveLeftCommand( double timeOut) {
    	super(timeOut);
    	
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println( getName() + " initialize");
   }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveSubsystem.curveDrive(0.2, -1.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
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
