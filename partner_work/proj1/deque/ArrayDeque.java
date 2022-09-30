package deque;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private T[] items;
    private int nextFirst, nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int size) {
        T[] arr = (T[]) new Object[size];
        int idx = 0;

        for (int i = nextFirst + 1; i < nextFirst + 1 + this.size; i++) {
            arr[idx] = items[i % items.length];
            idx++;
        }
        nextFirst = arr.length - 1;
        nextLast = this.size;

        items = arr;
    }

    @Override
    public void addFirst(T item) {
        if (size+5 > items.length) {
            resize(2 * items.length);
        }
        size++;
        items[nextFirst] = item;
        nextFirst--;
        if (nextFirst < 0) {
            nextFirst += items.length;
        }
    }

    @Override
    public void addLast(T item) {
        if (size+5 > items.length) {
            resize(2 * items.length);
        }
        size++;
        items[nextLast] = item;
        nextLast++;
        if (nextLast > items.length - 1) {
            nextLast -= items.length;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = nextFirst + 1; i < nextFirst + size; i++) {
            int idx = i % items.length;
            System.out.print(items[idx] + " ");
        }
        System.out.println(items[(nextFirst + 1 + size - 1) % items.length]);
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (size < 0.25 * items.length && items.length >= 16) {
            resize(items.length / 2);
        }
        size--;
        nextFirst++;
        if (nextFirst > items.length - 1) {
            nextFirst -= items.length;
        }
        T rm = items[nextFirst];
        items[nextFirst] = null;
        return rm;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size < 0.25 * items.length && items.length >= 16) {
            resize(items.length / 2);
        }
        size--;
        nextLast--;
        if (nextLast < 0) {
            nextLast += items.length;
        }
        T rm = items[nextLast];
        items[nextLast] = null;
        return rm;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("out of range");
        }
        return items[(nextFirst + 1 + index) % items.length];
    }
    @Override
    public boolean equals(Object o) {
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
