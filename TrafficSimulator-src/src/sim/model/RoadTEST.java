package sim.model;

import junit.framework.Assert;
import junit.framework.TestCase;
import sim.agent.*;

/**
 *
 * @author Admin
 */
public class RoadTEST extends TestCase {

    public RoadTEST(String name) {
        super(name);
    }

    TimeServer timeServer = new TimeServerLinked();

    public void testConstructor() {
        try {
            Road road = new Road(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        Road road = new Road(new Sink());
    }
    
    public void testEquals(){
        Road r2 = new Road(new Sink());
        Light l1 = new Light(r2);
        Road r1 = new Road(l1);
        
        Assert.assertEquals(l1, r1.getNext());
    }

    public void testRun() {
        Road r4 = new Road(new Sink());
        Light l3 = new Light(r4);
        Road r3 = new Road(l3);
        Light l2 = new Light(r3);
        Road r2 = new Road(l2);
        Light l1 = new Light(r2);
        Road r1 = new Road(l1);
        Source s = new Source(r1, timeServer);

        Assert.assertEquals(l2, r2.getNext());

        timeServer.enqueue(timeServer.currentTime(), s);
        timeServer.run(10);
    }
}
