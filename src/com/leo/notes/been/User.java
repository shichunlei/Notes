package com.leo.notes.been;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309974181538936739L;

	private String age;
	private String birthday;
	private String gender;
	private BmobFile headerpic;

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
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
