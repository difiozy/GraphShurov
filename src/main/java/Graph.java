import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.util.HashMap;

public class Graph<E extends Comparable> implements Serializable {

    private HashMap<E, HashMap<E, Integer>> adjacencyList;
    private Orient oriented = Orient.notOriented;
    private Balance balanced = Balance.weighted;

    Graph() {
        adjacencyList = new HashMap<E, HashMap<E, Integer>>();
        oriented = Orient.notOriented;
        balanced = Balance.weighted;
    }

    Graph(Orient orient, Balance balance) {
        adjacencyList = new HashMap<E, HashMap<E, Integer>>();
        oriented = orient;
        balanced = balance;
    }

    Graph(@NotNull File file) {
        deserialize(file);
    }

    Graph(@NotNull Graph<E> graph) {
        adjacencyList = graph.getCopyAdjacencyList();
        oriented = graph.getOriented();
        balanced = graph.getBalanced();
    }

    Graph(HashMap<E, HashMap<E, Integer>> copy) {
        adjacencyList = (HashMap<E, HashMap<E, Integer>>) copy.clone();
    }

    Graph(HashMap<E, HashMap<E, Integer>> copy, Orient orient) {
        adjacencyList = copy;
        oriented = orient;
    }

    public HashMap<E, HashMap<E, Integer>> getCopyAdjacencyList() {
        return new HashMap<E, HashMap<E, Integer>>(adjacencyList);
    }

    public HashMap<E, HashMap<E, Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public Orient getOriented() {
        return oriented;
    }

    public Balance getBalanced() {
        return balanced;
    }

    public void addVertex(E vertex) throws Exception {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new HashMap<E, Integer>());
        } else {
            throw new Exception("This vertex already have");
        }
    }

    public void addRib(E firstVertex, E secondVertex, Integer length) throws Exception {
        boolean have_first = adjacencyList.containsKey(firstVertex);
        boolean have_second = adjacencyList.containsKey(secondVertex);

        if (!have_first) {
            adjacencyList.put(firstVertex, new HashMap<E, Integer>());
        }
        if (!have_second) {
            adjacencyList.put(secondVertex, new HashMap<E, Integer>());
        }
        String exp = "";
        if (have_first && have_second)
            if (adjacencyList.get(firstVertex).containsKey(secondVertex))
                exp += "The edge already exists. It has been updated | ";
        adjacencyList.get(firstVertex).put(secondVertex, length);

        if (!oriented.getOrient()) {
            adjacencyList.get(secondVertex).put(firstVertex, length);
        }



        if (!have_first)
            exp += "Was added first vertex " + firstVertex.toString() + " | ";
        if (!have_second)
            exp += "Was added second vertex " + secondVertex.toString() + "\n";
        if (!have_first || !have_second) {
            throw new ValueException(exp);
        }
    }

    public void addRib(E firstVertex, E secondVertex) throws Exception {
        addRib(firstVertex, secondVertex, 0);

    }

    public void deleteVertex(E vertex) throws Exception {
        //Samara == vertex
        if (!adjacencyList.containsKey(vertex))
            throw new Exception("Vertex does not exist");
        for (E firstKey : adjacencyList.keySet()) {
            if (adjacencyList.get(firstKey).containsKey(vertex)) {
                adjacencyList.get(firstKey).remove(vertex);
            }
        }
        if (adjacencyList.containsKey(vertex)) {
            adjacencyList.remove(vertex);
        }
    }

    public void deleteRib(E firstVertex, E secondVertex) throws Exception {
        if (adjacencyList.containsKey(firstVertex)) {
            if (adjacencyList.get(firstVertex).containsKey(secondVertex)) {
                adjacencyList.get(firstVertex).remove(secondVertex);
            } else {
                throw new Exception("Edge does not exist");
            }
        } else {
            throw new Exception("Vertex is not exist");
        }
        if (oriented == Orient.notOriented) {
            if (adjacencyList.containsKey(secondVertex)
                    && adjacencyList.get(secondVertex).containsKey(firstVertex)) {
                adjacencyList.get(secondVertex).remove(firstVertex);
            }
        }
    }

    public void serialize(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deserialize(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Graph p = (Graph) ois.readObject();
            this.adjacencyList = p.getAdjacencyList();
            this.oriented = p.getOriented();
            this.balanced = p.getBalanced();
        } catch (IOException ex) {
            this.adjacencyList = new HashMap<E, HashMap<E, Integer>>();
            System.out.println("IOException, was created  empty adjacencyList");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (E firstKey : adjacencyList.keySet()) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(firstKey.toString() + " --- ");

            for (E secondKey : adjacencyList.get(firstKey).keySet()) {
                stringBuilder1.append(secondKey.toString());
                if (balanced == Balance.weighted) {
                    stringBuilder1.append("(" + adjacencyList.get(firstKey).get(secondKey).toString() + ")");
                }
                stringBuilder1.append(", ");
            }
            stringBuilder.append(stringBuilder1.toString() + '\n');
        }

        return stringBuilder.toString();
    }
}

