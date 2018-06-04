package com.safeway;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RouteInfoUpdates {

	public static void main(String[] argv) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		List<RouteInfoDomain> csvRouteData = ReadCSV.getCsvData();
		String deleteTableSQL = "delete from OSFLEOM_ETL.ROUTE_INFO_STG";
		String insertRouteInfoSQL = "INSERT INTO OSFLEOM_ETL.ROUTE_INFO_STG"
				+ "(STORE_NBR,SHIFT_NBR, PURCHASE_ORDERS_ID, VAN_NBR, VAN_DEP_TIME, VAN_ARR_TIME, STOP_ID, STOP_NBR) " 
				+ " VALUES (?,?,?,?,?,?,?,? )";
		
		System.out.println("Oracle JDBC Driver Registered!");
		System.out.println("Route info Data upload  to EOM process started");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;

		try {
			//Below connection details need to replaced based on environment
			//"jdbc:oracle:thin:@host:port/service", "user", "pwd"
			connection = DriverManager.getConnection();
			statement = connection.createStatement();
			statement.execute(deleteTableSQL);
			preparedStatement = connection.prepareStatement(insertRouteInfoSQL);
			connection.setAutoCommit(false);
			for (RouteInfoDomain routeInfoDomain : csvRouteData) {
				preparedStatement.setString(1, routeInfoDomain.getStore());
				preparedStatement.setString(2, routeInfoDomain.getShift());
				preparedStatement.setString(3, routeInfoDomain.getOrderId());
				preparedStatement.setString(4, routeInfoDomain.getVan());
				preparedStatement.setString(5, routeInfoDomain.getDepartDate());
				preparedStatement.setString(6, routeInfoDomain.getArrivalDate());
				preparedStatement.setString(7, routeInfoDomain.getRouteId());
				preparedStatement.setString(8, routeInfoDomain.getSequence());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			connection.close();
			System.out.println("Route info Data upload  to EOM is completed");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}
}
