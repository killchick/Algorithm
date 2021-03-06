/* A linked list is given such that each node contains an additional random pointer which could point to any node in the list or null.
Return a deep copy of the list.

Challenge: Could you solve it with O(1) space?

/* Definition for singly-linked list with a random pointer.
 * class RandomListNode {
 *     int label;
 *     RandomListNode next, random;
 *     RandomListNode(int x) { this.label = x; }
 * }; */

public class Solution {
    
    // 方法1，用HashMap。但是额外空间是n量级的，主要是mapping的这个关系占的空间
    // 答案所占用的空间，是必须的空间，不算是额外的空间
    // 这里的答案是一个deep copy出来的list，它是n空间的，但它一点也不算额外空间
    public RandomListNode copyRandomList(RandomListNode head) {
        if (head == null) {
            return null;
        }
        
        HashMap<RandomListNode,RandomListNode> mappingFromOldToNewNodes = new HashMap<>();
        
        RandomListNode curOldNode = head;
        RandomListNode curNewNode = new RandomListNode(curOldNode.label);
        mappingFromOldToNewNodes.put(curOldNode, curNewNode);
        
        RandomListNode newHead = curNewNode;
        
        // 复制各个nodes，以及各个next关系
        while(curOldNode.next != null) {
            RandomListNode nextOldNode = curOldNode.next;
            RandomListNode nextNewNode = new RandomListNode(nextOldNode.label);
            
            curNewNode.next = nextNewNode;
            mappingFromOldToNewNodes.put(nextOldNode, nextNewNode);
            
            curOldNode = nextOldNode;
            curNewNode = nextNewNode;
        }
           
        // 复制各个random关系
        curOldNode = head;
        while(curOldNode != null) {
            curNewNode = mappingFromOldToNewNodes.get(curOldNode);
            
            RandomListNode curOldRandom = curOldNode.random;
            RandomListNode curNewRandom = mappingFromOldToNewNodes.get(curOldRandom);
            
            curNewNode.random = curNewRandom;
            
            curOldNode = curOldNode.next; // 去到下一个old node。这个顺序是本while循环的纲
        }
        
        return newHead;
    }


    // 方法2: 非常巧妙！！先复制一遍，新旧的逐对串在一起: 1->1`->2->2`->3->3`->4->4`->...
    // 然后 old node 的 next 的 random = old node 的 random 的 next ！！！
    // 最后拆分节点, 一边扫描一边拆成两个链表，这里用到两个dummy node。第一个链表变回  1->2->3 , 然后第二变成 1`->2`->3`
    // 这个方法比前一个的好处在于，其额外空间是 O(1) 量级的
    public RandomListNode copyRandomList(RandomListNode head) {
        if (head == null) {
            return null;
        }
        duplicateAndConcatenate(head);
        copyRandom(head);
        return splitList(head);
    }
    // 把 1->2->3->4->... 变成 1->1`->2->2`->3->3`->4->4`->...
    private void duplicateAndConcatenate(RandomListNode head) {
        while (head != null) {
            RandomListNode newNode = new RandomListNode(head.label);
            // 注意！！！这里新node的random还是指向了老node的random！即指向的也是一个老node！！！
            newNode.random = head.random;
            newNode.next = head.next; // 1'->2
            
            head.next = newNode; // 1->1'
            head = head.next.next; // 1->1'->2
        }
    }
    private void copyRandom(RandomListNode head) {
        while (head != null) {
            // 注意！某些node的random可能是null！
            if (head.next.random != null) {
                // 注意！！！这样，就把new node的random从另一个new node变成其对应的old node了！！！
                head.next.random = head.random.next;
            }
            // 这时，新旧nodes组成的联合list还没有断开
            head = head.next.next;
        }
    }
    // 把 1->1`->2->2`->3->3`->4->4`->... 变成 1->2->3->4->...
    private RandomListNode splitList(RandomListNode head) {
        RandomListNode copiedHead = head.next;
        while (head != null) {
            RandomListNode temp = head.next;
            head.next = temp.next;
            head = head.next;
            
            if (temp.next != null) {
                temp.next = temp.next.next;
            }
        }
        return copiedHead;
    }
    
}
