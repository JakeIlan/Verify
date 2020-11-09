import java.io.*;
import java.util.ArrayList;
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
                    node.setType("diamond");
                } else if (line.contains("fun") || line.contains("return")) {
                    node.setType("rec");
                }
                dotNodes.addLast(node);
                if (rank > 0 && !node.data.equals("}")) {
                    if (node.rank == dotNodes.get(i - 1).rank) {
                        dotNodes.get(i - 1).addControlChild(node);
                    } else if (node.rank > dotNodes.get(i - 1).rank) {
                        dotNodes.get(i - 1).addControlChild(node);
                        node.setBool("True");
                    } else {
                        int j = node.id - 1;
                        while (node.rank != dotNodes.get(j).rank) {
                            j--;
                        }
                        if (!node.data.equals(dotNodes.get(i - 1).data)) {
                            dotNodes.get(j).addControlChild(node);
                            node.setBool("False");
                        }
                    }

                }
                if (line.contains("{")) {
                    rank++;
                }
                if (line.contains("}")) {
                    rank--;
                }

                line = reader.readLine();
                i++;
            }
            for (DotNode n : dotNodes) {
                if (n.data.contains("return")) {
                    n.removeAllChildren();
                }
                if (n.rank > 1 && dotNodes.get(dotNodes.indexOf(n) + 1).data.contains("}")) {
                    int j = n.id - 1;
                    while (n.rank != dotNodes.get(j).rank + 1) {
                        j--;
                    }
                    if (dotNodes.get(j).data.contains("while")) {
                        n.addControlChild(dotNodes.get(j));
                    }
                }
                writer.write(n.toString());
            }
            writer.flush();
            DotGraphBuilder graph = new DotGraphBuilder(dotNodes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
