package se.su.inlupp;

import java.util.HashMap;

import java.util.Map;

public class PathLibrary <T>{
    Map<String, GraphPath<T>> paths = new HashMap<>();

    public Map<String, GraphPath<T>> getAllPaths(){
        return paths;
    }

    public GraphPath<T> getPath(String pathName){
        return paths.get(pathName);
    }

    public void addPath(String pathName, GraphPath<T> path){
        paths.put(pathName,path);
    }

    public void removePath(String pathName){
        paths.remove(pathName);
    }

    public void resetPaths(){
        paths.clear();
    }
}
