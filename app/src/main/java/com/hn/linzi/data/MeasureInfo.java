package com.hn.linzi.data;

public class MeasureInfo {
	public int _id;
	public String time;
	public String day;
	public int	 high_handed;
	public int low_handed;
	public int pules;
	public String remark;
	@Override
	public String toString() {
		return "MeasureInfo [_id=" + _id + ", day=" + day + ", time=" + time +", time=" + time +
				", high_handed=" + high_handed + ", low_handed=" + low_handed +", pules=" + pules + ", remark=" + remark + "]";
	}
	
}
