package framework.math;

import static org.testng.Assert.*;
import jMono_Framework.Point;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Quaternion;
import jMono_Framework.math.Vector2;

import org.testng.annotations.Test;

import utilities.Vector2Comparer;

public class Vector2Test
{
    @Test
    public void constructors()
    {
        Vector2 expectedResult = new Vector2(1, 2);

        Vector2 expectedResult2 = new Vector2(2.2f, 2.2f);

        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(), Vector2.Zero()));
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(1, 2), expectedResult));
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(expectedResult), expectedResult));
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(2.2f), expectedResult2));
    }

    @Test
    public void properties()
    {
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(0, 0), Vector2.Zero()));
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(1, 1), Vector2.One()));
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(1, 0), Vector2.UnitX()));
        assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(0, 1), Vector2.UnitY()));
    }

    @Test
    public void dotProduct()
    {
        Vector2 vector1 = new Vector2(1, 2);
        Vector2 vector2 = new Vector2(0.5f, -3.8f);
        float expectedResult = -7.1f;

        assertEquals(vector1.dot(vector2), expectedResult, 0);

        // TODO(Eric): This method is not implemented in java (need a wrapper on the primitive).
        // float result;
        // Vector2.dot(vector1, vector2, result);

        // assertEquals(result, expectedResult);
    }

    @Test
    public void length()
    {
        Vector2 vector1 = new Vector2(1, 2);
        assertEquals(2.236067977499f, vector1.length(), 0);
    }

    @Test
    public void lengthSquared()
    {
        Vector2 vector1 = new Vector2(1, 2);
        assertEquals(5, vector1.lengthSquared(), 0);
    }

    @Test
    public void add()
    {
        Vector2 vector = new Vector2(1, 2);

        // Test add 0.0
        Vector2 expectedResult = new Vector2(vector);
        assertTrue(new Vector2(vector).add(0).equals(expectedResult));
        assertTrue(new Vector2(vector).add(Vector2.Zero()).equals(expectedResult));
        assertTrue(Vector2.add(Vector2.Zero(), vector).equals(expectedResult));
        assertTrue(Vector2.add(vector, Vector2.Zero()).equals(expectedResult));

        // Test add 2
        Vector2 scaledVec = new Vector2(vector.x + 2, vector.y + 2);
        assertTrue(new Vector2(vector).add(2).equals(scaledVec));
        assertTrue(new Vector2(vector).add(new Vector2(2)).equals(scaledVec));
        assertTrue(Vector2.add(new Vector2(2), vector).equals(scaledVec));
        assertTrue(Vector2.add(vector, new Vector2(2)).equals(scaledVec));
        assertTrue(Vector2.add(vector, new Vector2(2.0f)).equals(scaledVec));
        assertTrue(Vector2.add(new Vector2(2), vector).equals(Vector2.add(vector, new Vector2(2.0f))));
        assertTrue(Vector2.add(new Vector2(2), vector).equals(Vector2.add(vector, new Vector2(2))));

        Vector2 vector2 = new Vector2(2, 2);

        // Test two vectors addition.
        assertTrue(new Vector2(vector.x + vector2.x, vector.y + vector2.y).equals(Vector2.add(vector, vector2)));
        assertTrue(Vector2.add(vector2, vector).equals(new Vector2(vector.x + vector2.x, vector.y + vector2.y)));
        assertTrue(Vector2.add(vector, vector2).equals(Vector2.add(vector, vector2)));

        Vector2 refVec = new Vector2();

        // Overloads comparison
        Vector2 vector3 = Vector2.add(vector, vector2);
        Vector2.add(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void subtract()
    {
        Vector2 vector = new Vector2(1, 2);

        // Test subtract 0.0
        Vector2 expectedResult = new Vector2(vector);
        assertTrue(new Vector2(vector).subtract(0).equals(expectedResult));
        assertTrue(new Vector2(vector).subtract(Vector2.Zero()).equals(expectedResult));
        assertTrue(Vector2.subtract(Vector2.Zero(), vector).equals(Vector2.negate(expectedResult)));
        assertTrue(Vector2.subtract(vector, Vector2.Zero()).equals(expectedResult));

        // Test subtract 2
        Vector2 scaledVec = new Vector2(vector.x - 2, vector.y - 2);
        Vector2 scaledVec2 = new Vector2(2 - vector.x, 2 - vector.y);
        assertTrue(new Vector2(vector).subtract(2).equals(scaledVec));
        assertTrue(new Vector2(vector).subtract(new Vector2(2)).equals(scaledVec));
        assertTrue(Vector2.subtract(new Vector2(2), vector).equals(scaledVec2));
        assertTrue(Vector2.subtract(vector, new Vector2(2)).equals(scaledVec));
        assertTrue(Vector2.subtract(vector, new Vector2(2.0f)).equals(scaledVec));
        assertTrue(Vector2.subtract(vector, new Vector2(2)).equals(Vector2.subtract(vector, new Vector2(2.0f))));
        assertTrue(Vector2.subtract(vector, new Vector2(2)).equals(Vector2.subtract(vector, new Vector2(2))));

        Vector2 vector2 = new Vector2(2, 2);

        // Test two vectors subtraction.
        assertTrue(new Vector2(vector.x - vector2.x, vector.y - vector2.y).equals(Vector2.subtract(vector, vector2)));
        assertTrue(Vector2.subtract(vector2, vector).equals(new Vector2(vector2.x - vector.x, vector2.y - vector.y)));
        assertFalse(Vector2.subtract(vector, vector2).equals(Vector2.subtract(vector2, vector)));

        Vector2 refVec = new Vector2();

        // Overloads comparison
        Vector2 vector3 = Vector2.subtract(vector, vector2);
        Vector2.subtract(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void multiply()
    {
        Vector2 vector = new Vector2(1, 2);

        // Test 0.0 scale.
        assertTrue(new Vector2(vector).multiply(0).equals(Vector2.Zero()));
        assertTrue(new Vector2(vector).multiply(Vector2.Zero()).equals(Vector2.Zero()));
        assertTrue(Vector2.multiply(0, vector).equals(Vector2.Zero()));
        assertTrue(Vector2.multiply(vector, 0).equals(Vector2.Zero()));
        assertTrue(Vector2.multiply(vector, 0).equals(Vector2.multiply(vector, 0.0f)));

        // Test 1.0 scale.
        Vector2 expectedResult = new Vector2(vector);
        assertTrue(vector.multiply(1).equals(expectedResult));
        assertTrue(vector.multiply(Vector2.One()).equals(expectedResult));
        assertTrue(Vector2.multiply(1, vector).equals(expectedResult));
        assertTrue(Vector2.multiply(vector, 1).equals(expectedResult));
        assertTrue(Vector2.multiply(vector, 1).equals(Vector2.multiply(vector, 1.0f)));

        Vector2 scaledVec = Vector2.multiply(vector, 2);

        // Test 2.0 scale.
        assertTrue(new Vector2(vector).multiply(2).equals(scaledVec));
        assertTrue(new Vector2(vector).multiply(new Vector2(2)).equals(scaledVec));
        assertTrue(Vector2.multiply(2, vector).equals(scaledVec));
        assertTrue(Vector2.multiply(vector, 2).equals(scaledVec));
        assertTrue(Vector2.multiply(vector, 2.0f).equals(scaledVec));
        assertTrue(Vector2.multiply(2, vector).equals(Vector2.multiply(vector, 2.0f)));
        assertTrue(Vector2.multiply(2, vector).equals(Vector2.multiply(vector, 2)));

        scaledVec = Vector2.multiply(vector, 0.999f);

        // Test 0.999 scale.
        assertTrue(new Vector2(vector).multiply(0.999f).equals(scaledVec));
        assertTrue(new Vector2(vector).multiply(new Vector2(0.999f)).equals(scaledVec));
        assertTrue(Vector2.multiply(0.999f, vector).equals(scaledVec));
        assertTrue(Vector2.multiply(vector, 0.999f).equals(scaledVec));
        assertTrue(Vector2.multiply(0.999f, vector).equals(Vector2.multiply(vector, 0.999f)));

        Vector2 vector2 = new Vector2(2, 2);

        // Test two vectors multiplication.
        assertTrue(new Vector2(vector.x * vector2.x, vector.y * vector2.y).equals(Vector2.multiply(vector, vector2)));
        assertTrue(Vector2.multiply(vector2, vector).equals(new Vector2(vector.x * vector2.x, vector.y * vector2.y)));
        assertTrue(Vector2.multiply(vector, vector2).equals(Vector2.multiply(vector, vector2)));

        Vector2 refVec = new Vector2();

        // Overloads comparison
        Vector2 vector3 = Vector2.multiply(vector, vector2);
        Vector2.multiply(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));

        vector3 = Vector2.multiply(vector, 2);
        Vector2.multiply(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void divide()
    {
        final Vector2 vector = new Vector2(1, 2);

        // Test 0.0 scale.
        Vector2 expectedResult = new Vector2(Float.POSITIVE_INFINITY);
        assertTrue(new Vector2(vector).divide(0).equals(expectedResult));
        assertTrue(new Vector2(vector).divide(Vector2.Zero()).equals(expectedResult));
        assertTrue(Vector2.divide(vector, 0).equals(expectedResult));
        assertTrue(Vector2.divide(vector, 0).equals(Vector2.divide(vector, 0.0f)));

        // Test 1.0 scale.
        expectedResult = new Vector2(vector);
        assertTrue(vector.divide(1).equals(expectedResult));
        assertTrue(vector.divide(Vector2.One()).equals(expectedResult));
        assertTrue(Vector2.divide(vector, 1).equals(expectedResult));
        assertTrue(Vector2.divide(vector, 1).equals(Vector2.divide(vector, 1.0f)));

        Vector2 scaledVec = Vector2.divide(vector, 2);

        // Test 2.0 scale.
        assertTrue(new Vector2(vector).divide(2).equals(scaledVec));
        assertTrue(new Vector2(vector).divide(new Vector2(2)).equals(scaledVec));
        assertTrue(Vector2.divide(vector, 2).equals(scaledVec));
        assertTrue(Vector2.divide(vector, 2.0f).equals(scaledVec));
        assertTrue(Vector2.divide(vector, 2).equals(Vector2.divide(vector, 2.0f)));
        assertTrue(Vector2.divide(vector, 2).equals(Vector2.divide(vector, 2)));

        scaledVec = Vector2.divide(vector, 0.999f);

        // Test 0.999 scale.
        assertTrue(new Vector2(vector).divide(0.999f).equals(scaledVec));
        assertTrue(new Vector2(vector).divide(new Vector2(0.999f)).equals(scaledVec));
        assertTrue(Vector2.divide(vector, 0.999f).equals(scaledVec));
        assertTrue(Vector2.divide(vector, 0.999f).equals(Vector2.divide(vector, 0.999f)));

        Vector2 vector2 = new Vector2(2, 2);

        // Test two vectors multiplication.
        assertTrue(new Vector2(vector.x / vector2.x, vector.y / vector2.y).equals(Vector2.divide(vector, vector2)));
        assertTrue(Vector2.divide(vector2, vector).equals(new Vector2(vector2.x / vector.x, vector2.y / vector.y)));
        assertTrue(Vector2.divide(vector, vector2).equals(Vector2.divide(vector, vector2)));

        Vector2 refVec = new Vector2();

        // Overloads comparison
        Vector2 vector3 = Vector2.divide(vector, vector2);
        Vector2.divide(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));

        vector3 = Vector2.divide(vector, 2);
        Vector2.divide(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void distanceSquared()
    {
        Vector2 v1 = new Vector2(0.1f, 100.0f);
        Vector2 v2 = new Vector2(1.1f, -2.0f);
        float d = Vector2.distanceSquared(v1, v2);
        float expectedResult = 10405f;
        assertEquals(expectedResult, d);
    }

    @Test
    public void normalize()
    {
        Vector2 v1 = new Vector2(-10.5f, 0.2f);
        Vector2 v2 = new Vector2(-10.5f, 0.2f);
        v1.normalize();
        Vector2 expectedResult = new Vector2(-0.999818643451f, 0.019044164637f);
        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult, v1));
        v2 = Vector2.normalize(v2);
        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult, v2));
    }

    @Test
    public void hermite()
    {
        Vector2 t1 = new Vector2(1.40625f, 1.40625f);
        Vector2 t2 = new Vector2(2.662375f, 2.26537514f);

        Vector2 v1 = new Vector2(1, 1);
        Vector2 v2 = new Vector2(2, 2);
        Vector2 v3 = new Vector2(3, 3);
        Vector2 v4 = new Vector2(4, 4);
        Vector2 v5 = new Vector2(4, 3);
        Vector2 v6 = new Vector2(2, 1);
        Vector2 v7 = new Vector2(1, 2);
        Vector2 v8 = new Vector2(3, 4);

        assertTrue(Vector2Comparer.Epsilon.equals(t1, Vector2.hermite(v1, v2, v3, v4, 0.25f)));
        assertTrue(Vector2Comparer.Epsilon.equals(t2, Vector2.hermite(v5, v6, v7, v8, 0.45f)));

        Vector2 result1 = new Vector2();
        Vector2 result2 = new Vector2();

        Vector2.hermite(v1, v2, v3, v4, 0.25f, result1);
        Vector2.hermite(v5, v6, v7, v8, 0.45f, result2);

        assertTrue(Vector2Comparer.Epsilon.equals(t1, result1));
        assertTrue(Vector2Comparer.Epsilon.equals(t2, result2));
    }

    @Test
    public void catmullRom()
    {
        Vector2 expectedResult = new Vector2(5.1944f, 6.1944f);
        Vector2 v1 = new Vector2(1, 2);
        Vector2 v2 = new Vector2(3, 4);
        Vector2 v3 = new Vector2(5, 6);
        Vector2 v4 = new Vector2(7, 8);
        float value = 1.0972f;

        Vector2 result = new Vector2();
        Vector2.catmullRom(v1, v2, v3, v4, value, result);

        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult, Vector2.catmullRom(v1, v2, v3, v4, value)));
        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult, result));
    }

    @Test
    public void transform()
    {
        // STANDART OVERLOADS TEST

        Vector2 expectedResult1 = new Vector2(24, 28);
        Vector2 expectedResult2 = new Vector2(-0.0168301091f, 2.30964f);

        Vector2 v1 = new Vector2(1, 2);
        Matrix m1 = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Vector2 v2 = new Vector2(1.1f, 2.45f);
        Quaternion q2 = new Quaternion(0.11f, 0.22f, 0.33f, 0.55f);

        Quaternion q3 = new Quaternion(1, 2, 3, 4);

        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult1, Vector2.transform(v1, m1)));
        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult2, Vector2.transform(v2, q2)));

        // OUTPUT OVERLOADS TEST

        Vector2 result1 = new Vector2();
        Vector2 result2 = new Vector2();

        Vector2.transform(v1, m1, result1);
        Vector2.transform(v2, q2, result2);

        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult1, result1));
        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult2, result2));

        // TRANSFORM ON LIST (MATRIX)
        {
            Vector2[] sourceList1 = new Vector2[10];
            Vector2[] desinationList1 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList1[i] = (new Vector2(1 + i, 1 + i));
                desinationList1[i] = new Vector2();
            }

            Vector2.transform(sourceList1, 0, m1, desinationList1, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(desinationList1[i], new Vector2(19 + (6 * i), 22 + (8 * i))));
            }
        }
        // TRANSFORM ON LIST (MATRIX)(DESTINATION & SOURCE)
        {
            Vector2[] sourceList2 = new Vector2[10];
            Vector2[] desinationList2 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList2[i] = (new Vector2(1 + i, 1 + i));
                desinationList2[i] = new Vector2();
            }

            Vector2.transform(sourceList2, 2, m1, desinationList2, 1, 3);

            assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), desinationList2[0]));

            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(31, 38), desinationList2[1]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(37, 46), desinationList2[2]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(43, 54), desinationList2[3]));

            for (int i = 4; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), desinationList2[i]));
            }
        }
        // TRANSFORM ON LIST (MATRIX)(SIMPLE)
        {
            Vector2[] sourceList3 = new Vector2[10];
            Vector2[] desinationList3 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList3[i] = (new Vector2(1 + i, 1 + i));
                desinationList3[i] = new Vector2();
            }

            Vector2.transform(sourceList3, m1, desinationList3);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(desinationList3[i], new Vector2(19 + (6 * i), 22 + (8 * i))));
            }
        }
        // TRANSFORM ON LIST (QUATERNION)
        {
            Vector2[] sourceList4 = new Vector2[10];
            Vector2[] desinationList4 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList4[i] = (new Vector2(1 + i, 1 + i));
                desinationList4[i] = new Vector2();
            }

            Vector2.transform(sourceList4, 0, q3, desinationList4, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(-45 + (-45 * i), 9 + (9 * i)), desinationList4[i]));
            }
        }
        // TRANSFORM ON LIST (QUATERNION)(DESTINATION & SOURCE)
        {
            Vector2[] sourceList5 = new Vector2[10];
            Vector2[] desinationList5 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList5[i] = (new Vector2(1 + i, 1 + i));
                desinationList5[i] = new Vector2();
            }

            Vector2.transform(sourceList5, 2, q3, desinationList5, 1, 3);

            assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), desinationList5[0]));

            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(-135, 27), desinationList5[1]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(-180, 36), desinationList5[2]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(-225, 45), desinationList5[3]));

            for (int i = 4; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), desinationList5[i]));
            }
        }
        // TRANSFORM ON LIST (QUATERNION)(SIMPLE)
        {
            Vector2[] sourceList6 = new Vector2[10];
            Vector2[] desinationList6 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList6[i] = (new Vector2(1 + i, 1 + i));
                desinationList6[i] = new Vector2();
            }

            Vector2.transform(sourceList6, q3, desinationList6);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(-45 + (-45 * i), 9 + (9 * i)), desinationList6[i]));
            }
        }
    }

    @Test
    public void transformNormal()
    {
        Vector2 normal = new Vector2(1.5f, 2.5f);
        Matrix matrix = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Vector2 expectedResult1 = new Vector2(14, 18);
        Vector2 expectedResult2 = new Vector2(expectedResult1);

        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult1, Vector2.transformNormal(normal, matrix)));

        Vector2 result = new Vector2();
        Vector2.transformNormal(normal, matrix, result);

        assertTrue(Vector2Comparer.Epsilon.equals(expectedResult2, result));

        // TRANSFORM ON LIST
        {
            Vector2[] sourceArray1 = new Vector2[10];
            Vector2[] destinationArray1 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceArray1[i] = new Vector2(i, i);
                destinationArray1[i] = new Vector2();
            }

            Vector2.transformNormal(sourceArray1, 0, matrix, destinationArray1, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(0 + (6 * i), 0 + (8 * i)), destinationArray1[i]));
            }
        }
        // TRANSFORM ON LIST(SOURCE OFFSET)
        {
            Vector2[] sourceArray2 = new Vector2[10];
            Vector2[] destinationArray2 = new Vector2[10];

            for (int i = 0; i < 10; i++)
            {
                sourceArray2[i] = new Vector2(i, i);
                destinationArray2[i] = new Vector2();
            }

            Vector2.transformNormal(sourceArray2, 5, matrix, destinationArray2, 0, 5);

            for (int i = 0; i < 5; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(30 + (6 * i), 40 + (8 * i)), destinationArray2[i]));
            }

            for (int i = 5; i < 10; i++)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), destinationArray2[i]));
            }
        }
        // TRANSFORM ON LIST(DESTINATION OFFSET)
        {
            Vector2[] sourceArray3 = new Vector2[10];
            Vector2[] destinationArray3 = new Vector2[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray3[i] = new Vector2(i, i);
                destinationArray3[i] = new Vector2();
            }

            Vector2.transformNormal(sourceArray3, 0, matrix, destinationArray3, 5, 5);

            for (int i = 0; i < 6; ++i)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), destinationArray3[i]));
            }

            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(6, 8), destinationArray3[6]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(12, 16), destinationArray3[7]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(18, 24), destinationArray3[8]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(24, 32), destinationArray3[9]));
        }
        // TRANSFORM ON LIST(DESTINATION & SOURCE)
        {
            Vector2[] sourceArray4 = new Vector2[10];
            Vector2[] destinationArray4 = new Vector2[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray4[i] = new Vector2(i, i);
                destinationArray4[i] = new Vector2();
            }

            Vector2.transformNormal(sourceArray4, 2, matrix, destinationArray4, 3, 6);

            for (int i = 0; i < 3; ++i)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), destinationArray4[i]));
            }

            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(12, 16), destinationArray4[3]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(18, 24), destinationArray4[4]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(24, 32), destinationArray4[5]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(30, 40), destinationArray4[6]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(36, 48), destinationArray4[7]));
            assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(42, 56), destinationArray4[8]));

            assertTrue(Vector2Comparer.Epsilon.equals(Vector2.Zero(), destinationArray4[9]));
        }
        // TRANSFORM ON LIST(SIMPLE)
        {
            Vector2[] sourceArray5 = new Vector2[10];
            Vector2[] destinationArray5 = new Vector2[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray5[i] = new Vector2(i, i);
                destinationArray5[i] = new Vector2();
            }

            Vector2.transformNormal(sourceArray5, matrix, destinationArray5);

            for (int i = 0; i < 10; ++i)
            {
                assertTrue(Vector2Comparer.Epsilon.equals(new Vector2(0 + (6 * i), 0 + (8 * i)), destinationArray5[i]));
            }
        }
    }

    @Test(enabled = false)
    public void typeConverter()
    {
        // TODO(Eric): Enable this test once this class is created
        // NOTE(Eric): This is used to return the Vector2TypeConverter from Design
        // Vector2TypeConverter converter = TypeDescriptor.GetConverter(typeof(Vector2));
        // Locale invariantCulture = Locale.ROOT;

        // assertTrue(new Vector2(32, 64), converter.ConvertFromString(null, invariantCulture, "32, 64"));
        // assertTrue(new Vector2(0.5f, 2.75f), converter.ConvertFromString(null, invariantCulture, "0.5, 2.75"));
        // assertTrue(new Vector2(1024.5f, 2048.75f), converter.ConvertFromString(null, invariantCulture, "1024.5, 2048.75"));
        // assertTrue("32, 64", converter.ConvertToString(null, invariantCulture, new Vector2(32, 64)));
        // assertTrue("0.5, 2.75", converter.ConvertToString(null, invariantCulture, new Vector2(0.5f, 2.75f)));
        // assertTrue("1024.5, 2048.75", converter.ConvertToString(null, invariantCulture, new Vector2(1024.5f, 2048.75f)));

        // Locale otherCulture = new Locale("el-GR");

        // assertTrue(new Vector2(1024.5f, 2048.75f), converter.ConvertFromString(null, otherCulture, "1024,5; 2048,75"));
        // assertTrue("1024,5; 2048,75", converter.ConvertToString(null, otherCulture, new Vector2(1024.5f, 2048.75f)));
    }

    @Test
    public void hashCodeTest()
    {
        // Checking for overflows in hash calculation.
        Vector2 max = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        Vector2 min = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);
        assertNotEquals(max.hashCode(), Vector2.Zero().hashCode());
        assertNotEquals(min.hashCode(), Vector2.Zero().hashCode());

        // Common values
        Vector2 a = new Vector2(0f, 0f);
        assertEquals(a.hashCode(), Vector2.Zero().hashCode());
        assertNotEquals(a.hashCode(), Vector2.One().hashCode());

        // Individual properties alter hash
        Vector2 xa = new Vector2(2f, 1f);
        Vector2 xb = new Vector2(3f, 1f);
        Vector2 ya = new Vector2(1f, 2f);
        Vector2 yb = new Vector2(1f, 3f);
        assertNotEquals(xa.hashCode(), xb.hashCode(), "Different properties should change hash.");
        assertNotEquals(ya.hashCode(), yb.hashCode(), "Different properties should change hash.");
        assertNotEquals(xa.hashCode(), ya.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(xb.hashCode(), yb.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(xa.hashCode(), yb.hashCode());
        assertNotEquals(ya.hashCode(), xb.hashCode());
    }

    @Test
    public void toPoint()
    {
        assertTrue(new Point(0, 0).equals(new Vector2(0.1f, 0.1f).toPoint()));
        assertTrue(new Point(0, 0).equals(new Vector2(0.5f, 0.5f).toPoint()));
        assertTrue(new Point(0, 0).equals(new Vector2(0.55f, 0.55f).toPoint()));
        assertTrue(new Point(0, 0).equals(new Vector2(1.0f - 0.1f, 1.0f - 0.1f).toPoint()));
        assertTrue(new Point(1, 1).equals(new Vector2(1.0f - Float.MIN_VALUE, 1.0f - Float.MIN_VALUE).toPoint()));
        assertTrue(new Point(1, 1).equals(new Vector2(1.0f, 1.0f).toPoint()));
        assertTrue(new Point(19, 27).equals(new Vector2(19.033f, 27.1f).toPoint()));
    }

    @Test
    public void toStringTest()
    {
        assertTrue(new Vector2(10, -20.5f).toString().equals("{X:10.0 Y:-20.5}"));
    }

}
