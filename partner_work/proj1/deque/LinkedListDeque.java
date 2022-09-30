package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        T item;
        Node prev, next;

        public Node() {
            this.item = null;
            prev = this;
            next = this;
        }

        public Node(T item) {
            this.item = item;
            prev = null;
            next = null;
        }

        public T getItem() {
            return item;
        }

        public void print() {
            if (next == null) {
                System.out.println(item.toString());
            }
            else {
                System.out.printf("%s ", item.toString());
            }
        }

        public T getRecursive(int index) {
            if (index == 0) return item;
            return next.getRecursive(index-1);
        }
    }

    private Node sentinel;
    private int size;
    public LinkedListDeque() {
        size = 0; // only sentinel keeps track of size, other nodes' sizes will remain 0
        sentinel = new Node();
    }

    @Override
    public void addFirst(T item) {
        size++;
        Node newNode = new Node(item), first = sentinel.next;
        sentinel.next = newNode;
        first.prev = newNode;
        newNode.prev = sentinel;
        newNode.next = first;
    }

    @Override
    public void addLast(T item) {
        size++;
        Node newNode = new Node(item), last = sentinel.prev;
        sentinel.prev = newNode;
        last.next = newNode;
        newNode.next = sentinel;
        newNode.prev = last;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (size == 0) {
            return;
        }
        sentinel.next.print();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size--;
        Node first = sentinel.next;
        first.next.prev = sentinel;
        sentinel.next = first.next;
        return first.getItem();
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size--;
        Node last = sentinel.prev;
        last.prev.next = sentinel;
        sentinel.prev = last.prev;
        return last.getItem();
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("out of range");
        }

        Node cur = sentinel.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.getItem();
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("out of range");
        }
        return sentinel.next.getRecursive(index);
    }

    @Override
    public boolean equals(Object o) { // not efficient but whatever
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> ad = (Deque<T>) o;
        if (size != ad.size()) return false;
        for (int i = 0; i < size; i++) {
            if (!this.get(i).equals(ad.get(i))) {
                return false;
            }
        }
        return true;
    }
}
