package sim.model;

import java.util.ArrayList;
import java.util.List;
import sim.agent.Agent;
import sim.agent.TimeServer;
import sim.util.Util;

/**
 *
 * @author Admin
 */
class Intersection implements Agent {

    Intersection(TimeServer time) {
        if (time == null) {
            throw new IllegalArgumentException();
        }
        timeServer = time;
        lights = new Light[2];
    } // Created only by this package

    private Light[] lights;
    private final int EW = 0, NS = 1;
    private TimeServer timeServer;
    private double intersectionLength = Util.nextRandom(MP.intersectionLengthMin, MP.intersectionLengthMax);
    private double greenDurationNS = Util.nextRandom(MP.greenTimeMin, MP.greenTimeMax);
    private double yellowDurationNS = Util.nextRandom(MP.yellowTimeMin, MP.yellowTimeMax);
    private double greenDurationEW = Util.nextRandom(MP.greenTimeMin, MP.greenTimeMax);
    private double yellowDurationEW = Util.nextRandom(MP.yellowTimeMin, MP.yellowTimeMax);

    public void run() {
        
        // Calculate currect light cycle
        double lightCycle = timeServer.currentTime() % (greenDurationNS + yellowDurationNS + greenDurationEW + yellowDurationEW);
        LightColor ewLightColor = LightColor.GREEN;
        LightColor nsLightColor = LightColor.RED;
        boolean ewLightState = true;
        boolean nsLightState = !ewLightState;
        
        // If light cycle is within green and yellow range, find the next light color
        if (lightCycle <= greenDurationEW + yellowDurationEW) {

            if (lightCycle <= greenDurationEW) {
                ewLightColor = LightColor.GREEN;
            } else {
                ewLightColor = LightColor.YELLOW;
            }
            nsLightColor = LightColor.RED;
        } else {
            ewLightState = false;
            nsLightState = !ewLightState;
            if (lightCycle - (greenDurationEW + yellowDurationEW) <= greenDurationNS) {
                nsLightColor = LightColor.GREEN;
            } else {
                nsLightColor = LightColor.YELLOW;
            }
            ewLightColor = LightColor.RED;
        }

        if (!lights[EW].getCars().isEmpty()) {
            nsLightState = false;
        }
        if (!lights[NS].getCars().isEmpty()) {
            ewLightState = false;
        }
        
        lights[EW].setState(ewLightState);
        lights[EW].setColor(ewLightColor);
        
        lights[NS].setState(nsLightState);
        lights[NS].setColor(nsLightColor);

        timeServer.enqueue(timeServer.currentTime() + MP.timeStep, this);
    }

    public void setLight(Light l, Direction d) {
        int ld = 0;
        switch (d) {
            case EASTWEST:
                ld = EW;
                break;
            case NORTHSOUTH:
                ld = NS;
                break;
            default:
                throw new IllegalArgumentException();
        }
        lights[ld] = l;
        lights[ld].setDirection(d);
        lights[ld].setIntersectionLength(intersectionLength);
    }

    public Light[] getLights() {
        return lights;
    }
}
