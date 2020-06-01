package synthesizer;

/**
 * Created by Fengyu He on 5/25/2020
 * @param <T>
 */

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;
    public int capacity() {
        return capacity;
    }

    public int fillCount() {
        return fillCount;
    }
}
