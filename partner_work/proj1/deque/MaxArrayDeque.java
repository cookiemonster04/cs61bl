package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }

    public T max() {
        int size = size();
        if (size == 0) {
            return null;
        }
        T maximum = get(0);
        for (int i = 1; i < size; i++) {
            T item = get(i);
            if (cmp.compare(maximum, item) < 0) {
                maximum = item;
            }
        }
        return maximum;
    }

    public T max(Comparator<T> c) {
        int size = size();
        if (size == 0) {
            return null;
        }
        T maximum = get(0);
        for (int i = 1; i < size; i++) {
            T item = get(i);
            if (c.compare(maximum, item) < 0) {
                maximum = item;
            }
        }
        return maximum;
    }
}
