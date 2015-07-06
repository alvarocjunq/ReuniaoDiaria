package br.com.consinco.reuniaodiaria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public Connection getConnection() throws SQLException {
		Connection connection = null;
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection =  DriverManager.getConnection("jdbc:oracle:thin:@//192.168.250.210:1521/ORAREAL",
					"orareal", "vmorri");
		}
        catch(SQLException ex) {
        	System.out.println("Erro de SQLException");
        	System.out.println(ex.getErrorCode() + " - " + ex.getMessage());
        }
		catch (ClassNotFoundException e) {
			System.out.println("Erro de ClassNotFoundException");
			e.printStackTrace();
		}
		
		return connection;
	}

}
