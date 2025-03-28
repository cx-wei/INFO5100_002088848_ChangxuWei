package Exercises.exercise5;

//Main class to run those three patterns
import java.util.Random;
import java.util.Scanner;

public class WeatherStation {
    public static void main(String[] args) {
        System.out.println("=== Weather Monitoring System ===");
        System.out.println("Demonstrating Observer, Strategy, and Factory Method patterns\n");
        
        // Observer Pattern Setup
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay();
        ForecastDisplay forecastDisplay = new ForecastDisplay();
        
        weatherData.registerObserver(currentDisplay);
        weatherData.registerObserver(forecastDisplay);
        
        // Strategy Pattern Setup
        WeatherProcessor processor = new WeatherProcessor(new BasicWeatherProcessing());
        
        // Factory Method Pattern Setup
        WeatherReportFactory textFactory = new TextReportFactory();
        WeatherReportFactory htmlFactory = new HTMLReportFactory();
        
        // Simulate weather changes
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        
        for (int i = 0; i < 3; i++) {
            System.out.println("\n=== Weather Update " + (i+1) + " ===");
            
            // Generate random weather data
            float temp = 15 + random.nextFloat() * 20; // random 15-35°C
            float humidity = 30 + random.nextFloat() * 70; // random 30-100%
            float pressure = 980 + random.nextFloat() * 40; // random 980-1020 hPa
            
            // Observer pattern demonstration
            weatherData.setMeasurements(temp, humidity, pressure);
            
            // Strategy pattern demonstration
            System.out.println("\n[Strategy Pattern]");
            System.out.println("Basic Processing:");
            System.out.println(processor.process(temp, humidity, pressure));
            
            processor.setStrategy(new AdvancedWeatherProcessing());
            System.out.println("\nAdvanced Processing:");
            System.out.println(processor.process(temp, humidity, pressure));
            
            // Factory method pattern demonstration
            System.out.println("\n[Factory Method Pattern]");
            System.out.println("Text Report:");
            textFactory.generateAndDisplay(temp, humidity, pressure);
            
            System.out.println("HTML Report:");
            htmlFactory.generateAndDisplay(temp, humidity, pressure);
            
            if (i < 2) {
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
        System.out.println("\n=== End of Demonstration ===");
    }
}