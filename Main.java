
//base class shape
abstract class Shape{ 
    String color;
    static String className = "Shape";

    //Constructor
    public Shape(String color){
        this.color = color;
    }

    //Abstract method
    abstract double calculateArea();
    abstract double calculatePerimeter();
    
    //method to display color of the shape
    public void displayColor(){
        System.out.println("Color of the shape is:" + color);
    }
}

//Derived class Triangle
class Triangle extends Shape{
    double base, height, side1, side2, side3;

    //Constructor
    Triangle(String color, double base, double height, double s1, double s2, double s3){
        super(color);
        this.base = base;
        this.height = height;
        this.side1 = s1;
        this.side2 = s2;
        this.side3 = s3;
    }

    //Override calclulate Perimeter thod
    @Override
    double calculatePerimeter(){
        return side1 + side2 + side3;
    }

    //Overriding method to calculate area of triangle
    @Override
    double calculateArea(){
        return 0.5 * base * height;
    }
}

//Derived class Rectangle
class Rectangle extends Shape{
    double length, width;

    //Constructor
    Rectangle(String color, double length, double width){
        super(color);
        this.length = length;
        this.width = width;
    }

    //override calculateArea method
    @Override
    double calculateArea(){
        return length * width;
    }

    //Override calculatePerimeter method
    @Override
    double calculatePerimeter(){
        return 2 * (length + width);
    }
}

//Derived class Circle
class Circle extends Shape{
    double radius;

    //Constructor
    Circle(String color, double radius){
        super(color);
        this.radius = radius;
    }

    //Override calculateArea method
    @Override
    double calculateArea(){
        return Math.PI * radius * radius;
    }

    //Override calculatePerimeter method
    @Override
    double calculatePerimeter(){
        return Math.PI * radius * 2;
    }
}

//Derived class Sqaure
class Square extends Shape{
    double side;

    //Constructor
    Square(String color, double side){
        super(color);
        this.side = side;
    }

    //Override calculateArea method
    @Override
    double calculateArea(){
        return side * side;
    }

    //Override calculatePerimeter method
    @Override
    double calculatePerimeter(){
        return 4 * side;
    }
}

//Main class
public class Main{
    public static void main(String[] args){
        //creating instances of different shapes
        Shape triangle = new Triangle("Red", 2, 4, 5, 6, 7);
        Shape rectangle = new Rectangle("Blue", 10, 20);
        Shape circle = new Circle("Green", 5);
        Shape square = new Square("Yellow", 4);

        //Demonstrating polymorphism
        Shape[] shapes = {triangle, rectangle, circle, square};
        for (Shape shape : shapes){
            System.out.println("Class:" + Shape.className);
            shape.displayColor();
            System.out.println("Area:" + shape.calculateArea());
            System.out.println("Periemeter:" + shape.calculatePerimeter());
            System.out.println();
        }
        
    }
}