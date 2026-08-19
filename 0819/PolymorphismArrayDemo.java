abstract class Shape {
    private String name;

    Shape(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    abstract double area();
}

class RectangleShape extends Shape {
    private double width;
    private double height;

    RectangleShape(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }

    @Override
    double area() {
        return width * height;
    }
}

class CircleShape extends Shape {
    private double radius;

    CircleShape(double radius) {
        super("Circle");
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

public class PolymorphismArrayDemo {
    public static void main(String[] args) {
        Shape[] shapes = {
            new RectangleShape(4, 5),
            new CircleShape(3)
        };

        for (Shape shape : shapes) {
            System.out.printf("%s area=%.2f%n",
                shape.getName(), shape.area());
        }
    }
}