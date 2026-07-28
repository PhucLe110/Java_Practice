// 1. Lớp cơ sở Shape
class Shape {
    protected double width;
    protected double height;

    public Shape(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void displayInfo() {
        System.out.println("SHAPE: ");
        System.out.println("- Width: " + width);
        System.out.println("- Height: " + height);
    }
}

// 2. Lớp con Rectangle
class Rectangle extends Shape {

    public Rectangle(double width, double height) {
        super(width, height);
    }

    public double getArea() {
        return width * height;
    }

    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public void displayInfo() {
        System.out.println("RECTANGLE: ");
        System.out.println("- Width: " + width + " | Height: " + height);
        System.out.println("- Area: " + getArea());
        System.out.println("- Perimeter: " + getPerimeter());
    }
}

// 3. Lớp con Circle 
class Circle extends Shape {

    public Circle(double diameter) {
        super(diameter, diameter);
    }

    public double getRadius() {
        return width / 2.0;
    }

    public double getArea() {
        double r = getRadius();
        return Math.PI * r * r;
    }

    public double getCircumference() {
        return width * 3.14;
    }

    @Override
    public void displayInfo() {
        System.out.println("CIRCLE: ");
        System.out.println("- Diameter: " + width + " | Radius: " + getRadius());
        System.out.println("- Area: " + getArea());
        System.out.println("- Circumference: " + getCircumference());
    }
}

// 4. Lớp chính
public class Practice1 {
    public static void main(String[] args) {
        Shape genericShape = new Shape(10.0, 20.0);
        Rectangle rectangle = new Rectangle(5.0, 8.0);
        Circle circle = new Circle(10.0);

        genericShape.displayInfo();
        System.out.println();

        rectangle.displayInfo();
        System.out.println();

        circle.displayInfo();
    }
}