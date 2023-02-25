package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;

import static com.driver.model.TripStatus.CANCELED;
import static com.driver.model.TripStatus.COMPLETED;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;
	@Autowired
	private CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer=customerRepository2.findById(customerId).get();
		List<TripBooking> tripBookingList=customer.getTripBookingList();
		for(TripBooking book : tripBookingList){
			Driver driver=book.getDriver();
			Cab cab=driver.getCab();
			cab.setSeatBooked(true);
			driverRepository2.save(driver);
		}
		customerRepository2.delete(customer);


	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList = driverRepository2.findAll();
		Driver drivers = null;
		for(Driver currDriver : driverList){
			if(currDriver.getCab().isSeatBooked()){
				if((drivers == null) || (currDriver.getDriverId() < drivers.getDriverId())){
					drivers = currDriver;
				}
			}
		}
		if (drivers == null) throw new Exception("No cab available!");


		// finding trip details
		TripBooking bookedTrip = new TripBooking();
		Customer customer = customerRepository2.findById(customerId).get();
		bookedTrip.setCustomer(customer);
		bookedTrip.setFromLocation(fromLocation);
		bookedTrip.setToLocation(toLocation);
		bookedTrip.setDistanceInKm(distanceInKm);
		bookedTrip.setBill(distanceInKm * 10);
		bookedTrip.setTripStatus(TripStatus.CONFIRMED);
		bookedTrip.setDriver(drivers);
		drivers.getCab().setSeatBooked(false);
		driverRepository2.save(drivers);

		customer.getTripBookingList().add(bookedTrip);
		customerRepository2.save(customer);

		tripBookingRepository2.save(bookedTrip);
		return null;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooked = tripBookingRepository2.findById(tripId).get();
		tripBooked.setTripStatus(CANCELED);
		tripBooked.setBill(0);
		tripBooked.getDriver().getCab().setSeatBooked(true);
		tripBookingRepository2.save(tripBooked);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooked = tripBookingRepository2.findById(tripId).get();
		tripBooked.setTripStatus(COMPLETED);
		tripBooked.getDriver().getCab().setSeatBooked(true);
		tripBookingRepository2.save(tripBooked);
	}
}
