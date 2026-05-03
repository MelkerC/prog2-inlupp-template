package se.su.inlupp;

public interface Edge <T>{

    T getDestination();

    int getWeight();

    void setWeight(int weight);

    String getName();

    String toString();
}
