package com.valtamtechnologies.util.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.valtamtechnologies.util.RedBlackTree;


public class RedBlackTreeTest {
	
	@Test
	public void putTest() {
		String abcs[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		
		RedBlackTree<String, Integer> tree = new RedBlackTree<String, Integer>();
		for (int i = 0; i < abcs.length; i++) 
        	tree.put(abcs[i], i);
		
		System.out.println("size = " + tree.size());
        System.out.println();

        System.out.println("keys:");
        for (String s : tree.keys()) 
        	System.out.println(s + " " + tree.get(s)); 
        
        Assert.assertEquals(abcs.length, tree.size());
        Assert.assertTrue(RedBlackTree.check(tree));
	}
	
	@Test
	public void deleteTest() {
		String abcs[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		
		RedBlackTree<String, Integer> tree = new RedBlackTree<String, Integer>();
		for (int i = 0; i < abcs.length; i++) 
        	tree.put(abcs[i], i);
		
		System.out.println("size = " + tree.size());
        System.out.println();

        System.out.println("keys:");
        for (String s : tree.keys()) 
        	System.out.println(s + " " + tree.get(s)); 
        System.out.println();
                
        List<String> toDelete = Arrays.asList(abcs);
        for (String k : toDelete)
        	tree.delete(k);
        
        System.out.println("size after delete = " + tree.size());
        System.out.println();
        
        System.out.println("keys after delete:");
        for (String s : tree.keys()) {
        	if (toDelete.contains(s))
        		System.out.println(s + " was not deleted!!!");
        	System.out.println(s + " " + tree.get(s));
        }
        
        Assert.assertEquals(abcs.length - toDelete.size(), tree.size());
        Assert.assertTrue(RedBlackTree.check(tree));
	}
}
