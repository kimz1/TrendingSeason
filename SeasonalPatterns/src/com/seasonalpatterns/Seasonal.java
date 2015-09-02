package com.seasonalpatterns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Seasonal {
	
	private String url = null;
	private String user = null;
	private String password = null;
	FileInputStream in = null;
	Connection con = null;
	PreparedStatement pst = null;
	static String props = "C:/props.txt";
	String tableName = "Corn_CBOT";
	static String excelFilePath = "C:/Users/Miroslav Tóth/Documents/Trading/Corn_CBOT/";
	String[] files;
	ResultSet rs;
	java.sql.Date currentYear = null;
	
	public Seasonal(String[] files, java.sql.Date currentYear) {
		this.files = files;
		this.currentYear = currentYear;
	}

	public void createEmptyTable(java.sql.Date firstDate, java.sql.Date finalDate) {
		
		try {
			
			in = new FileInputStream(props);
			Properties props = new Properties();
			props.load(in);
			
			url = props.getProperty("db.url");
			user = props.getProperty("db.user");
			password = props.getProperty("db.password");
		
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
		
		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connected...");
			con.setAutoCommit(false);
			
			pst = con.prepareStatement("DROP TABLE IF EXISTS " + this.tableName);
			pst.executeUpdate();
			pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.tableName 
									   + "(Id INTEGER(5) AUTO_INCREMENT PRIMARY KEY, Date DATE)");
			pst.executeUpdate();
			java.sql.Date tempDate = finalDate;
			pst = con.prepareStatement("INSERT INTO " + this.tableName 
					   + "(Date) VALUES(?)");
			
			while (tempDate.after(firstDate)) {
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(tempDate);
				pst.setDate(1, tempDate);
				pst.executeUpdate();

				tempDate = createPreviousDayDate(tempDate);
				System.out.println(tempDate);
			}
			
			con.commit();
		
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
			if (con != null){
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
				} if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}
		System.out.println("Job's done.");
	}
	
	static java.sql.Date createPreviousDayDate(java.sql.Date date){

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DATE) == 1 &&
			cal.get(Calendar.MONTH) == 0) {
			cal.roll(Calendar.YEAR, -1);
		}
		
		cal.roll(Calendar.DAY_OF_YEAR, -1);
		Date utilDate = cal.getTime();
		java.sql.Date newDate = new java.sql.Date(utilDate.getTime());
		return newDate;
	}
	
	public void fillTable() {

		
		int counts = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connected...");
			con.setAutoCommit(false);
			
			for(String ticker : this.files){
				
				pst = con.prepareStatement("ALTER TABLE " + this.tableName + " ADD " + ticker + " DECIMAL(5, 3)");
				pst.executeUpdate();
				
				pst = con.prepareStatement("SELECT * FROM " + ticker);
				rs = pst.executeQuery();
				
				while (rs.next()) {
					
					Date tempDate = rs.getDate(1);
					float tempNum = rs.getFloat(2);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(tempDate);
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DATE);
					java.sql.Date sqlTempDate = new java.sql.Date(tempDate.getTime());
					String tickerYear = ticker.substring(ticker.length() - 2);
					
					pst = con.prepareStatement("UPDATE " + this.tableName + " SET " 
							+ ticker + "=" + tempNum + " WHERE EXTRACT(MONTH FROM date)='" + month 
							+ "' AND EXTRACT(DAY FROM date)='" + day 
							+ "' AND YEAR('" + currentYear + "') - YEAR(STR_TO_DATE('" + tickerYear 
							+ "', '%Y')) = YEAR(date) - YEAR('" + sqlTempDate + "');");
					
					counts += pst.executeUpdate();
				}
			}
			con.commit();
		
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
			if (con != null){
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
				} if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}
		System.out.println("Job's done. " + counts + "statements has been executed");
	}
	
	public void computeSeasonality() {
		
		try {
			
			in = new FileInputStream(props);
			Properties props = new Properties();
			props.load(in);
			
			url = props.getProperty("db.url");
			user = props.getProperty("db.user");
			password = props.getProperty("db.password");
		
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
		
		try {

			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connected...");
			
			for (String ticker : files) {
				
				double max = getMax(ticker);
				double min = getMin(ticker);
				double range = max - min;
				
				pst = con.prepareStatement("SELECT Id, " + ticker + " FROM " + this.tableName + ";", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pst.executeQuery();
				int count = 0;
				
				//Normalization - converting price into percentage
				
				while (rs.next()) {

					float price = rs.getFloat(ticker);
					if (!rs.wasNull()) {
						rs.updateDouble(ticker, (price - min) / range);
						rs.updateRow();
					}
					System.out.println(count++);
					//pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + ticker + "=("
					//+ ticker + " - " + min + ") / " + range + " WHERE " + ticker + " IS NOT NULL;");
				}
			}
			
			pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS sc(Id INT AUTO_INCREMENT PRIMARY KEY, avg DECIMAL(5, 4))");
			pst.executeUpdate();
			pst = con.prepareStatement("SELECT * FROM corn;");
			rs = pst.executeQuery();
			
			float avg = 0;
			int count = 0;
			
			//Calculating the average percentage

			while (rs.next()) {
				for (String ticker : files) {
					float num = rs.getFloat(ticker);
					if (!rs.wasNull()) {
						avg += num;
						count++;
					}
				}
				
				//Writes final values into cs table
				if (avg !=0 && count != 0){
					avg = avg / count;
					pst = con.prepareStatement("INSERT INTO sc(avg) VALUES(?)");
					pst.setFloat(1, avg);
					pst.executeUpdate();
					avg = 0;
					count = 0;
				}
			}
			
			SQLExporter("C:/SeasonalOutput.xls");
			
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (con != null) {
					con.close();
				} if (rs != null) {
					rs.close();
				} if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}

	public void computeSeasonalityOnPriceDifference() {
		
		try {
			
			in = new FileInputStream(props);
			Properties props = new Properties();
			props.load(in);
			
			url = props.getProperty("db.url");
			user = props.getProperty("db.user");
			password = props.getProperty("db.password");
		
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
		
		try {

			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connected...");
			
			pst = con.prepareStatement("SELECT * FROM " + this.tableName + " ORDER BY Id DESC;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pst.executeQuery();
			System.out.println(this.tableName + " table successfully ordered...");
			
			pst = con.prepareStatement("DROP TABLE IF EXISTS sc");
			pst.executeUpdate();
			
			pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS sc(Id INT AUTO_INCREMENT PRIMARY KEY, date DATE, priceDiff DECIMAL(5, 4))");
			pst.executeUpdate();
			System.out.println("Table created...");
			
			pst = con.prepareStatement("SELECT MAX(Id) FROM corn;");
			ResultSet maxIdRs = pst.executeQuery();
			maxIdRs.next();
			int maxId = maxIdRs.getInt(1);
			System.out.println("maxId set to " + maxId);
			int i = 1;
			double priceDiff = 0;
			ArrayList<Double> priceList = new ArrayList<>();
			double sum = 0;
			
			while (rs.absolute(i)) {
				java.sql.Date date = rs.getDate(2);
				
				for (String ticker : files) {
					double max = getMax(ticker);
					double min = getMin(ticker);
					double range = max - min;
					
					rs.absolute(i);
					double value = rs.getFloat(ticker);
					if (i > 1 && !rs.wasNull()) {
						
						boolean secValNull = true;
						int j = i;
						double secondValue = 0;
						
						do {
							j--;
							rs.absolute(j);
							secondValue = rs.getDouble(ticker);
							secValNull = rs.wasNull();
							
						} while (j > 1 && rs.wasNull());
						
						if (!secValNull) {
							priceDiff = ((value - min) / range) - ((secondValue - min) / range);
							priceList.add(priceDiff);
						}
					} //else nothing
				}
				// Sum all values in priceList if not empty and write them into 
				// table cs together with its date.

				for (double f : priceList) {
					sum += f;
				}
				if (!priceList.isEmpty()) {
					pst = con.prepareStatement("INSERT INTO sc(date, priceDiff) VALUES(?, ?);");
					pst.setDate(1, date);
					pst.setDouble(2, sum);
					pst.executeUpdate();
				}
				i++;
				priceList.clear();
			}
			maxIdRs.close();
			adjustWeekends();
			SQLExporter("C:/SeasonalOutput.xls");
			
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (con != null) {
					con.close();
				} if (rs != null) {
					rs.close();
				} if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	public double getMax(String ticker) throws SQLException {
		
		pst = con.prepareStatement("SELECT MAX(" + ticker + ") FROM corn;");
		ResultSet resultSet = pst.executeQuery();
		resultSet.next();
		
		double max = resultSet.getDouble(1);
		return max;
	}
	
	public double getMin(String ticker) throws SQLException {
		
		pst = con.prepareStatement("SELECT MIN(" + ticker + ") FROM corn;");
		ResultSet resultSet = pst.executeQuery();
		resultSet.next();
		
		double min = resultSet.getDouble(1);
		return min;
	}
	
	public void SQLExporter(String excelFilePath) throws SQLException{
		
		WritableWorkbook ww = null;
		
		try {
			ww = Workbook.createWorkbook(new File(excelFilePath));
			WritableSheet ws = ww.createSheet("Sheet", 0);
			
			pst = con.prepareStatement("SELECT * FROM sc;");
			rs = pst.executeQuery();
			int i = 0;
			
			while (rs.next()) {
				
				java.sql.Date date = rs.getDate(2);
				double num = rs.getDouble(3);
				if (!rs.wasNull()) {
					
					jxl.write.Number number = new jxl.write.Number(1, i, num);
					
					Date utilDate = new Date(date.getTime());
					jxl.write.DateTime dateTime = new jxl.write.DateTime(0, i, utilDate);
					
					ws.addCell(dateTime);
					ws.addCell(number);
					
				} else {
					
					Date utilDate = new Date(date.getTime());
					jxl.write.DateTime dateTime = new jxl.write.DateTime(0, i, utilDate);
					
					ws.addCell(dateTime);
					
				}
				i++;
			}
			ww.write();
		} catch (WriteException ex) {
			System.out.println("Caught WriteException: " + ex.getStackTrace());
		} catch (IOException ex) {
			System.out.println("Caught WriteException: " + ex.getStackTrace());
		} finally {
			if (ww !=null) {
				try {
					ww.close();
				} catch (IOException ex) {
					System.out.println("Caught IOException: " + ex.getStackTrace());
				} catch (WriteException ex) {
					System.out.println("Caught IOException: " + ex.getStackTrace());
				}
			}
		}
		System.out.println("WORKSHEET SUCCESSFULLY CREATED...");
	}

	public void adjustWeekends() throws SQLException{
		
		// Set all weekend values to null
		
		pst = con.prepareStatement("SELECT * FROM sc;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = pst.executeQuery();
		
		while (rs.next()) {
			java.sql.Date date = rs.getDate(2);
			Date utilDate = (Date) date;
			Calendar cal = Calendar.getInstance();
			cal.setTime(utilDate);
			
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				
				rs.updateDate(3, null);
				rs.updateRow();
			}
		}
	}
}
