package com.tommycondon.ca1;

public class UnionAndFind {

    /*
    Class for union and find methods with static references
     */

    // Integer
    public static int find(int[] array, int id){
        if(array[id]==-1) return -1;

        if(array[id]==id) {
            return id;
        }
        else {
            return find(array,array[id]);
        }
    }

    // Object --> For DisjointSetNodes as integers maybe
    public static DisjointSetNode<?> find(DisjointSetNode<?> node){
        if(node.parent==null) return node;
        else return find(node.parent);
    }

    // Union both sets by making a node reference the root
    public static void union(int[] a, int p, int q){
        a[find(a,q)]=find(a,p);
    }

}
