package RoboTicTacToe;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * RoboTicTacToe - a robot by Mihai, Lucian, Mircea
 */
public class RoboTicTacToe extends AdvancedRobot
{
	double enemyEnergy = 100.00;
    int moveDirection = 1;
	
	public void run() {
		setBodyColor(new Color(0f, 0f, 1f, 0f));
		setGunColor(new Color(0f, 0f, 1f, 0f));

        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
	}

	public void onScannedRobot(ScannedRobotEvent e) {
        double enemyDirection = getHeading() - getGunHeading() + e.getBearing();
        
        setTurnGunRight(enemyDirection);
        setTurnRight(enemyDirection);
        if (getTime() % 40 == 0) {
            moveDirection *= -1;
            setAhead(50 * moveDirection);
        }
        setAhead((e.getDistance() - 50) * moveDirection * avoidWalls());
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(Math.min(600 / e.getDistance(), 3));
        }

		double changeEnergy = enemyEnergy - e.getEnergy();
		if (changeEnergy >= 0 && changeEnergy <= 3){
			setAhead(50 * moveDirection * avoidWalls());
			enemyEnergy = e.getEnergy();
		}

        if (getVelocity() == 0) {
	        moveDirection *= -1;
        }
	}

    private double avoidWalls() {

        double wall_avoid_distance = 60;
		
        double fieldHeight = getBattleFieldHeight();
        double fieldWidth = getBattleFieldWidth();
        double centerX = (fieldWidth / 2);
        double centerY = (fieldHeight / 2);
        double x = getX();
        double y = getY();
        if (x < wall_avoid_distance || x > fieldWidth - wall_avoid_distance) {
        	moveDirection *= -1;
        }
        if (y < wall_avoid_distance || y > fieldHeight - wall_avoid_distance) {
        	moveDirection *= -1;
        }
        return moveDirection;
    }

	public void onHitByBullet(HitByBulletEvent e) {
	}
	
	public void onHitRobot(HitRobotEvent e) {
        moveDirection *= -1;
	}
	
	public void onHitWall(HitWallEvent e) {
        moveDirection *= -1;
	}	
	
	public void onBulletMissed(BulletMissedEvent e) {
        setAhead(50 * moveDirection * avoidWalls());
	}	
}
