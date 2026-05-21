package se.su.inlupp;

import java.util.*;

public class ListGraph <T> implements Graph<T>{

    private Map<T, Set<Edge<T>>> graph = new HashMap<>();
    private Set<String> cityNames = new HashSet<>();

    public Iterator<T> iterator(){

        return graph.keySet().iterator(); //Vänta tills jag förstår bättre
    }

    @Override
    public void add(T node) {
        graph.putIfAbsent(node, new HashSet<>());
        System.out.println(graph.toString());
    }

    @Override
    public void remove(T node) {
        if(hasNode(node)){
            for(Edge<T> edge: new ArrayList<>(getEdgesFrom(node))){
                disconnect(node, edge.getDestination());
            }
            graph.remove(node);
        }else{
            throw new NoSuchElementException("At least one node is not connected");
        }
    }

    @Override
    public boolean hasNode(T node) {return graph.containsKey(node);}

    @Override
    public void connect(T node1, T node2, String name, int weight) {

        if(!hasNode(node1) || !hasNode(node2)){ throw new NoSuchElementException();}
        if(isNeighbor(node1, node2)){ throw new IllegalStateException();}
        if(weight < 0){throw new IllegalArgumentException();}

        this.add(node1);
        this.add(node2);

        Set<Edge<T>> edgesNode1 = graph.get(node1);
        Set<Edge<T>> edgesNode2 = graph.get(node2);

        edgesNode1.add(new TrainRail<>(node2, name, weight));
        edgesNode2.add(new TrainRail<>(node1, name, weight));
    }

    @Override
    public void disconnect(T node1, T node2) {

        if(!hasNode(node1) || !hasNode(node2)){throw new NoSuchElementException("At least one node is not connected");}
        if(!isNeighbor(node1, node2)){ throw new IllegalStateException();}

        graph.get(node1).remove(getEdgeBetween(node1, node2));
        graph.get(node2).remove(getEdgeBetween(node2, node1));
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        if(hasNode(node1) && hasNode(node2) && getEdgeBetween(node1, node2) != null){
            getEdgeBetween(node1, node2).setWeight(weight);
            getEdgeBetween(node2, node1).setWeight(weight);
        }else{
            throw new NoSuchElementException("At least node or node is not connected");
        }
    }

    @Override
    public Set<T> getNodes() {return graph.keySet();}

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if(!hasNode(node)){throw new NoSuchElementException();}
        return graph.get(node);
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        //if(!isNeighbor(node1, node2)){throw new NoSuchElementException();}
        if(!hasNode(node1) || !hasNode(node2)){throw new NoSuchElementException();}

        for(Edge<T> edge: graph.get(node1)){
            if(edge.getDestination().equals(node2)){
                return edge;
            }
        }
        return null;

    }

    public boolean hasPath(T node1, T node2){
        Set<T> visited = new HashSet<>();

        if(getEdgesFrom(node1) == null || getEdgesFrom(node2) == null){return false;}

        visit(node1, visited);
        return visited.contains(node2);
    }

    private void visit(T current, Set<T> visited){
        visited.add(current);
        if(graph.get(current).isEmpty()){return;}
        for(Edge<T> edge: graph.get(current)){
            T destination = edge.getDestination();
            if(!visited.contains(destination)){
                visit(destination, visited);
            }
        }
    }

    public Set<String> getCityNames(){
        if(graph.isEmpty()){
            return new HashSet<>();
        }else{
            return cityNames;
        }
    }

    public boolean isNeighbor(T node1, T node2){
        for(Edge<T> edge: graph.get(node1)){
            if(edge.getDestination().equals(node2)){
                return true;
            }
        }
        return false;
    }

    public void addCityName(String cityName){cityNames.add(cityName);}

    public void removeCityName(String cityName){cityNames.remove(cityName);}

    public String toString(){return graph.toString();}
}
