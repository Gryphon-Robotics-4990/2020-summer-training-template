/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.F310Gamepad.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer implements Loggable {
  // Define robot subsystems and commands
  private final DriveTrain driveTrain = new DriveTrain();
  private final Turret turret = new Turret();
  private final Cargo cargo = new Cargo();

  private static final Solenoid hatchGrabber = new Solenoid(Constants.kPCMID, Constants.HatchGrabber.kSolenoidID);
  private static final Solenoid turretExtender = new Solenoid(Constants.kPCMID, Constants.TurretExtender.kSolenoidID);

  private final Compressor compressor = new Compressor(Constants.kPCMID);
  
  @Log // Add the PDP to the Shuffleboard
  private final PowerDistributionPanel powerDistributionPanel = new PowerDistributionPanel(Constants.kPDPID);
  private final UsbCamera usbCamera = CameraServer.getInstance().startAutomaticCapture();

  //Basic sample autonomus sequence
  private Command m_autoCommand = new SequentialCommandGroup(
    new TurnTurretToDegrees(turret, 0),
    new WaitCommand(0.5),
    new TurnTurretToDegrees(turret, 90),
    new WaitCommand(0.5),
    new TurnTurretToDegrees(turret, 180))
  .withTimeout(10); //Stop after 10 seconds

  private double xSpeedModifier = Constants.DriveTrain.kXSpeedModifierDefault;
  private double zRotationModifier = Constants.DriveTrain.kZRotationModifierDefault;

  /**
   * The container for the robot.  Contains subsystems, devices, and commands.
   */
  public RobotContainer() {

    configureButtonBindings();
    configureUsbCamera();

    turretExtender.set(true); //extend turretExtender
    hatchGrabber.set(false);  //retract hatchGrabber
  }

  /**
   * Use this method to define your button -> command mappings. 
   */
  private void configureButtonBindings() {
    final F310Gamepad operatorController = new F310Gamepad(0);
    final F310Gamepad driverController = new F310Gamepad(1); //not currently used
    
    operatorController.getButton(Buttons.start).whenPressed(new PrintCommand("START pressed on operatorController."));
    driverController.getButton(Buttons.start).whenPressed(new PrintCommand("START pressed on driverController."));

    //compressor toggle
    operatorController.getButton(Buttons.back).whenPressed(() -> { 
			compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
			System.out.println(compressor.getClosedLoopControl() ? "Compressor off" : "Compressor holding pressure");
    });

    //drive with joysticks
    driveTrain.setDefaultCommand(new TeleopDrive(driveTrain, 
    () -> driverController.getRawAxis(Axis.leftJoystickY).getAsDouble() * xSpeedModifier, 
    () -> driverController.getRawAxis(Axis.rightJoystickX).getAsDouble() * zRotationModifier));

    //software slow/low gear
    driverController.getButton(Buttons.x).whenPressed(() -> {
      xSpeedModifier = (xSpeedModifier == Constants.DriveTrain.kXSpeedModifierDefault) ? 
      Constants.DriveTrain.kXSpeedModifierLow : Constants.DriveTrain.kXSpeedModifierDefault;
    });
    //slow turning
    driverController.getButton(Buttons.b).whenPressed(() -> {
      zRotationModifier = (xSpeedModifier == Constants.DriveTrain.kZRotationModifierDefault) ? 
      Constants.DriveTrain.kZRotationModifierLow : Constants.DriveTrain.kZRotationModifierDefault;
    });

    //toggle pneumatics
    operatorController.getPOVButton(POV.south).whenPressed(() -> hatchGrabber.set(!hatchGrabber.get()));
    operatorController.getAxis(Axis.rightJoystickY).whenPressed(() -> turretExtender.set(!turretExtender.get()));

    //spin cargo intake/outake
    operatorController.getAxis(Axis.leftTrigger).whileHeld(
      () -> cargo.setSpeed(-operatorController.getRawAxis(Axis.leftTrigger).getAsDouble()), 
      cargo);
    operatorController.getAxis(Axis.rightTrigger).whileHeld(
      () -> cargo.setSpeed(operatorController.getRawAxis(Axis.rightTrigger).getAsDouble()), 
      cargo);

    //manually turn turret
    operatorController.getAxis(Axis.leftJoystickX).whileHeld(
      () -> turret.setSpeed((1/6) * Math.pow(operatorController.getRawAxis(Axis.leftJoystickX).getAsDouble(), 3.0)), 
      cargo);

    //turret preset positions
    operatorController.getButton(Buttons.y).whenPressed(new TurnTurretToDegrees(turret, 0));
    operatorController.getButton(Buttons.x).whenPressed(new TurnTurretToDegrees(turret, -90));
    operatorController.getButton(Buttons.b).whenPressed(new TurnTurretToDegrees(turret, 90));
    operatorController.getButton(Buttons.a).whenPressed(new TurnTurretToDegrees(turret, 180));
    operatorController.getButton(Buttons.start).whenPressed(new TurnTurretToDegrees(turret, -45));

    //turret reset
    operatorController.getButton(Buttons.leftJoystickButton).whenPressed(new InstantCommand
      (() -> System.out.println("Turret Schedueler Reset."), turret));
  }

  /**
   * Set up the USB Camera.
   */
  private void configureUsbCamera() {
    usbCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    usbCamera.setResolution(160, 120);
    usbCamera.setFPS(15);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_autoCommand;
  }

  @Log
  public static Boolean turretContinuousRotation() {
    return turretExtender.get();
  }
}
