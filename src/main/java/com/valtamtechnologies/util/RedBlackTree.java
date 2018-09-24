package com.valtamtechnologies.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RedBlackTree<K extends Comparable<K>, V> {
	// Color
    private enum Color {
    	BLACK,
    	RED;
    	
    	public static Color flip(Color color) {
    		return color == BLACK ? RED : BLACK;
    	}
    };
    
    // Node type
    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private Color color;

        public Node(K key, V value, Color color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;
    
    
    public void put(K key, V value) {
        root = put(root, key, value);
        root.color = Color.BLACK;
    }
    
    public V get(K key) {
        return get(root, key);
    }

    public boolean contains(K key) {
        return get(key) != null;
    }
    
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    
    public int height() { 
    	return height(root); 
    }
    
    public Iterable<K> keys() {
    	final Collection<K> keys;
    	if (isEmpty())
    		keys = Collections.emptyList();
    	else {
    		keys = new ArrayList<K>(size());
    		keys(root, keys);
    	}
        return keys;
    }
    
    public Iterable<V> values() {
        final Collection<V> values;
        if (isEmpty())
        	values = Collections.emptyList();
        else {
        	values = new ArrayList<V>(size());
        	values(root, values);
        }
        return values;
    }
    
    public void delete(K key) { 
        if (key == null) 
        	throw new IllegalArgumentException("null key is not allowed");
        
        if (!contains(key)) 
        	return;

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = Color.RED;

        root = delete(root, key);
        
        // Adjust size right after delete
        size--;
        
        if (!isEmpty()) 
        	root.color = Color.BLACK;
    }
    
    public void clear() {
    	root = null;
    	size = 0;
    }
    
    private Node put(Node node, K key, V value) { 
        if (node == null) {
            size++;
            return new Node(key, value, Color.RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) 
        	node.left = put(node.left,  key, value); 
        else if (cmp > 0)
        	node.right = put(node.right, key, value); 
        else
        	node.value = value;

        // fix any right-leaning links
        if (isRed(node.right) && !isRed(node.left))
        	node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
        	node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
        	flipColors(node);

        return node;
    }
    
    // Standard binary search
    private V get(Node node, K key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) 
            	node = node.left;
            else if (cmp > 0) 
            	node = node.right;
            else
            	return node.value;
        }
        return null;
    }
    
    private Node delete(Node node, K key) { 
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = delete(node.left, key);
        }
        else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (key.compareTo(node.key) == 0 && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node x = min(node.right);
                node.key = x.key;
                node.value = x.value;
                node.right = deleteMin(node.right);
            }
            else 
            	node.right = delete(node.right, key);
        }
        return balance(node);
    }


    private boolean isRed(Node node) {
    	// null nodes are BLACK by definition
        return node != null && node.color == Color.RED;
    }

    private Node rotateRight(Node node) {
        Node n = node.left;
        node.left = n.right;
        n.right = node;
        n.color = node.color;
        node.color = Color.RED;
        return n;
    }

    private Node rotateLeft(Node node) {
        Node n = node.right;
        node.right = n.left;
        n.left = node;
        n.color = node.color;
        node.color = Color.RED;
        return n;
    }

    private void flipColors(Node node) {
        node.color = Color.flip(node.color);
        node.left.color = Color.flip(node.left.color);
        node.right.color = Color.flip(node.right.color);
    }
    
    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) { 
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) { 
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    // restore red-black tree invariant
    private Node balance(Node node) {
        if (isRed(node.right))
        	node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) 
        	node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
        	flipColors(node);

        return node;
    }
    
    private int height(Node node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    private void keys(Node node, Collection<K> keys) { 
        if (node == null)
        	return; 
        keys(node.left, keys); 
        keys.add(node.key); 
        keys(node.right, keys); 
    }
    
    private void values(Node node, Collection<V> values) { 
        if (node == null)
        	return; 
        values(node.left, values); 
        values.add(node.value); 
        values(node.right, values); 
    } 

    private Node min(Node node) {
    	// keep going down the left side to find min
        return node.left == null ? node : min(node.left); 
    } 
    
    private Node deleteMin(Node node) { 
        if (node.left == null)
            return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private boolean isBST() {
        return isBST(root, null, null);
    }

    private boolean isBST(Node node, K min, K max) {
    	// By definition (empty tree)
        if (node == null) 
        	return true;
        
        if (min != null && node.key.compareTo(min) <= 0) 
        	return false;
        
        if (max != null && node.key.compareTo(max) >= 0) 
        	return false;
        
        // All nodes to the left are less than this node's key and greater than min and...
        // all nodes to the right are greater than this node's key and less than max
        return isBST(node.left, min, node.key) && isBST(node.right, node.key, max);
    } 

    // Check if all paths from root to leaf have same number of black nodes
    private boolean isBalanced() { 
        int black = 0;     // number of black links on path from root to min
        Node node = root;
        while (node != null) {
            if (!isRed(node)) 
            	black++;
            node = node.left;
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node node, int black) {
        // By definition
    	if (node == null) 
        	return black == 0;
        
        if (!isRed(node)) 
        	black--;
        // Node is balanced if both, left and right subtrees are balanced
        return isBalanced(node.left, black) && isBalanced(node.right, black);
    }
    
    // Used for testing integrity
    public static <K extends Comparable<K>, V> boolean check(RedBlackTree<K, V> tree) {
        if (!tree.isBST())
        	throw new IllegalStateException("Not in symmetric order");
        
        if (!tree.isBalanced())
        	throw new IllegalStateException("Not balanced");
        
        return true;
    }
}
