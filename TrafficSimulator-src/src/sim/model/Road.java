package sim.model;

import java.util.List;
import java.util.ArrayList;
import sim.util.Util;

/**
 * A road holds cars.
 */
public class Road implements CarAcceptor {

    Road(CarAcceptor next) {
        if (next == null) {
            throw new IllegalArgumentException();
        }
        nextRoad = next;
    } // Created only by this package

    private List<Car> _cars = new ArrayList<Car>();
    private double endPosition;
    private double roadLength = (int) Math.ceil(Util.nextRandom(MP.roadLengthMin, MP.roadLengthMax));
    private CarAcceptor nextRoad;

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
     * If no cars in front, return the next obstacle.
     * 
     * @param c The car
     * @param fromPosition The front position of car
     * @return obstacle position
     */
    public double distanceToObstacle(Car c, double fromPosition) {
        double obstaclePosition = this.distanceToCarBack(fromPosition) - c.getCarLength();
        if (obstaclePosition == Double.POSITIVE_INFINITY) {
            obstaclePosition
                    = nextRoad.distanceToObstacle(c, fromPosition);
        }
        return obstaclePosition - fromPosition;
    }

    /**
     * 
     * @param c The car
     * @param frontPosition Front position of car
     * @return boolean to accept car or not
     */
    public boolean accept(Car c, double frontPosition) {

        /**
         * If car is instance of Light, check that road's last car's back position
         * is not less than max car length. If is less, do not update position.
         */
        if (c.getCurrentRoad() instanceof Light) {

            if (!_cars.isEmpty()) {
                double lastCarPos = _cars.get(_cars.size() - 1).backPosition();
                if (lastCarPos < MP.carLengthMax) {
                    return true;
                }
            }
        }

        _cars.remove(c);
        endPosition = roadLength - c.getCarLength();
        if (frontPosition > endPosition) {
            return nextRoad.accept(c, frontPosition - this.endPosition);
        } else {
            c.setCurrentRoad(this);
            c.setFrontPosition(frontPosition);
            _cars.add(c);
            return true;
        }
    }

    public List<Car> getCars() {
        return _cars;
    }

    public double getRoadLength() {
        return roadLength;
    }

    public CarAcceptor getNext() {
        return nextRoad;
    }
}
