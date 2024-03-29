/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.enumeration.DrivingType;

public class WallCommand extends Command {
  public WallCommand() {
    requires(Robot.ultrasonic);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.drivingType = DrivingType.NORMAL;
    //Robot.vision.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.ultrasonic.getDistanceCM() > 70) {
      Robot.driveExecutor.setY(-0.3);
    } else if(Robot.ultrasonic.getDistanceCM() < 70) {
      Robot.driveExecutor.setY(-0.15);
    } else if(Robot.ultrasonic.getDistanceCM() < 50) {
      Robot.driveExecutor.setY(-0.06);
    } else if(Robot.ultrasonic.getDistanceCM() < Constants.hatchToWallDistance) {
      Robot.driveExecutor.setY(0);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.ultrasonic.getDistanceCM() < Constants.hatchToWallDistance;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveExecutor.setY(0);
    Robot.drivingType = DrivingType.FIELD_ORIENTED;
    //Robot.vision.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.driveExecutor.setY(0);
    Robot.drivingType = DrivingType.FIELD_ORIENTED;
  }
}
