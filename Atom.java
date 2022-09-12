public class Atom extends TreeNode {
    public String value;

    public Atom(String value) {
        this.value = value;
        this.nodeType = NodeType.ATOM;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
