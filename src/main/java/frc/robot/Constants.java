/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final int kPCMID = 12;
    public static final int kPDPID = 1;

    public static final class DriveTrain {

        public static final double kOpenLoopRamp = 0.1;
        public static final double kDifferentialDriveExpiration = 0.4;
        public static final boolean kQuickTurn = true;

        public static final double kXSpeedModifierDefault = 1.0;
        public static final double kXSpeedModifierLow = 0.3;
        public static final double kZRotationModifierDefault = 0.6;
        public static final double kZRotationModifierLow = 0.3;
        
        public static final class Left {

            public static final int kFrontCANID = 23;
            public static final int kRearCANID = 24;
            public static final FeedbackDevice kSelectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;

            public static final Boolean kSensorPhase = false;
            public static final Boolean kFrontInverted = false;
            public static final Boolean kRearInverted = false;
            
            public static final int kPIDID = 0;
            public static final double kF = 0.5;
            public static final double kP = 0.4;
            public static final double kI = 0.0;
            public static final double kD = 0.1;
            public static final int kIZone = 50;
            public static final double kClosedLoopPeakOutput = 1.0;
            public static final int kAllowableError = 0;

            public static final double kFudgeCoefficent = 0.85;
        }

        public static final class Right {

            public static final int kFrontCANID = 21;
            public static final int kRearCANID = 22;
            public static final FeedbackDevice kSelectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;

            public static final Boolean kSensorPhase = false;
            public static final Boolean kFrontInverted = false;
            public static final Boolean kRearInverted = false;
            
            public static final int kPIDID = 0;
            public static final double kF = 0.5;
            public static final double kP = 0.4;
            public static final double kI = 0.0;
            public static final double kD = 0.1;
            public static final int kIZone = 50;
            public static final double kClosedLoopPeakOutput = 1.0;
            public static final int kAllowableError = 0;

            public static final double kFudgeCoefficent = 1.0;
            
        }

    }

    public static final class Turret {

        public static final class TalonSRX {

            public static final int kCANID = 25;
            public static final FeedbackDevice kSelectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Absolute; //needs verification
            public static final int kContinuousCurrentLimit = 2;
            public static final int kPeakCurrentLimit = 0;

            public static final Boolean kSensorPhase = true;
            public static final Boolean kInverted = false;
            
            public static final int kPIDID = 0;
            public static final double kF = 0.553;
            public static final double kP = 0.7;
            public static final double kI = 0.005;
            public static final double kD = 4;

            public static final int kMotionCruiseVelocity = 1800;
            public static final int kMotionAcceleration = 1800;
            public static final int kMotionSCurveStrength = 4;
            public static final int kIntegralZone = 80;
            public static final int kAllowableClosedloopError = 3;

        }

        public static final int kHallEffectA_DIO = 0;
        public static final int kHallEffectB_DIO = 1;

    }

    public static final class HatchGrabber {

        public static final int kSolenoidID = 3;
    
    }

    public static final class TurretExtender {

        public static final int kSolenoidID = 0;
    
    }


    public static final class Cargo {

        public static final int kCANID = 25;
        public static final int kHoldingCurrent = 10;    //need verification
        public static final double kDefaultOutput = 0.4; //need verification

    }

    public static final class PneumaticLift {

        public static final int kFrontSolenoidID = 2;
        public static final int kRearSolenoidID = 1;

    }

    public static final class TalonSRXDefault {
        
        public static final int kTalonSRX_timeout = 5; //in milliseconds
        public static final int kTalonSRX_period = 10; //in milliseconds
        public static final double kNeutralDeadband = 0.001;
        public static final int kNominalOutputForward = 0;
        public static final int kNominalOutputReverse = 0;
        public static final int kPeakOutputForward = +1;
        public static final int kPeakOutputReverse = -1;

    }

}
