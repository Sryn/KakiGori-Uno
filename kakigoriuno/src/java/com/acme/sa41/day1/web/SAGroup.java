package com.acme.sa41.day1.web;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("sagroup")
@SessionScoped
public class SAGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Member> members = new LinkedList<>();
	private String name;

	@Inject private Member member;

	@PostConstruct
	private void init() {
		System.out.println(">>> in post construct");
	}

	@PreDestroy
	private void cleanup() {
		System.out.println(">>>in pre destroy");
	}


	public void add() {
		System.out.println(">>> @Inject member: " + member.getClass().getName());
		System.out.println(">> copy: " + member.copy().getClass().getName());
		if (name == null)
			name = member.getGroupId();
		members.add(member.copy());
	}

	public void add(Member member) {
		if (name == null)
			name = member.getGroupId();
		members.add(member.copy());
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
