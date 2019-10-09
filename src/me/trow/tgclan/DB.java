package me.trow.tgclan;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
 
public class DB {
	
	private Connection conn;
	private File file;
	private Statement stmt;
	
	private DB(File f) {
		file = f;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + file);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	private DB(String urlconn) {
		try {
			conn = DriverManager.getConnection(urlconn);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public static DB load(File f) {
		return new DB(f);
	}
 
	public static DB load(String f) {
		return new DB(new File(f));
	}
 
	public static DB load(String host, String database, String user,
			String pass) {
		return new DB("jdbc:mysql://" + host + "/" + database + "?"
				+ "user=" + user + "&password=" + pass+"&autoReconnect=true");
	}
 
	public void update(String q) {
		try {
			stmt.executeUpdate(q);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public ResultSet query(String q) {
		try {
			return stmt.executeQuery(q);
		} catch (Exception e) {
		}
		return null;
	}
	
	public void close() {
		try {
			stmt.close();
			conn.close();
		} catch (Exception e) {}
	}
	
	public boolean isConnected() {
		try {
			return stmt!=null && conn!=null && !stmt.isClosed() && !conn.isClosed();
		} catch (Exception e) {}
		return false;
	}

	public Connection getConnection() {
		return conn;
	}
 
}