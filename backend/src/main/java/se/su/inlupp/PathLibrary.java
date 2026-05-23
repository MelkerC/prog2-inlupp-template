package se.su.inlupp;

import java.util.HashMap;

import java.util.Map;

public class PathLibrary <T>{
    Map<String, GraphPath<T>> paths = new HashMap<>();
    PathFinder<T>  pathFinder = new BFSPathFinder<>();

    public Map<String, GraphPath<T>> getAllPaths(){
        return paths;
    }

    public GraphPath<T> getPath(String pathName){
        return paths.get(pathName);
    }

    public void addPath(String pathName, GraphPath path){
        paths.put(pathName,path);
    }

    public void removePath(String pathName){
        paths.remove(pathName);
    }

    public void removePathsWithNode(T node){
        //Om jag har tid gör jag en metod som updaterar specifikt alla paths som innehåller noden
    }

    public void updateAllPaths(ListGraph<T> graph){
        for(String key : paths.keySet()){

            if(paths.get(key)==null){break;}
            if(!graph.hasPath(paths.get(key).getEnd(), paths.get(key).getStart())){paths.remove(key); break;}
            paths.put(key, (GraphPath<T>)pathFinder.findPath(graph, paths.get(key).getStart(), paths.get(key).getEnd()));
        }
    }


}
