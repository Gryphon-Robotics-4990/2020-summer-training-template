/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;

import static frc.robot.Constants.Cargo.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Cargo extends SubsystemBase implements Loggable{

  private WPI_TalonSRX talonSRX = new WPI_TalonSRX(kCANID);
  
  /**
   * Creates a new ExampleSubsystem.
   */
  public Cargo() {
  }

  public double getSpeed() {
    return talonSRX.get();
  }

  public void setSpeed(double speed) {
    talonSRX.set(speed);
  }

  public double getCurrent() {
    return talonSRX.getStatorCurrent();
  }

  public void stop() {
    talonSRX.set(ControlMode.Disabled, 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run (20ms)
  }
}
