package framework;

import static org.testng.Assert.*;
import jMono_Framework.Point;
import jMono_Framework.Rectangle;
import jMono_Framework.math.Vector2;

import org.testng.annotations.Test;

public class RectangleTest
{
    @Test
    public void constructorsAndProperties()
    {
        Rectangle rectangle = new Rectangle(10, 20, 64, 64);

        // Constructor

        Rectangle rect = new Rectangle();
        rect.x = 10;
        rect.y = 20;
        rect.width = 64;
        rect.height = 64;
        assertEquals(rect, rectangle);

        // Constructor 2

        rect.x = 1;
        rect.y = 2;
        rect.width = 4;
        rect.height = 45;
        assertEquals(rect, new Rectangle(new Point(1, 2), new Point(4, 45)));

        // Left property

        assertEquals(10, rectangle.left());

        // Right property

        assertEquals(64 + 10, rectangle.right());

        // Top property

        assertEquals(20, rectangle.top());

        // Bottom property

        assertEquals(64 + 20, rectangle.bottom());

        // Location property

        assertEquals(new Point(10, 20), rectangle.getLocation());

        // Center property

        assertEquals(new Point(10 + 32, 20 + 32), rectangle.getCenter());

        // Size property

        assertEquals(new Point(64, 64), rectangle.getSize());

        // IsEmpty property

        assertEquals(false, rectangle.isEmpty());
        assertEquals(true, new Rectangle().isEmpty());

        // Empty - static property

        assertEquals(new Rectangle(), Rectangle.empty());
    }

    @Test
    public void containsPoint()
    {
        Rectangle rectangle = new Rectangle(0, 0, 64, 64);

        Point p1 = new Point(-1, -1);
        Point p2 = new Point(0, 0);
        Point p3 = new Point(32, 32);
        Point p4 = new Point(63, 63);
        Point p5 = new Point(64, 64);

        // boolean result;

        // rectangle.contains(p1, result);
        // assertEquals(false, result);
        // rectangle.contains(p2, result);
        // assertEquals(true, result);
        // rectangle.contains(p3, result);
        // assertEquals(true, result);
        // rectangle.contains(p4, result);
        // assertEquals(true, result);
        // rectangle.contains(p5, result);
        // assertEquals(false, result);

        assertEquals(false, rectangle.contains(p1));
        assertEquals(true, rectangle.contains(p2));
        assertEquals(true, rectangle.contains(p3));
        assertEquals(true, rectangle.contains(p4));
        assertEquals(false, rectangle.contains(p5));
    }

    @Test
    public void containsVector2()
    {
        Rectangle rectangle = new Rectangle(0, 0, 64, 64);

        Vector2 p1 = new Vector2(-1, -1);
        Vector2 p2 = new Vector2(0, 0);
        Vector2 p3 = new Vector2(32, 32);
        Vector2 p4 = new Vector2(63, 63);
        Vector2 p5 = new Vector2(64, 64);

        // boolean result;

        // rectangle.contains(p1, result);
        // assertEquals(false, result);
        // rectangle.contains(p2, result);
        // assertEquals(true, result);
        // rectangle.contains(p3, result);
        // assertEquals(true, result);
        // rectangle.contains(p4, result);
        // assertEquals(true, result);
        // rectangle.contains(p5, result);
        // assertEquals(false, result);

        assertEquals(false, rectangle.contains(p1));
        assertEquals(true, rectangle.contains(p2));
        assertEquals(true, rectangle.contains(p3));
        assertEquals(true, rectangle.contains(p4));
        assertEquals(false, rectangle.contains(p5));
    }

    @Test
    public void containsInts()
    {
        Rectangle rectangle = new Rectangle(0, 0, 64, 64);

        int x1 = -1;
        int y1 = -1;
        int x2 = 0;
        int y2 = 0;
        int x3 = 32;
        int y3 = 32;
        int x4 = 63;
        int y4 = 63;
        int x5 = 64;
        int y5 = 64;

        assertEquals(false, rectangle.contains(x1, y1));
        assertEquals(true, rectangle.contains(x2, y2));
        assertEquals(true, rectangle.contains(x3, y3));
        assertEquals(true, rectangle.contains(x4, y4));
        assertEquals(false, rectangle.contains(x5, y5));
    }

