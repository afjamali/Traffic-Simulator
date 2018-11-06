package sim.model;

/**
 * Interface for accepting cars and calculating distance to obstacles.
 * @author Admin
 */
public interface CarAcceptor {

    public boolean accept(Car c, double frontPosition);

    public double distanceToObstacle(Car c, double fromPosition);
}
