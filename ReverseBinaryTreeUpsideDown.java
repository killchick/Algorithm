/* Given a binary tree where all the right nodes are either leaf nodes with a sibling 
(a left node that shares the same parent node) or empty, 
flip it upside down and turn it into a tree where the original right nodes turned into left leaf nodes. 
Return the new root.

For example:
Given a binary tree {1,2,3,4,5},
    1
   / \
  2   3
 / \
4   5
return the root of the binary tree [4,5,2,#,#,3,1].
   4
  / \
 5   2
    / \
   3   1  
   
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * } */
 
public class Solution {
    
    /* 方法1：很巧妙的 Recursion，要熟练掌握！上场可以优先用这个方法
    
        1                  4             1 (right child of 2)
       / \                / \           / 
      2   3      =>      5   2    即   2 —— 3 (left child of 2)
     / \                    / \       / 
    4   5                  3   1     4 —— 5                                     */
   
    public TreeNode reverse(TreeNode root) {
        if (root == null || root.left == null) {
          return root;
        }

        TreeNode newRoot = reverse(root.left);

        // things to be done in the current level:
        root.left.left = root.right;
        root.left.right = root;
        root.left = null;
        root.right = null;

        return newRoot;
    }   
    
    
    /* 方法2：很巧妙的 iteration。不容易想到
    
            (parent) null                            (parent) 1                            (parent) 2
                     / \                                     / \                                   / \
              (cur) 1   null (rightSibling)           (cur) 2   3 (rightSibling)                  3   1
                   / \                                     / \                       
      (leftChild) 2   3                       (leftChild) 4   5                     (cur) 4   5 (rightSibling)
                 / \
                4   5
    */
    public TreeNode upsideDownBinaryTree(TreeNode root) {
        TreeNode parent = null;
        TreeNode rightSibling = null;
        TreeNode cur = root;
        TreeNode leftChild = null;
        
        while (cur != null)
        {
            // save left child of cur, since we'll reset cur's left child soon
            leftChild = cur.left; 
            cur.left = rightSibling;
            
            // save right child or cur, since we'll reset cur's right child soon
            rightSibling = cur.right;
            cur.right = parent;
            
            parent = cur;
            cur = leftChild;
        }
        return parent;
    }
    

    // 方法3：我的思路：用一个Stack来做。速度很慢
    /* Every right child will become left child (in inverted order),
    every left child will become root of sub-tree (in inverted order).
    So we can record them into a Stack, and then retrieve them from the Stack.
    Notice: if the n-th right child is null, then the last n-th left child of the new tree will be null. */
    public TreeNode upsideDownBinaryTree(TreeNode root) 
    {
        if (root == null)
            return null;
        
        Stack<Boolean> hasRightChild = new Stack<>();
        
        // 读取所有nodes，按照为将来做铺垫的顺序
        Stack<TreeNode> nodeStack = new Stack<>();
        nodeStack.push(root);
        TreeNode curNode = root;
        while (curNode.left!=null)
        {
            if (curNode.right != null)
            {
                nodeStack.push(curNode.right);
                hasRightChild.push(true);
                // must remove the record of the right child of curNode now,
                // to prevent self-loop when popping the Stack later
                curNode.right = null;
            }
            else
                hasRightChild.push(false);
                
            TreeNode left = curNode.left;
            nodeStack.push(left);
            // must remove the record of the left child of curNode now,
            // to prevent self-loop when popping the Stack later
            curNode.left = null;
            curNode = left;
        }

        // 重新排布所有nodes，构成新的tree
        TreeNode newRoot = nodeStack.pop();
        curNode = newRoot;
        while (!nodeStack.isEmpty())
        {
            if (hasRightChild.pop())
                curNode.left = nodeStack.pop();
            
            curNode.right = nodeStack.pop();
            curNode = curNode.right;
        }
        return newRoot;
    } 
}
