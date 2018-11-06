package sim.model;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import sim.agent.*;

/**
 *
 * @author Admin
 */
public class IntersectionTEST extends TestCase {

    public IntersectionTEST(String name) {
        super(name);
    }

    public void testConstructor() {
        TimeServer timeServer = new TimeServerLinked();
        try {
            Intersection instance = new Intersection(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        Intersection instance = new Intersection(timeServer);
    }

    public void testRun() {
        new Worker().start();

    }
}

class Worker extends Thread implements Agent {

    TimeServer timeServer = new TimeServerLinked();
    List<Light> _lightElements;
    Intersection c1;
    Intersection c2;
    Intersection c3;
    int EW = 0;
    int NS = 1;

    public Worker() {
        _lightElements = new ArrayList<Light>();
        constructGrid();
        timeServer.run(1000);
    }

    public void constructGrid() {
        Light l1 = new Light(new Road(new Sink()));
        Light l2 = new Light(new Road(new Sink()));
        
        c1 = new Intersection(timeServer);
        c1.setLight(l1, Direction.EASTWEST);
        c1.setLight(l2, Direction.NORTHSOUTH);


        timeServer.enqueue(timeServer.currentTime(), c1);

        timeServer.enqueue(timeServer.currentTime() + MP.timeStep, this);
    }

    public void run() {

        for (Light e : _lightElements) {
            if (e.getState()) {
                Assert.assertTrue(e.getColor() == LightColor.GREEN || e.getColor() == LightColor.YELLOW);
            }
            if (e.getState() == false) {
                Assert.assertTrue(e.getColor() == LightColor.RED);
            }
        }
        Assert.assertTrue(c1.getLights()[EW].getDirection() == Direction.EASTWEST);
        Assert.assertTrue(c1.getLights()[NS].getDirection() == Direction.NORTHSOUTH);

        if (c1.getLights()[EW].getState()) {
            Assert.assertTrue(c1.getLights()[NS].getState() == false);
        }
        if (c1.getLights()[NS].getState()) {
            Assert.assertTrue(c1.getLights()[EW].getState() == false);
        }

        timeServer.enqueue(timeServer.currentTime() + MP.timeStep, this);
    }
}
