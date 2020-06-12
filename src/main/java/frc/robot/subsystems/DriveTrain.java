/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import static frc.robot.Constants.DriveTrain.*;
import static frc.robot.Constants.TalonSRXDefault.*;

public class DriveTrain extends SubsystemBase implements Loggable {

  private final WPI_TalonSRX leftFront = new WPI_TalonSRX(Left.kFrontCANID);
  private final WPI_TalonSRX leftRear = new WPI_TalonSRX(Left.kRearCANID);
  private final WPI_TalonSRX rightFront = new WPI_TalonSRX(Left.kFrontCANID);
  private final WPI_TalonSRX rightRear = new WPI_TalonSRX(Left.kRearCANID);

  private final DifferentialDrive differentialDrive = new DifferentialDrive(
    new SpeedControllerGroup(leftFront, leftRear), new SpeedControllerGroup(rightFront, rightRear));
  
  /**
   * Creates a new ExampleSubsystem.
   */
  public DriveTrain() {
    configureTalonSRX();
    configurePID();
    
    differentialDrive.setExpiration(kDifferentialDriveExpiration);
  }

  private void configureTalonSRX() {
    
    leftFront.configFactoryDefault(kTalonSRX_timeout);

    leftFront.setSensorPhase(Left.kSensorPhase);
    leftFront.setInverted(Left.kFrontInverted);
    leftRear.setInverted(Left.kRearInverted);
    
    leftFront.configNeutralDeadband(kNeutralDeadband, kTalonSRX_timeout);

    leftFront.configPeakOutputForward(kPeakOutputForward, kTalonSRX_timeout);
    leftFront.configPeakOutputReverse(kPeakOutputReverse, kTalonSRX_timeout);

    leftFront.configSelectedFeedbackSensor(Left.kSelectedFeedbackSensor, Left.kPIDID, kTalonSRX_timeout);
    leftFront.configOpenloopRamp(kOpenLoopRamp, kTalonSRX_timeout);

    leftRear.follow(leftFront);

    /*-----*/

    rightFront.configFactoryDefault(kTalonSRX_timeout);

    rightFront.setSensorPhase(Right.kSensorPhase);
    rightFront.setInverted(Right.kFrontInverted);
    rightRear.setInverted(Right.kRearInverted);
    
    rightFront.configNeutralDeadband(kNeutralDeadband, kTalonSRX_timeout);

    rightFront.configPeakOutputForward(kPeakOutputForward, kTalonSRX_timeout);
    rightFront.configPeakOutputReverse(kPeakOutputReverse, kTalonSRX_timeout);

    rightFront.configSelectedFeedbackSensor(Right.kSelectedFeedbackSensor, Right.kPIDID, kTalonSRX_timeout);
    rightFront.configOpenloopRamp(kOpenLoopRamp, kTalonSRX_timeout);

    rightRear.follow(rightFront);

  }

  private void configurePID() {

    leftFront.selectProfileSlot(0, 0);
    leftFront.config_kP(0, Left.kP, kTalonSRX_timeout); 
    leftFront.config_kI(0, Left.kI, kTalonSRX_timeout); 
    leftFront.config_kD(0, Left.kD, kTalonSRX_timeout); 
    leftFront.config_kF(0, Left.kF, kTalonSRX_timeout);
    leftFront.config_IntegralZone(0, Left.kIZone, kTalonSRX_timeout); 
    leftFront.configClosedLoopPeakOutput(0, Left.kClosedLoopPeakOutput, kTalonSRX_timeout); 
    leftFront.configAllowableClosedloopError(0, Left.kAllowableError, kTalonSRX_timeout); 

    /*-----*/

    rightFront.selectProfileSlot(0, 0);
    rightFront.config_kP(0, Right.kP, kTalonSRX_timeout); 
    rightFront.config_kI(0, Right.kI, kTalonSRX_timeout); 
    rightFront.config_kD(0, Right.kD, kTalonSRX_timeout); 
    rightFront.config_kF(0, Right.kF, kTalonSRX_timeout);
    rightFront.config_IntegralZone(0, Right.kIZone, kTalonSRX_timeout); 
    rightFront.configClosedLoopPeakOutput(0, Right.kClosedLoopPeakOutput, kTalonSRX_timeout); 
    rightFront.configAllowableClosedloopError(0, Right.kAllowableError, kTalonSRX_timeout); 

  }

  public void setSpeed(double speed) {
    setSpeed(speed, speed);
  }

  public void setSpeed(double leftSpeed, double rightSpeed) {
    differentialDrive.tankDrive(leftSpeed, rightSpeed, false);
  }
  
  public void drive(double xSpeed, double zRotation) {
    differentialDrive.curvatureDrive(xSpeed, zRotation, kQuickTurn);
  }

  public void stop() {
    differentialDrive.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run (20ms)
  }
}
