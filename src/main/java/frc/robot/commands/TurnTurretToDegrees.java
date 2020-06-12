/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.Turret;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Constants.Turret.*;

public class TurnTurretToDegrees extends CommandBase {

  private Turret turret;
  private int targetPosition;

  /**
   * Creates a new TurnTurretToDegrees command.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurnTurretToDegrees(Turret subsystem, int targetDegrees) {
    addRequirements(subsystem);
    turret = subsystem;
    targetPosition = turret.degreesToPosition(targetDegrees);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.setPosition(targetPosition);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(turret.getPosition()-targetPosition) > TalonSRX.kAllowableClosedloopError;
  }
}
