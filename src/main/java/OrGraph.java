import java.lang.instrument.IllegalClassFormatException;

public class OrGraph<E extends Comparable> extends Graph<E> {
    public OrGraph(Balance balance) {
        super(Orient.oriented, balance);
    }


    //Вернуть полустепень исхода данной вершины орграфа.
    public int halfPowerOut(E vertex) throws IllegalClassFormatException {
        if (oriented != Orient.notOriented) {
            throw new IllegalClassFormatException("Граф не ориентированный!");
        }

        int count = 0;
        if (adjacencyList.containsKey(vertex))
        {
            return adjacencyList.get(vertex).size();
        }
        else
        {
            throw new IllegalArgumentException("Вершины не существует");
        }
    }

    //Определить, существует ли вершина, в которую есть дуга как из вершины u,
    // так и из вершины v. Вывести такую вершину.
    public boolean isFromUAndFromV(E U, E V)
    {
        if (adjacencyList.containsKey(U) && adjacencyList.containsKey(V))
        {
            for (E first : adjacencyList.get(U).keySet())
            {
                if (adjacencyList.get(V).containsKey(first))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            throw new IllegalArgumentException("Vertex does not exist");
        }
    }



}
