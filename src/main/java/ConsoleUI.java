import java.io.File;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Scanner;

public class ConsoleUI {

    static void consoleWrite(Graph graph) {
        HashMap<String, HashMap<String, Integer>> list = graph.getAdjacencyList();
        String format = list.toString();
        format = format.replace("}, ","}\n");
        System.out.println(format);
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
            graph.addRib(firstVertex, secondVertex, length);
        } else {
            graph.addRib(firstVertex, secondVertex);
        }
    }

    static void addVertex(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        try {
            graph.addVertex(vertex);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    static void deleteRib(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input first name vertex: ");
        String firstVertex = scanner.nextLine();
        System.out.println("Input second name vertex: ");
        String secondVertex = scanner.nextLine();

        graph.deleteRib(firstVertex, secondVertex);
    }

    static void deleteVertex(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input vertex name: ");
        String vertex = scanner.nextLine();
        graph.deleteVertex(vertex);

    }

    static void setOrient(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input number: 1 - Orient\n2 - Not orient");
        int number = scanner.nextInt();
        if (number == 1) {
            graph.setOriented(Orient.oriented);
        } else if (number == 2) {
            graph.setOriented(Orient.notOriented);
        } else {
            System.out.println("Incorrect input");
        }
    }

    static void setBalance(Graph graph) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input number: 1 - Weighted\n2 - Not weighted");
        int number = scanner.nextInt();
        if (number == 1) {
            graph.setBalanced(Balance.weighted);
        } else if (number == 2) {
            graph.setBalanced(Balance.notWeighted);
        } else {
            System.out.println("Incorrect input");
        }
    }

    static void startTesting() {
        Graph<String> graph = new Graph<>();
        Scanner scanner = new Scanner(System.in);
        int commandNumber;
        while (true) {

            System.out.println("Command: 1 - Show current graph\n2 - Add rib\n3 - Add vertex\n4 - Delete rib\n" +
                    "5 - Delete vertex\n6 - Set balance factor\n7 - Set orient factor\n0 - End testing");
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
                    setBalance(graph);
                    break;
                case (7):
                    setOrient(graph);
                    break;
                case (0):
                    return;
            }

        }
    }

    static void createFileNotOrientNotBalance() {
        Graph<String> graph = new Graph<>();
        graph.setBalanced(Balance.notWeighted);
        graph.setOriented(Orient.notOriented);

        try {
            graph.addVertex("Norilsk"); // isolated
            graph.addRib("Moscow", "Moscow"); // loop

            graph.addRib("Moscow", "Saratov");
            graph.addRib("Saratov", "Samara");
            graph.addRib("Saratov", "Sochi");
            graph.addRib("Krasnodar", "Saratov");
            graph.addRib("Samara", "Krasnodar");
            graph.addRib("Samara", "Krasnoyarsk");
            graph.addRib("Krasnoyarsk", "Novosibirsk");
            consoleWrite(graph);
            graph.serialize(new File("src/main/resources/NotBalanceNotOrient.bet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createFileOrientNotBalance() {
        Graph<String> graph = new Graph<>();
        graph.setBalanced(Balance.notWeighted);
        graph.setOriented(Orient.oriented);

        try {
            graph.addVertex("Norilsk"); // isolated
            graph.addRib("Moscow", "Moscow"); // loop

            graph.addRib("Moscow", "Saratov");
            graph.addRib("Saratov", "Samara");
            graph.addRib("Saratov", "Sochi");
            graph.addRib("Krasnodar", "Saratov");
            graph.addRib("Samara", "Krasnodar");
            graph.addRib("Samara", "Krasnoyarsk");
            graph.addRib("Krasnoyarsk", "Novosibirsk");
            consoleWrite(graph);
            graph.serialize(new File("src/main/resources/NotBalanceOrient.bet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createFileNotOrientBalance() {
        Graph<String> graph = new Graph<>();
        graph.setBalanced(Balance.weighted);
        graph.setOriented(Orient.notOriented);

        try {
            graph.addVertex("Norilsk"); // isolated
            graph.addRib("Moscow", "Moscow", 10); // loop

            graph.addRib("Moscow", "Saratov", 100);
            graph.addRib("Saratov", "Samara", 20);
            graph.addRib("Saratov", "Sochi", 25);
            graph.addRib("Krasnodar", "Saratov", 56);
            graph.addRib("Samara", "Krasnodar", 89);
            graph.addRib("Samara", "Krasnoyarsk", -20);
            graph.addRib("Krasnoyarsk", "Novosibirsk", 76);
            consoleWrite(graph);
            graph.serialize(new File("src/main/resources/BalanceNotOrient.bet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createFileOrientBalance() {
        Graph<String> graph = new Graph<>();
        graph.setBalanced(Balance.weighted);
        graph.setOriented(Orient.oriented);

        try {
            graph.addVertex("Norilsk"); // isolated
            graph.addRib("Moscow", "Moscow", 10); // loop

            graph.addRib("Moscow", "Saratov", 100);
            graph.addRib("Saratov", "Samara", 20);
            graph.addRib("Saratov", "Sochi", 25);
            graph.addRib("Krasnodar", "Saratov", 56);
            graph.addRib("Samara", "Krasnodar", 89);
            graph.addRib("Samara", "Krasnoyarsk", -20);
            graph.addRib("Krasnoyarsk", "Novosibirsk", 76);
            consoleWrite(graph);
            graph.serialize(new File("src/main/resources/BalanceOrient.bet"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        createFileNotOrientNotBalance();
        createFileOrientNotBalance();
        createFileNotOrientBalance();
        createFileOrientBalance();


    }
}
