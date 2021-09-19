import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.util.HashMap;

public class Graph<E extends Comparable> implements Serializable {

    private HashMap<E, HashMap<E, Integer>> adjacencyList;
    private Orient oriented = Orient.notOriented;
    private Balance balanced = Balance.weighted;

    Graph() {
        adjacencyList = new HashMap<E, HashMap<E, Integer>>();
    }

    Graph(@NotNull File file) {
        deserialize(file);
    }

    Graph(@NotNull Graph<E> graph) {
        adjacencyList = graph.getCopyAdjacencyList();
        oriented = graph.getOriented();
    }

    Graph(HashMap<E, HashMap<E, Integer>> copy) {
        adjacencyList = copy;
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

    public void setOriented(Orient orient) {
        oriented = orient;
    }

    public Balance getBalanced() {
        return balanced;
    }

    public void setBalanced(Balance balance) {
        this.balanced = balance;
    }

    public void addVertex(E vertex) throws Exception {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new HashMap<E, Integer>());
        } else {
            throw new Exception("This vertex already have");
        }
    }

    public void addRib(E firstVertex, E secondVertex, Integer length) {
        if (!adjacencyList.containsKey(firstVertex)) {
            adjacencyList.put(firstVertex, new HashMap<E, Integer>());
        }
        if (!adjacencyList.containsKey(secondVertex)) {
            adjacencyList.put(secondVertex, new HashMap<E, Integer>());
        }
        adjacencyList.get(firstVertex).put(secondVertex, length);
        if (!oriented.getOrient()) {
            adjacencyList.get(secondVertex).put(firstVertex, length);
        }

    }

    public void addRib(E firstVertex, E secondVertex) {
        if (!adjacencyList.containsKey(firstVertex)) {
            adjacencyList.put(firstVertex, new HashMap<E, Integer>());
        }
        if (!adjacencyList.containsKey(secondVertex)) {
            adjacencyList.put(secondVertex, new HashMap<E, Integer>());
        }
        adjacencyList.get(firstVertex).put(secondVertex, 0);
        if (!oriented.getOrient()) {
            adjacencyList.get(secondVertex).put(firstVertex, 0);
        }

    }

    public void deleteVertex(E vertex) {
        //Samara == vertex
        for (E firstKey : adjacencyList.keySet()) {
            if (adjacencyList.get(firstKey).containsKey(vertex)) {
                adjacencyList.get(firstKey).remove(vertex);
            }
        }
        if (adjacencyList.containsKey(vertex)) {
            adjacencyList.remove(vertex);
        }
    }

    public void deleteRib(E firstVertex, E secondVertex) {
        if (adjacencyList.containsKey(firstVertex)) {
            if (adjacencyList.get(firstVertex).containsKey(secondVertex)) {
                adjacencyList.get(firstVertex).remove(secondVertex);
            }
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
        } catch (IOException ex) {
            this.adjacencyList = new HashMap<E, HashMap<E, Integer>>();
            System.out.println("IOException, was created  empty adjacencyList");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}

