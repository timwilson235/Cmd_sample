
package org.usfirst.frc.team4183.robot.subsystems;

import org.usfirst.frc.team4183.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveSubsystem extends Subsystem {

	// Gear ratio encoder to wheel
	private final int ENCODER_REV_PER_WHEEL_REV = 3;
	// Encoder pulses per rev (set w/ DIP switches)
	private final int ENCODER_CODES_PER_ENCODER_REV = 2048;

	private CANTalon[] motors = new CANTalon[4];

	private RobotDrive robotDrive;

	enum Mode {
		ROBOT_DRIVE, POSITION, SPEED;
	}

	private Mode currentMode;  // Initially "null"

	private int loopcnt = 0;

	public DriveSubsystem() {

		motors[0] = new CANTalon(RobotMap.LEFT_FRONT_MOTOR);
		motors[1] = new CANTalon(RobotMap.LEFT_REAR_MOTOR);
		motors[2] = new CANTalon(RobotMap.RIGHT_FRONT_MOTOR);
		motors[3] = new CANTalon(RobotMap.RIGHT_REAR_MOTOR);

		robotDrive = new RobotDrive(motors[0], motors[1], motors[2], motors[3]);
	}

	// + turn is right, - turn is left
	public void arcadeDrive(double fwd, double turn) {

		setMode(Mode.ROBOT_DRIVE);

		// RobotDrive.arcadeDrive defines +turn as left,
		// hence the - sign.
		robotDrive.arcadeDrive(fwd, -turn);
	}

	// speed = [-1 .. +1]
	// radius in meters; + means right, - means left
	public void curveDrive(double fwd, double radius) {

		setMode(Mode.ROBOT_DRIVE);

		robotDrive.drive(fwd, Math.signum(radius) * Math.exp(-Math.abs(radius) / RobotMap.WHEEL_TRACK));
	}

	public void setPosition(double position) {

		setMode(Mode.POSITION);

		CANTalon m = motors[0];
		m.set(position);

		// Must set all the motors to keep MotorSafety happy
		for (int i = 1 ; i < motors.length ; i++)
			motors[i].set(m.getDeviceID());

		if (++loopcnt >= 50) {
			loopcnt = 0;
			System.out.println(
					"Trg:" + position + " FB:" + m.get() + " Drv:" + m.getOutputVoltage() + " Err:" + m.getError());
		}
	}

	public void setSpeed(double speed) {

		setMode(Mode.SPEED);

		CANTalon m = motors[0];
		m.set(speed);

		// Must set all the motors to keep MotorSafety happy
		for (int i = 1 ; i < motors.length ; i++)
			motors[i].set(m.getDeviceID());

		if (++loopcnt >= 50) {
			loopcnt = 0;
			System.out.println(
					"Trg:" + speed + " FB:" + m.get() + " Drv:" + m.getOutputVoltage() + " Err:" + m.getError());
		}
	}

	private void setMode(Mode mode) {

		if (mode == currentMode)
			return;

		switch (mode) {
		case ROBOT_DRIVE:
			initRobotDriveMode();
			break;
		case POSITION:
			initPositionMode();
			break;
		case SPEED:
			initSpeedMode();
			break;
		}

		currentMode = mode;
	}

	private void initRobotDriveMode() {

		System.out.println("initRobotDriveMode");

		for (CANTalon m : motors) {
			// RobotDrive requires
			m.changeControlMode(CANTalon.TalonControlMode.PercentVbus);

			// All motors are backwards from what RobotDrive expects
			m.setInverted(true);

			// Watch out for this one; the way the units are defined,
			// if the arg is less than 1.17, it might as well be zero (disabled)
			// (see the code in CANTalon).
			m.setVoltageRampRate(50.0);
		}
	}

	private void initPositionMode() {

		System.out.println("initPositionMode");

		// Master motor
		CANTalon m = motors[0];
		m.changeControlMode(CANTalon.TalonControlMode.Position);

		setupClosedLoop();

		// All these params are stored in Flash on the Talon (manual sec. 11)
		// so must set them all if you don't want surprises!

		// Note on setClosedLoopRampRate:
		// If the arg is less then 11.7, it might well be 0 (disabled)
		// (see code in CANTalon class).
		
		// Note 2: using AllowableClosedLoopErr & NominalOutputVoltage,
		// rather than I-gain, to overcome dead zone. Works pretty good.
		// But make sure the Talon SRX firmware is at least 2.0 
		// or it doesn't work at all.
		
		m.setPID(0.05, 0.0, 0.0); // P, I, D
		m.setF(0.0);
		m.setIZone(0);
		m.setCloseLoopRampRate(50.0);  // Smoothes things a bit
		m.setAllowableClosedLoopErr(100);
		m.configNominalOutputVoltage(1.0, -1.0);  
		m.configPeakOutputVoltage(+12.0, -12.0);

		// Current position defined to be 0
		m.setPosition(0.0);
	}

	private void initSpeedMode() {

		System.out.println("initSpeedMode");

		// Master motor
		CANTalon m = motors[0];
		m.changeControlMode(CANTalon.TalonControlMode.Speed);

		setupClosedLoop();

		// All these params are stored in Flash on the Talon (manual sec. 11)
		// so must set them all if you don't want surprises!

		m.setPID(0.1, .001, 0.0);
		m.setF(0.1);
		m.setIZone(0);
		m.setCloseLoopRampRate(0.0);  // Works better disabled
		m.setAllowableClosedLoopErr(0);
		m.configNominalOutputVoltage(0.0, 0.0);
		m.configPeakOutputVoltage(+12.0, -12.0);
	}

	private void setupClosedLoop() {

		// Have to undo stuff that might have been done for Percent VBus
		// so it doesn't surprise us in closed loop modes.
		// Too bad there's no CANTalon call to restore everything to initial
		// state...

		for (CANTalon m : motors) {
			// Don't want setInverted(true) in these modes;
			// And don't let the doc & comments fool you, setInverted DOES have an
			// effect in modes other than Percent VBus
			m.setInverted(false);
			// Get this out of the way also (value of zero disables ramp)
			m.setVoltageRampRate(0.0);
		}

		// Master motor
		CANTalon m = motors[0];

		// So + drive goes forward
		m.reverseOutput(true);

		// Using left encoder plugged into left-front motor controller
		m.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		m.configEncoderCodesPerRev(ENCODER_REV_PER_WHEEL_REV * ENCODER_CODES_PER_ENCODER_REV);

		// So going forward produces + output from encoder
		m.reverseSensor(true);

		// Slave remaining 3 motors to master
		for( int i = 1 ; i < motors.length ; i++) {
			motors[i].changeControlMode(CANTalon.TalonControlMode.Follower);
			motors[i].set(m.getDeviceID());
		}

		// Set up inversions.
		// I hope I read documentation correctly;
		// For slaves (followers), says whether you want a sign flip *relative
		// to Master*.
		motors[1].reverseOutput(false); // Left rear follow master
		motors[2].reverseOutput(true);  // Both right side follow master w/ inversion
		motors[3].reverseOutput(true);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
	}
}
