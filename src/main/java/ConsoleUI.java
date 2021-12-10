
import com.google.gson.stream.JsonToken;

import javax.crypto.spec.PSource;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.SplittableRandom;

public class ConsoleUI {

    static void consoleWrite(Graph<String> graph) {
        System.out.println("{\n" + graph.toString() + "}");

    }

    static void addRib(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input first name vertex: ");
        String firstVertex = scanner.nextLine().strip();
        System.out.println("Input second name vertex: ");
        String secondVertex = scanner.nextLine().strip();
        if (graph.getBalanced().getBalance()) {
            System.out.println("Input weight or length: ");
            Integer length = scanner.nextInt();
            try {
                graph.addRib(firstVertex, secondVertex, length);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                graph.addRib(firstVertex, secondVertex);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void addVertex(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        try {
            graph.addVertex(vertex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteRib(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input first name vertex: ");
        String firstVertex = scanner.nextLine();
        System.out.println("Input second name vertex: ");
        String secondVertex = scanner.nextLine();

        try {
            graph.deleteRib(firstVertex, secondVertex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteVertex(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        try {
            graph.deleteVertex(vertex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    static void startTesting() {
        Scanner scanner = new Scanner(System.in);


        Graph<String> graph = new Graph<>();
        int commandNumber;
        System.out.println("Load From file?(Y/N)");
        String command = scanner.nextLine();
        while (true) {
            if (command.equals("y") || command.equals("Y")) {
                graph = loadFromFile();
                break;
            }
            if (command.equals("n") || command.equals("N")) {
                break;
            } else {
                System.out.println("Try one more time\n");
                command = scanner.nextLine();
            }
        }

        if (command.equals("n") || command.equals("N")) {
            System.out.println("Input: 1 - weighted\n2 - Unweighted");
            int num = scanner.nextInt();

            System.out.println("Input: 1 - Oriented\n 2 - Not oriented");
            int num2 = scanner.nextInt();

            graph = new Graph<>((num2 == 1) ? Orient.oriented : Orient.notOriented,
                    (num == 1) ? Balance.weighted : Balance.notWeighted);
        }
        StringBuilder commandCombo = new StringBuilder("Command: 1 - Show current graph\n" +
                                                       "2 - Add Edge\n" +
                                                       "3 - Add vertex\n" +
                                                       "4 - Delete Edge\n" +
                                                       "5 - Delete vertex\n" +
                                                       "6 - Save as file\n");
        commandCombo.append("7 - get half out power\n" +
                            "8 - get From U and From V\n");
        commandCombo.append("9 - Graph with mirror edge\n");
        commandCombo.append("10 - Check is strong connect\n");
        commandCombo.append("11 - List where all way less then k\n");
        commandCombo.append("12 - Kruskal\n");
        commandCombo.append("13 - isMinWayUAndWLessThenL\n");
        commandCombo.append("14 - allShortestPathsFromU \n");
        commandCombo.append("15 - allShortestPath\n");
        commandCombo.append("0 - End testing");
        while (true) {
            System.out.println(commandCombo);
            System.out.println("Input command number: ");

            commandNumber = scanner.nextInt();

            switch (commandNumber) {
                case (1):
                    consoleWrite(graph);
                    break;
                case (2):
                    addRib(graph);
                    break;
                case (3):
                    addVertex(graph);
                    break;
                case (4):
                    deleteRib(graph);
                    break;
                case (5):
                    deleteVertex(graph);
                    break;
                case (6):
                    saveAsFile(graph);
                    break;
                case (7):
                    getHalfOutPower(graph);
                    break;
                case (8):
                    getFromUAndFromV(graph);
                    break;
                case (9):
                    orientGraphWithMirrorEdge(graph);
                    break;
                case (10):
                    checkStrongConnect(graph);
                    break;
                case (11):
                    allWayLowerThenK(graph);
                    break;
                case(12):
                    Kruskal(graph);
                    break;
                case (13):
                    isMinWayUAndWLessThenL(graph);
                    break;
                case(14):
                    allShortestPathsFromU(graph);
                    break;
                case (15):
                    allShortestPaths(graph);
                    break;
                case (0):
                    return;
                default:
                    break;
            }

        }

    }

    private static void allShortestPaths(Graph<String> graph) {
        HashMap<String, HashMap<String, Integer>> ans = graph.allShortestPath();
        for (String firstKey : ans.keySet())
        {
            System.out.print(firstKey + '=');
            System.out.println(ans.get(firstKey));
            for (String secondKey : ans.get(firstKey).keySet())
            {

            }
        }
    }

    private static void allShortestPathsFromU(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите вершину из которой нужно найти минимальные пути до всех остальных");
        String u = scanner.nextLine();
        System.out.println(graph.allShortestPathsFromU(u));
    }


    private static void isMinWayUAndWLessThenL(Graph<String> graph) {


        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите первую вершину: ");
        String firstVertex = scanner.nextLine();

        System.out.println("Введите вторую вершину: ");
        String secondVertex = scanner.nextLine();

        System.out.println("Введите длину L: ");
        Integer L = scanner.nextInt();


        try{
            System.out.println(graph.isMinWayUAndWLessThenL(firstVertex, secondVertex, L));
        }
        catch (Exception e)
        {
            System.out.println("Введены неверные вершины");
        }

    }

    private static void Kruskal(Graph<String> graph) {
        try {
            System.out.println(graph.kruskal());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void allWayLowerThenK(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("input k = ");
        int k = scanner.nextInt();
        while(k<0)
        {
            System.out.println("Try one more time\n");
            k = scanner.nextInt();
        }

        System.out.println(graph.allWayLowerThenK(k));
    }

    private static void checkStrongConnect(Graph<String> graph) {
        System.out.println(graph.isStrongConnect());
    }

    private static Graph<String> orientGraphWithMirrorEdge(Graph<String> graph) {
        Graph<String> res = graph.orientGraphWithMirrorEdge();
        consoleWrite(res);
        return res;
    }

    private static void getFromUAndFromV(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input first vertex name: ");
        String vertexFirst = scanner.nextLine();
        System.out.println("Input second vertex name");
        String vertexSecond = scanner.nextLine();
        try {
            System.out.println(graph.isFromUAndFromV(vertexFirst, vertexSecond));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getHalfOutPower(Graph<String> graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        try {
            System.out.println(graph.halfPowerOut(vertex));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void createFileNotOrientNotBalance() {
        Graph<String> graph = new Graph<>(Orient.notOriented, Balance.notWeighted);


        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Moscow", "Moscow"); // loop
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            graph.addRib("Moscow", "Saratov");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Samara");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Sochi");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnodar", "Saratov");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnodar");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/NotBalanceNotOrient.bet"));
    }

    static void createFileOrientNotBalance() {
        Graph<String> graph = new Graph<>(Orient.oriented, Balance.notWeighted);


        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Moscow", "Moscow"); // loop
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            graph.addRib("Moscow", "Saratov");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Samara");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Sochi");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnodar", "Saratov");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnodar");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/NotBalanceOrient.bet"));

    }

    static void createFileNotOrientBalance() {
        Graph<String> graph = new Graph<>(Orient.notOriented, Balance.weighted);


        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Moscow", "Moscow", 10); // loop
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            graph.addRib("Moscow", "Saratov", 100);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Samara", 20);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Sochi", 25);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnodar", "Saratov", 56);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnodar", 89);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk", -20);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk", 76);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/BalanceNotOrient.bet"));

    }

    static void createFileOrientBalance() {
        Graph<String> graph = new Graph<>(Orient.oriented, Balance.weighted);
        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Moscow", "Moscow", 10); // loop
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            graph.addRib("Moscow", "Saratov", 100);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Samara", 20);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Saratov", "Sochi", 25);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnodar", "Saratov", 56);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnodar", 89);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk", -20);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk", 76);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/BalanceOrient.bet"));

    }

    static void saveAsFile(Graph<String> graph) {
        try {
            graph.serialize(new File("src/main/resources/Temp.bet"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static Graph<String> loadFromFile() {
        return new Graph<>(new File("src/main/resources/Temp.bet"));
    }

    public static void main(String[] args) {


        startTesting();


    }
}
