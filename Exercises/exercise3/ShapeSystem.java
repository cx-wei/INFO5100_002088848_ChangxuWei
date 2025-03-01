package Exercises.exercise3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


abstract class Shape implements Serializable {
    //base class from exercise 2
    String color;
    static String className = "Shape";
    
    // Constructor
    public Shape(String color) {
        this.color = color;
    }
    
    // Abstract method
    abstract double calculateArea();
    abstract double calculatePerimeter();
    
    // Method to display color of the shape
    public void displayColor() {
        System.out.println("Color: " + color);
    }
}

// Derived class Triangle from exercerise 2
class Triangle extends Shape {
    double base, height, side1, side2, side3;

    // Constructor
    Triangle(String color, double base, double height, double side1, double side2, double side3) {
        super(color);
        this.base = base;
        this.height = height;
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    // Override calculateArea method
    @Override
    double calculateArea() {
        return 0.5 * base * height;
    }

    // Override 
    @Override
    double calculatePerimeter() {
        return side1 + side2 + side3;
    }
}

// Derived class Rectangle from exercerise 2
class Rectangle extends Shape {
    double length, width;

    // Constructor
    Rectangle(String color, double length, double width) {
        super(color);
        this.length = length;
        this.width = width;
    }

    // Override calculateArea method
    @Override
    double calculateArea() {
        return length * width;
    }

    // Override calculatePerimeter method
    @Override
    double calculatePerimeter() {
        return 2 * (length + width);
    }
}

// Derived class Circle from exercerise 2
class Circle extends Shape {
    double radius;

    // Constructor
    Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    // Override calculateArea method
    @Override
    double calculateArea() {
        return Math.PI * radius * radius;
    }

    // Override calculatePerimeter method
    @Override
    double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

// Derived class Square from exercerise 2
class Square extends Shape {
    double side;

    // Constructor
    Square(String color, double side) {
        super(color);
        this.side = side;
    }

    // Override calculateArea method
    @Override
    double calculateArea() {
        return side * side;
    }

    // Override calculatePerimeter method
    @Override
    double calculatePerimeter() {
        return 4 * side;
    }
}


//Make the classes in exercise # 2 serializable
//Test serialization of the objects of the classes
//created in exercise # 2.

public class ShapeSystem {
    //main class from exercise 2
    public static void main(String[] args) {
        // Create instances of different shapes
        Shape triangle = new Triangle("Red", 5, 10, 5, 5, 5);
        Shape rectangle = new Rectangle("Blue", 10, 20);
        Shape circle = new Circle("Green", 7);
        Shape square = new Square("Yellow", 6);
    
    //trying sth new: to serialize the objests
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("shapes.ser"))) {
            oos.writeObject(triangle);
            oos.writeObject(rectangle);
            oos.writeObject(circle);
            oos.writeObject(square);
            System.out.println("Shapes have been serialized to shapes.ser");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

    //and deserialization of the objects of the classes
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("shapes.ser"))) {
            Shape deserializedTriangle = (Shape) ois.readObject();
            Shape deserializedRectangle = (Shape) ois.readObject();
            Shape deserializedCircle = (Shape) ois.readObject();
            Shape deserializedSquare = (Shape) ois.readObject();

            //output, starting to check all the methods
            System.out.println("Deserialized Shapes:");
            
            deserializedTriangle.displayColor();
            System.out.println("Area: " + triangle.calculateArea());
            System.out.println("Perimeter: " + triangle.calculatePerimeter());

            deserializedRectangle.displayColor();
            System.out.println("Area: " + rectangle.calculateArea());
            System.out.println("Perimeter: " + rectangle.calculatePerimeter());

            deserializedCircle.displayColor();
            System.out.println("Area: " + circle.calculateArea());
            System.out.println("Perimeter: " + circle.calculatePerimeter());

            deserializedSquare.displayColor();
            System.out.println("Area: " + square.calculateArea());
            System.out.println("Perimeter: " + square.calculatePerimeter());

        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}

