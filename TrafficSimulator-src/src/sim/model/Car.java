package sim.model;

import sim.agent.*;
import sim.util.Util;

public class Car implements Agent {

    Car(TimeServer time) {
        if (time == null){
            throw new IllegalArgumentException();
        }
        timeServer = time;
    } // Created only by this package

    private TimeServer timeServer;
    private CarAcceptor currentRoad;
    private boolean brakeOnYellow;
    private double frontPosition;
    private double _velocity;
    private double newFrontPosition;
    private double carLength = Util.nextRandom(MP.carLengthMin, MP.carLengthMax);
    private double maxVelocity = Util.nextRandom(MP.velocityMin, MP.velocityMax);
    private double brakeDistance = Util.nextRandom(MP.brakeDistanceMin, MP.brakeDistanceMax);
    private double stopDistance = Util.nextRandom(MP.stopDistanceMin, MP.stopDistanceMax);
    private java.awt.Color _color = new java.awt.Color((int) Math.ceil(Math.random() * 255), (int) Math.ceil(Math.random() * 255), (int) Math.ceil(Math.random() * 255));

    public double getPosition() {
        return frontPosition;
    }
    
    public double getBrakeDistance(){
        return brakeDistance;
    }

    public double backPosition() {
        return frontPosition - carLength;
    }

    public double getCarLength() {
        return carLength;
    }

    public void setCurrentRoad(CarAcceptor r) {
        currentRoad = r;
    }

    public CarAcceptor getCurrentRoad() {
        return currentRoad;
    }

    public void setFrontPosition(double f) {
        frontPosition = f;
    }

    public java.awt.Color getColor() {
        return _color;
    }
    
    public void setBrkOnYllw(boolean b){
        brakeOnYellow = b;
    }
    
    public boolean getBrkOnYllw(){
        return brakeOnYellow;
    }

    /**
     * Calculate distance to obstacle. Determine velocity of car and calculate new front position.
     * Send this object to current road to accept. If accepted, enqueue in time server.     * 
     */
    public void run() {
        double disToObst = currentRoad.distanceToObstacle(this, frontPosition);
        _velocity = (maxVelocity / (brakeDistance - stopDistance))
                * (disToObst - stopDistance);
        _velocity = Math.max(0.0, _velocity);
        _velocity = Math.min(maxVelocity, _velocity);

        newFrontPosition = frontPosition + _velocity * MP.timeStep;

        // calculate newFrontPosition
        if (currentRoad.accept(this, newFrontPosition)) {
            timeServer.enqueue(timeServer.currentTime() + MP.timeStep, this);
        }
    }
}
