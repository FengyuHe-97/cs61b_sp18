/**
 * Created by Fengyu He on 2020/5/11.
 */
public interface Deque <T>{
    public void addFirst(T item);
    public void addLast(T item);
    public boolean isEmpty();
    public int size();
    public void printDeque();
    public T removeFirst();
    public T removeLast();
    public T get(int index);
}
