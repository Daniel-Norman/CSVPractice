package com.DanielNorman.CSVPractice;

import java.util.StringTokenizer;


public class Record
{
	int id;
	String firstName, lastName;
	SimpleDate dob;

	Record(int id, String firstName, String lastName, SimpleDate dob)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
	}
	Record(int id, String firstName, String lastName, int d, int m, int y)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = new SimpleDate(d, m, y);
	}
	
	String getDobString()
	{
		return (dob.month < 10 ? "0" : "") + dob.month + (dob.day < 10 ? "/0" : "/") + dob.day + "/" + dob.year;
	}
	
	public class SimpleDate
	{
		int day, month, year;
		SimpleDate(int d, int m, int y) { day = d; month = m; year = y; }
		SimpleDate(String str)
		{
			StringTokenizer st = new StringTokenizer(str, "/");
			month = Integer.parseInt(st.nextToken());
			day = Integer.parseInt(st.nextToken());
			year = Integer.parseInt(st.nextToken());
		}
	}
}
