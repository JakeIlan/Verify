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
                    case "}": {
                        if (tmp.length > 1) {
                            if (tmp[1].equals("else")) {
                                node.setType("else");
                            }
                        } else {
                            node.setType("muted");
                        }
                        break;
                    }
                }

                dotNodes.add(node);

                if (line.contains("{")) {
                    rank++;
                }
                if (line.contains("}")) {
                    rank--;
                }

                line = reader.readLine();
                i++;
            }

            for (i = 0; i < dotNodes.size(); i++) {
                if (dotNodes.get(i).rank > 0 && !dotNodes.get(i).text.contains("}")
                        && !dotNodes.get(i - 1).text.contains("else")) {
                    if (dotNodes.get(i).rank == dotNodes.get(i - 1).rank) {             // same rank
                        dotNodes.get(i - 1).addControlChild(dotNodes.get(i));
                    } else if (dotNodes.get(i).rank > dotNodes.get(i - 1).rank) {       // rank is up => cycle body or true branch
                        dotNodes.get(i - 1).addControlChild(dotNodes.get(i));
                        dotNodes.get(i).setBool("True");
                    } else {                                                            // rank is down => after cycle or false branch
                        int j = dotNodes.get(i).id - 1;
                        boolean isElse = false;
                        int elseId = 0;
                        while (dotNodes.get(i).rank != dotNodes.get(j).rank) {
                            if (dotNodes.get(j).text.contains("else")) {
                                isElse = true;
                                elseId = j;
                            }
                            j--;
                        }
                        if (!isElse) {
                            dotNodes.get(j).addControlChild(dotNodes.get(i));
                            dotNodes.get(i).setBool("False");
                        } else {
                            dotNodes.get(j).addControlChild(dotNodes.get(elseId + 1));
                            dotNodes.get(getLastDataNodeID(dotNodes, elseId - 1))
                                    .addControlChild(dotNodes.get(i));
                            dotNodes.get(getLastDataNodeID(dotNodes, i - 1))
                                    .addControlChild(dotNodes.get(i));
                            dotNodes.get(elseId + 1).setBool("False");
                        }

                    }
                }
                if (dotNodes.get(i).type.equals("return")) {                          //trim any paths after return
                    dotNodes.get(i).removeAllChildren();
                }
                if (dotNodes.get(i).rank > 1 && dotNodes.get(i + 1).text.contains("}")) {       //while back-loop
                    int j = dotNodes.get(i).id - 1;
                    while (dotNodes.get(i).rank != dotNodes.get(j).rank + 1) {
                        j--;
                    }
                    if (dotNodes.get(j).type.equals("while")) {
                        dotNodes.get(i).addControlChild(dotNodes.get(j));
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
                        break;
                    }
                    case "while": {
//                        String s = dotNodes.get(i).text.replaceAll("(^while\\s\\(|\\)\\s\\{)", "");    // I speak regex
                        String s = dotNodes.get(i).text.substring(7, dotNodes.get(i).text.length() - 3);
                        dotNodes.get(i).setText(s);
                        break;
                    }
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


                writer.write(dotNodes.get(i).toString());
            }

            writer.flush();
            DotGraphBuilder graph = new DotGraphBuilder(dotNodes);
            graph.buildDataPath();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static public int getLastDataNodeID(ArrayList<DotNode> dotNodes, int id) {
        int res = 0;
        if (id == 0) return res;
        int i = id;
        while (dotNodes.get(i).rank == dotNodes.get(id).rank) {
            if (!dotNodes.get(i).text.contains("}")) {
                return i;
            }
            i--;
        }
        return res;
    }
}
