// PROG2 VT2026, Inlämningsuppgift, del 1
// Grupp 070
// Melker Cronmark mecr7998

package se.su.inlupp;

import java.util.*;

public class GraphPath<T> implements Path<T>{

    private final List<Edge<T>> pathEdges =  new LinkedList<>();
    private final List<T> pathNodes = new LinkedList<>();
    private final String algorithm;

    private int totalWeight;

    public GraphPath(LinkedList<T> pathNodes, LinkedList<Edge<T>> pathEdges,  String algortitm){
        this.pathNodes.addAll(pathNodes);
        this.pathEdges.addAll(pathEdges);
        this.algorithm = algortitm;

        for(Edge<T> edge: pathEdges){
            totalWeight += edge.getWeight();
        }
    }

    @Override
    public Iterator<Edge<T>> iterator(){return pathEdges.iterator();}

    @Override
    public T getStart() {return pathNodes.getFirst();}

    @Override
    public T getEnd() {return pathNodes.getLast();}

    @Override
    public int getTotalWeight() {
        return totalWeight;
    }

    @Override
    public List<Edge<T>> getEdges() {return pathEdges;}

    @Override
    public List<T> getNodes() {return pathNodes;}

    public int getAlgorithmIndex(){
        return switch (algorithm) {
            case "BFS" -> 1;
            case "DFS" -> 2;
            default -> 0;
        };
    }

    @Override
    public String toString(){return String.format("%s -> %s, %skm, %s", pathNodes.getFirst(), pathNodes.getLast(), totalWeight, algorithm);}

    public String getPathDescription(){
        String pathString = "";
        for(int index = 0; index < pathNodes.size() - 1; index++){
            pathString += pathNodes.get(index);
            pathString += " --" + pathEdges.get(index).getName();
            pathString += " " + pathEdges.get(index).getWeight() + "km--> ";
        }
        pathString += pathNodes.getLast();
        return pathString;
    }
}