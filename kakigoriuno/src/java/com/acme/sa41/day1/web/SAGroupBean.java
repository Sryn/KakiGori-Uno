package com.acme.sa41.day1.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless
public class SAGroupBean {

	private static final String INSERT_GROUP = "insert into sagroup values (?, ?)";
	private static final String INSERT_MEMBER = "insert into member values (?, ?, ?, ?)";

	@Resource(lookup = "jdbc/acme") DataSource ds;

	@PostConstruct
	private void init() {
		System.out.println(">>> SAGroupBean created");
	}

	@PreDestroy
	private void cleanup() {
		System.out.println(">>> clean up");
	}

	//Transaction - begins
	public void createGroup(SAGroup grp) throws SQLException, SAGroupException  {
		try (Connection conn = ds.getConnection()) {
			//auto-commit
			conn.setAutoCommit(false);
			try {
				//Create group
				PreparedStatement ps = conn.prepareStatement(INSERT_GROUP);
				ps.setString(1, grp.getName());
				ps.setString(2, grp.getName());
				ps.executeUpdate();

				int i = 0;
				for (Member m : grp.getMembers()) {
//					if (i++ > 1)
//						throw new SQLException("Fake exception");
					ps = conn.prepareStatement(INSERT_MEMBER);
					ps.setString(1, m.getMatricId());
					ps.setString(2, m.getName());
					ps.setString(3, m.getEmail());
					ps.setString(4, m.getGroupId());
					ps.executeUpdate();
				}
				//conn.commit(); //NOt necessary
			} catch (SQLException ex) {
				System.out.println(">>> will roll back");
//				throw new NullPointerException("Fake exception NPE");
				///throw new SAGroupException(ex.getMessage());
				conn.rollback();
				throw ex;
			}

		}
	}
	//Tranaction ends - depends

}
