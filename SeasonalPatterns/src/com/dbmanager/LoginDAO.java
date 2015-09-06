package com.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class LoginDAO {

	private static DataSource ds = null;
	private static LoginDAO login = new LoginDAO();

	static {
		getDataSource();
	}

	private LoginDAO() {
	}

	private static void getDataSource() {

		try {
			Context initCtx = new InitialContext();
			Context ctx = (Context) initCtx.lookup("comp/env");

			ds = (DataSource) ctx.lookup("jdbc/users");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {

		Connection conn = ds.getConnection();
		return conn;
	}

	public static boolean validate(String username, String password) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = login.getConnection();
			ps = conn.prepareStatement("SELECT username from users where username = ? and password = ?");
			ps.setString(1, username);
			ps.setString(2, password);

			rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			// TODO Loging
		} finally {
			try {
				if (conn != null) conn.close();
				if (ps != null) ps.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}