/** Array based list.
 *  @author Fengyu He
 */

public class ArrayDeque<T>{
    private T [] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /** Creates an empty Array. */
    public ArrayDeque(){
         items=(T []) new Object[8];
         size=0;
         nextFirst=1;
         nextLast=2;
    }

    /** Return true if deque is full, false otherwise. */
    private boolean isFull() {
        return size == items.length;
    }
    /** Return true if usage factor less than 25%, false otherwise. */
    private boolean isSparse(){
        return items.length >= 16 && size < (items.length / 4);
    }

    /** Add one circularly*/
    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    /** Minus one circularly */
    private int minusOne(int index) {
        return (index - 1 + items.length) % items.length;
    }

    private void resize(int capacity) {
        T[] newDeque = (T[]) new Object[capacity];
        int oldIndex = plusOne(nextFirst);
        for (int newIndex = 0; newIndex < size; newIndex++) {
            newDeque[newIndex] = items[oldIndex];
            oldIndex = plusOne(oldIndex);
        }
        items = newDeque;
        nextFirst = capacity - 1;
        nextLast = size;
    }


    /** Inserts X into the front of the list. */
    public void addFirst(T item){
        if(isFull()){
            resize(size*2);
        }
        items[nextFirst]=item;
        nextFirst=minusOne(nextFirst);
        size++;
    }

    /** Inserts X into the back of the list. */
    public void addLast(T item) {
        if (isFull()){
            resize(size*2);
        }
        items[nextLast]=item;
        nextFirst=plusOne(nextLast);
        size++;
    }

    /**Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty(){
        return size==0;
    }


    /**Prints the items in the deque from first to last, separated by a space.*/
    public void printDeque(){
        int toPrint= plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[toPrint] + " ");
            System.out.println();
            toPrint = plusOne(toPrint);
        }

    }
    /**Removes and returns the item at the front of the deque.
     *  If no such item exists, returns null.*/
    public T removeFirst(){
        if (size == 0) {
            return null;
        }
        nextFirst=plusOne(nextFirst);
        T toRemove=items[nextFirst];
   //     items[nextFirst] = null;
        size--;
        if (isSparse()) {
            resize(items.length/2);
        }
        return toRemove;
    }

    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    public T removeLast(){
        if (size == 0) {
            return null;
        }
        nextLast=minusOne(nextLast);
        T toRemove=items[nextLast];
    //    items[nextLast] = null;
        size--;
        if (isSparse()) {
            resize(items.length/2);
        }
        return toRemove;
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }




    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int start = plusOne(nextFirst);
        return items[(start + index) % items.length];
    }
}