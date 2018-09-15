package com.example.vlposition;

public class pixel {
	public int red;
	public int blue;
	public int green;
	public int all;
	public int luma;
	
	public int getLuma() {
		return luma;
	}
	public void setLuma(int luma) {
		this.luma = luma;
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getAll() {
		return all;
	}
	public void setAll(int all) {
		this.all = all;
	}
	
	public void pixel(int r,int g,int b, int a){
		this.red=r;
		this.green=g;
		this.blue=b;
		this.all=a;
	}

}
