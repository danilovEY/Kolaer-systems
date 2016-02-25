package ru.kolaer.server.dao.entities;

public class JSONDate {
	private int day;
	private int month;
	private int year;
	private int dayOfWeek;
	
	public JSONDate(final int day, final int month, final int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public JSONDate() {
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	
}
