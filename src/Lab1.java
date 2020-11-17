import java.io.*;
import java.util.ArrayList;


public class Lab1 {
    public static void main(String[] args) {
        File input = new File("src/input.txt");
        File output = new File("src/output.txt");

        try {
            FileWriter writer = new FileWriter(output, false);
            FileReader fr = new FileReader(input);
            BufferedReader reader = new BufferedReader(fr);
            ArrayList<DotNode> dotNodes = new ArrayList<>();
            String line = reader.readLine();
            int i = 0;
            int rank = 0;
            while (line != null) {

                if (line.contains("}")) {
                    rank--;
                }
                line = line.replaceFirst("}", "");
                line = line.trim();
                if (line.isEmpty()) {
                    line = reader.readLine();
                    continue;
                }
                DotNode node = new DotNode(line, i, rank);

                String[] tmp = line.split(" ");
                switch (tmp[0]) {
                    case "if": {
                        node.setShape("diamond");
                        node.setType("if");
                        break;
                    }
                    case "while": {
                        node.setShape("diamond");
                        node.setType("while");
                        break;
                    }
                    case "var": {
                        node.setType("variable");
                        break;
                    }
                    case "fun": {
                        node.setType("function");
                        break;
                    }
                    case "return": {
                        node.setType("return");
                        break;
                    }
                    case "else": {
                        node.setType("else");
                        break;
                    }
                }

                if (tmp.length > 1 && tmp[1].equals("=")) {
                    node.setType("setter");
                }

                dotNodes.add(node);

                if (line.contains("{")) {
                    rank++;
                }

                line = reader.readLine();
                i++;
            }

            for (i = 0; i < dotNodes.size(); i++) {
//                if (!dotNodes.get(i - 1).text.contains("else")) {
//                    if (dotNodes.get(i).rank == dotNodes.get(i - 1).rank) {             // same rank
//                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(i));
//                    } else if (dotNodes.get(i).rank > dotNodes.get(i - 1).rank) {       // rank is up => cycle body or true branch
//                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(i));
//                        if (dotNodes.get(i - 1).rank > 0) dotNodes.get(i).setBool("True");
//                    } else {                                                            // rank is down => after cycle or false branch
//                        int j = dotNodes.get(i).id - 1;
//                        boolean isElse = false;
//                        int elseId = 0;
//                        while (dotNodes.get(i).rank != dotNodes.get(j).rank) {
//                            if (dotNodes.get(j).text.contains("else")) {
//                                isElse = true;
//                                elseId = j;
//                            }
//                            j--;
//                        }
//                        if (!isElse) {
//                            dotNodes.get(j).setLeftControlChild(dotNodes.get(i));
//                            dotNodes.get(i).setBool("False");
//                            dotNodes.get(getLastDataNodeID(dotNodes, i - 1))
//                                    .setRightControlChild(dotNodes.get(i));
//                        } else {
//                            dotNodes.get(j).setLeftControlChild(dotNodes.get(elseId + 1));
//                            dotNodes.get(getLastDataNodeID(dotNodes, elseId - 1))
//                                    .setRightControlChild(dotNodes.get(i));
//
//                            dotNodes.get(getLastDataNodeID(dotNodes, i - 1))
//                                    .setRightControlChild(dotNodes.get(i));
//                            dotNodes.get(elseId + 1).setBool("False");
//                        }
//
//                    }
//                }
                if (dotNodes.get(i).rank == dotNodes.get(i + 1).rank) {
                    dotNodes.get(i).setRightControlChild(dotNodes.get(i + 1));
                }
                if (dotNodes.get(i).rank < dotNodes.get(i + 1).rank) {
                    if (dotNodes.get(i).type.equals("function")) {
                        dotNodes.get(i).setRightControlChild(dotNodes.get(i + 1));
                    }
                    if (dotNodes.get(i).type.matches("while|if")) {
                        dotNodes.get(i).setRightControlChild(dotNodes.get(i + 1));
                        dotNodes.get(i + 1).setBool("True");
                    }
                    if (dotNodes.get(i).type.equals("else")) {
                        int j = i - 1;
                        while (!(dotNodes.get(j).rank == dotNodes.get(i).rank && dotNodes.get(j).type.equals("if")) && j > 0) {
                            j--;
                        }
                        dotNodes.get(j).setLeftControlChild(dotNodes.get(i + 1));
                        dotNodes.get(i + 1).setBool("False");

                    }
                }
                if (dotNodes.get(i).rank > dotNodes.get(i + 1).rank) {
                    if (dotNodes.get(i + 1).type.equals("else")) {
                        int j = i + 2;
                        while (dotNodes.get(j).rank > dotNodes.get(i + 1).rank) {
                            j++;
                        }
                        dotNodes.get(i).setRightControlChild(dotNodes.get(j));
                    } else {
                        int j = i - 1;
                        while (!(dotNodes.get(j).rank == dotNodes.get(i + 1).rank && dotNodes.get(j).type.matches("if|while")) && j > 0) {
                            if (dotNodes.get(j).type.equals("if") && dotNodes.get(j).leftControlChild == null) {
                                dotNodes.get(j).setLeftControlChild(dotNodes.get(i + 1));
                                dotNodes.get(i + 1).setBool("False");
                            }
                            j--;
                        }
                        if (dotNodes.get(j).leftControlChild == null) {
                            dotNodes.get(j).setLeftControlChild(dotNodes.get(i + 1));
                            dotNodes.get(i + 1).setBool("False");
                        }
                        dotNodes.get(i).setRightControlChild(dotNodes.get(i + 1));

                    }

                }

                if (dotNodes.get(i).type.equals("return")) {                          //trim any paths after return
                    dotNodes.get(i).removeAllChildren();
                }
                if (dotNodes.get(i).rank > dotNodes.get(i + 1).rank) {       //while back-loop
                    int j = dotNodes.get(i).id - 1;
                    while (dotNodes.get(i).rank != dotNodes.get(j).rank + 1) {
                        j--;
                    }
                    if (dotNodes.get(j).type.equals("while")) {
                        dotNodes.get(i).setRightControlChild(dotNodes.get(j));
                    }
                }


                switch (dotNodes.get(i).type) {
                    case "variable": {
                        String s = dotNodes.get(i).text.replaceAll("^var ", "");
                        dotNodes.get(i).setText(s);
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                dotNodes.get(i).data.add(v);
                            }
                        }

                        break;
                    }
                    case "if": {
                        String s = dotNodes.get(i).text.substring(4, dotNodes.get(i).text.length() - 3);
                        dotNodes.get(i).setText(s);
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                dotNodes.get(i).data.add(v);
                            }
                        }
                        break;
                    }
                    case "while": {
                        String s = dotNodes.get(i).text.replaceAll("(^while\\s\\(|\\)\\s\\{)", "");    // I speak regex
//                        String s = dotNodes.get(i).text.substring(7, dotNodes.get(i).text.length() - 3);
                        dotNodes.get(i).setText(s);
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                dotNodes.get(i).data.add(v);
                            }
                        }
                        break;
                    }
                    case "return":
                    case "setter":
                    case "default": {
                        String s = dotNodes.get(i).text;
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                dotNodes.get(i).data.add(v);
                            }
                        }
                        break;
                    }
                }
            }

            ArrayList<DotNode> subTree = new ArrayList<>();
            for (i = 0; i < dotNodes.size() - 1; i++) {
                if (dotNodes.get(i).type.matches("variable|setter|default")) {
//                    String var = dotNodes.get(i).data.get(0);
                    subTree.clear();
//                    getControlBranch(dotNodes.get(i), subTree);
//                    for (DotNode n : subTree) {
//                        if (n.data.get(0).equals(var) && n.type.equals("setter")) {
//                            break;
//                        } else if (n.data.contains(var) && !var.equals("print")) {
//                            if (!dotNodes.get(i).dataChildren.contains(n)) dotNodes.get(i).addDataChild(n);
//                        }
//                    }
                    traceData(dotNodes.get(i), subTree, dotNodes.get(i));
                }
            }

            for (DotNode n : dotNodes) {
                writer.write(n.toString());
//                if (n.rightControlChild != null) writer.write("Right: " + n.rightControlChild.toString());
//                if (n.leftControlChild != null) writer.write("Left: " + n.leftControlChild.toString());
            }

            writer.flush();
            DotGraphBuilder graph = new DotGraphBuilder(dotNodes);
            graph.buildDataPath();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static private void traceData(DotNode node, ArrayList<DotNode> storage, DotNode base) {
        if (node.leftControlChild != null && !storage.contains(node.leftControlChild)
                && !node.leftControlChild.type.matches("muted|else")) {
            storage.add(node.leftControlChild);
            if (!(node.leftControlChild.type.equals("setter") && node.leftControlChild.data.get(0).equals(base.data.get(0)))) {
                if (node.leftControlChild.data.size() > 0) {
                    if (node.leftControlChild.data.contains(base.data.get(0)) && !base.data.get(0).equals("print")) {
                        base.addDataChild(node.leftControlChild);
                    }
                }
                traceData(node.leftControlChild, storage, base);
            }
        }
        if (node.rightControlChild != null && !storage.contains(node.rightControlChild)
                && !node.rightControlChild.type.matches("muted|else")) {
            storage.add(node.rightControlChild);
            if (!(node.rightControlChild.type.equals("setter") && node.rightControlChild.data.get(0).equals(base.data.get(0)))) {
                if (node.rightControlChild.data.size() > 0) {
                    if (node.rightControlChild.data.contains(base.data.get(0)) && !base.data.get(0).equals("print")) {
                        base.addDataChild(node.rightControlChild);
                    }
                }
                traceData(node.rightControlChild, storage, base);
            }
        }
    }

}