    @Test
    public void containsFloats()
    {
        Rectangle rectangle = new Rectangle(0, 0, 64, 64);

        float x1 = -1;
        float y1 = -1;
        float x2 = 0;
        float y2 = 0;
        float x3 = 32;
        float y3 = 32;
        float x4 = 63;
        float y4 = 63;
        float x5 = 64;
        float y5 = 64;

        assertEquals(false, rectangle.contains(x1, y1));
        assertEquals(true, rectangle.contains(x2, y2));
        assertEquals(true, rectangle.contains(x3, y3));
        assertEquals(true, rectangle.contains(x4, y4));
        assertEquals(false, rectangle.contains(x5, y5));
    }

    @Test
    public void containsRectangle()
    {
        Rectangle rectangle = new Rectangle(0, 0, 64, 64);
        Rectangle rect1 = new Rectangle(-1, -1, 32, 32);
        Rectangle rect2 = new Rectangle(0, 0, 32, 32);
        Rectangle rect3 = new Rectangle(0, 0, 64, 64);
        Rectangle rect4 = new Rectangle(1, 1, 64, 64);

        // boolean result;

        // rectangle.contains(rect1, result);

        // assertEquals(false, result);

        // rectangle.contains(rect2, result);

        // assertEquals(true, result);

        // rectangle.contains(rect3, result);

        // assertEquals(true, result);

        // rectangle.contains(rect4, result);

        // assertEquals(false, result);

        assertEquals(false, rectangle.contains(rect1));
        assertEquals(true, rectangle.contains(rect2));
        assertEquals(true, rectangle.contains(rect3));
        assertEquals(false, rectangle.contains(rect4));
    }

    @Test
    public void inflate()
    {
        Rectangle rectangle = new Rectangle(0, 0, 64, 64);
        rectangle.inflate(10, -10);
        assertEquals(new Rectangle(-10, 10, 84, 44), rectangle);
        Rectangle rectangleF = new Rectangle(0, 0, 64, 64);
        rectangleF.inflate(10.0f, -10.0f);
        assertEquals(new Rectangle(-10, 10, 84, 44), rectangleF);
    }

    @Test
    public void intersect()
    {
        Rectangle first = new Rectangle(0, 0, 64, 64);
        Rectangle second = new Rectangle(-32, -32, 64, 64);
        Rectangle expected = new Rectangle(0, 0, 32, 32);

        // First overload testing(forward and backward)

        Rectangle result = new Rectangle();
        Rectangle.intersect(first, second, result);

        assertEquals(expected, result);

        Rectangle.intersect(second, first, result);

        assertEquals(expected, result);

        // Second overload testing(forward and backward)

        assertEquals(expected, Rectangle.intersect(first, second));
        assertEquals(expected, Rectangle.intersect(second, first));
    }

    @Test
    public void union()
    {
        Rectangle first = new Rectangle(-64, -64, 64, 64);
        Rectangle second = new Rectangle(0, 0, 64, 64);
        Rectangle expected = new Rectangle(-64, -64, 128, 128);

        // First overload testing(forward and backward)

        Rectangle result = new Rectangle();
        Rectangle.union(first, second, result);

        assertEquals(expected, result);

        Rectangle.union(second, first, result);

        assertEquals(expected, result);

        // Second overload testing(forward and backward)

        assertEquals(expected, Rectangle.union(first, second));
        assertEquals(expected, Rectangle.union(second, first));
    }

    @Test
    public void toStringTest()
    {
        assertEquals("{X:-10 Y:10 Width:100 Height:1000}", new Rectangle(-10, 10, 100, 1000).toString());
    }

}
