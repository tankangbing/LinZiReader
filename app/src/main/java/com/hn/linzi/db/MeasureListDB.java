package com.hn.linzi.db;

public interface MeasureListDB {
	String DB_NAME = "measure.db";
	int VERSION = 1;

	public interface MeasureList {
		String _ID="_id";
		String DAY="day";
		String TIME="time";
		String HIGH_HANDED="high_handed";
		String LOW_HANDED="low_handed";
		String PULSE = "pules";
		String REMARK = "remark";
		String TABLE_NAME="measurelist";
		String CREATE_TABLE_SQL = "create table measurelist(_id integer primary key autoincrement,day text,time text," +
				"high_handed integer,low_handed integer ,pules integer,remark text )";
	}
}
