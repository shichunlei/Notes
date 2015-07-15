package com.leo.notes.been;

import cn.bmob.v3.BmobObject;

public class Group extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1057527998896251001L;
	private int id;
	private int num;
	private String cover;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", num=" + num + ", cover=" + cover
				+ ", name=" + name + "]";
	}

}
