package com.tommycondon.ca1;

public class DisjointSet {

    // Integer
    public static int find(int [] array, int id){
        if(array[id]==id) return id;
        else return find(array,array[id]);
    }


    // Object
    public static DisjointSetNode<?> find(DisjointSetNode<?> node){
        if(node.parent==null) return node;
        else return find(node.parent);
    }


    // Union both sets by making a node reference the root
    public static void union(int[] a, int p, int q){
        a[find(a,q)]=p;
    }

}
