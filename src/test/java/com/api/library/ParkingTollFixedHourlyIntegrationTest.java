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

import com.api.library.entity.SedanCar;
import com.api.library.exception.ApiError;
import com.api.library.response.Invoice;
import com.api.library.response.ParkResponse;
import com.api.library.response.ParkingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("fixedHourly")
public class ParkingTollFixedHourlyIntegrationTest {

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
		assertTrue(responseJson.getCar() instanceof SedanCar);
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
		assertTrue(responseJson.getCar() instanceof SedanCar);
		assertTrue(responseJson.getHoursSpentInParking() == 1);
		assertTrue(responseJson.getBill() == 6);

	}

	@Test
	public void test3_testGetParkingStatus() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(get("/parking-toll/api/v1/car?type=SEDAN"))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		ParkingStatus status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 2);
		assertTrue(status.getOccupiedSlots() == 0);
		assertEquals(status.getName(), "Parking for Sedan Cars");
	}

	// testing the parking is full
	// use case:
	// - check the parking status for Sedan cars (2 slots available)
	// - park two cars
	// - check the parking status for Sedan cars (0 slots available)
	// - park the third car
	// - check the response
	// - check the parking status for Sedan cars (again 0 slots available)

	@Test
	public void test4_testParkingFull() throws Exception {

		// check the parking status before cars coming
		MockHttpServletResponse response = mockMvc.perform(get("/parking-toll/api/v1/car?type=SEDAN"))
				.andExpect(status().isOk()).andReturn().getResponse();
		ParkingStatus status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 2);

		// Calling the API to park two cars
		mockMvc.perform(post("/parking-toll/api/v1/car/{plate}/park?type=SEDAN", "AR511RR"))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/parking-toll/api/v1/car/{plate}/park?type=SEDAN", "AB111RR"))
				.andExpect(status().isCreated());

		// check the parking status before the third car coming
		response = mockMvc.perform(get("/parking-toll/api/v1/car?type=SEDAN")).andExpect(status().isOk())
				.andReturn().getResponse();
		status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 0);

		// Calling the API to park a third car: an exception will be thrown (there are
		// only two parking slots for Sedan)
		response = mockMvc.perform(post("/parking-toll/api/v1/car/{plate}/park?type=SEDAN", "AH876UU"))
				.andExpect(status().isNotAcceptable()).andReturn().getResponse();

		ApiError error = objectMapper.readValue(response.getContentAsString(), ApiError.class);

		assertNotNull(error);
		assertEquals(error.getCode(), "error.parking.full");
		assertEquals(error.getMessage(), "The Parking is FULL !");

		// check the parking status is always 0 slots available
		response = mockMvc.perform(get("/parking-toll/api/v1/car?type=SEDAN")).andExpect(status().isOk())
				.andReturn().getResponse();
		status = objectMapper.readValue(response.getContentAsString(), ParkingStatus.class);
		assertTrue(status.getAvailableSlots() == 0);

		// free the parking slots
		mockMvc.perform(post("/parking-toll/api/v1/car/{plate}/free?type=SEDAN", "AR511RR"))
				.andExpect(status().isOk());
		mockMvc.perform(post("/parking-toll/api/v1/car/{plate}/free?type=SEDAN", "AB111RR"))
				.andExpect(status().isOk());

	}

	// this test will only prove that there is no reason that two cars with the same
	// plate can park
	@Test
	public void test5_testParkSedanCar_parkTwoSedanCarsWithSamePlate() throws Exception {
		mockMvc.perform(post("/parking-toll/api/v1/car/{plate}/park?type=SEDAN", "AR511RR"))
				.andExpect(status().isCreated());
		MockHttpServletResponse response = mockMvc
				.perform(post("/parking-toll/api/v1/car/{carType}/park?type=SEDAN", "AR511RR"))
				.andExpect(status().isNotAcceptable()).andReturn().getResponse();

		ApiError error = objectMapper.readValue(response.getContentAsString(), ApiError.class);

		assertNotNull(error);
		assertEquals(error.getCode(), "error.car.already.parked");
		assertEquals(error.getMessage(), "The car with this plate is already in the Parking");
	}

	@Test
	public void test6_testParkSedanCar_WrongCarType() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(post("/parking-toll/api/v1/car/{plate}/park?type=WRONG", "AR511RR"))
				.andExpect(status().isBadRequest()).andReturn().getResponse();

		ApiError error = objectMapper.readValue(response.getContentAsString(), ApiError.class);

		assertNotNull(error);
		assertEquals(error.getCode(), "error.wrong.car.type");
		assertEquals(error.getMessage(), "The car type 'WRONG' is not allowed");
	}

	@Test
	public void test7_testFreeSedanCar_freeWrongCarWithNotExistingPlate() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(post("/parking-toll/api/v1/car/{plate}/free?type=SEDAN", "NOT_EXISTING"))
				.andExpect(status().isNotFound()).andReturn().getResponse();
		assertThat(response.getContentAsString()).isNotEmpty();

		ApiError error = objectMapper.readValue(response.getContentAsString(), ApiError.class);

		assertNotNull(error);
		assertEquals(error.getCode(), "error.no.car.found");
		assertEquals(error.getMessage(), "There is no car to leave with plate NOT_EXISTING");
	}

	@Test
	public void test8_testFreeSedanCar_WrongCarType() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(post("/parking-toll/api/v1/car/{plate}/free?type=AAA", "AR511RR"))
				.andExpect(status().isBadRequest()).andReturn().getResponse();

		ApiError error = objectMapper.readValue(response.getContentAsString(), ApiError.class);

		assertNotNull(error);
		assertEquals(error.getMessage(), "The car type 'AAA' is not allowed");
	}

	@Test
	public void test9_testGetAvailableSlots_WrongCarType() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(get("/parking-toll/api/v1/car?type=AAA"))
				.andExpect(status().isBadRequest()).andReturn().getResponse();

		ApiError error = objectMapper.readValue(response.getContentAsString(), ApiError.class);
		
		assertNotNull(error);
		assertEquals(error.getMessage(), "The car type 'AAA' is not allowed");
	}

}
