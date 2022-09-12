

public class UnaryTreeNode extends TreeNode {

    //  public String type;
    public String operator;
    public TreeNode target;

    public UnaryTreeNode(String operator, TreeNode target) {
        this.operator = operator;
        this.target = target;
        this.nodeType = NodeType.UNARY;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public TreeNode getTarget() {
        return target;
    }

    public void setTarget(TreeNode target) {
        this.target = target;
    }
}
