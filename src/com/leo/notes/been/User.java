package com.leo.notes.been;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309974181538936739L;

	/** 年龄 */
	private Integer age;
	/** 生日 */
	private String birthday;
	/** 性别 */
	private String gender;
	/** 头像 */
	private BmobFile headerpic;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public BmobFile getHeaderpic() {
		return headerpic;
	}

	public void setHeaderpic(BmobFile headerpic) {
		this.headerpic = headerpic;
	}

}
