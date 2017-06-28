import java.sql.*;
import java.util.ArrayList;

public class SendDataToSQL{
	public SendDataToSQL(){
	
	}
	
	public void bulkInsert(String dataFile, String database, String tableName, String fieldTerminator, String rowTerminator, StringBuilder errorLog){
		Statement stmt = null;
		String query = "BULK INSERT " + tableName + " FROM '" + dataFile + "' WITH (FIELDTERMINATOR ='" + fieldTerminator + "',ROWTERMINATOR ='" + rowTerminator + "');";
		Connection connection = null;
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// the sql server url
			String url = "jdbc:sqlserver://localhost:1433;DatabaseName=" + database + ";integratedSecurity=true";

			connection = DriverManager.getConnection(url);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			stmt.addBatch(query);
			stmt.executeBatch();
			if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
		
	}
	
	public void bulkInsert(String dataFile, String database, String tableName, StringBuilder errorLog){
		bulkInsert(dataFile, database, tableName, ",", "\\n", errorLog);
	}
	
	//wrote this method when creating optimizer for AWS
	public void insert(String hostname, String port, String userName, String password, String database, String query, StringBuilder errorLog){
		Statement stmt = null;
		String url;
		Connection connection = null;
		
		url = "jdbc:sqlserver://" + hostname + ":" + port + ";DatabaseName=" + database;
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			connection = DriverManager.getConnection(url,userName,password);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			stmt.addBatch(query);
			stmt.executeBatch();
			if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			//System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//System.exit(2);
		}
		
	}
	
	/*
	public ArrayList<ArrayList<String>> getQuery(String query, String database, String tableName, ArrayList<String> colNames, int numCols, StringBuilder errorLog){
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		String curCol = "";
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		ArrayList curRow = new ArrayList<String>();
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// the sql server url
			String url = "jdbc:sqlserver://localhost:1433;DatabaseName=" + database + ";integratedSecurity=true";

			connection = DriverManager.getConnection(url);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){
				for(int i = 0; i < colNames.size(); i++){
					curCol = rs.getString(colNames.get(i));
					curRow.add(curCol);
				}
				results.add(curRow);
			}
			if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
		
		return results;
	}
	*/
	
	
	public ResultSet getQuery(String query, String database, StringBuilder errorLog){
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// the sql server url
			String url = "jdbc:sqlserver://localhost:1433;DatabaseName=" + database + ";integratedSecurity=true";

			connection = DriverManager.getConnection(url);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			//if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
		
		return rs;
	}
	
	public void executeQuery(String query, String database, StringBuilder errorLog){
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// the sql server url
			String url = "jdbc:sqlserver://localhost:1433;DatabaseName=" + database + ";integratedSecurity=true";

			connection = DriverManager.getConnection(url);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			stmt.addBatch(query);
			stmt.executeBatch();
			if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
	}

	public ResultSet executeStmtAWS(String hostname, String port, String userName, String password, String database, String query, StringBuilder errorLog){
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		
		String url = "jdbc:sqlserver://" + hostname + ":" + port + ";DatabaseName=" + database;
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			connection = DriverManager.getConnection(url,userName,password);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			stmt.addBatch(query);
			stmt.executeBatch();
			if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
		
		return rs;
	}
			
	public ResultSet getQueryAWS(String hostname, String port, String userName, String password, String database, String query, StringBuilder errorLog){
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		
		String url = "jdbc:sqlserver://" + hostname + ":" + port + ";DatabaseName=" + database;
		
		try
		{
			// the sql server driver string
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			connection = DriverManager.getConnection(url,userName,password);
			
			// now do whatever you want to do with the connection
			// ...
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			//if (stmt != null) { stmt.close(); }
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
		
		return rs;
	}
}
