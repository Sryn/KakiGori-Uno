package com.acme.sa41.day1.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class SAGroupManager {

	private static final String INSERT_GROUP = "insert into sagroup values (?, ?)";
	private static final String INSERT_MEMBER = "insert into member values (?, ?, ?, ?)";

	private final DataSource ds;

	public SAGroupManager(DataSource d) {
		System.out.println(">>> In SAGroupManager.java SAGroupManager()");
		ds = d;
	}

	public void createGroup(SAGroup grp) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			//auto-commit
			System.out.println(">>> In SAGroupManager.java createGroup()");
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
				conn.commit(); //NOt necessary
			} catch (SQLException ex) {
				System.out.println(">>> SAGroupManager.java: will roll back");
				conn.rollback();
				throw ex;
			}

		}
	}

}
