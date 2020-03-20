package com.example.lifecycle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Consumer;

public class NotifyTest {




    public static void main(String[] args) {
        //链表反向
          NotifyTest te= new NotifyTest();
//        Node head = new Node(3);
//        Node tea = head;
//        for(int i =0;i<10;i++){
//            tea.next = new Node(i);
//            tea = tea.next;
//        }
//        Node head1 = head;
//        while(head1!=null){
//            System.out.println(head1.data);
//            head1 = head1.next;
//        }
//        Node d = te.revert(head);
//        while(d!=null){
//            System.out.println(d.data);
//           d= d.next;
//        }

        //二分法查找 数组为已排序好的
//        int[] array = new int[]{1,2,3,5,9,65,95,643,2255};
//        System.out.println(te.binarySerach(array,5));

        Tree.levelTravel(Tree.generyTree());
    }

    /**
     * 二分查找
     * @param array
     * @param key
     * @return
     */
    public  int binarySerach(@NotNull int[] array, Integer key){
        int left = 0;
        int right = array.length -1 ;
        while(left<=right){
            int mid = (right+left)/2;
            if(array[mid] == key){
                return mid;
            }
            if(array[mid]<key){
                left = mid + 1;
            }else{
                right = mid-1;
            }
        }
        return -1;
    }

    //链表
    //反转
    public Node revert(Node a){
        Node cur = a ;
        Node pre = null;
        while(cur!=null){
            Node tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }
    static class Node{
        public Integer data;
        public Node next;
        public Node(int data){
            this.data = data;
        }
    }

    //二叉树
    public static class Tree{
        public Tree left;
        public Tree right;
        public int root;
        public Tree(int root){
            this.root =root;
        }

        public static Tree generyTree(){
            Tree root = new Tree(5);
            Tree tem = root;
            for(int i =0;i<3;i++){
                tem.left = new Tree(i);
                tem.right = new Tree(i+2);
                tem = tem.right;
            }
            return root;
        }
        //递归前序
        public static void preOrder(Tree tree){
            if(tree!=null){
                System.out.println(tree.root);
                preOrder(tree.left);
                preOrder(tree.right);
            }
        }
        //递归后序

        public static void postOrder(Tree tree){
            if(tree!=null){
                postOrder(tree.left);
                postOrder(tree.right);
                System.out.print(tree.root);
            }
        }

        //递归中序
        public static void inOrder(Tree tree){
            if(tree!=null){
                inOrder(tree.left);
                System.out.print(tree.root);
                inOrder(tree.right);

            }
        }


        //层序遍历
        public static void levelTravel(Tree tree){
            Queue<Tree> queue = new LinkedList<Tree>();
            queue.add(tree);
            while(!queue.isEmpty()){
                Tree tree1 = queue.poll();
                System.out.println(tree1.root+" ");
                if(tree1.left!=null) queue.offer(tree1.left);
                if(tree1.right!=null) queue.offer(tree1.right);
            }


        }

        // todo 非递归后续
        public static void iterativePostOrder(Tree tree){
            Tree tr = tree;
            Stack<Tree> stack = new Stack<>();
            while(stack.size()>0 || tr!=null){

                while(tr!=null){

                    stack.push(tr);
                    tr = tr.left;
                }

                if(stack.size()>0){
                    tr = stack.peek();
                    if(tr.right!=null){
                        //有右孩子
                        stack.push(tr.right);
                        tr = tr.right;
                    }else{
                        tr = stack.pop();
                        System.out.print(" "+tr.root);
                        tr=null;
                    }
                }
            }
        }

        //非递归前序
        public static void iterativePreOrder(Tree tree){
            Tree tr = tree;
            Stack<Tree> stack = new Stack<>();
            while(tr!=null|| stack.size()>0){
                while(tr!=null){
                    System.out.print(" "+tr.root);
                    stack.push(tr);
                    tr = tr.left;
                }
                if(stack.size()>0){
                    tr = stack.pop();
                    tr = tr.right;
                }
            }

        }

        //非递归中序
        public static void iterativeInOrder(Tree tree){
            Stack<Tree>  stack = new Stack<>();
            Tree tr = tree;
            while(tr!=null || stack.size()>0){

                while(tr!=null){
                    stack.push(tr);
                    tr = tr.left;
                }
                if(stack.size()>0) {
                    tr = stack.pop();
                    System.out.print(" " + tr.root);
                    tr = tr.right;
                }
            }
        }
    }
}
