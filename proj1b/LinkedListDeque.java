public class LinkedListDeque<T> implements Deque<T> {

    /**
     * ItemNode is a node of double-ended queue.
     * @param <T>
     */
    private class ItemNode {
        private ItemNode prev;
        private T item;
        private ItemNode next;

        ItemNode(ItemNode p, T i, ItemNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private  ItemNode sentinel; // textbook 2.2
    private  int size;

    public LinkedListDeque() {
        sentinel = new ItemNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

//    public LinkedListDeque(T item){
//        sentinel= new ItemNode(null, null,null);
//        sentinel.next=new ItemNode(sentinel, item, sentinel);
//        sentinel.prev=sentinel.next;
//        size=1;
//    }

    @Override
    public void addFirst(T item) {
        sentinel.next = new ItemNode(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size++;
    }

    @Override
    public  void addLast(T item) {
        sentinel.prev = new  ItemNode(sentinel.prev, item, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        ItemNode toPrint = sentinel;
        for (int i = 0; i < size; i++) {
            System.out.print(toPrint.item + " ");
            toPrint = toPrint.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            size--;
        } else {
            return null;
        }
        T toRemove = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return toRemove;
    }

    @Override
    public T removeLast() {
        if (!isEmpty()) {
            size--;
        } else {
            return null;
        }
        T toRemove = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return toRemove;
    }

    @Override
    public T get(int index) {
        if (size <= index) {
            return null;
        }
        ItemNode toReturn = sentinel.next;
        int i = 0;
        while (i < index) {
            i++;
            toReturn = toReturn.next;
        }
        return toReturn.item;
    }
    /**recursion with a help method*/
    private T getRecursive(int index, ItemNode tmp) {
        if (index == 0) {
            return tmp.item;
        } else {
            return getRecursive(index - 1, tmp.next);
        }
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }


}
