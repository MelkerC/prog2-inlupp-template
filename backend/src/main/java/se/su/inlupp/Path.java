// PROG2 VT2026, Inlämningsuppgift, del 1
// Grupp 070
// Melker Cronmark mecr7998

package se.su.inlupp;

import java.util.*;

public interface Path<T> extends Iterable<Edge<T>> {

    T getStart();

    T getEnd();

    int getTotalWeight();

    List<Edge<T>> getEdges();

    List<T> getNodes();
}

