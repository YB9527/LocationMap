package com.xupu.locationmap.arcruntime;

import java.util.HashMap;
import java.util.List;

public class MultiIPointObject {
    private List<Double[]> points;
    private HashMap<String, Integer> spatialReference;

    public List<Double[]> getPoints() {
        return points;
    }

    public void setPoints(List<Double[]> points) {
        this.points = points;
    }

    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }

}
