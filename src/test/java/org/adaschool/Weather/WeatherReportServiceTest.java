package org.adaschool.Weather;

import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.webservices.server.AutoConfigureMockWebServiceClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class WeatherReportServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private WeatherReportService weatherReportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherReportService = new WeatherReportService();
        weatherReportService .setRestTemplate(restTemplate);
    }

    @Test
    public void testGetWeatherReport() {
        // Arrange
        double latitude = 10.0;
        double longitude = 20.0;

        WeatherApiResponse weatherApiResponse = new WeatherApiResponse();
        WeatherApiResponse.Main main = new WeatherApiResponse.Main();
        main.setTemperature(5.0);
        main.setHumidity(60);
        weatherApiResponse.setMain(main);

        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class)))
                .thenReturn(weatherApiResponse);

        // Act
        WeatherReport report = weatherReportService.getWeatherReport(latitude, longitude);

        // Assert
        assertEquals(5.0, report.getTemperature());
        assertEquals(60.0, report.getHumidity());

        verify(restTemplate, times(1))
                .getForObject(anyString(), eq(WeatherApiResponse.class));
    }
}
