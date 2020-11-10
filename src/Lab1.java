import java.io.*;
import java.util.LinkedList;

public class Lab1 {
    public static void main(String[] args) {
        File input = new File("src/input.txt");
        File output = new File("src/output.txt");

        try {
            FileWriter writer = new FileWriter(output, false);
            FileReader fr = new FileReader(input);
            BufferedReader reader = new BufferedReader(fr);
            LinkedList<DotNode> dotNodes = new LinkedList<>();
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

                if (line.contains("if") || line.contains("while")) {
                    node.setShape("diamond");
                } else if (line.contains("fun") || line.contains("return")) {
                    node.setShape("rec");
                }
                dotNodes.addLast(node);

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
                if (dotNodes.get(i).rank > 0 && !dotNodes.get(i).data.contains("}")
                        && !dotNodes.get(i - 1).data.contains("else")) {
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
                            if (dotNodes.get(j).data.contains("else")) {
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
                            dotNodes.get(getLastDataNodeID(dotNodes, elseId - 1 ))
                                    .addControlChild(dotNodes.get(i));
                            dotNodes.get(getLastDataNodeID(dotNodes, i - 1 ))
                                    .addControlChild(dotNodes.get(i));
                            dotNodes.get(elseId + 1).setBool("False");
                        }

                    }
                }

                if (dotNodes.get(i).data.contains("return")) {                          //trim any paths after return
                    dotNodes.get(i).removeAllChildren();
                }
                if (dotNodes.get(i).rank > 1 && dotNodes.get(i + 1).data.contains("}")) {       //while back-loop
                    int j = dotNodes.get(i).id - 1;
                    while (dotNodes.get(i).rank != dotNodes.get(j).rank + 1) {
                        j--;
                    }
                    if (dotNodes.get(j).data.contains("while")) {
                        dotNodes.get(i).addControlChild(dotNodes.get(j));
                    }
                }
                writer.write(dotNodes.get(i).toString());
            }
            writer.flush();
            DotGraphBuilder graph = new DotGraphBuilder(dotNodes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static public int getLastDataNodeID (LinkedList<DotNode> dotNodes, int id) {
        int res = 0;
        if (id == 0) return res;
        int i = id;
        while (dotNodes.get(i).rank == dotNodes.get(id).rank) {
            if (!dotNodes.get(i).data.contains("}")) {
                return i;
            }
            i--;
        }
        return res;
    }
}
