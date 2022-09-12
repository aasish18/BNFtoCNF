import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BNFtoCNFConverter {

    boolean verbose;

    public TreeNode eliminateIff(TreeNode treeNode) {

        if (treeNode.nodeType.equals(NodeType.BINARY) && ((BinaryTreeNode) treeNode).operator.equals("<=>")) {
            BinaryTreeNode result = new BinaryTreeNode("&",
                    new BinaryTreeNode("=>", ((BinaryTreeNode) treeNode).left, ((BinaryTreeNode) treeNode).right),
                    new BinaryTreeNode("=>", ((BinaryTreeNode) treeNode).right, ((BinaryTreeNode) treeNode).left));
            if(verbose)
                System.out.println("Converting '<=>': " + TreeNode.parseTree(result));
            return result;
        }
        return treeNode;
    }

    public TreeNode eliminateImplies(TreeNode treeNode) {
        if (treeNode.nodeType == NodeType.BINARY && ((BinaryTreeNode) treeNode).operator.equals("=>")) {
            BinaryTreeNode result = new BinaryTreeNode("|",
                    new UnaryTreeNode("!", ((BinaryTreeNode) treeNode).left),
                    ((BinaryTreeNode) treeNode).right);
            if(verbose)
                System.out.println("Converting '=>': " + TreeNode.parseTree(result));
            return result;
        }
        return treeNode;
    }

    public TreeNode eliminateNegation(TreeNode treeNode) {
        if (treeNode.nodeType == NodeType.UNARY && ((UnaryTreeNode) treeNode).operator.equals("!")) {
            if (((UnaryTreeNode) treeNode).target.nodeType == NodeType.BINARY && ((BinaryTreeNode)((UnaryTreeNode) treeNode).target).operator.equals("&")) {
                BinaryTreeNode result = new BinaryTreeNode("|",
                        new UnaryTreeNode("!", ((BinaryTreeNode)((UnaryTreeNode) treeNode).target).left),
                        new UnaryTreeNode("!", ((BinaryTreeNode)((UnaryTreeNode) treeNode).target).right));
                if(verbose)
                    System.out.println(
                        "Demorgan's law application : " + TreeNode.parseTree(result));
                return result;
            } else if(((UnaryTreeNode) treeNode).target.nodeType == NodeType.BINARY && ((BinaryTreeNode)((UnaryTreeNode) treeNode).target).operator.equals("|")){
                BinaryTreeNode resultTreeNode = new BinaryTreeNode("&",
                        new UnaryTreeNode("!", ((BinaryTreeNode)((UnaryTreeNode) treeNode).target).left),
                        new UnaryTreeNode("!", ((BinaryTreeNode)((UnaryTreeNode) treeNode).target).right));
                if(verbose)
                    System.out.println(
                        "Demorgan's law application : " + TreeNode.parseTree(resultTreeNode));
                return resultTreeNode;
            } else if (((UnaryTreeNode) treeNode).target.nodeType == NodeType.UNARY && ((UnaryTreeNode) ((UnaryTreeNode) treeNode).target).operator.equals("!")) {
                if(verbose)
                    System.out.println("Demorgan's law application : " + TreeNode.parseTree(
                        ((UnaryTreeNode) ((UnaryTreeNode) treeNode).target).target));
                return ((UnaryTreeNode) ((UnaryTreeNode) treeNode).target).target;
            }
        }
        return treeNode;
    }

    public TreeNode ApplyDistribution(TreeNode treeNode) {
        if (treeNode.nodeType.equals(NodeType.BINARY) && ((BinaryTreeNode) treeNode).operator.equals("|")) {
            TreeNode left = ((BinaryTreeNode) treeNode).left;
            TreeNode right = ((BinaryTreeNode) treeNode).right;
            if (right.nodeType.equals(NodeType.BINARY) && ((BinaryTreeNode) right).operator.equals("&")) {
                BinaryTreeNode result = new BinaryTreeNode("&",
                        new BinaryTreeNode("|", left, ((BinaryTreeNode) right).left),
                        new BinaryTreeNode("|", left, ((BinaryTreeNode) right).right));
                if(verbose)
                    System.out.println(
                        "Applying Distribution rule: " + TreeNode.parseTree(result));
                return result;
            } else if (left.nodeType.equals(NodeType.BINARY) && ((BinaryTreeNode) left).operator.equals("&")) {
                BinaryTreeNode result = new BinaryTreeNode("&",
                        new BinaryTreeNode("|", right, ((BinaryTreeNode) left).left),
                        new BinaryTreeNode("|", right, ((BinaryTreeNode) left).right));
                if(verbose)
                    System.out.println(
                        "Applying Distribution rule: " + TreeNode.parseTree(result));
                return result;
            }
        }
        return treeNode;
    }

    public TreeNode cnfConverter(TreeNode treeNode) {
        if (treeNode.nodeType.equals(NodeType.BINARY)) {
            treeNode = eliminateIff(treeNode);
            treeNode = eliminateImplies(treeNode);
            treeNode = eliminateNegation(treeNode);
            treeNode = ApplyDistribution(treeNode);
            BinaryTreeNode binaryTreeNode = (BinaryTreeNode) treeNode;
            binaryTreeNode.left = cnfConverter(binaryTreeNode.left);
            binaryTreeNode.right = cnfConverter(binaryTreeNode.right);
            return binaryTreeNode;
        } else if (treeNode.nodeType.equals(NodeType.UNARY)) {
            treeNode = eliminateNegation(treeNode);
            if (treeNode.nodeType.equals(NodeType.UNARY)) {
                UnaryTreeNode unaryTreeNode = (UnaryTreeNode) treeNode;
                unaryTreeNode.target = cnfConverter(unaryTreeNode.target);
                return unaryTreeNode;
            } else {
                treeNode = cnfConverter(treeNode);
                return treeNode;
            }
        }
        return treeNode;
    }

    public List<TreeNode> splitByAnd(TreeNode treeNode){
        List<TreeNode> treeNodes = new ArrayList<>();
        if(checkAnd(treeNode)){
            BinaryTreeNode binaryTreeNode = (BinaryTreeNode) treeNode;
            List<TreeNode> leftSplit = splitByAnd(binaryTreeNode.left);
            List<TreeNode> rightSplit = splitByAnd(binaryTreeNode.right);
            treeNodes.addAll(leftSplit);
            treeNodes.addAll(rightSplit);
        }
        else{
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    public static boolean checkAnd(TreeNode treeNode){
        if(treeNode.nodeType.equals(NodeType.BINARY))
            return ((BinaryTreeNode) treeNode).operator.equals("&");
        return false;
    }

    public void bnfconverter(String inputFilePath, boolean verbose) throws IOException {
        this.verbose = verbose;
        List<TreeNode> treeNodes = TreeNode.parseInput(inputFilePath);
        List<TreeNode> output = new ArrayList<>();
        for(TreeNode e : treeNodes){
            while(!TreeNode.parseTree(e).equals(TreeNode.parseTree(cnfConverter(e))))
                e = cnfConverter(e);
            List<TreeNode> exs =splitByAnd(e);
            output.addAll(exs);
        }
        output.stream().forEach(node-> System.out.println(TreeNode.parseTree(node).replace("|","")));
    }
}