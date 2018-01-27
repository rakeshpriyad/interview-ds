package com.test.interview.linklist;

import java.util.HashMap;
import java.util.Map;

/**
 * http://www.geeksforgeeks.org/implement-lru-cache/
 * Test cases:
 * MAX_SIZE greater than 1
 * Delete when empty
 * Delete when full
 * Enter data more than max
 * Delete till cache is empty
 * 
 * 
 * The popular method to implement an LRU cache is to use a bounded Queue. 

The key to for implementing an LRU is to put any recently used data at the head of the queue.

Before each insert, we check whether the queue is full. If the queue is full, we delete its last element, and insert the new node at the beginning of the queue.  

If the queue is not full, we just add the data at the beginning of the queue. 

When we want to delete a node or update a node, we need to quickly find the position of the node in the queue. So a HashTable or HashMap should be used to support the fast look up operation. In this case, the time complexity of the get operation is O(1). 

Since we also need to efficiently remove a node in the middle of the queue, so a double linked list is needed.

There are two cases we need to remove a node in the middle of the queue:

The client specify that a node need to be removed. 
A node is updated, it needs to be removed and insert at the head of the queue. 
By using a double linked list, once we use the HashMap to located the position of the node to be removed, we can remove the node from the queue in O(1) time. 

When we need update the cache for a key, we first use the HashMap to located the corresponding node, update the value, then we remove the node from the queue and put that node at the beginning of the Double Linked list.

The following diagram demonstrates how to use HashMap and Double Linked list to build an efficient LRU cache. 
 */
public class LRUCache {

    private Node head;
    private Node tail;
    private Map<Integer,Node> map = new HashMap<Integer,Node>();
    private int MAX_SIZE = 5;
    private int size = 0;
    public LRUCache(int size){
        MAX_SIZE = size;
    }
    
    public void used(int data){
        if(containsInCache(data)){
            Node node = map.get(data);
            if(node != head){
                deleteFromCache(data);
                node.next = head;
                head.before = node;
                head = node;
                map.put(data, node);
            }
        }else{
            addIntoCache(data);
        }
    }
    
    public void addIntoCache(int data){
        size++;
        if(head == null){
            head = Node.newNode(data);
            tail = head;
            return;
        }
        if(size > MAX_SIZE){
            tail = tail.before;
            Node next = tail.next;
            tail.next = null;
            next.before = null;
            map.remove(next.data);
        }
        Node newNode = Node.newNode(data);
        newNode.next = head;
        if(head != null){
            head.before = newNode;
        }
        head = newNode;
        map.put(data, newNode);
        return;
    }
    
    public void printCache(){
        Node temp = head;
        while(temp != null){
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }
    
    public boolean containsInCache(int data)
    {
        return map.containsKey(data);
    }
    
    public void deleteFromCache(int data){
        Node node = map.get(data);
        if(node == null){
            return;
        }
        map.remove(data);
        if(size == 1){
            head = null;
            tail = null;
        }
        else if(node == head){
            head = head.next;
            if(head != null){
                head.before = null;
            }
            node.next = null;
        }else if(node == tail){
            tail = tail.before;
            tail.next = null;
        }else{
            Node before = node.before;
            Node next = node.next;
            before.next = next;
            next.before = before;
        }
    }

    public static void main(String args[]){
        LRUCache lruCache = new LRUCache(5);
        lruCache.used(4);
        
        lruCache.used(5);
        lruCache.printCache();
        lruCache.used(6);
        lruCache.printCache();
        lruCache.used(5);
        lruCache.printCache();
        lruCache.used(9);
        lruCache.printCache();
        lruCache.used(10);
        lruCache.printCache();
        lruCache.used(11);
        lruCache.printCache();
        lruCache.used(16);
        lruCache.printCache();
        lruCache.used(10);
        lruCache.printCache();
        lruCache.deleteFromCache(10);
        lruCache.printCache();
        lruCache.deleteFromCache(9);
        lruCache.printCache();
    }
}
