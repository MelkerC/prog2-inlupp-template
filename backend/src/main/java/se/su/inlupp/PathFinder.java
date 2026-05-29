// PROG2 VT2026, Inlämningsuppgift, del 1
// Grupp 070
// Melker Cronmark mecr7998

package se.su.inlupp;

public interface PathFinder<T> {
    Path<T> findPath(Graph<T> graph, T from, T to);
}

