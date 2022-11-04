package RoboTicTacToeOne;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * RoboTicTacToe - a robot by Mihai, Lucian, Mircea
 */
public class MlucRoboTicTacToeDeltaVOne extends AdvancedRobot
{
	double enemyEnergy = 100.00;
	int moveDirection = 1;

	public void run() {
		setBodyColor(new Color(0f, 0f, 0f, 1f));
		setGunColor(new Color(0f, 0f, 1f, 0f));

		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		turnRadarRightRadians(Double.POSITIVE_INFINITY);
	}

	private void shootTheEnemy(ScannedRobotEvent e) 
	{
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(Math.min(600 / e.getDistance(), 3));
        }
				
		setGunHeading(e);
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		setHeading();
		shootTheEnemy(e);
		setNextAhead(e);
	}
	

	public void setGunHeading(ScannedRobotEvent e)
	{
		double enemyAngle = e.getBearing();
		if ( enemyAngle < 0 ) enemyAngle = 360 + enemyAngle;
		double adjust = getHeading() + enemyAngle-getGunHeading();
		adjust += getAdjustmentForRobotMove(e);
		if ( adjust < 180 )
			setTurnGunRight(adjust);
		else
			setTurnGunLeft(360-adjust);
	}

	public double getAdjustmentForRobotMove(ScannedRobotEvent e)
	{
		double delta = e.getVelocity()/8*4;
		double robotHeading = e.getHeading();
		boolean goesDown = ( robotHeading > 90 && robotHeading < 270 );
		boolean isToTheRight = getGunHeading()<180;
		return (!(isToTheRight^goesDown))?delta:-delta;
	}

	boolean headingNotSet = true;	
	public void setHeading()
	{
		if (headingNotSet ) {
			headingNotSet =false;
			double currentHeading = getHeading();
			if ( currentHeading > 180 )
				setTurnRight(360-currentHeading);
			else
				setTurnLeft(currentHeading);
		}
	}
	
	public void setNextAhead(ScannedRobotEvent e) {
		if (shouldHoldPosition()) {
			// do something, I guess
		} else {
			double next = 200;

        double minDistance = 50;
        double moveDistance = (Math.random() * (next - minDistance)) + minDistance;
		if (getTime() % 5 == 0)
		moveDirection = (Math.random() > 0.5) ? 1: -1;
		
		if ( getY()-18 < moveDistance )
        	moveDirection = 1;	
        else if ( getY()+moveDistance+18 > getBattleFieldHeight())
        	moveDirection = -1;
        setAhead(moveDistance*moveDirection);
    }
}

private boolean shouldHoldPosition() {
	if (getTime() % 5 == 0) {
		return true;
	}

	return false;
}

public void onHitByBullet(HitByBulletEvent e) {
}

public void onHitRobot(HitRobotEvent e) {
}

public void onHitWall(HitWallEvent e) {
}	

public void onBulletMissed(BulletMissedEvent e) {
}	
}
