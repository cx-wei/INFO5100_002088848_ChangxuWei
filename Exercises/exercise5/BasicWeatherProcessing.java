package Exercises.exercise5;

//Method 2 Strategy Pattern
// WeatherProcessingStrategy

interface WeatherProcessingStrategy {
    String processWeatherData(float temperature, float humidity, float pressure);
}

class BasicWeatherProcessing implements WeatherProcessingStrategy {
    @Override
    public String processWeatherData(float temperature, float humidity, float pressure) {
        return String.format("Basic Report: Temp=%.1f°C, Hum=%.1f%%, Pres=%.1fhPa", 
               temperature, humidity, pressure);
    }
}

class AdvancedWeatherProcessing implements WeatherProcessingStrategy {
    @Override
    public String processWeatherData(float temperature, float humidity, float pressure) {
        String comfort;
        if (temperature > 25 && humidity > 70) comfort = "Uncomfortably hot and humid";
        else if (temperature < 5) comfort = "Very cold";
        else comfort = "Comfortable";
        
        return String.format("Advanced Report: Temp=%.1f°C (%s), Hum=%.1f%%, Pres=%.1fhPa", 
               temperature, comfort, humidity, pressure);
    }
}

class WeatherProcessor {
    private WeatherProcessingStrategy strategy;

    public WeatherProcessor(WeatherProcessingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(WeatherProcessingStrategy strategy) {
        this.strategy = strategy;
    }

    public String process(float temperature, float humidity, float pressure) {
        return strategy.processWeatherData(temperature, humidity, pressure);
    }
}

