import java.util.LinkedList;
import java.util.Queue;


public class Dictionary {
  public static Node root;

  /**
  * Constructor
  */
  public Dictionary() {
    this.root = null;
  }

  /**
  * Insering words to the binary search tree.
  *@param word input word
  */
  public void insert(String word) {
    Node n = new Node(word);
    if (this.root == null) {
      root = n;
      //System.out.println(root.getE());
    } else {
      Node current = root;
      Node parent = null;
      while (true) {
        parent = current;
        if (word.compareTo(current.getE())<0) {
          current = current.getLeft();
          if (current==null) {
            parent.setLeft(n);
            return;
          }
        } else if ((word.compareTo(current.getE())>0) || (word.compareTo(current.getE())==0) ) {
          current = current.getRight();
          if (current == null) {
            parent.setRight(n);
            return;
          }
        }
      }
    }
  }

  /**
  * Searching for the word in tree.
  * @param word input word
  * @return returns the word if it is found
  */
  public String search(String word) {
    Node n = root;
    while (!(n==null)) {
      if (word.compareTo(n.getE())==0) {
        return n.getE();
      } else if (word.compareTo(n.getE())<0) {
        n = n.getLeft();
      } else {
        n = n.getRight();
      }
    }
    return null;
  }

  /**
  * Removes the word from the binary tree
  * @param word Word to remove
  */
  public boolean delete(String word) {
    Node parent = root;
    Node current = root;
    boolean isLeftChild = false;
    while (!(word.compareTo(current.getE())==0)) {
      parent = current;
      if (word.compareTo(current.getE())<0) {
        isLeftChild = true;
        current = current.getLeft();
      } else {
        isLeftChild = false;
        current = current.getRight();
      }
      if (current == null) {
        return false;
      }
    }

    if(current.getLeft()==null && current.getRight()==null){
      if(current==root){
        root = null;
      }
      if(isLeftChild ==true){
        parent.setLeft(null);
      }else{
        parent.setRight(null);
      }
    }  else if (current.getRight()==null){
      if(current==root){
        root = current.getLeft();
      }else if(isLeftChild){
        parent.setLeft(current.getLeft());
      }else{
        parent.setRight(current.getLeft());
      }
    }
    else if(current.getLeft()==null){
      if(current==root){
        root = current.getRight();
      }else if(isLeftChild){
        parent.setLeft(current.getRight());
      }else{
        parent.setRight(current.getRight());
      }
    } else if(current.getLeft()!=null && current.getRight()!=null){
      Node successor	 =  getSuccessor(current);
      if(current==root){
        root = successor;
      }else if(isLeftChild){
        parent.setLeft(successor);
      }else{
        parent.setRight(successor);
      }
      successor.setLeft(current.getLeft());
    }
    return true;
  }

  public Node getSuccessor(Node delNode){
    Node successsor =null;
    Node successsorParent =null;
    Node current = delNode.getRight();
    while(current!=null){
      successsorParent = successsor;
      successsor = current;
      current = current.getLeft();
    }

    if(successsor!=delNode.getRight()){
      successsorParent.setLeft(successsor.getRight());
      successsor.setRight(delNode.getRight());
    }
    return successsor;
  }

  /**
  * Returns the root of the tree
  */
  public Node getRoot() {
    return this.root;
  }

  /**
  *Returns the max depth of the tree
  * @param root Root of the tree
  * @return max depth
  */
  public int maxDepth(Node root) {
    if (root == null) {
      return 0;
    }
    int left = maxDepth(root.getLeft());
    int right = maxDepth(root.getRight());
    return Math.max(left, right) + 1;
  }

  /**
  * Calculates number of nodes per tree level (depth)
  * @param node Node (root is being passed first)
  * @param counts array that holds number of nodes per lvl
  * @param lvl depth of tree
  */
  public static void nodesPerDepth(Node node, int[] counts, int lvl) {
    if (node==null) {
      return;
    }
    counts[lvl]++;
    nodesPerDepth(node.getLeft(),counts,lvl+1);
    nodesPerDepth(node.getRight(),counts,lvl+1);
  }

  /**
  * Finding most right element (in this case alphabetically first word)
  * @param root Root of the tree
  * @return word
  */
  public String findRightElement(Node root) {
    if(root == null) {
      return null;
    }
    while(root.getRight() != null)
    root = root.getRight();
    return root.getE();
  }

  /**
  * Finding most left element (in this case alphabetically last word)
  * @param root Root of the tree
  * @return word
  */
  public String findLeftElement(Node root) {
    if(root == null) {
      return null;
    }
    while(root.getLeft() != null)
    root = root.getLeft();
    return root.getE();
  }

  //Inner class node with all setters and getters
  class Node {
    private String e;
    private Node left;
    private Node right;


    public Node(String e) {
      this.e = e;
      this.right = null;
      this.left = null;
    }

    public String getE() {
      return this.e;
    }

    public void setLeft(Node n) {
      this.left = n;
    }

    public void setRight(Node n) {
      this.right = n;
    }

    public Node getLeft() {
      return this.left;
    }

    public Node getRight() {
      return this.right;
    }
  }
}
