package com.safeway;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
public class ReadCSV {

	public static List<RouteInfoDomain> getCsvData() {

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
		return rpInfoList;
	}
}