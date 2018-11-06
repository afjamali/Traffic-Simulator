package sim.agent;

import java.util.Observable;
import sim.model.*;

public final class TimeServerLinked extends Observable implements TimeServer {

    private static final class Node {

        final double waketime;
        final Agent agent;
        Node next;

        public Node(double waketime, Agent agent, Node next) {
            this.waketime = waketime;
            this.agent = agent;
            this.next = next;
        }
    }
    private double _currentTime;
    private int _size;
    private Node _head;

    /*
     * Invariant: _head != null
     * Invariant: _head.agent == null
     * Invariant: (_size == 0) iff (_head.next == null)
     */
    public TimeServerLinked() {
        _size = 0;
        _head = new Node(0, null, null);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node node = _head.next;
        String sep = "";
        while (node != null) {
            sb.append(sep).append("(").append(node.waketime).append(",")
                    .append(node.agent).append(")");
            node = node.next;
            sep = ";";
        }
        sb.append("]");
        return (sb.toString());
    }

    public double currentTime() {
        return (double) Math.round(_currentTime * 100) / 100;
    }

    public void enqueue(double waketime, Agent agent)
            throws IllegalArgumentException {
        if (waketime < _currentTime) {
            throw new IllegalArgumentException();
        }
        Node prevElement = _head;
        while ((prevElement.next != null)
                && (prevElement.next.waketime <= waketime)) {
            prevElement = prevElement.next;
        }
        Node newElement = new Node(waketime, agent, prevElement.next);
        prevElement.next = newElement;
        _size++;
    }

    Agent dequeue() {
        if (_size < 1) {
            throw new java.util.NoSuchElementException();
        }
        Agent rval = _head.next.agent;
        _head.next = _head.next.next;
        _size--;
        return rval;
    }

    int size() {
        return _size;
    }

    boolean empty() {
        return size() == 0;
    }

    public void run(double duration) {
        double _time = 0;
        double endtime = _currentTime + duration;
        while ((!empty()) && (_head.next.waketime <= endtime)) {
            if ((_currentTime - _head.next.waketime) > 1e-09) {
                super.setChanged();
                super.notifyObservers();
            }
            _currentTime = _head.next.waketime;
            dequeue().run();
            if (_time != _currentTime) {
                super.setChanged();
                super.notifyObservers();
                _time = _currentTime;
            }
        }
        _currentTime = endtime;
    }
}
