package frc.robot;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Button;
public class F310Gamepad extends Joystick {

	public static enum POV {
		north(0), northWest(45), west(90), 
		southWest(135), south(180), southEast(225), 
		east(270), northEast(315);

		private int value;
    
        POV(int value) {
            this.value = value;
        }
    
        public int get() {
            return value;
        }
	}

	public enum Buttons {
		a(1), b(2), x(3), y(4), 
		leftBumper(5), rightBumper(6),
		start(8), back(7), 
		rightJoystickButton(10), 
		leftJoystickButton(9);

		private int value;
    
        Buttons(int value) {
            this.value = value;
        }
    
        public int get() {
            return value;
        }
	}

	public enum Axis {
		leftTrigger(2), rightTrigger(3),
		leftJoystickX(0), leftJoystickY(1),
		rightJoystickX(4), rightJoystickY(5);

		private int value;
    
        Axis(int value) {
            this.value = value;
        }
    
        public int get() {
            return value;
        }
	}

	/**
	 * Initializes new F310 Gamepad
	 * @param joystickNumber From drive station port number. (0 to 3?) BASED ON WHEN PLUGGED IN, not specific ports.
	 */
	public F310Gamepad(int joystickNumber) {
		super(joystickNumber);
	}

	public Boolean isButtonPressed(int button) {
		return this.getRawButton(button);
	}

	public Boolean isButtonPressed(Buttons button) {
		return this.getRawButton(button.get());
	}

	public JoystickButton getButton(Buttons button) {
		return new JoystickButton(this, button.get());
	}

	public JoystickAnalogButton getAxis(Axis axis) {
		switch (axis) {
			case leftJoystickY:  return new JoystickAnalogButton(this, 1, 0.0078126, true);
			case leftTrigger:    return new JoystickAnalogButton(this, 2, 0.01);
			case rightTrigger:   return new JoystickAnalogButton(this, 3, 0.01);
			case rightJoystickX: return new JoystickAnalogButton(this, 4, 0.0391);
			case rightJoystickY: return new JoystickAnalogButton(this, 5, 0.01);
			default: return new JoystickAnalogButton(this, axis.get());
	}}

	public DoubleSupplier getRawAxis(Axis axis) {
		return getAxis(axis).getRawAxis();
	}

	public BooleanSupplier getBooleanAxis(Axis axis) {
		return () -> getAxis(axis).get();
	}

	public BooleanSupplier isPOVPressed(int pov) {
		return () -> this.getPOV(pov) == pov;
	}

	public BooleanSupplier isPOVPressed(POV pov) {
		return () -> this.getPOV(pov.get()) == pov.get();
	}

	public POVButton getPOVButton(int pov) {
		return new POVButton(this, pov);
	}

	public POVButton getPOVButton(POV pov) {
		return new POVButton(this, pov.get());
	}

	public class JoystickAnalogButton extends Button {
		
		GenericHID m_gamepad;
		int m_axisNumber;
		double m_threshold = 0;
		Boolean m_inverted = false;

		/**
		 * Create a button for triggering commands off a joystick's analog axis
		 * 
		 * @param gamepad
		 *            The GenericHID object that has the button (e.g. Joystick,
		 *            KinectStick, etc)
		 * @param axisNumber
		 *            The axis number
		 */
		public JoystickAnalogButton(GenericHID gamepad, int axisNumber) {
			m_gamepad = gamepad;
			m_axisNumber = axisNumber;
		}

		/**
		 * Create a button for triggering commands off a joystick's analog axis
		 * 
		 * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
		 * @param axisNumber The axis number
		 * @param threshold The threshold to trigger above (positive) or below (negative)
		 */
		public JoystickAnalogButton(GenericHID joystick, int axisNumber, double threshold) {
			this(joystick, axisNumber);
			m_threshold = Math.abs(threshold);
		}

		public JoystickAnalogButton(GenericHID joystick, int axisNumber, double threshold, Boolean inverted) {
			this(joystick, axisNumber, threshold);
			m_inverted = true;
		}
		
		/**
		 * Returns boolean value of analog button.
		 * @return boolean value of button
		 */

		public BooleanSupplier getSupplier() {
				return () -> Math.abs(m_gamepad.getRawAxis(m_axisNumber)) > m_threshold;    //Return true if axis value is less than negative threshold
		}
		
		public boolean get() {
			return this.getSupplier().getAsBoolean();
		}
		
		/**
		 * Returns double value of axis.
		 * @return double value of axis.
		 */
		public DoubleSupplier getRawAxis() {
			if (!m_inverted) { //not inverted
				return () -> Math.abs(m_gamepad.getRawAxis(m_axisNumber)) > m_threshold ? 
				m_gamepad.getRawAxis(m_axisNumber) 
				: 0;
			} else { //inverted
				return () -> Math.abs(m_gamepad.getRawAxis(m_axisNumber)) > m_threshold ?
				-m_gamepad.getRawAxis(m_axisNumber) 
				: 0;
			}
			
		}
	}
}
