/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;
import io.github.oblarg.oblog.Loggable;

import static frc.robot.Constants.Turret.*;

import java.util.function.BooleanSupplier;

public class Turret extends SubsystemBase implements Loggable {

  private WPI_TalonSRX talonSRX = new WPI_TalonSRX(TalonSRX.kCANID);

  private Double setSpeed = 0.0;

  private DigitalInput hallEffectA = new DigitalInput(kHallEffectA_DIO);
  private DigitalInput hallEffectB = new DigitalInput(kHallEffectB_DIO);

  private BooleanSupplier inResetZone = () -> !hallEffectA.get() && !hallEffectB.get();
  
  /**
   * Creates a new ExampleSubsystem.
   */
  public Turret() {
    initalizeTalonSRX();
  }

  public void initalizeTalonSRX() {

    talonSRX.configFactoryDefault();
    talonSRX.configSelectedFeedbackSensor(TalonSRX.kSelectedFeedbackSensor, TalonSRX.kPIDID, TalonSRXDefault.kTalonSRX_timeout);
    talonSRX.setSensorPhase(TalonSRX.kSensorPhase);
    talonSRX.setInverted(TalonSRX.kInverted);
		talonSRX.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, TalonSRXDefault.kTalonSRX_period, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, TalonSRXDefault.kTalonSRX_period, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.configNominalOutputForward(TalonSRXDefault.kNominalOutputForward, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.configNominalOutputReverse(TalonSRXDefault.kNominalOutputReverse, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.configPeakOutputForward(TalonSRXDefault.kPeakOutputForward, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.configPeakOutputReverse(TalonSRXDefault.kPeakOutputReverse, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.configContinuousCurrentLimit(TalonSRX.kContinuousCurrentLimit);
		talonSRX.configPeakCurrentLimit(TalonSRX.kPeakCurrentLimit);
    talonSRX.enableCurrentLimit(true);
    
    talonSRX.selectProfileSlot(TalonSRX.kPIDID, 0);
		talonSRX.config_kF(TalonSRX.kPIDID, TalonSRX.kF, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.config_kP(TalonSRX.kPIDID, TalonSRX.kP, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.config_kI(TalonSRX.kPIDID, TalonSRX.kI, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.config_kD(TalonSRX.kPIDID, TalonSRX.kD, TalonSRXDefault.kTalonSRX_timeout);
    talonSRX.config_IntegralZone(TalonSRX.kPIDID, TalonSRX.kIntegralZone, TalonSRXDefault.kTalonSRX_timeout);
    talonSRX.configAllowableClosedloopError(TalonSRX.kPIDID, TalonSRX.kAllowableClosedloopError, TalonSRXDefault.kTalonSRX_timeout);

    talonSRX.configMotionCruiseVelocity(TalonSRX.kMotionCruiseVelocity, TalonSRXDefault.kTalonSRX_timeout); //was 1600
		talonSRX.configMotionAcceleration(TalonSRX.kMotionAcceleration, TalonSRXDefault.kTalonSRX_timeout);
		talonSRX.configMotionSCurveStrength(TalonSRX.kMotionSCurveStrength, TalonSRXDefault.kTalonSRX_timeout);
  }

  public void setSpeed(Double speed) {
    setSpeed = speed;
  }

  public Double getSpeed() {
    return talonSRX.get();
  }

  public void setPosition(int position) {
    setSpeed = 0.0;
    talonSRX.set(ControlMode.MotionMagic, position);
  }

  public int getPosition() {
    return talonSRX.getSelectedSensorPosition();
  }

  public void stop() {
    setSpeed = 0.0;
    talonSRX.set(ControlMode.Disabled, 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run (20ms)

    double currentPosition = talonSRX.getSelectedSensorPosition();
		if (currentPosition > 22500 && setSpeed != 0) {
			talonSRX.set(ControlMode.PercentOutput, -Math.abs(setSpeed)); //all motion should go counter-clockwise
			System.out.println("[Turret] motion in danger zone, past BACK point, at " + currentPosition);
		} else if (currentPosition < -6700 && setSpeed != 0) {
			talonSRX.set(ControlMode.PercentOutput, Math.abs(setSpeed)); //all motion should go clockwise
			System.out.println("[Turret] motion in danger zone, past LEFT point, at " + currentPosition);
		} else if (talonSRX.getControlMode() != ControlMode.MotionMagic) {
			talonSRX.set(ControlMode.PercentOutput, setSpeed);
		}

		if (inResetZone.getAsBoolean()) {
			resetPosition();
		}
  }

  public void resetPosition() {
		talonSRX.setSelectedSensorPosition(0, TalonSRX.kPIDID, TalonSRXDefault.kTalonSRX_timeout);
		if (inResetZone.getAsBoolean()) {
			System.out.println("[Turret position reset] Successfully reset encoder to " + talonSRX.getSelectedSensorPosition());
		} else {
			System.out.println("[Turret position reset] SENSOR NOT ENGAGED. Reset encoder to: " + talonSRX.getSelectedSensorPosition());
		}
  }

  /**
   * Experimental, needs verification
   * @param position encoder count
   * @return degrees of turret
   */
  
  public int positionToDegrees(double position) {
    position = 0.0102 * (position + 4500);
    while (position >= 360) { position =- 360;}
    while (position < 0) { position =+ 360;}
    return (int) position - 90;
  }

  public int degreesToPosition(int degrees) {
    return (int) 100 * ((degrees + 90) % 360) - 4500;
  }

}
