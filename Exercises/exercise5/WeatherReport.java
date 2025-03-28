package Exercises.exercise5;

//Method 3 Factory Method Pattern
// WeatherReport

abstract class WeatherReport {
    protected float temperature;
    protected float humidity;
    protected float pressure;

    public WeatherReport(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public abstract void generateReport();
}

class TextWeatherReport extends WeatherReport {
    public TextWeatherReport(float temperature, float humidity, float pressure) {
        super(temperature, humidity, pressure);
    }

    @Override
    public void generateReport() {
        System.out.println("=== Text Weather Report ===");
        System.out.printf("Temperature: %.1f°C%n", temperature);
        System.out.printf("Humidity: %.1f%%%n", humidity);
        System.out.printf("Pressure: %.1f hPa%n%n", pressure);
    }
}

class HTMLWeatherReport extends WeatherReport {
    public HTMLWeatherReport(float temperature, float humidity, float pressure) {
        super(temperature, humidity, pressure);
    }

    @Override
    public void generateReport() {
        System.out.println("=== HTML Weather Report ===");
        System.out.println("<html><body>");
        System.out.println("<h1>Weather Report</h1>");
        System.out.printf("<p>Temperature: %.1f°C</p>%n", temperature);
        System.out.printf("<p>Humidity: %.1f%%</p>%n", humidity);
        System.out.printf("<p>Pressure: %.1f hPa</p>%n", pressure);
        System.out.println("</body></html>\n");
    }
}

abstract class WeatherReportFactory {
    public abstract WeatherReport createReport(float temperature, float humidity, float pressure);
    
    public void generateAndDisplay(float temperature, float humidity, float pressure) {
        WeatherReport report = createReport(temperature, humidity, pressure);
        report.generateReport();
    }
}

class TextReportFactory extends WeatherReportFactory {
    @Override
    public WeatherReport createReport(float temperature, float humidity, float pressure) {
        return new TextWeatherReport(temperature, humidity, pressure);
    }
}

class HTMLReportFactory extends WeatherReportFactory {
    @Override
    public WeatherReport createReport(float temperature, float humidity, float pressure) {
        return new HTMLWeatherReport(temperature, humidity, pressure);
    }
}

