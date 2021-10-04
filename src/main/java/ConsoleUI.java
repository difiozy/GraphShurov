import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.io.File;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUI {

    static void consoleWrite(Graph graph) {
        System.out.println("{\n" + graph.toString() + "}");

    }

    static void addRib(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input first name vertex: ");
        String firstVertex = scanner.nextLine();
        System.out.println("Input second name vertex: ");
        String secondVertex = scanner.nextLine();
        if (graph.getBalanced().getBalance()) {
            System.out.println("Input weight or length: ");
            Integer length = scanner.nextInt();
            try {
                graph.addRib(firstVertex, secondVertex, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                graph.addRib(firstVertex, secondVertex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void addVertex(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        try {
            graph.addVertex(vertex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteRib(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input first name vertex: ");
        String firstVertex = scanner.nextLine();
        System.out.println("Input second name vertex: ");
        String secondVertex = scanner.nextLine();

        try {
            graph.deleteRib(firstVertex, secondVertex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteVertex(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        try {
            graph.deleteVertex(vertex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static void startTesting() {
        Scanner scanner = new Scanner(System.in);


        Graph<String> graph;
        int commandNumber;

        while (true) {
            System.out.println("Load From file?(Y/N)");
            String command = scanner.nextLine();

            if (command.equals("y") || command.equals("Y")) {
                graph = loadFromFile();
                break;
            } else if (command.equals("n") || command.equals("N")) {
                System.out.println("Input: 1 - weighted\n2 - Unweighted");
                int num = scanner.nextInt();

                System.out.println("Input: 1 - Oriented\n 2 - Not oriented");
                int num2 = scanner.nextInt();
                graph = new Graph<>((num2 == 1) ? Orient.oriented : Orient.notOriented,
                        (num == 1) ? Balance.weighted : Balance.notWeighted);

                break;
            } else {
                System.out.println("Try one more time\n");
            }
        }
        while (true) {

            System.out.println("Command: 1 - Show current graph\n2 - Add Edge\n3 - Add vertex\n4 - Delete Edge\n" +
                    "5 - Delete vertex\n6 - Save as file\n0 - End testing");
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
                case (0):
                    return;
            }

        }
    }

    static void createFileNotOrientNotBalance() {
        Graph<String> graph = new Graph<>(Orient.notOriented, Balance.notWeighted);


        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Moscow", "Moscow"); // loop
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            graph.addRib("Moscow", "Saratov");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Samara");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Sochi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnodar", "Saratov");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnodar");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk");
        } catch (Exception e) {
            e.printStackTrace();
        }

        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/NotBalanceNotOrient.bet"));
    }

    static void createFileOrientNotBalance() {
        Graph<String> graph = new Graph<>(Orient.oriented, Balance.notWeighted);


        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Moscow", "Moscow"); // loop
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            graph.addRib("Moscow", "Saratov");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Samara");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Sochi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnodar", "Saratov");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnodar");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk");
        } catch (Exception e) {
            e.printStackTrace();
        }


        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/NotBalanceOrient.bet"));

    }

    static void createFileNotOrientBalance() {
        Graph<String> graph = new Graph<>(Orient.notOriented, Balance.weighted);


        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Moscow", "Moscow", 10); // loop
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            graph.addRib("Moscow", "Saratov", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Samara", 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Sochi", 25);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnodar", "Saratov", 56);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnodar", 89);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk", -20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk", 76);
        } catch (Exception e) {
            e.printStackTrace();
        }


        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/BalanceNotOrient.bet"));

    }

    static void createFileOrientBalance() {
        Graph<String> graph = new Graph<>(Orient.oriented, Balance.weighted);

        try {
            graph.addVertex("Norilsk"); // isolated
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Moscow", "Moscow", 10); // loop
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            graph.addRib("Moscow", "Saratov", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Samara", 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Saratov", "Sochi", 25);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnodar", "Saratov", 56);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnodar", 89);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Samara", "Krasnoyarsk", -20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.addRib("Krasnoyarsk", "Novosibirsk", 76);
        } catch (Exception e) {
            e.printStackTrace();
        }


        consoleWrite(graph);
        graph.serialize(new File("src/main/resources/BalanceOrient.bet"));

    }

    static void saveAsFile(Graph graph) {
        try {
            graph.serialize(new File("src/main/resources/Temp.bet"));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    static Graph<String> loadFromFile() {
        return new Graph<>(new File("src/main/resources/Temp.bet"));
    }

    public static void main(String[] args) {
        createFileNotOrientNotBalance();
        createFileOrientNotBalance();
        createFileNotOrientBalance();
        createFileOrientBalance();

        startTesting();


    }
}
