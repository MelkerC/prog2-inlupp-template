package se.su.inlupp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GraphEdge<T> implements Edge<T>{

    private T destination;
    private String name;
    private int weight;

    public GraphEdge(T destination, String name, int weight){
        this.destination = destination;
        this.name = name;
        this.weight = weight;
    }
    @Override
    public T getDestination(){
        return destination;
    }

    @Override
    public int getWeight(){
        return weight;
    }

    @Override
    public void setWeight(int weight){
        if(weight < 0){
            throw new IllegalArgumentException("weight must be greater than 0");
        }
        this.weight = weight;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return String.format("%s -> %s", getName(), getWeight(), getDestination().toString()); //formatera snyggt
    }
}
