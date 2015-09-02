package com.seasonalpatterns.dbmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.seasonalpatterns.comm.Commodity;

public class DBManager {

	private static DataSource ds = null;
	private static DBManager manager = new DBManager();
	private PreparedStatement pst = null;
	private ResultSet rs = null;

	static {
		createDataSource();
	}

	private DBManager() {
	}

	public static DBManager getInstance() {
		return manager;
	}

	private static void createDataSource() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/pool");

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {

		Connection conn = null;
		;
		conn = DBManager.ds.getConnection();

		return conn;
	}

	public int updateDB(InputStreamReader isr, Commodity comm) {

		Scanner scan = null;
		int[] batchExecuted = null;
		BufferedReader br = null;
		Connection con = null;

		try {
			con = this.getConnection();
			con.setAutoCommit(false);
			// Creates an empty table if necessary named after the commodity;
			pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS "
					+ comm.toString() + "(Date DATE, Last DECIMAL(10,5));");
			pst.executeUpdate();

			// A Scanner inside a BufferedReader is responsible for writing to
			// the table.
			br = new BufferedReader(isr);
			String line = null;

			pst = con.prepareStatement("INSERT INTO " + comm.toString()
					+ "(Date, Last) VALUES (?, ?);");

			int updateCount = 0; // Is used to be compared to int[] batchExecuted;
			while ((line = br.readLine()) != null) {

				scan = new Scanner(line);
				scan.useLocale(Locale.US);
				System.out.println(line);
				scan.useDelimiter(",");

				String dateString = scan.next();
				java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd")
						.parse(dateString);
				java.sql.Date date = new java.sql.Date(utilDate.getTime());
				BigDecimal price = new BigDecimal(scan.nextDouble());

				pst.setDate(1, date);
				pst.setBigDecimal(2, price);
				pst.addBatch();
				updateCount++;
			}

			batchExecuted = pst.executeBatch();
			
			con.commit();
			comm.setLastUpdateTime();
			comm.setNextUpdateTime();
			
			if (batchExecuted.length == updateCount && batchExecuted.length > 0) {
				System.out.println("Data downloaded for " + comm
						+ ", batchExecuted = " + batchExecuted.length
						+ ", updateCount = " + updateCount);
			}

		} catch (SQLException | ParseException | IOException ex) {
			System.err.println(ex.getMessage());
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (br != null) {
					br.close();
				}
				if (scan != null) {
					scan.close();
				}
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (batchExecuted != null) {
			return batchExecuted.length;
		} else {
			return 0;
		}
	}

	public Date getLastDate(Commodity comm) {

		Connection con = null;
		Date result = null;
		try {
			con = this.getConnection();
			pst = con.prepareStatement("SELECT max(date) FROM "
					+ comm.toString() + ";");
			rs = pst.executeQuery();

			while (rs.next()) {
				rs.getDate("max(Date)");
				long time = 0L;
				if (!rs.wasNull()) {

					time = rs.getDate("max(Date)").getTime();
					Date date = new Date(time);
					result = date;

				} else {
					try {
						result = new SimpleDateFormat("yyyy-MM-dd")
								.parse("1980-01-01");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
}