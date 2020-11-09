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
            while (line != null) {
                line = line.replaceAll("[{}]", "");
                line = line.trim();
                if (line.isEmpty()) {
                    line = reader.readLine();
                    continue;
                }
                DotNode data = new DotNode(line, i);
                if (line.contains("if")) {
                    data.setType("diamond");
                } else if (line.contains("fun") || line.contains("return")) {
                    data.setType("rec");
                }

                if (!dotNodes.isEmpty()) {
                    dotNodes.getLast().addControlChild(data);
                }
                dotNodes.addLast(data);
                writer.write(line + "\n");
                line = reader.readLine();
                i++;
            }
            writer.flush();
            DotGraphBuilder graph = new DotGraphBuilder(dotNodes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
