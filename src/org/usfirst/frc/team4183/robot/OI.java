package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team4183.robot.commands.PositionCommand;
import org.usfirst.frc.team4183.robot.commands.TeleopDriveCommand;
import org.usfirst.frc.team4183.robot.commands.VelocityCommand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	
    private final Joystick joystick0 = new Joystick(0);
    
    private final Button square = new JoystickButton(joystick0, PS4Constants.SQUARE.getValue());
    private final Button circle = new JoystickButton(joystick0, PS4Constants.CIRCLE.getValue());
    private final Button triangle = new JoystickButton(joystick0, PS4Constants.TRIANGLE.getValue());
    
    public OI() {
    	
    	triangle.whenPressed(new TeleopDriveCommand());
    	square.whenPressed(new PositionCommand());
    	circle.whenPressed(new VelocityCommand());
    }
    
    
    // Go with raw axis numbers rather than the messed-up naming in Joystick.
        
    public double getFwdStick() {
    	// - sign because + axis of controller defined to point down
    	return -joystick0.getRawAxis(PS4Constants.LEFT_STICK_Y.getValue());
    }
    
    public double getTurnStick() {
        return joystick0.getRawAxis(PS4Constants.LEFT_STICK_X.getValue());
    }

    public double getPosStick() {
    	return getFwdStick();
    }
    
    public double getSpeedStick() {
    	return getFwdStick();
    }

    
}

