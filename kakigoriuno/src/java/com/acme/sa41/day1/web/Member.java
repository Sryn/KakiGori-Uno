package com.acme.sa41.day1.web;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;

	private String matricId;
	private String name;
	private String email;
	private String groupId;

	public String getMatricId() {
		return matricId;
	}

	public void setMatricId(String matricId) {
		this.matricId = matricId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Member copy() {
		Member m = new Member();
		m.matricId = matricId;
		m.name = name;
		m.email = email;
		m.groupId = groupId;
		return (m);
	}

	@Override
	public String toString() {
		return ("name:" + name);
	}
	
}
