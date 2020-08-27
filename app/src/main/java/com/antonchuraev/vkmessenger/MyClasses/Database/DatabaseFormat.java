package com.antonchuraev.vkmessenger.MyClasses.Database;

public class DatabaseFormat {
	public String name;
	public String photoURL;
	public boolean online;

	@Override
	public String toString() {
		return "DatabaseFormat{" +
				"name='" + name + '\'' +
				", photoURL='" + photoURL + '\'' +
				", online=" + online +
				'}';
	}
}
