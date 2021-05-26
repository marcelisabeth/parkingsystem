package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	
	private static TicketDAO ticketDAO = new TicketDAO();
	int recurrentUser = 0;


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
		
		
		  /**
	     * 
	     * Rend le stationnement gratuit si le client reste moins de 30 minutes
	     * @author melisabeth
	     */
		
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
			
			  /**
		     * 
		     * Applique une remise de 5% si le client est déjà venu au moins une fois
		     * @author melisabeth
		     */
		
		recurrentUser = ticketDAO.getRecurrentUser(ticket.getVehicleRegNumber());
			
		 if (recurrentUser > 1)  {
		    ticket.setPrice(ticket.getPrice()*0.95);
		    	
		    }
		    
		} 
	}
}	

		
		
	
		      
		 
	
	


