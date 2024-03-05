package com.tommycondon.ca1;

import javafx.scene.shape.Rectangle;

public class Pill {
    private double area;
    private XYPoint point;
    private int rootValue;
    private Rectangle rectangle;

    public Pill(double area, XYPoint point, int root, Rectangle rectangle) {
        this.area = area;
        this.point = point;
        this.rootValue = root;
        this.rectangle = rectangle;
    }

    public String toString(){
        return "Pill --> Area of pill: " + area + ", " + "Root Value: " + rootValue + ", ";
    }


}
