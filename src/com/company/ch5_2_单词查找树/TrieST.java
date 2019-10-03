package com.company.ch5_2_单词查找树;


import java.util.LinkedList;
import java.util.Queue;

public class TrieST<Value> implements StringST<Value> {
    private static final int R = 256;
    private Node root;

    @Override
    public int size() {
        return size(root);
    }

    /**
     * 延时递归实现
     * <p>
     * size的实现方式:
     * 即时实现：用一个实例变量保存键的数量。
     * 更加即时的实现：用结点的实例变量保存子单词查找树中键的数量，在递归的put()和delete()方法调用之后更新它们。
     * 延时递归实现：如上页框注“单词查找树的延时递归方法size()”所示。它会遍历单词查找树中的所有结点并记录非空值结点的总数。
     */
    private int size(Node x) {
        if (x == null) return 0;
        int cnt = 0;
        if (x.val != null) cnt++;
        for (char c = 0; c < R; c++)
            cnt += size(x.next[c]);
        return cnt;
    }

    @Override
    public void put(String key, Value val) {
        put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value get(String key) {
        Node node = get(root, key, 0);
        if (node == null) return null;
        return (Value) node.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    @Override
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    @Override
    public Iterable<String> keysWithPrefix(String s) {
        Queue<String> queue = new LinkedList<>();
        collect(get(root, s, 0), s, queue);//找到前缀终止的节点，然后开始搜集
        return queue;
    }

    private void collect(Node x, String pre, Queue<String> q) {
        if (x == null) return;
        if (x.val != null) q.add(pre);
        for (char c = 0; c < R; c++)
            collect(x.next[c], pre + c, q);
    }

    @Override
    public Iterable<String> keysThatMatch(String s) {
        Queue<String> queue = new LinkedList<>();
        collect(root, "", s, queue);
        return queue;
    }

    /**
     * 模式匹配
     *
     * @param pat 模式，仅支持.符号
     */
    private void collect(Node x, String pre, String pat, Queue<String> q) {
        int d = pre.length();
        if (x == null) return;
        if (d == pat.length()) {
            if (x.val != null) q.add(pre);
            return;
        }
        char next = pat.charAt(d);
        for (char c = 0; c < R; c++)
            if (next == '.' || c == next)
                collect(x.next[c], pre + c, pat, q);
    }


    @Override
    public void delete(String key) {

    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String longestPrefixOf(String s) {
        return null;
    }


    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }
}
