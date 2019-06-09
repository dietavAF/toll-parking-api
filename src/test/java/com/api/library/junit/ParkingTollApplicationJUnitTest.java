package com.api.library.junit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.api.library.controller.ParkingController;
import com.api.library.entity.Car;
import com.api.library.entity.CarType;
import com.api.library.entity.Electric20kwCar;
import com.api.library.entity.SedanCar;
import com.api.library.entity.Slot;
import com.api.library.response.Invoice;
import com.api.library.response.ParkResponse;
import com.api.library.response.ParkingStatus;
import com.api.library.service.ParkingManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParkingTollApplicationJUnitTest {
	
	private MockMvc mockMvc;
	
    @Autowired
    private ParkingController parkingController;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ParkingManagementService parkingService;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.parkingController).build();
    }
	
	@Test
	public void testBillingSpot_ThreeHoursLimit() {
		Slot spot = new Slot();
		Date date = new Date();
		spot.setStartPark(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, 3);
		c.add(Calendar.MINUTE, -1);
		spot.setStopPark(c.getTime());
		int hoursSpent = spot.getHoursSpentInParking();
		
		// Spent 2h59 minutes in the parking
		assertTrue(hoursSpent == 3);
		
		c.add(Calendar.MINUTE, 1);
		spot.setStopPark(c.getTime());
		hoursSpent = spot.getHoursSpentInParking();
		
		// Spent 3h00 minutes in the parking
		assertTrue(hoursSpent == 4);
	}
	
	@Test
	public void testParkSedanCar() throws Exception {
		Car sedan = new SedanCar();
		sedan.setType(CarType.SEDAN);
		sedan.setPlate("AR511RR");
		ParkResponse response = new ParkResponse();
		response.setCar(sedan);
		response.setMessage("The car has been parked");
		
		when(parkingService.parkCar(CarType.SEDAN, "AR511RR")).thenReturn(response);
		
		MockHttpServletResponse result = mockMvc
        .perform(post("/parking-toll/api/v1/car/{plate}/park?type=SEDAN", "AR511RR"))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse();
		
		ParkResponse responseJson = objectMapper.readValue(result.getContentAsString(), 
        		ParkResponse.class);
		
		assertNotNull(responseJson);
		assertEquals(responseJson.getCar().getPlate(), "AR511RR");
		assertEquals(responseJson.getMessage(), "The car has been parked");
	}
	
	@Test
	public void testGetAvailableSlots() throws Exception {
		ParkingStatus parkingStatus = new ParkingStatus();
		parkingStatus.setAvailableSlots(1);
		parkingStatus.setOccupiedSlots(0);
		parkingStatus.setName("Sedan Car Parking");
		
		when(parkingService.getAvailableParkingSlots(CarType.SEDAN)).thenReturn(parkingStatus);
		
		MockHttpServletResponse response = mockMvc
                .perform(get("/parking-toll/api/v1/car?type=SEDAN"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();
    
		ParkingStatus status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertNotNull(status);
		assertTrue(status.getAvailableSlots() == 1);
	}

	
	@Test
	public void testFreeSlot() throws Exception { 
		
		Car elec20 = new Electric20kwCar();
		elec20.setType(CarType.ELECTRIC_20);
		elec20.setPlate("AR511RR");
		
		Invoice invoice = new Invoice();
		invoice.setBill(2);
		invoice.setHoursSpentInParking(1);
		invoice.setCar(elec20);
		
		when(parkingService.freeSlot(CarType.ELECTRIC_20, "AR511RR")).thenReturn(invoice);
		
		MockHttpServletResponse response = mockMvc
                .perform(post("/parking-toll/api/v1/car/{plate}/free?type=ELECTRIC_20", "AR511RR"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
		
        Invoice responseJson = objectMapper.readValue(response.getContentAsString(), Invoice.class);
        assertNotNull(responseJson);
        assertTrue(responseJson.getBill() == 2);
        assertTrue(responseJson.getHoursSpentInParking() == 1);
        assertTrue(responseJson.getCar().getType().equals(CarType.ELECTRIC_20));
	}
	
}
