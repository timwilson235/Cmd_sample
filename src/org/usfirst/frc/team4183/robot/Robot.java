
package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import org.usfirst.frc.team4183.robot.commands.CurveLeftCommand;
import org.usfirst.frc.team4183.robot.commands.CurveRightCommand;
import org.usfirst.frc.team4183.robot.commands.TeleopDriveCommand;
import org.usfirst.frc.team4183.robot.commands.VelocityCommand;
import org.usfirst.frc.team4183.robot.commands.WaggleCommand;
import org.usfirst.frc.team4183.robot.commands.PositionCommand;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public static final DriveSubsystem driveSubsystem;
	public static final OI oi;
	
	static {

		try {
			// Careful of order! OI requires driveSubsystem to exist.
			driveSubsystem = new DriveSubsystem();
			oi = new OI();
		}
		catch( Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

    private SendableChooser chooser;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	//NetworkTable dash = NetworkTable.getTable("SmartDashboard");
    	//autonomousCommand.dash.clearPersistent("Auto mode");
   
    	System.out.println("robotInit()");
    	    	
    	chooser = new SendableChooser();   	
        chooser.addDefault( "Curve Right", new CurveRightCommand(5.0));
        chooser.addObject( "Curve Left", new CurveLeftCommand(5.0));
        chooser.addObject( "Waggle", new WaggleCommand());
        
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        Command autonomousCommand = (Command) chooser.getSelected();
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
    	
    	// I'm betting that starting teleopDrive 
    	// will preempt autonomous commands
        //if (autonomousCommand != null) autonomousCommand.cancel();
        
    	
    	// Commands here are started by button presses, see OI
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
