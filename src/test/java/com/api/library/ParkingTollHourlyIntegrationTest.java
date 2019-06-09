package com.api.library;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.api.library.entity.CarType;
import com.api.library.response.Invoice;
import com.api.library.response.ParkResponse;
import com.api.library.response.ParkingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("hourly")
public class ParkingTollHourlyIntegrationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void test1_testParkSedanCar_parkOneSedanCar() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(post("/parking-toll/api/v1/car/{plate}/park?type=SEDAN", "AR511RR"))
				.andExpect(status().isCreated()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		ParkResponse responseJson = objectMapper.readValue(response.getContentAsString(), ParkResponse.class);

		assertNotNull(responseJson.getCar());
		assertTrue(responseJson.getCar().getType().equals(CarType.SEDAN));
		assertEquals(responseJson.getMessage(), "The car has been parked");

	}

	@Test
	public void test2_testFreeSedanCar_freeTheParkedSedanCar() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(post("/parking-toll/api/v1/car/{plate}/free?type=SEDAN", "AR511RR"))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		Invoice responseJson = objectMapper.readValue(response.getContentAsString(), Invoice.class);

		assertNotNull(responseJson.getCar());
		assertTrue(responseJson.getCar().getType().equals(CarType.SEDAN));
		assertTrue(responseJson.getHoursSpentInParking() == 1);
		assertTrue(responseJson.getBill() == 2);

	}
	
	@Test
	public void test3_testGetParkingStatusForSedanCars() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(get("/parking-toll/api/v1/car?type=SEDAN"))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		ParkingStatus status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 6);
		assertTrue(status.getOccupiedSlots() == 0);
		assertEquals(status.getName(), "Parking for Sedan Cars");
	}
	
	@Test
	public void test4_testGetParkingStatusForElectric20Cars() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(get("/parking-toll/api/v1/car?type=ELECTRIC_20"))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		ParkingStatus status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 10);
		assertTrue(status.getOccupiedSlots() == 0);
		assertEquals(status.getName(), "Parking for 20kw Electric Cars");
	}
	
	@Test
	public void test5_testGetParkingStatusForElectric50Cars() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(get("/parking-toll/api/v1/car?type=ELECTRIC_50"))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		ParkingStatus status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 15);
		assertTrue(status.getOccupiedSlots() == 0);
		assertEquals(status.getName(), "Parking for 50kw Electric Cars");
	}

}
