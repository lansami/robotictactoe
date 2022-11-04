package RoboTicTacToe;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * RoboTicTacToe - a robot by Mihai, Lucian, Mircea
 */
public class MlucRoboTicTacToeDelta extends AdvancedRobot
{
	double enemyEnergy = 100.00;
    int moveDirection = 1;
        double minFieldSize = 1;
	boolean once= true;	

	public void run() {
		setBodyColor(new Color(0f, 0f, 0f, 1f));
		setGunColor(new Color(0f, 0f, 1f, 0f));

        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
		
      setMaxVelocity(50);
		
        double fieldHeight = getBattleFieldHeight();
        double fieldWidth = getBattleFieldWidth();
        double min = Math.min(fieldHeight,fieldWidth);
        minFieldSize = Math.min(fieldHeight,fieldWidth);
		

		
         
		
	}

	public void onScannedRobot(ScannedRobotEvent e) {
System.out.println("current heading: "+getHeading());
        //double enemyDirection = e.getBearing();
		//setTurnGunRight(getHeading() - getGunHeading() + enemyDirection);
		setGunHeading(e);
		setHeading();
        setNextAhead(e);
		
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(Math.min(600 / e.getDistance(), 3));
        }
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
		double next = 100;
		/*if ( e.getDistance() < minFieldSize/6 )
            next = 10;
        else {
            next = 10+e.getDistance()/12;
        }*/
		System.out.println("getY="+getY()+" getBattleFieldHeight="+getBattleFieldHeight());
		if ( getY()-18 < next )
			moveDirection = 1;	
		else if ( getY()+next+18 > getBattleFieldHeight())
		moveDirection = -1;
		double minDistance = 30;
		double moveDistance = (Math.random() * (next - minDistance)) + minDistance;
		setAhead(moveDistance*moveDirection);
		}
        
	}

	
	private boolean shouldHoldPosition() {
		if (getTime() % 3 == 0) {
			return true;
		}
		
		return false;
	}

    private double avoidWalls() {

        double wall_avoid_distance = 60;
		
        double fieldHeight = getBattleFieldHeight();
        double fieldWidth = getBattleFieldWidth();
		System.out.println("fieldWidth="+fieldWidth+" fieldHeight="+fieldHeight);
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
