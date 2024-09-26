package org.adaschool.Weather;

import org.adaschool.Weather.controller.WeatherReportController;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.adaschool.Weather.data.WeatherApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(WeatherReportController.class)
public class WeatherReportAppTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private RestTemplate restTemplate;

	@MockBean
	private WeatherReportService weatherReportService;

	@Test
	public void testGetWeatherReportController() throws Exception {
		// Given (configuración o precondiciones)
		WeatherReport report = new WeatherReport();
		report.setTemperature(25.0);
		report.setHumidity(60);

		// Mockeamos la respuesta esperada del servicio
		when(weatherReportService.getWeatherReport(anyDouble(), anyDouble()))
				.thenReturn(report);

		// When (acción que estamos probando)
		mockMvc.perform(get("/v1/api/weather-report")
						.param("latitude", "40")
						.param("longitude", "-40"))

				// Then (verificaciones y validaciones de lo que debería suceder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.temperature").value(25.0))
				.andExpect(jsonPath("$.humidity").value(60));

		// Verify (asegurarse de que los métodos se llamaron correctamente)
		verify(weatherReportService, times(1)).getWeatherReport(anyDouble(), anyDouble());
	}

	@Test
	public void testMissingParameters() throws Exception {
		// Given (no se proporcionan parámetros, por lo tanto no hay precondiciones adicionales)

		// When (acción que estamos probando: una petición GET sin parámetros)
		mockMvc.perform(get("/v1/api/weather-report")) // No se envían los parámetros 'latitude' y 'longitude'

				// Then (validamos que la respuesta sea un BadRequest, es decir, 400)
				.andExpect(status().isBadRequest());

		// Verify (como no se hace ninguna llamada al servicio, no es necesario un verify en este caso)
		Mockito.verifyNoInteractions(weatherReportService); // Verificamos que el servicio no fue llamado
	}

}
