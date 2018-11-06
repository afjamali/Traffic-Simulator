package sim.model;

/**
 * Static class for model parameters.
 */
public class MP {

    private MP() {
    }
    public static double lineLength = 200;
    public static double lightSize = 10;
    /**
     * Maximum car velocity, in meters/second
     */
    public static double velocityMin = 10;
    public static double velocityMax = 30;
    /**
     * Car stop distance, in meters
     */
    public static double stopDistanceMin = 0.5;
    public static double stopDistanceMax = 5;
    /**
     * Car brake distance, in meters
     */
    public static double brakeDistanceMin = 9;
    public static double brakeDistanceMax = 10;
    /**
     * Traffic light green time and yellow time, in seconds
     */
    public static double greenTimeMin = 30;
    public static double greenTimeMax = 180;
    public static double yellowTimeMin = 4;
    public static double yellowTimeMax = 5;
    /**
     * Simulation time step, in meters/second
     */
    public static double timeStep = 0.1;
    /**
     * Simulation run time, in seconds
     */
    public static double runTime = 1000;
    /**
     * Size of grid
     */
    public static int gridRows = 2;
    public static int gridColumns = 3;
    /**
     * Traffic pattern
     */
    public static String trafficPattern = "alternating";
    /**
     * Car generation delay, in second/car
     */
    public static double entryRateMin = 1;
    public static double entryRateMax = 25;
    /**
     * Road segment length, in meters
     */
    public static double roadLengthMin = 200;
    public static double roadLengthMax = 500;
    /**
     * Intersection length, in meters
     */
    public static double intersectionLengthMin = 10;
    public static double intersectionLengthMax = 15;
    /**
     * Car length, in meters
     */
    public static double carLengthMin = 5;
    public static double carLengthMax = 10;

    public static StringBuilder getCurrentValues() {

        StringBuilder sb = new StringBuilder();

        sb.append("Simulation time step (seconds)\t[" + timeStep + "]" + "\n");
        sb.append("Simulation run time (seconds)\t[" + runTime + "]" + "\n");
        sb.append("Grid size (number of roads)\t[row=" + gridRows + ",column=" + gridColumns + "]" + "\n");
        sb.append("Traffic pattern\t[" + trafficPattern + "]" + "\n");
        sb.append("Car entry rate (seconds/car)\t[min=" + entryRateMin + ",max=" + entryRateMax + "]" + "\n");
        sb.append("Road segment length (meters)\t[min=" + roadLengthMin + ",max=" + roadLengthMax + "]" + "\n");
        sb.append("Intersection length (meters)\t[min=" + intersectionLengthMin + ",max=" + intersectionLengthMax + "]" + "\n");
        sb.append("Car length (meters)\t[min=" + carLengthMin + ",max=" + carLengthMax + "]" + "\n");
        sb.append("Car maximum velocity (meters/second)\t[min=" + velocityMin + ",max=" + velocityMax + "]" + "\n");
        sb.append("Car stop distance (meters)\t[min=" + stopDistanceMin + ",max=" + stopDistanceMax + "]" + "\n");
        sb.append("Car brake distance (meters)\t[min=" + brakeDistanceMin + ",max=" + brakeDistanceMax + "]" + "\n");
        sb.append("Traffic light green time (seconds)\t[min=" + greenTimeMin + ",max=" + greenTimeMax + "]" + "\n");
        sb.append("Traffic light yellow time (seconds)\t[min=" + yellowTimeMin + ",max=" + yellowTimeMax + "]" + "\n");

        return sb;
    }

    public static void reset() {
        velocityMin = 10;
        velocityMax = 30;
        stopDistanceMin = 0.5;
        stopDistanceMax = 5;
        brakeDistanceMin = 9;
        brakeDistanceMax = 10;
        greenTimeMin = 30;
        greenTimeMax = 180;
        yellowTimeMin = 4;
        yellowTimeMax = 5;
        timeStep = 0.1;
        runTime = 1000;
        gridRows = 2;
        gridColumns = 3;
        trafficPattern = "alternating";
        entryRateMin = 2;
        entryRateMax = 25;
        roadLengthMin = 200;
        roadLengthMax = 500;
        intersectionLengthMin = 10;
        intersectionLengthMax = 15;
        carLengthMin = 5;
        carLengthMax = 10;
    }
}
