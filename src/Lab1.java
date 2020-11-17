import java.io.*;
import java.util.ArrayList;


public class Lab1 {
    public static void main(String[] args) {
        File input = new File("src/input2.txt");
        File output = new File("src/output.txt");
        //TODO: 'FOR' CYCLE
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

            for (i = 1; i < dotNodes.size(); i++) {
                if (dotNodes.get(i - 1).rank == dotNodes.get(i).rank) {
                    dotNodes.get(i - 1).setRightControlChild(dotNodes.get(i));
                }
                if (dotNodes.get(i - 1).rank < dotNodes.get(i).rank) {
                    if (dotNodes.get(i - 1).type.equals("function")) {
                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(i));
                    }
                    if (dotNodes.get(i - 1).type.matches("while|if")) {
                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(i));
                        dotNodes.get(i).setBool("True");
                    }
                    if (dotNodes.get(i - 1).type.equals("else")) {
                        int j = i - 2;
                        while (!(dotNodes.get(j).rank == dotNodes.get(i - 1).rank && dotNodes.get(j).type.equals("if")) && j > 0) {
                            j--;
                        }
                        dotNodes.get(j).setLeftControlChild(dotNodes.get(i));
                        dotNodes.get(i).setBool("False");

                    }
                }
                if (dotNodes.get(i - 1).rank > dotNodes.get(i).rank) {
                    if (dotNodes.get(i).type.equals("else")) {
                        int j = i + 1;
                        while (dotNodes.get(j).rank > dotNodes.get(i).rank) {
                            j++;
                        }
                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(j));
                    } else {
                        int j = i - 2;
                        while (!(dotNodes.get(j).rank == dotNodes.get(i).rank && dotNodes.get(j).type.matches("if|while")) && j > 0) {
                            if (dotNodes.get(j).type.equals("if") && dotNodes.get(j).leftControlChild == null) {
                                dotNodes.get(j).setLeftControlChild(dotNodes.get(i));
                                dotNodes.get(i).setBool("False");
                            }
                            j--;
                        }
                        if (dotNodes.get(j).leftControlChild == null) {
                            dotNodes.get(j).setLeftControlChild(dotNodes.get(i));
                            dotNodes.get(i).setBool("False");
                        }
                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(i));

                    }

                }

                if (dotNodes.get(i - 1).rank > dotNodes.get(i).rank) {       //while back-loop
                    int j = i - 2;
                    while (dotNodes.get(i - 1).rank != dotNodes.get(j).rank + 1) {
                        j--;
                    }
                    if (dotNodes.get(j).type.equals("while")) {
                        dotNodes.get(i - 1).setRightControlChild(dotNodes.get(j));
                    }
                }


            }

            for (DotNode node : dotNodes) {
                // Parsing data into the node and rearranging it's text
                switch (node.type) {
                    case "variable": {
                        String s = node.text.replaceAll("^var ", "");
                        node.setText(s);
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                node.data.add(v);
                            }
                        }

                        break;
                    }
                    case "if": {
                        String s = node.text.substring(4, node.text.length() - 3);
                        node.setText(s);
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                node.data.add(v);
                            }
                        }
                        break;
                    }
                    case "function": {
                        String s = node.text.replaceAll("^fun\\s|:\\s[A-Za-z]+\\s\\{","");
                        node.setText(s);
                        s = s.replaceAll("^[A-Za-z]+\\(|\\)$", "");
                        String[] tmp = s.split(":\\s[A-Za-z]+|,|\\s");
                        for (String v : tmp) {
                            if (!v.isEmpty()) {
                                node.data.add(v);
                            }
                        }

                        break;
                    }
                    case "while": {
                        String s = node.text.replaceAll("(^while\\s\\(|\\)\\s\\{)", "");    // I speak regex
//                        String s = dotNodes.get(i).text.substring(7, dotNodes.get(i).text.length() - 3);
                        node.setText(s);
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                node.data.add(v);
                            }
                        }
                        break;
                    }
                    case "return":
                    case "setter":
                    case "default": {
                        String s = node.text;
                        String[] tmp = s.split("[\\s+\\-=*/()]+");
                        for (String v : tmp) {
                            if (!v.isEmpty() && !v.matches("[0-9]+")) {
                                node.data.add(v);
                            }
                        }
                        break;
                    }
                }
            }

            for (DotNode node : dotNodes) {
                if (node.type.equals("return")) node.removeAllChildren();
            }

            ArrayList<DotNode> subTree = new ArrayList<>();
            for (i = 0; i < dotNodes.size() - 1; i++) {
                if (dotNodes.get(i).type.matches("variable|setter|default")) {
                    subTree.clear();
                    traceData(dotNodes.get(i), subTree, dotNodes.get(i), dotNodes.get(i).data.get(0));
                } else if (dotNodes.get(i).type.equals("function")) {
                    for (String var : dotNodes.get(i).data) {
                        subTree.clear();
                        traceData(dotNodes.get(i), subTree, dotNodes.get(i), var);
                    }
                }
            }

            for (DotNode n : dotNodes) {
                writer.write(n.toString() + n.printData());
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

    static private void traceData(DotNode node, ArrayList<DotNode> storage, DotNode base, String var) {
        if (node.leftControlChild != null && !storage.contains(node.leftControlChild)
                && !node.leftControlChild.type.matches("muted|else")) {
            storage.add(node.leftControlChild);
            if (!(node.leftControlChild.type.equals("setter") && node.leftControlChild.data.get(0).equals(var))) {
                if (node.leftControlChild.data.size() > 0) {
                    if (node.leftControlChild.data.contains(var) && !var.equals("print")) {
                        base.addDataChild(node.leftControlChild);
                    }
                }
                traceData(node.leftControlChild, storage, base, var);
            }
        }
        if (node.rightControlChild != null && !storage.contains(node.rightControlChild)
                && !node.rightControlChild.type.matches("muted|else")) {
            storage.add(node.rightControlChild);
            if (!(node.rightControlChild.type.equals("setter") && node.rightControlChild.data.get(0).equals(var))) {
                if (node.rightControlChild.data.size() > 0) {
                    if (node.rightControlChild.data.contains(var) && !var.equals("print")) {
                        base.addDataChild(node.rightControlChild);
                    }
                }
                traceData(node.rightControlChild, storage, base, var);
            }
        }
    }

}
