// PROG2 VT2026, Inlämningsuppgift, del 1
// Grupp 070
// Melker Cronmark mecr7998

package se.su.inlupp;

public interface Edge<T> {

    T getDestination();

    int getWeight();

    void setWeight(int weight);

    String getName();
}
