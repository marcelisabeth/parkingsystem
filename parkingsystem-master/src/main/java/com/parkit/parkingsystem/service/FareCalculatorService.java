package com.parkit.parkingsystem.service;

import java.sql.*;


import com.mysql.cj.x.protobuf.MysqlxCrud.Column;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		// Conversion de la durée en minutes

		// int inHour = ticket.getInTime().getHours();
		long inHour = ticket.getInTime().getTime() / 1000 / 60;

		// int outHour = ticket.getOutTime().getHours();
		long outHour = ticket.getOutTime().getTime() / 1000 / 60;

		// TODO: Some tests are failing here. Need to check if this logic is correct
		long duration = outHour - inHour;
		
		
		// code : gratuit pour moins de 30 minutes
		if (duration <= 30) {
			ticket.setPrice(0);
		}

		else {

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / 60);
				break;
			}
			case BIKE: {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / 60);
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
		
		//fidelité de 5% si passage supérieur à 2
		Connection con = null;
		
		String query = "SELECT VEHICLE_REG_NUMBER,  COUNT(*) AS fidelity FROM prod.ticket GROUP BY VEHICLE_REG_NUMBER";
		 try (Statement stmt = con.createStatement()) {
		      ResultSet rs = stmt.executeQuery(query);
		      while (rs.next()) {
		          
		          int fidelity = rs.getInt("fidelity");
		          
		      }
		      
		 }
	
		
		if ( fidelity  > 1) {
			
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR / 60) * 0.95);
				break;
			}
			case BIKE: {
				ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR / 60) *0.95);
				break;
			}
			
			else {

				switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / 60);
					break;
				}
				case BIKE: {
					ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / 60);
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}
			}
			
	
	

}
