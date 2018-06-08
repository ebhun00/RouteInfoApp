package com.safeway;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class RouteInfoUpdates {

	public static void main(String[] argv) {

		class RouteInfoDomain {
			private String store;
			private String shift;
			private String orderId;
			private String van;
			private String departDate;
			private String arrivalDate;
			private String routeId;
			private String sequence;

			public String getStore() {
				return store;
			}

			public void setStore(String store) {
				this.store = store;
			}

			public String getShift() {
				return shift;
			}

			public void setShift(String shift) {
				this.shift = shift;
			}

			public String getOrderId() {
				return orderId;
			}

			public void setOrderId(String orderId) {
				this.orderId = orderId;
			}

			public String getVan() {
				return van;
			}

			public void setVan(String van) {
				this.van = van;
			}

			public String getDepartDate() {
				return departDate;
			}

			public void setDepartDate(String departDate) {
				this.departDate = departDate;
			}

			public String getArrivalDate() {
				return arrivalDate;
			}

			public void setArrivalDate(String arrivalDate) {
				this.arrivalDate = arrivalDate;
			}

			public String getRouteId() {
				return routeId;
			}

			public void setRouteId(String routeId) {
				this.routeId = routeId;
			}

			public String getSequence() {
				return sequence;
			}

			public void setSequence(String sequence) {
				this.sequence = sequence;
			}

		}
		
		class Address {
			
			public String getUdfString1() {
				return udfString1;
			}
			public void setUdfString1(String udfString1) {
				this.udfString1 = udfString1;
			}
			public String getStreet_address() {
				return street_address;
			}
			public void setStreet_address(String street_address) {
				this.street_address = street_address;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
			public String getState() {
				return state;
			}
			public void setState(String state) {
				this.state = state;
			}
			public String getZipcode() {
				return zipcode;
			}
			public void setZipcode(String zipcode) {
				this.zipcode = zipcode;
			}
			public String getCountry() {
				return country;
			}
			public void setCountry(String country) {
				this.country = country;
			}
			public String getTest() {
				return test;
			}
			public void setTest(String test) {
				this.test = test;
			}
			public String getStore_nbr() {
				return store_nbr;
			}
			public void setStore_nbr(String store_nbr) {
				this.store_nbr = store_nbr;
			}
			//UDFString1,Street_address,CITY,state,zipcode,country,test,store_nbr; -- CSV header
			private String udfString1;
			private String street_address;
			private String city;
			private String state;
			private String zipcode;
			private String country;
			private String test;
			private String store_nbr;
		}
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		// *********************** Route Info CSV manupulation*****************************//

		BufferedReader fileReader = null;
		CSVReader csvReader = null;

		List<RouteInfoDomain> rpInfoList = new ArrayList<RouteInfoDomain>();

		try {

			fileReader = new BufferedReader(new FileReader("./../datain/RPVans.csv"));
			csvReader = new CSVReader(fileReader);
			String[] record;

			while ((record = csvReader.readNext()) != null) {

				RouteInfoDomain rpinfo = new RouteInfoDomain();
				rpinfo.setStore(record[0]);
				rpinfo.setShift(record[1]);
				rpinfo.setOrderId(record[2]);
				rpinfo.setVan(record[3]);
				rpinfo.setDepartDate(record[4]);
				rpinfo.setArrivalDate(record[5]);
				rpinfo.setRouteId(record[6]);
				rpinfo.setSequence(record[7]);
				rpInfoList.add(rpinfo);
			}

		} catch (Exception e) {
			System.out.println("Reading CSV Error!");
		} finally {
			try {
				fileReader.close();
				csvReader.close();
			} catch (IOException e) {
				System.out.println("Closing fileReader/csvParser Error!");
			}
		}

		// ***************************************************//
		
		// *********************** Address CSV manupulation*****************************//

		BufferedReader addressFileReader = null;
		CSVReader addressCsvReader = null;

		List<Address> addressInfoList = new ArrayList<Address>();

		try {

			fileReader = new BufferedReader(new FileReader("./../datain/Address.csv"));
			csvReader = new CSVReader(fileReader);
			String[] record;

			//UDFString1,Street_address,CITY,state,zipcode,country,test,store_nbr; -- CSV header
			
			while ((record = csvReader.readNext()) != null) {

				Address addressInfo = new Address();
				addressInfo.setUdfString1(record[0]);
				addressInfo.setStreet_address(record[1]);
				addressInfo.setCity(record[2]);
				addressInfo.setState(record[3]);
				addressInfo.setZipcode(record[4]);
				addressInfo.setCountry(record[5]);
				addressInfo.setTest(record[6]);
				addressInfo.setStore_nbr(record[7]);
				addressInfoList.add(addressInfo);
			}

		} catch (Exception e) {
			System.out.println("Reading Address CSV Error!");
		} finally {
			try {
				fileReader.close();
				csvReader.close();
			} catch (IOException e) {
				System.out.println("Closing fileReader/csvParser Error!");
			}
		}

		// ***************************************************//


		List<RouteInfoDomain> csvRouteData = rpInfoList;
		String route_info_deleteTableSQL = "delete from OSFLEOM_ETL.ROUTE_INFO_STG";
		String route_info_insertSQL = "INSERT INTO OSFLEOM_ETL.ROUTE_INFO_STG"
				+ "(STORE_NBR,SHIFT_NBR, PURCHASE_ORDERS_ID, VAN_NBR, VAN_DEP_TIME, VAN_ARR_TIME, STOP_ID, STOP_NBR) "
				+ " VALUES (?,?,?,?,?,?,?,? )";

		List<Address> addressInfoListData = addressInfoList;
		String address_info_deleteTableSQL = "delete from OSFLEOM_ETL.ADDRESS";
		String address_info_insertSQL = "INSERT INTO OSFLEOM_ETL.ADDRESS"
				+ "(ORDER_NBR,STREET_ADDRESS, CITY, STATE, ZIPCODE, COUNTRY, TEST_STORE, TEST_FLG) "
				+ " VALUES (?,?,?,?,?,?,?,? )";
		
		System.out.println("Oracle JDBC Driver Registered!");
		System.out.println("Route info Data upload  to EOM process started");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;

		try {
			//Below connection details need to replaced based on environment
			//"jdbc:oracle:thin:@host:port/service", "user", "pwd"
			connection = DriverManager.getConnection("jdbc:oracle:thin:@host:port/service", "user", "pwd");
			
			statement = connection.createStatement();
			statement.execute(route_info_deleteTableSQL);
			statement.execute(address_info_deleteTableSQL);
			preparedStatement = connection.prepareStatement(route_info_insertSQL);
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
			
			//*************** Address insert *****************//
			
			preparedStatement = connection.prepareStatement(address_info_insertSQL);
			connection.setAutoCommit(false);
			//UDFString1,Street_address,CITY,state,zipcode,country,test,store_nbr; -- CSV header

			for (Address addressInfoDomain : addressInfoListData) {
				preparedStatement.setString(1, addressInfoDomain.getUdfString1());
				preparedStatement.setString(2, addressInfoDomain.getStreet_address());
				preparedStatement.setString(3, addressInfoDomain.getCity());
				preparedStatement.setString(4, addressInfoDomain.getState());
				preparedStatement.setString(5, addressInfoDomain.getZipcode());
				preparedStatement.setString(6, addressInfoDomain.getCountry());
				preparedStatement.setString(7, addressInfoDomain.getTest());
				preparedStatement.setString(8, addressInfoDomain.getStore_nbr());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			//************************************************//
			statement.close();
			preparedStatement.close();
			connection.commit();
			connection.close();
			System.out.println("Route info & Address Data upload  to EOM is completed");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
//			e.printStackTrace();
//			return;
			System.exit(1);
		}
	}

}
