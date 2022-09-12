import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class TreeNode {
    public NodeType nodeType;

    public static void main(String[] args) throws Exception {
        //-v -mode cnf /
        Options options = new Options();
        Option inputFilePathOption = new Option("input", "input-file", true,
                "input file path");
        Option verboseOption = new Option("v", "verbose", false, "print verbose output");
        Option modeOption = new Option("mode", true, "mode type");

        options.addOption(modeOption);
        options.addOption(verboseOption);
        options.addOption(inputFilePathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        boolean verbose = cmd.hasOption("verbose");
        String inputFilePath = cmd.getOptionValue("input");
        String mode = cmd.getOptionValue("mode");

        //String inputFilePath = "/Users/asishaddepalli/Desktop/example1.txt";
        //String inputFilePath = "/Users/asishaddepalli/Desktop/example1.txt";
        BNFtoCNFConverter bnFtoCNFConverter =  new BNFtoCNFConverter();
        if(mode.equals("cnf"))
            bnFtoCNFConverter.bnfconverter(inputFilePath, verbose);
        else if (mode.equals("dpll")) {
            System.out.println();
            System.out.println("Unable to complete DPLL");
        }

    }

    public static String parseTree(TreeNode treeNode){
        if(treeNode.nodeType.equals(NodeType.UNARY)){
            return ((UnaryTreeNode) treeNode).operator + parseTree(((UnaryTreeNode) treeNode).target);
        }
        if(treeNode.nodeType.equals(NodeType.BINARY)){
            return parseTree(((BinaryTreeNode) treeNode).left) + " " + ((BinaryTreeNode) treeNode).operator +  " " + parseTree(((BinaryTreeNode) treeNode).right);
        }
        if(treeNode.nodeType.equals(NodeType.ATOM)){
            return ((Atom) treeNode).value;
        }
        return "";
    }
//(!A & (!B | !C)) | !P | Q | W)

    public static List<TreeNode> parseInput(String inputFilePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
        return lines.stream().map(TreeNode::parseExpression)
                .collect(Collectors.toList());
    }



    public static TreeNode parseExpression(String input){
        input = input.replaceAll(" ", "");
        if(input.contains("<=>")){
            return binaryExpressionUtil(input, "<=>");
        }
        else if(input.contains("=>")){
            return binaryExpressionUtil(input, "=>");
        }
        else if(input.contains("|")){
            return binaryExpressionUtil(input, "|");
        }
        else if(input.contains("&")){
            return binaryExpressionUtil(input, "&");
        }
        else if(input.contains("!")){
            String[] split = input.split("!",2);
            TreeNode target = parseExpression(split[1]);
            return new UnaryTreeNode("!", target);
        }
        else {
            return new Atom(input);
        }

    }

    public static TreeNode binaryExpressionUtil(String input, String type){
        String seperator= type;
        if(seperator.equals("|")){
            seperator = "\\|";
        }
        String[] split = input.split(seperator,2);
        TreeNode left = parseExpression(split[0]);
        TreeNode right = parseExpression(split[1]);
        return new BinaryTreeNode(type, left, right);
    }

    public static void print(TreeNode treeNode){
        if(treeNode.nodeType == NodeType.BINARY) {
            print(((BinaryTreeNode) treeNode).left);
            print(((BinaryTreeNode) treeNode).right);
        }
        else if(treeNode.nodeType == NodeType.UNARY){
            System.out.print(((UnaryTreeNode) treeNode).operator);
            print(((UnaryTreeNode) treeNode).target);
            System.out.println(" ");
        }
        else{
            System.out.print(((Atom) treeNode).value);
            System.out.print(" ");
        }
    }

}
