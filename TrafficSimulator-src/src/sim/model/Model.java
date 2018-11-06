package sim.model;

import sim.agent.*;
import java.util.Observable;
import sim.util.Animator;

/**
 * An example to model for a simple visualization. The model contains roads
 * organized in a matrix. See {@link #Model(AnimatorBuilder, int, int)}.
 */
public class Model extends Observable {

    private TimeServer timeServer = new TimeServerLinked();
    private Animator _animator;
    private boolean _disposed;
    private final AnimatorBuilder builder;

    /**
     * Creates a model to be visualized using the <code>builder</code>. If the
     * builder is null, no visualization is performed. The number of
     * <code>rows</code> and <code>columns</code> indicate the number of
     * {@link Light}s, organized as a 2D matrix. These are separated and
     * surrounded by horizontal and vertical {@link Road}s. For example, calling
     * the constructor with 1 row and 2 columns generates a model of the form:
     * <pre>
     *     |  |
     *   --@--@--
     *     |  |
     * </pre> where <code>@</code> is a {@link Light}, <code>|</code> is a
     * vertical {@link Road} and <code>--</code> is a horizontal {@link Road}.
     * Each road has one {@link Car}.
     *
     * <p>
     * The {@link AnimatorBuilder} is used to set up an {@link
     *  Animator}. {@link AnimatorBuilder#getAnimator()} is registered as an
     * observer of this model.
     * <p>
     */
    public Model(AnimatorBuilder builder) {
        int rows = MP.gridRows;
        int columns = MP.gridColumns;
        this.builder = builder;
        if (rows < 0 || columns < 0 || (rows == 0 && columns == 0)) {
            throw new IllegalArgumentException();
        }
        if (builder == null) {
            builder = new NullAnimatorBuilder();
        }
        setup();
        _animator = builder.getAnimator();
        timeServer.addObserver(_animator); // Add animator to observer.
    }

    /**
     * Run the simulation for <code>duration</code> model seconds.
     */
    public void run() {
        if (_disposed) {
            throw new IllegalStateException();
        }
        timeServer.run(MP.runTime);
    }

    /**
     * Throw away this model.
     */
    public void dispose() {
        _animator.dispose();
        _disposed = true;
    }

    /**
     * Construct the model, establishing correspondences with the visualizer.
     */
    private void setup() {

        makeSquareGrid();
    }

    /**
     * Create roads, intersections, lights, and sources.
     * 
     * @param lc Light controller
     * @param d Direction
     * @param num n row or column
     * @return 
     */
    private Source makeSegment(Intersection[] lc, Direction d, int num) {

        int size = 0;
        CarAcceptor next = new Road(new Sink());

        switch (d) {
            case EASTWEST:
                size = MP.gridColumns;
                builder.addHorizontalRoad((Road) next, num, size, false);
                for (int i = size - 1; i >= 0; i--) {
                    Light l = new Light(next);
                    lc[i].setLight(l, Direction.EASTWEST);
                    timeServer.enqueue(timeServer.currentTime(), lc[i]);
                    next = new Road(l);
                    Road r = (Road) next;
                    l.setLightPosition(r.getRoadLength());
                    builder.addHorizontalRoad((Road) next, num, i, false);
                }
                break;
            case WESTEAST:
                size = MP.gridColumns;
                builder.addHorizontalRoad((Road) next, num, 0, true);
                for (int i = 1; i <= size; i++) {
                    Light l = new Light(next);
                    lc[i - 1].setLight(l, Direction.EASTWEST);
                    timeServer.enqueue(timeServer.currentTime(), lc[i - 1]);
                    next = new Road(l);
                    Road r = (Road) next;
                    l.setLightPosition(r.getRoadLength());
                    builder.addHorizontalRoad((Road) next, num, i, true);
                }
                break;
            case NORTHSOUTH:
                size = MP.gridRows;
                builder.addVerticalRoad((Road) next, size, num, false);
                for (int i = size - 1; i >= 0; i--) {
                    Light l = new Light(next);
                    builder.addLight(l, i, num);
                    lc[i].setLight(l, Direction.NORTHSOUTH);
                    timeServer.enqueue(timeServer.currentTime(), lc[i]);
                    next = new Road(l);
                    Road r = (Road) next;
                    l.setLightPosition(r.getRoadLength());
                    builder.addVerticalRoad((Road) next, i, num, false);
                }
                break;
            case SOUTHNORTH:
                size = MP.gridRows;
                builder.addVerticalRoad((Road) next, 0, num, true);
                for (int i = 1; i <= size; i++) {
                    Light l = new Light(next);
                    builder.addLight(l, i - 1, num);
                    lc[i - 1].setLight(l, Direction.NORTHSOUTH);
                    timeServer.enqueue(timeServer.currentTime(), lc[i - 1]);
                    next = new Road(l);
                    Road r = (Road) next;
                    l.setLightPosition(r.getRoadLength());
                    builder.addVerticalRoad((Road) next, i, num, true);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return new Source(next, timeServer);
    }

    // rotate a 2D array
    private Intersection[][] rotate(Intersection[][] orig) {
        Intersection[][] value = new Intersection[orig[0].length][orig.length];
        for (int i = 0; i < orig[0].length; i++) {
            for (int j = 0; j < orig.length; j++) {
                value[i][j] = orig[j][i];
            }
        }
        return value;
    }

    /**
     * Declare Intersections eastWest and northSouth and pass them to makeSegment
     * @see makeSegment
     */
    private void makeSquareGrid() {
        Intersection[][] eastWest = new Intersection[MP.gridRows][MP.gridColumns];
        for (int i = 0; i < MP.gridRows; i++) {
            for (int j = 0; j < MP.gridColumns; j++) {
                eastWest[i][j] = new Intersection(timeServer);
                timeServer.enqueue(timeServer.currentTime(), eastWest[i][j]);
            }
        }
        Intersection[][] northSouth = rotate(eastWest);

        // Case for simple traffic pattern
        if (MP.trafficPattern.equals("simple")) {
            for (int i = 0; i < MP.gridRows; i++) {
                timeServer.enqueue(timeServer.currentTime(), makeSegment(eastWest[i], Direction.EASTWEST, i));
            }
            for (int i = 0; i < MP.gridColumns; i++) {
                timeServer.enqueue(timeServer.currentTime(), makeSegment(northSouth[i], Direction.NORTHSOUTH, i));
            }
        } else {
            
            // Case for alternating traffic pattern
            boolean eastToWest = true;
            boolean northToSouth = true;
            for (int i = 0; i < MP.gridRows; i++) {
                if (eastToWest) {
                    timeServer.enqueue(timeServer.currentTime(), makeSegment(eastWest[i], Direction.EASTWEST, i));
                    eastToWest = false;
                    continue;
                }
                if (!eastToWest) {
                    timeServer.enqueue(timeServer.currentTime(), makeSegment(eastWest[i], Direction.WESTEAST, i));
                    eastToWest = true;
                }
            }
            for (int i = 0; i < MP.gridColumns; i++) {
                if (northToSouth) {
                    timeServer.enqueue(timeServer.currentTime(), makeSegment(northSouth[i], Direction.NORTHSOUTH, i));
                    northToSouth = false;
                    continue;
                }
                if (!northToSouth) {
                    timeServer.enqueue(timeServer.currentTime(), makeSegment(northSouth[i], Direction.SOUTHNORTH, i));
                    northToSouth = true;
                }
            }
        }
    }
}
