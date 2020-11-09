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
                if (line.contains("{")) {
                    rank++;
                }
                if (line.contains("}")) {
                    rank--;
                }
                if (line.contains("if")) {
                    node.setType("diamond");
                } else if (line.contains("fun") || line.contains("return")) {
                    node.setType("rec");
                }
                dotNodes.addLast(node);
                if (rank > 1 || !dotNodes.isEmpty()) {
                    if (node.rank >= dotNodes.get(i).rank) {
                        if (!node.data.equals(dotNodes.get(i).data)) {
                            dotNodes.get(i).addControlChild(node);
                        }
                    } else {
                        int j = node.id;
                        while (node.rank != dotNodes.get(j).rank) {
                            j--;
                        }
                        dotNodes.get(j).addControlChild(node);
                    }
                }

//                writer.write(line + "\n");
                line = reader.readLine();
                i++;
            }
            for (DotNode n : dotNodes) {
                writer.write(n.toString() + n.printChildren());
            }
            writer.flush();
            DotGraphBuilder graph = new DotGraphBuilder(dotNodes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
