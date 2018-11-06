package sim.model;

import junit.framework.Assert;
import junit.framework.TestCase;
import sim.agent.*;

/**
 *
 * @author Admin
 */
public class CarTEST extends TestCase {

    public CarTEST(String name) {
        super(name);
    }

    TimeServer timeServer = new TimeServerLinked();

    public void testConstructor() {
        try {
            Car car = new Car(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        
        Car car = new Car(timeServer);
    }

    public void testRun() {
        Light light = new Light(new Road(new Sink()));
        Road road = new Road(light);
        Agent car1 = new Car(timeServer);
        road.accept((Car) car1, 0);
        car1.run();

    }
}
