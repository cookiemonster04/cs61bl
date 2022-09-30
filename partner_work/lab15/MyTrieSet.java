import java.util.*;

public class MyTrieSet implements TrieSet61BL{
    private class Node {
        char c;
        boolean isEnd;
        HashMap<Character, Node> children = new HashMap<>();

        public Node(char cc, boolean e) {
            c = cc;
            isEnd = e;
        }

        public boolean hContains(String key, int idx) {
            if (key.length() == idx) {
                if (isEnd) {
                    return true;
                } else {
                    return false;
                }
            }

            if (!this.children.containsKey(key.charAt(idx))) {
                return false;
            }

            Node next = children.get(key.charAt(idx));
            return next.hContains(key, idx + 1);
        }

        public List<String> hPrefix(String key, int idx) {
            if (key.length() == idx) {
                return getRest(key.substring(0, key.length()-1));
            }
            if (!this.children.containsKey(key.charAt(idx))) {
                return new ArrayList<>();
            }
            Node next = children.get(key.charAt(idx));
            return next.hPrefix(key, idx + 1);
        }

        public ArrayList<String> getRest(String curSequence) {
            curSequence += c;
            ArrayList<ArrayList<String>> rets = new ArrayList<>();
            ArrayList<String> largest = new ArrayList<>();
            // Get strings of all children
            for (Map.Entry<Character, Node> e : children.entrySet()) {
                ArrayList<String> ret = e.getValue().getRest(curSequence);
                if (ret.size() > largest.size()) {
                    rets.add(largest);
                    largest = ret;
                }
                else {
                    rets.add(ret);
                }
            }
            // Merge small to large
            for (ArrayList<String> al : rets) {
                largest.addAll(al);
            }
            // add itself if end
            if (isEnd) {
                largest.add(curSequence);
            }
            return largest;
        }
    }

    Node root = new Node(' ', false);

    @Override
    public void clear() {
        root.children.clear();
    }

    @Override
    public boolean contains(String key) {
        return root.hContains(key, 0);
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) {
                curr.children.put(c, new Node(c, false));
            }
            curr = curr.children.get(c);
        }
        curr.isEnd = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        return root.hPrefix(prefix, 0);
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }
}
