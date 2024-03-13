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
        return "Pill --> [" + point.getX()+ "," + point.getY() + "] " + "Root Value: " + rootValue + ", ";
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public XYPoint getPoint() {
        return point;
    }

    public void setPoint(XYPoint point) {
        this.point = point;
    }

    public int getRootValue() {
        return rootValue;
    }

    public void setRootValue(int rootValue) {
        this.rootValue = rootValue;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
