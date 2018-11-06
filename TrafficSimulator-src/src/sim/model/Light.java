package sim.model;

import java.util.ArrayList;
import java.util.List;
import sim.agent.Agent;

/**
 * A light has a boolean state.
 */
public class Light implements CarAcceptor {

    Light(CarAcceptor next) {
        if (next == null) {
            throw new IllegalArgumentException();
        }
        nextRoad = next;
    } // Created only by this package

    private boolean _state = true;
    private double lightPosition;
    private double endPosition;
    private double intersectionLength;
    private LightColor lightColor;
    private Direction direction;
    private List<Car> _cars = new ArrayList<Car>();
    private final CarAcceptor nextRoad;

    /**
     * 
     * @param fromPosition The front position of the car
     * @return Return back position of car in front
     */
    private double distanceToCarBack(double fromPosition) {
        double carBackPosition = Double.POSITIVE_INFINITY;
        for (Car c : _cars) {
            if (c.backPosition() >= fromPosition
                    && c.backPosition() < carBackPosition) {
                carBackPosition = c.backPosition();
            }
        }
        return carBackPosition;
    }

    /**
     * Pos is the beginning position of the light - car length
     * 
     * if car is an instance of Light, return front cars back position.
     * Otherwise if car is within breaking distance of yellow light, pass thru. 
     * 
     * If light is Green:
     * If car is not within breaking distance of yellow light, then return position.
     * If back position of car on the next road is less than car length(max), return pos
     * 
     * If Light is Red:
     * return pos.
     * 
     * @param c The car
     * @param fromPosition The front position of car
     * @return 
     * 
     */
    public double distanceToObstacle(Car c, double fromPosition) {

        if (c.getCurrentRoad() instanceof Light) {
            double obstaclePosition = this.distanceToCarBack(fromPosition) - c.getCarLength();
            return obstaclePosition - fromPosition;
        }

        double pos = lightPosition - c.getCarLength();

        if (_state) {

            if (lightColor == LightColor.YELLOW) {
                if ((pos - fromPosition) >= c.getBrakeDistance()) {
                    c.setBrkOnYllw(true);
                    return pos;
                } else {
                    if (c.getBrkOnYllw()) {
                        return pos;
                    }
                    c.setBrkOnYllw(false);
                }
            }

            Road r = (Road) nextRoad;
            List<Car> traffic = r.getCars();

            if (!traffic.isEmpty()) {
                Car lastCar = traffic.get(traffic.size() - 1);
                double distance = lastCar.backPosition();
                if (distance < MP.carLengthMax) {
                    return pos;
                }
            }

            return Double.POSITIVE_INFINITY;

        } else {

            return pos;
        }
    }

    public void setState(boolean b) {
        _state = b;
    }

    public boolean getState() {
        return _state;
    }

    public List<Car> getCars() {
        return _cars;
    }

    public void setLightPosition(double r) {
        lightPosition = r;
    }

    public LightColor getColor() {
        return lightColor;
    }

    public void setColor(LightColor c) {
        lightColor = c;
    }

    public void setDirection(Direction d) {
        direction = d;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setIntersectionLength(double l) {
        intersectionLength = l;
    }

    /**
     * 
     * @param c The car
     * @param frontPosition Front position of car
     * @return boolean to accept car or not
     */
    public boolean accept(Car c, double frontPosition) {
        _cars.remove(c);
        endPosition = intersectionLength - c.getCarLength();
        if (frontPosition > endPosition) {
            return nextRoad.accept(c, c.getCarLength());
        } else {
            c.setCurrentRoad(this);
            c.setFrontPosition(frontPosition);
            _cars.add(c);
            return true;
        }
    }
}
