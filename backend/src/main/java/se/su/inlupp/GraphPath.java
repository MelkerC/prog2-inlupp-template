package se.su.inlupp;

import java.util.*;

public class GraphPath<T> implements Path<T>{

    private List<Edge<T>> pathEdges =  new LinkedList<>();
    private final List<T> pathNodes = new LinkedList<>();

    private String pathName;
    private int length;

    public GraphPath(LinkedList<T> pathNodes, LinkedList<Edge<T>> pathEdges){
        this.pathNodes.addAll(pathNodes);
        this.pathEdges.addAll(pathEdges);
    }

    @Override
    public Iterator<Edge<T>> iterator(){return pathEdges.iterator();}

    @Override
    public T getStart() {return pathNodes.getFirst();}

    @Override
    public T getEnd() {return pathNodes.getLast();}

    @Override
    public int getTotalWeight() {
        int totalWeight = 0;

        for(Edge<T> edge: pathEdges){
            totalWeight += edge.getWeight();
        }
        return totalWeight;
    }

    public void setLength(int length){
        this.length = length;
    }
    
    public int getLength(){
        return length;
    }

    @Override
    public List<Edge<T>> getEdges() {return pathEdges;}

    @Override
    public List<T> getNodes() {return pathNodes;}

    public void setPathName(String pathName) {this.pathName = pathName;}

    public String getPathName() {return pathName;}

    @Override
    public String toString(){return "Path consists of: " + pathNodes;}

}