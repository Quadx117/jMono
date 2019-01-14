package framework.math;

import static org.testng.Assert.*;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Quaternion;
import jMono_Framework.math.Vector3;
import jMono_Framework.math.Vector4;

import org.testng.annotations.Test;

import utilities.FloatComparer;
import utilities.QuaternionComparer;

public class QuaternionTest
{
    private void compare(Quaternion expected, Quaternion source)
    {
        assertTrue(QuaternionComparer.Epsilon.equals(source, expected));
    }

    private void compare(float expected, float source)
    {
        assertTrue(FloatComparer.Epsilon.equals(source, expected));
    }

    @Test
    public void constructors()
    {
        Quaternion expected = new Quaternion();
        expected.x = 1;
        expected.y = 2;
        expected.z = 3;
        expected.w = 4;
        compare(expected, new Quaternion(1, 2, 3, 4));
        compare(expected, new Quaternion(new Vector3(1, 2, 3), 4));
        compare(expected, new Quaternion(new Vector4(1, 2, 3, 4)));
    }

    @Test
    public void properties()
    {
        compare(new Quaternion(0, 0, 0, 1), Quaternion.identity());
    }

    @Test
    public void add()
    {
        Quaternion q1 = new Quaternion(1, 2, 3, 4);
        Quaternion q2 = new Quaternion(1, 2, 3, 4);
        Quaternion expected = new Quaternion(2, 4, 6, 8);
        compare(expected, Quaternion.add(q1, q2));

        Quaternion result = new Quaternion();
        Quaternion.add(q1, q2, result);
        compare(expected, result);
    }

    @Test
    public void concatenate()
    {
        Quaternion q1 = new Quaternion(1, 2.5f, 3, 4);
        Quaternion q2 = new Quaternion(1, 2, -3.8f, 2);
        Quaternion expected = new Quaternion(21.5f, 6.2f, -8.7f, 13.4f);
        compare(expected, Quaternion.concatenate(q1, q2));

        Quaternion result = new Quaternion();
        Quaternion.concatenate(q1, q2, result);
        compare(expected, result);
    }

    @Test
    public void conjugate()
    {
        Quaternion q = new Quaternion(1, 2, 3, 4);
        Quaternion expected = new Quaternion(-1, -2, -3, 4);
        compare(expected, Quaternion.conjugate(q));

        Quaternion result = new Quaternion();
        Quaternion.conjugate(q, result);
        compare(expected, result);

        q.conjugate();
        compare(expected, q);
    }

    @Test
    public void createFromAxisAngle()
    {
        Vector3 axis = new Vector3(0.5f, 1.1f, -3.8f);
        float angle = 0.25f;
        Quaternion expected = new Quaternion(0.06233737f, 0.1371422f, -0.473764f, 0.9921977f);

        compare(expected, Quaternion.createFromAxisAngle(axis, angle));

        Quaternion result = new Quaternion();
        Quaternion.createFromAxisAngle(axis, angle, result);
        compare(expected, result);
    }

    @Test
    public void createFromRotationMatrix()
    {
        Matrix matrix = Matrix.createFromYawPitchRoll(0.15f, 1.18f, -0.22f);
        Quaternion expected = new Quaternion(0.5446088f, 0.1227905f, -0.1323988f, 0.8190203f);
        compare(expected, Quaternion.createFromRotationMatrix(matrix));

        Quaternion result = new Quaternion();
        Quaternion.createFromRotationMatrix(matrix, result);
        compare(expected, result);
    }

    @Test
    public void createFromYawPitchRoll()
    {
        Quaternion expected = new Quaternion(0.5446088f, 0.1227905f, -0.1323988f, 0.8190203f);
        compare(expected, Quaternion.createFromYawPitchRoll(0.15f, 1.18f, -0.22f));

        Quaternion result = new Quaternion();
        Quaternion.createFromYawPitchRoll(0.15f, 1.18f, -0.22f, result);
        compare(expected, result);
    }

    @Test
    public void divide()
    {
        Quaternion q1 = new Quaternion(1, 2, 3, 4);
        Quaternion q2 = new Quaternion(0.2f, -0.6f, 11.9f, 0.01f);
        Quaternion expected = new Quaternion(-0.1858319f, 0.09661285f, -0.3279344f, 0.2446305f);
        compare(expected, Quaternion.divide(q1, q2));

        Quaternion result = new Quaternion();
        Quaternion.divide(q1, q2, result);
        compare(expected, result);
    }

    @Test
    public void length()
    {
        Quaternion q1 = new Quaternion(1, 2, 3, 4);
        compare(5.477226f, q1.length());
    }

    @Test
    public void lengthSquared()
    {
        Quaternion q1 = new Quaternion(1, 2, 3, 4);
        compare(30.0f, q1.lengthSquared());
    }

    @Test
    public void Normalize()
    {
        Quaternion q = new Quaternion(1, 2, 3, 4);
        Quaternion expected = new Quaternion(0.1825742f, 0.3651484f, 0.5477226f, 0.7302967f);

        compare(expected, Quaternion.normalize(q));

        Quaternion result = new Quaternion();
        Quaternion.normalize(q, result);
        compare(expected, result);

        q.normalize();
        compare(expected, q);
    }

}
