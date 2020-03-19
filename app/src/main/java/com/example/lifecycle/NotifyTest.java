package com.example.lifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.function.Consumer;

public class NotifyTest {

    static class Node{
        public Integer data;
        public Node next;
        public Node(int data){
            this.data = data;
        }
    }



    public static void main(String[] args) {
        NotifyTest te= new NotifyTest();
        Node head = new Node(3);
        Node tea = head;
        for(int i =0;i<10;i++){
            tea.next = new Node(i);
            tea = tea.next;
        }
        Node head1 = head;
        while(head1!=null){
            System.out.println(head1.data);
            head1 = head1.next;
        }
        Node d = te.revert(head);
        while(d!=null){
            System.out.println(d.data);
           d= d.next;
        }

    }

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
}
