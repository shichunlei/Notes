package com.leo.notes.been;

import cn.bmob.v3.BmobObject;

public class Notes extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7638570521195111151L;

	/** 标题（名称） */
	private String title;
	/** 内容 */
	private String content;
	/** 创建时间 */
	private String time;
	/** 分组 */
	private int group;

	private User author;

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Notes [title=" + title + ", content=" + content + ", time="
				+ time + ", group=" + group + ", author=" + author + "]";
	}

}
