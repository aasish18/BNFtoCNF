

public class BinaryTreeNode extends TreeNode {

    public String operator;
    public TreeNode left;
    public TreeNode right;

    public BinaryTreeNode(String operator, TreeNode left, TreeNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.nodeType = NodeType.BINARY;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
