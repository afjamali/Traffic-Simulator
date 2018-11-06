package sim.model;

import java.util.List;
import java.util.Queue;
import sim.agent.Agent;
import sim.agent.TimeServer;
import sim.util.Util;

/**
 *
 * @author Admin
 */
public class Source implements Agent {

    Source(CarAcceptor next, TimeServer time) {
        nextRoad = next;
        timeServer = time;
        entryRate = Util.nextRandom(MP.entryRateMin, MP.entryRateMax);
    } // Created only by this package

    private CarAcceptor nextRoad;
    private TimeServer timeServer;
    private double entryRate;

    public void run() {

        if (timeServer.currentTime() % entryRate == 0) {

            /**
             * check that road's last car's back position is not less than max
             * car length. If is less, do not add new car to road.
             */
            Road r = (Road) nextRoad;
            List<Car> traffic = r.getCars();
            if (!traffic.isEmpty()) {
                Car lastCar = traffic.get(traffic.size() - 1);
                double distance = lastCar.backPosition();
                if (distance > MP.carLengthMax) {
                    Agent car1 = new Car(timeServer);
                    if (nextRoad.accept((Car) car1, 0)) {
                        car1.run();
                    }
                }
            } else {
                Agent car1 = new Car(timeServer);
                if (nextRoad.accept((Car) car1, 0)) {
                    car1.run();
                }
            }

        }
        timeServer.enqueue(timeServer.currentTime() + MP.timeStep, this);
    }

    public Road getRoad() {
        return (Road) nextRoad;
    }
}
