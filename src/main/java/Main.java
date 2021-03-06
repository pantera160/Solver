import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        LinkedHashMap<String, ArrayList<Integer>> map = main.loopOne(main.loadInDoc("Input/Cycles_Input.txt"));
        main.writeToFileMap(map);
        main.loopTwo(new File("Output_Cycles_no_doubles.txt"));
        //main.loopThree(new File("Output_Cycles_single_visit.txt"));    //Loop after removing tours
        main.loopThree(new File("Output_Cycles_no_doubles.txt"));        //loop after removing doubles

    }

    /**
     * load in node document from resources folder
     *
     * @param filename
     * @return
     * @throws Exception
     */
    private File loadInDoc(String filename) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(filename).toURI());
    }

    /**
     * write output from map to document
     *
     * @param map
     * @throws IOException
     */
    private void writeToFileMap(LinkedHashMap<String, ArrayList<Integer>> map) throws IOException {

        File file = new File("Output_Cycles_no_doubles.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        map.forEach((k, v) -> {
                    v.forEach(v2 -> {
                                try {
                                    bw.write(v2 + " ");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                    try {
                        bw.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        bw.close();

        System.out.println("Done");

    }

    /**
     * @param list
     * @param name
     * @throws IOException
     */
    private void writeTofileList(ArrayList<String> list, String name) throws IOException {
        File file = new File(name);
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for (String s : list) {
            bw.write(s);
            bw.write("\n");
        }
        bw.close();

        System.out.println("Done");
    }

    /**
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    private LinkedHashMap<String, ArrayList<Integer>> loopOne(File file) throws FileNotFoundException {
        LinkedHashMap<String, ArrayList<Integer>> result = new LinkedHashMap();

        Scanner scanner = new Scanner(file);
        int counter = 0;
        while (scanner.hasNextLine()) {
            counter++;
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (!Objects.equals(split[i], ""))
                    list.add(Integer.parseInt(split[i]));
            }
            Collections.sort(list);
            String sortedline = "";
            for (int s : list) {
                sortedline = sortedline + s;
            }
            result.put(sortedline, list);
        }
        System.out.println("Number of lines in original file: " + counter);
        System.out.println("Number of lines after filtering: " + result.size());
        return result;
    }

    /**
     * @param file
     * @throws IOException
     */
    private void loopTwo(File file) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().trim());
        }
        lines.sort(new CustomCompare().reversed());
        writeTofileList(lines, "Output_Cycles_sorted_length.txt");
        removeExtraTours(lines);
    }

    private void loopThree(File file) throws IOException {
        ArrayList<String> rows = new ArrayList<>();


        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            int[] columns = new int[2200];
            String line = scanner.nextLine();
            String[] split = (line.trim()).split(" ");
            for (String s : split) {
                try {
                    columns[Integer.parseInt(s) - 1] = 1;
                } catch (Exception e) {
                    System.out.println("Error parsing following number: " + s);
                }
            }

            rows.add(Arrays.toString(columns));
        }
        ArrayList<Integer> notVisited = testAllNodes(new File("Output_Cycles_single_visit.txt"));
        for (int i : notVisited) {
            int[] columns = new int[2200];
            columns[i - 1] = 1;
            rows.add(Arrays.toString(columns));
        }
        writeTofileList(rows, "Output_Cycles_3.txt");
    }

    /**
     * @param lines
     * @throws IOException
     */
    private void removeExtraTours(ArrayList<String> lines) throws IOException {
        boolean[] visited = new boolean[2200];
        ArrayList<String> result = new ArrayList<>();

        for (String s : lines) {
            if (s != "") {
                boolean visit = true;
                String[] split = s.split(" ");
                for (int i = 0; i < split.length; i++) {
                    if (!Objects.equals(split[i], ""))
                        if (!visited[Integer.parseInt(split[i]) - 1]) {
                            visited[Integer.parseInt(split[i]) - 1] = true;
                            visit = false;
                        }
                }
                if (!visit) {
                    result.add(s);
                }
            }
        }
        System.out.println("Lines after single visit: " + result.size());
        writeTofileList(result, "Output_Cycles_single_visit.txt");
    }

    /**
     * @param file
     * @throws FileNotFoundException
     */
    private ArrayList<Integer> testAllNodes(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        ArrayList<Integer> notvisited = new ArrayList<>();
        boolean[] visited = new boolean[2200];

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            for (int i = 0; i < split.length; i++) {
                int node = Integer.parseInt(split[i]) - 1;
                visited[node] = true;
            }
        }
        int counter = 0;
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) {
                counter++;
            } else {
                notvisited.add(i + 1);
                System.out.println("Following node is not visited: " + (i + 1));
            }
        }
        System.out.println("Number of visited nodes: " + counter);
        return notvisited;
    }
}
