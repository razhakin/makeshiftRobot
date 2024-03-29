/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Command which rotates the shooter upwards
 */
public class RotateUpCommand extends Command {
  public RotateUpCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.shooterRotate);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("Rotating up");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.shooterRotate.getPotentiometer() < 25){
      Robot.shooterRotate.rotateUp();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (Robot.shooterRotate.getPotentiometer() >= 9); //Upper threshold should be 24
    /*     Values for shooting:
        9.6/9.22 Good for shooting cargo when alligned with cargo hatch
        3.94 Good for shooting lower rocket from one meter
    */
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.shooterRotate.stopRotate();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.shooterRotate.stopRotate();
  }
}