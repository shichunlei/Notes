package com.leo.notes.been;

public class Notes {

	/** id */
	private int id;
	/** 标题（名称） */
	private String title;
	/** 内容 */
	private String content;
	/** 创建时间 */
	private String time;
	/** 分组 */
	private int group;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "Notes [id=" + id + ", title=" + title + ", content=" + content
				+ ", time=" + time + ", group=" + group + "]";
	}

}
