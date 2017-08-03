package com.example.ftkj.mylockview;

/**
 * Created by FTKJ on 2017/8/3.
 */

public class Point {
    private float cx;
    private float cy;
    private float radius;
    private boolean isLight;

    public Point(float cx, float cy, float radius, boolean isLight) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.isLight = isLight;
    }

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean light) {
        isLight = light;
    }
}
