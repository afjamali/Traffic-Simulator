/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.model;

/**
 *
 * @author Admin
 */
class Sink implements CarAcceptor {

    public double distanceToObstacle(Car c, double fromPosition) {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Remove car from road.
     * 
     * @param c
     * @param frontPosition
     * @return 
     */
    public boolean accept(Car c, double frontPosition) {
        Road r = (Road)c.getCurrentRoad();
        r.getCars().remove(c);
        return false;
    }
}
