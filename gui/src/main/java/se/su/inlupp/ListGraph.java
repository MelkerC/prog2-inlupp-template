package se.su.inlupp;

import java.util.*;

public class ListGraph <T> implements Graph<T>{

    private final Map<T, Set<Edge<T>>> graph = new HashMap<>();

    public Iterator<T> iterator(){

        return null; //Vänta tills jag förstår bättre
    }

    @Override
    public final void add(T node) {
    graph.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void remove(T node) { //kolla på denna ifall alla edges behöver tar bort
        if(hasNode(node)){
            for(Edge<T> edge: graph.get(node)){
                disconnect(node, edge.getDestination());
            }
            graph.remove(node);
        }else{
            throw new NoSuchElementException("At least node or node is not connected");
        }
    }

    @Override
    public boolean hasNode(T node) {
        return graph.containsKey(node);
    }

    @Override
    public void connect(T node1, T node2, String name, int weight, int time) {
        this.add(node1);
        this.add(node2);

        Set<Edge<T>> edgesNode1 = graph.get(node1);

        edgesNode1.add(new TrainRail<>(node2, name, weight, time));
    }

    @Override
    public void disconnect(T node1, T node2) {
        if(hasNode(node1) && hasNode(node2) && getEdgeBetween(node1, node2) != null){
            graph.get(node1).remove(getEdgeBetween(node1, node2));
        }else{
            throw new NoSuchElementException("At least node or node is not connected");
        }
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int distance) {
        if(hasNode(node1) && hasNode(node2) && getEdgeBetween(node1, node2) != null){
            getEdgeBetween(node1, node2).setWeight(distance);
        }else{
            throw new NoSuchElementException("At least node or node is not connected");
        }
    }

    @Override
    public Set<T> getNodes() {
        return graph.keySet();
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        return graph.get(node);
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        for(Edge<T> edge: graph.get(node1)){
            if(edge.getDestination().equals(node2)){
                return edge;
            }
        }
        return null;
    }

    public String toString(){
        return graph.toString();
    }
}
