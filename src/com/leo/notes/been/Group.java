package com.leo.notes.been;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Group extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1057527998896251001L;

	/** 数量 */
	private Integer num;
	/** 封面图片 */
	private BmobFile cover;
	/** 分组名称 */
	private String name;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public BmobFile getCover() {
		return cover;
	}

	public void setCover(BmobFile cover) {
		this.cover = cover;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "{\"num\":" + num + ", \"cover\":\"" + cover + "\", \"name\":\""
				+ name + "\"}";
	}

}
