/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.utils.ReportingInterface;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class for gyro and PID for rotating
 */
public class RotateSubsystem extends PIDSubsystem implements ReportingInterface {

  public AHRS ahrs;
  private int[] position = {0, 1, 2, 3, 4, 5 , 6, 7};
  public int currentPosition = 0;

  /**
   * Constructor, creates the PID controller, sets properties
   */
  public RotateSubsystem() {
    super("Gyroscope", Constants.gyroKp, Constants.gyroKi, Constants.gyroKd);
    setAbsoluteTolerance(Constants.gyroTolerance);
    setInputRange(-180.0f, 180.0f);
    setOutputRange(-0.7, 0.7);
    getPIDController().setContinuous(true);
    ahrs = new AHRS(SPI.Port.kMXP);
  }

  /**
   * Method for checking to which position to rotate to.
   * 8 pre-defined angles. Vary by 45 degrees.
   * @param rotate int between the values of -2 and 2.
   * -1 and 1 are for rotating by 45 degrees.
   * -2 and 2 are for rotating by 90 degrees.
   * Sets whether we wish to turn to the left or to the right and by how much.
   * @return position towards which we will rotate after this method call.
   */
  public int rotateRobot(int rotate) {
    if(rotate > 0) {
      int adding = currentPosition + rotate;
      if(adding <= 7) {
        currentPosition = position[adding];
      } else if(adding == 8) {
        currentPosition = position[0];
      } else if(adding == 9) {
        currentPosition = position[1];
      } else {
        System.err.println("Something happened with adding");
      }
      
    } else if(rotate < 0) {
      int subtracting = currentPosition + rotate;
      System.out.println(subtracting);
      if(subtracting >= 0) {
        currentPosition = position[subtracting];
      } else if(subtracting == -1) {
        currentPosition = position[position.length-1];
      } else if(subtracting == -2) {
        currentPosition = position[position.length-2];
      } else {
        System.err.println("Something happened with subtracting");
      }
    }
    return currentPosition;
  }

  /**
   * Method for checking whether the PID controller is enabled
   * @return boolean - true for enabled, false for disabled
   */
  public boolean isEnabled() {
    return getPIDController().isEnabled();
  }

  /**
   * Method for passing info to the PID controller
   * @return angle from gyro
   */
  @Override
  protected double returnPIDInput() {
    return ahrs.getYaw();
  }

  /**
   * Method for using the output of the PID controller
   * @param output double between -1 and 1 used for turning speed
   */
  @Override
  protected void usePIDOutput(double output) {
    if(output > 0) {
      if(output < 0.13 && output > 0.04) {
        output = 0.13;
      }
    } else if(output < 0) {
      if(output > -0.13 && output < -0.04) {
        output = -0.13;
      }
    }
    Robot.driveExecutor.setZ(output);
  }

  /**
   * Resets the heading of the gyro.
   * Sets the current heading to 0.
   * Sets the position to forward.
   */
  public void resetGyro(){
    ahrs.reset();
    currentPosition = 0;
  }

  /**
   * Defaut command, needed by the subsystem. 
   */
  @Override
  public void initDefaultCommand() {
    // setDefaultCommand(new MySpecialCommand());
  }

  public double getAngleDifference() {
    double angle = 0;
    double currentAngle = ahrs.getAngle() % 360;
    double angleDifference;
    switch(currentPosition) {
      case 0: angle = 0;
        break;
      case 1: angle = 29;
        break;
      case 2: angle = 90;
        break;
      case 3: angle = 151;
        break;
      case 4: angle = 180;
        break;
      case 5: angle = -151;
        break;
      case 6: angle = -90;
        break;
      case 7: angle = -29;
        break;
    }
    angleDifference = currentAngle - angle;
    return angleDifference;
  }

  public void report() {
    SmartDashboard.putBoolean("Gyro enabled", isEnabled());
    SmartDashboard.putNumber("Angle", ahrs.getYaw());
    SmartDashboard.putNumber("Current position", currentPosition);
    SmartDashboard.putNumber("Gyro Setpoint", getSetpoint());
  }
}
