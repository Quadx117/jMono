package framework.math;

import static org.testng.Assert.*;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Quaternion;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector3;

import org.testng.annotations.Test;

import utilities.Vector3Comparer;

public class Vector3Test
{
    @Test
    public void constructors()
    {
        Vector3 expectedResult = new Vector3(1, 2, 3);

        Vector3 expectedResult2 = new Vector3(2.2f, 2.2f, 2.2f);

        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(), Vector3.Zero()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(1, 2, 3), expectedResult));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(expectedResult), expectedResult));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(new Vector2(1, 2), 3), expectedResult));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(2.2f), expectedResult2));
    }

    @Test
    public void properties()
    {
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, 0, 0), Vector3.Zero()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(1, 1, 1), Vector3.One()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(1, 0, 0), Vector3.UnitX()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, 1, 0), Vector3.UnitY()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, 0, 1), Vector3.UnitZ()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, 1, 0), Vector3.Up()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, -1, 0), Vector3.Down()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(1, 0, 0), Vector3.Right()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(-1, 0, 0), Vector3.Left()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, 0, -1), Vector3.Forward()));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0, 0, 1), Vector3.Backward()));
    }

    @Test
    public void dotProduct()
    {
        Vector3 vector1 = new Vector3(1, 2, 3);
        Vector3 vector2 = new Vector3(0.5f, 1.1f, -3.8f);
        float expectedResult = -8.7f;

        assertEquals(Vector3.dot(vector1, vector2), expectedResult, 0);

        // TODO(Eric): This method is not implemented in java (need a wrapper on the primitive).
        // float result;
        // Vector3.dot(vector1, vector2, result);

        // assertEquals(result, expectedResult);
    }

    @Test
    public void length()
    {
        Vector3 vector1 = new Vector3(1, 2, 3);
        assertEquals(3.741657386773f, vector1.length(), 0);
    }

    @Test
    public void lengthSquared()
    {
        Vector3 vector1 = new Vector3(1, 2, 3);
        assertEquals(14, vector1.lengthSquared(), 0);
    }

    @Test
    public void add()
    {
        Vector3 vector = new Vector3(1, 2, 3);

        // Test add 0.0
        Vector3 expectedResult = new Vector3(vector);
        assertTrue(new Vector3(vector).add(Vector3.Zero()).equals(expectedResult));
        assertTrue(Vector3.add(Vector3.Zero(), vector).equals(expectedResult));
        assertTrue(Vector3.add(vector, Vector3.Zero()).equals(expectedResult));

        // Test add 2
        Vector3 scaledVec = new Vector3(vector.x + 2, vector.y + 2, vector.z + 2);
        assertTrue(new Vector3(vector).add(new Vector3(2)).equals(scaledVec));
        assertTrue(Vector3.add(new Vector3(2), vector).equals(scaledVec));
        assertTrue(Vector3.add(vector, new Vector3(2)).equals(scaledVec));
        assertTrue(Vector3.add(vector, new Vector3(2.0f)).equals(scaledVec));
        assertTrue(Vector3.add(new Vector3(2), vector).equals(Vector3.add(vector, new Vector3(2.0f))));
        assertTrue(Vector3.add(new Vector3(2), vector).equals(Vector3.add(vector, new Vector3(2))));

        Vector3 vector2 = new Vector3(2, 2, 2);

        // Test two vectors addition.
        assertTrue(new Vector3(vector.x + vector2.x, vector.y + vector2.y, vector.z + vector2.z).equals(Vector3.add(vector, vector2)));
        assertTrue(Vector3.add(vector2, vector).equals(new Vector3(vector.x + vector2.x, vector.y + vector2.y, vector.z + vector2.z)));
        assertTrue(Vector3.add(vector, vector2).equals(Vector3.add(vector, vector2)));

        Vector3 refVec = new Vector3();

        // Overloads comparison
        Vector3 vector3 = Vector3.add(vector, vector2);
        Vector3.add(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void subtract()
    {
        Vector3 vector = new Vector3(1, 2, 3);

        // Test subtract 0.0
        Vector3 expectedResult = new Vector3(vector);
        assertTrue(new Vector3(vector).subtract(Vector3.Zero()).equals(expectedResult));
        assertTrue(Vector3.subtract(Vector3.Zero(), vector).equals(Vector3.negate(expectedResult)));
        assertTrue(Vector3.subtract(vector, Vector3.Zero()).equals(expectedResult));

        // Test subtract 2
        Vector3 scaledVec = new Vector3(vector.x - 2, vector.y - 2, vector.z - 2);
        Vector3 scaledVec2 = new Vector3(2 - vector.x, 2 - vector.y, 2 - vector.z);
        assertTrue(new Vector3(vector).subtract(new Vector3(2)).equals(scaledVec));
        assertTrue(Vector3.subtract(new Vector3(2), vector).equals(scaledVec2));
        assertTrue(Vector3.subtract(vector, new Vector3(2)).equals(scaledVec));
        assertTrue(Vector3.subtract(vector, new Vector3(2.0f)).equals(scaledVec));
        assertTrue(Vector3.subtract(vector, new Vector3(2)).equals(Vector3.subtract(vector, new Vector3(2.0f))));
        assertTrue(Vector3.subtract(vector, new Vector3(2)).equals(Vector3.subtract(vector, new Vector3(2))));

        Vector3 vector2 = new Vector3(2, 2, 2);

        // Test two vectors subtraction.
        assertTrue(new Vector3(vector.x - vector2.x, vector.y - vector2.y, vector.z - vector2.z).equals(Vector3.subtract(vector, vector2)));
        assertTrue(Vector3.subtract(vector2, vector).equals(new Vector3(vector2.x - vector.x, vector2.y - vector.y, vector2.z - vector.z)));
        assertFalse(Vector3.subtract(vector, vector2).equals(Vector3.subtract(vector2, vector)));

        Vector3 refVec = new Vector3();

        // Overloads comparison
        Vector3 vector3 = Vector3.subtract(vector, vector2);
        Vector3.subtract(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void multiply()
    {
        Vector3 vector = new Vector3(1, 2, 3);

        // Test 0.0 scale.
        assertTrue(new Vector3(vector).multiply(0).equals(Vector3.Zero()));
        assertTrue(new Vector3(vector).multiply(Vector3.Zero()).equals(Vector3.Zero()));
        assertTrue(Vector3.multiply(vector, 0).equals(Vector3.Zero()));
        assertTrue(Vector3.multiply(vector, 0).equals(Vector3.multiply(vector, 0.0f)));

        // Test 1.0 scale.
        Vector3 expectedResult = new Vector3(vector);
        assertTrue(vector.multiply(1).equals(expectedResult));
        assertTrue(vector.multiply(Vector3.One()).equals(expectedResult));
        assertTrue(Vector3.multiply(vector, 1).equals(expectedResult));
        assertTrue(Vector3.multiply(vector, 1).equals(Vector3.multiply(vector, 1.0f)));

        Vector3 scaledVec = Vector3.multiply(vector, 2);

        // Test 2.0 scale.
        assertTrue(new Vector3(vector).multiply(2).equals(scaledVec));
        assertTrue(new Vector3(vector).multiply(new Vector3(2)).equals(scaledVec));
        assertTrue(Vector3.multiply(vector, 2).equals(scaledVec));
        assertTrue(Vector3.multiply(vector, 2.0f).equals(scaledVec));
        assertTrue(Vector3.multiply(vector, 2).equals(Vector3.multiply(vector, 2.0f)));
        assertTrue(Vector3.multiply(vector, 2).equals(Vector3.multiply(vector, 2)));

        scaledVec = Vector3.multiply(vector, 0.999f);

        // Test 0.999 scale.
        assertTrue(new Vector3(vector).multiply(0.999f).equals(scaledVec));
        assertTrue(new Vector3(vector).multiply(new Vector3(0.999f)).equals(scaledVec));
        assertTrue(Vector3.multiply(vector, 0.999f).equals(scaledVec));
        assertTrue(Vector3.multiply(vector, 0.999f).equals(Vector3.multiply(vector, 0.999f)));

        Vector3 vector2 = new Vector3(2, 2, 2);

        // Test two vectors multiplication.
        assertTrue(new Vector3(vector.x * vector2.x, vector.y * vector2.y, vector.z * vector2.z).equals(Vector3.multiply(vector, vector2)));
        assertTrue(Vector3.multiply(vector2, vector).equals(new Vector3(vector.x * vector2.x, vector.y * vector2.y, vector.z * vector2.z)));
        assertTrue(Vector3.multiply(vector, vector2).equals(Vector3.multiply(vector, vector2)));

        Vector3 refVec = new Vector3();

        // Overloads comparison
        Vector3 vector3 = Vector3.multiply(vector, vector2);
        Vector3.multiply(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));

        vector3 = Vector3.multiply(vector, 2);
        Vector3.multiply(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void divide()
    {
        final Vector3 vector = new Vector3(1, 2, 3);

        // Test 0.0 scale.
        Vector3 expectedResult = new Vector3(Float.POSITIVE_INFINITY);
        assertTrue(new Vector3(vector).divide(0).equals(expectedResult));
        assertTrue(new Vector3(vector).divide(Vector3.Zero()).equals(expectedResult));
        assertTrue(Vector3.divide(vector, 0).equals(expectedResult));
        assertTrue(Vector3.divide(vector, 0).equals(Vector3.divide(vector, 0.0f)));

        // Test 1.0 scale.
        expectedResult = new Vector3(vector);
        assertTrue(vector.divide(1).equals(expectedResult));
        assertTrue(vector.divide(Vector3.One()).equals(expectedResult));
        assertTrue(Vector3.divide(vector, 1).equals(expectedResult));
        assertTrue(Vector3.divide(vector, 1).equals(Vector3.divide(vector, 1.0f)));

        Vector3 scaledVec = Vector3.divide(vector, 2);

        // Test 2.0 scale.
        assertTrue(new Vector3(vector).divide(2).equals(scaledVec));
        assertTrue(new Vector3(vector).divide(new Vector3(2)).equals(scaledVec));
        assertTrue(Vector3.divide(vector, 2).equals(scaledVec));
        assertTrue(Vector3.divide(vector, 2.0f).equals(scaledVec));
        assertTrue(Vector3.divide(vector, 2).equals(Vector3.divide(vector, 2.0f)));
        assertTrue(Vector3.divide(vector, 2).equals(Vector3.divide(vector, 2)));

        scaledVec = Vector3.divide(vector, 0.999f);

        // Test 0.999 scale.
        assertTrue(new Vector3(vector).divide(0.999f).equals(scaledVec));
        assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(vector).divide(new Vector3(0.999f)), scaledVec));
        assertTrue(Vector3.divide(vector, 0.999f).equals(scaledVec));
        assertTrue(Vector3.divide(vector, 0.999f).equals(Vector3.divide(vector, 0.999f)));

        Vector3 vector2 = new Vector3(2, 2, 2);

        // Test two vectors multiplication.
        assertTrue(new Vector3(vector.x / vector2.x, vector.y / vector2.y, vector.z / vector2.z).equals(Vector3.divide(vector, vector2)));
        assertTrue(Vector3.divide(vector2, vector).equals(new Vector3(vector2.x / vector.x, vector2.y / vector.y, vector2.z / vector.z)));
        assertTrue(Vector3.divide(vector, vector2).equals(Vector3.divide(vector, vector2)));

        Vector3 refVec = new Vector3();

        // Overloads comparison
        Vector3 vector3 = Vector3.divide(vector, vector2);
        Vector3.divide(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));

        vector3 = Vector3.divide(vector, 2);
        Vector3.divide(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void distanceSquared()
    {
        Vector3 v1 = new Vector3(0.1f, 100.0f, -5.5f);
        Vector3 v2 = new Vector3(1.1f, -2.0f, 5.5f);
        float d = Vector3.distanceSquared(v1, v2);
        float expectedResult = 10526;
        assertEquals(expectedResult, d);
    }

    @Test
    public void normalize()
    {
        Vector3 v1 = new Vector3(-10.5f, 0.2f, 1000.0f);
        Vector3 v2 = new Vector3(-10.5f, 0.2f, 1000.0f);
        v1.normalize();
        Vector3 expectedResult = new Vector3(-0.0104994215f, 0.000199988979f, 0.999944866f);
        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult, v1));
        v2 = Vector3.normalize(v2);
        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult, v2));
    }

    // TODO(Eric): Add hermite test for Vector3
    @Test(enabled = false)
    public void hermite()
    {
/*        Vector3 t1 = new Vector3(1.40625f, 1.40625f);
        Vector3 t2 = new Vector3(2.662375f, 2.26537514f);

        Vector3 v1 = new Vector3(1, 1);
        Vector3 v2 = new Vector3(2, 2);
        Vector3 v3 = new Vector3(3, 3);
        Vector3 v4 = new Vector3(4, 4);
        Vector3 v5 = new Vector3(4, 3);
        Vector3 v6 = new Vector3(2, 1);
        Vector3 v7 = new Vector3(1, 2);
        Vector3 v8 = new Vector3(3, 4);

        assertTrue(Vector3Comparer.Epsilon.equals(t1, Vector3.hermite(v1, v2, v3, v4, 0.25f)));
        assertTrue(Vector3Comparer.Epsilon.equals(t2, Vector3.hermite(v5, v6, v7, v8, 0.45f)));

        Vector3 result1 = new Vector3();
        Vector3 result2 = new Vector3();

        Vector3.hermite(v1, v2, v3, v4, 0.25f, result1);
        Vector3.hermite(v5, v6, v7, v8, 0.45f, result2);

        assertTrue(Vector3Comparer.Epsilon.equals(t1, result1));
        assertTrue(Vector3Comparer.Epsilon.equals(t2, result2));*/
    }

    // TODO(Eric): Add catmullRom test for Vector3
    @Test(enabled = false)
    public void catmullRom()
    {
/*        Vector3 expectedResult = new Vector3(5.1944f, 6.1944f);
        Vector3 v1 = new Vector3(1, 2);
        Vector3 v2 = new Vector3(3, 4);
        Vector3 v3 = new Vector3(5, 6);
        Vector3 v4 = new Vector3(7, 8);
        float value = 1.0972f;

        Vector3 result = new Vector3();
        Vector3.catmullRom(v1, v2, v3, v4, value, result);

        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult, Vector3.catmullRom(v1, v2, v3, v4, value)));
        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult, result));*/
    }

    @Test
    public void transform()
    {
        // STANDARD OVERLOADS TEST

        Vector3 expectedResult1 = new Vector3(51, 58, 65);
        Vector3 expectedResult2 = new Vector3(33, -14, -1);

        Vector3 v1 = new Vector3(1, 2, 3);
        Matrix m1 = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Vector3 v2 = new Vector3(1, 2, 3);
        Quaternion q1 = new Quaternion(2, 3, 4, 5);

        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult1, Vector3.transform(v1, m1)));
        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult2, Vector3.transform(v2, q1)));

        // OUTPUT OVERLOADS TEST

        Vector3 result1 = new Vector3();
        Vector3 result2 = new Vector3();

        Vector3.transform(v1, m1, result1);
        Vector3.transform(v2, q1, result2);

        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult1, result1));
        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult2, result2));

        // TODO(Eric): Finish transform test
        // TRANSFORM ON LIST (MATRIX)
        /*
         * {
         * Vector3[] sourceList1 = new Vector3[10];
         * Vector3[] desinationList1 = new Vector3[10];
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * sourceList1[i] = (new Vector3(1 + i, 1 + i, 1 + i));
         * desinationList1[i] = new Vector3();
         * }
         * 
         * Vector3.transform(sourceList1, 0, m1, desinationList1, 0, 10);
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * assertTrue(Vector3Comparer.Epsilon.equals(desinationList1[i], new Vector3(19 + (6 * i), 22 + (8 * i))));
         * }
         * }
         * // TRANSFORM ON LIST (MATRIX)(DESTINATION & SOURCE)
         * {
         * Vector3[] sourceList2 = new Vector3[10];
         * Vector3[] desinationList2 = new Vector3[10];
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * sourceList2[i] = (new Vector3(1 + i, 1 + i, 1 + i));
         * desinationList2[i] = new Vector3();
         * }
         * 
         * Vector3.transform(sourceList2, 2, m1, desinationList2, 1, 3);
         * 
         * assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), desinationList2[0]));
         * 
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(31, 38), desinationList2[1]));
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(37, 46), desinationList2[2]));
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(43, 54), desinationList2[3]));
         * 
         * for (int i = 4; i < 10; i++)
         * {
         * assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), desinationList2[i]));
         * }
         * }
         * // TRANSFORM ON LIST (MATRIX)(SIMPLE)
         * {
         * Vector3[] sourceList3 = new Vector3[10];
         * Vector3[] desinationList3 = new Vector3[10];
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * sourceList3[i] = (new Vector3(1 + i, 1 + i, 1 + i));
         * desinationList3[i] = new Vector3();
         * }
         * 
         * Vector3.transform(sourceList3, m1, desinationList3);
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * assertTrue(Vector3Comparer.Epsilon.equals(desinationList3[i], new Vector3(19 + (6 * i), 22 + (8 * i))));
         * }
         * }
         * // TRANSFORM ON LIST (QUATERNION)
         * {
         * Vector3[] sourceList4 = new Vector3[10];
         * Vector3[] desinationList4 = new Vector3[10];
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * sourceList4[i] = (new Vector3(1 + i, 1 + i, 1 + i));
         * desinationList4[i] = new Vector3();
         * }
         * 
         * Vector3.transform(sourceList4, 0, q3, desinationList4, 0, 10);
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(-45 + (-45 * i), 9 + (9 * i)), desinationList4[i]));
         * }
         * }
         * // TRANSFORM ON LIST (QUATERNION)(DESTINATION & SOURCE)
         * {
         * Vector3[] sourceList5 = new Vector3[10];
         * Vector3[] desinationList5 = new Vector3[10];
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * sourceList5[i] = (new Vector3(1 + i, 1 + i, 1 + i));
         * desinationList5[i] = new Vector3();
         * }
         * 
         * Vector3.transform(sourceList5, 2, q3, desinationList5, 1, 3);
         * 
         * assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), desinationList5[0]));
         * 
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(-135, 27), desinationList5[1]));
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(-180, 36), desinationList5[2]));
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(-225, 45), desinationList5[3]));
         * 
         * for (int i = 4; i < 10; i++)
         * {
         * assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), desinationList5[i]));
         * }
         * }
         * // TRANSFORM ON LIST (QUATERNION)(SIMPLE)
         * {
         * Vector3[] sourceList6 = new Vector3[10];
         * Vector3[] desinationList6 = new Vector3[10];
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * sourceList6[i] = (new Vector3(1 + i, 1 + i, 1 + i));
         * desinationList6[i] = new Vector3();
         * }
         * 
         * Vector3.transform(sourceList6, q3, desinationList6);
         * 
         * for (int i = 0; i < 10; i++)
         * {
         * assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(-45 + (-45 * i), 9 + (9 * i)), desinationList6[i]));
         * }
         * }
         */
    }

    // TODO(Eric): Add transformNormal for Vector3
    @Test(enabled = false)
    public void transformNormal()
    {
/*        Vector3 normal = new Vector3(1.5f, 2.5f);
        Matrix matrix = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Vector3 expectedResult1 = new Vector3(14, 18);
        Vector3 expectedResult2 = new Vector3(expectedResult1);

        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult1, Vector3.transformNormal(normal, matrix)));

        Vector3 result = new Vector3();
        Vector3.transformNormal(normal, matrix, result);

        assertTrue(Vector3Comparer.Epsilon.equals(expectedResult2, result));

        // TRANSFORM ON LIST
        {
            Vector3[] sourceArray1 = new Vector3[10];
            Vector3[] destinationArray1 = new Vector3[10];

            for (int i = 0; i < 10; i++)
            {
                sourceArray1[i] = new Vector3(i, i);
                destinationArray1[i] = new Vector3();
            }

            Vector3.transformNormal(sourceArray1, 0, matrix, destinationArray1, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0 + (6 * i), 0 + (8 * i)), destinationArray1[i]));
            }
        }
        // TRANSFORM ON LIST(SOURCE OFFSET)
        {
            Vector3[] sourceArray2 = new Vector3[10];
            Vector3[] destinationArray2 = new Vector3[10];

            for (int i = 0; i < 10; i++)
            {
                sourceArray2[i] = new Vector3(i, i);
                destinationArray2[i] = new Vector3();
            }

            Vector3.transformNormal(sourceArray2, 5, matrix, destinationArray2, 0, 5);

            for (int i = 0; i < 5; i++)
            {
                assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(30 + (6 * i), 40 + (8 * i)), destinationArray2[i]));
            }

            for (int i = 5; i < 10; i++)
            {
                assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), destinationArray2[i]));
            }
        }
        // TRANSFORM ON LIST(DESTINATION OFFSET)
        {
            Vector3[] sourceArray3 = new Vector3[10];
            Vector3[] destinationArray3 = new Vector3[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray3[i] = new Vector3(i, i);
                destinationArray3[i] = new Vector3();
            }

            Vector3.transformNormal(sourceArray3, 0, matrix, destinationArray3, 5, 5);

            for (int i = 0; i < 6; ++i)
            {
                assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), destinationArray3[i]));
            }

            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(6, 8), destinationArray3[6]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(12, 16), destinationArray3[7]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(18, 24), destinationArray3[8]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(24, 32), destinationArray3[9]));
        }
        // TRANSFORM ON LIST(DESTINATION & SOURCE)
        {
            Vector3[] sourceArray4 = new Vector3[10];
            Vector3[] destinationArray4 = new Vector3[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray4[i] = new Vector3(i, i);
                destinationArray4[i] = new Vector3();
            }

            Vector3.transformNormal(sourceArray4, 2, matrix, destinationArray4, 3, 6);

            for (int i = 0; i < 3; ++i)
            {
                assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), destinationArray4[i]));
            }

            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(12, 16), destinationArray4[3]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(18, 24), destinationArray4[4]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(24, 32), destinationArray4[5]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(30, 40), destinationArray4[6]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(36, 48), destinationArray4[7]));
            assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(42, 56), destinationArray4[8]));

            assertTrue(Vector3Comparer.Epsilon.equals(Vector3.Zero(), destinationArray4[9]));
        }
        // TRANSFORM ON LIST(SIMPLE)
        {
            Vector3[] sourceArray5 = new Vector3[10];
            Vector3[] destinationArray5 = new Vector3[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray5[i] = new Vector3(i, i);
                destinationArray5[i] = new Vector3();
            }

            Vector3.transformNormal(sourceArray5, matrix, destinationArray5);

            for (int i = 0; i < 10; ++i)
            {
                assertTrue(Vector3Comparer.Epsilon.equals(new Vector3(0 + (6 * i), 0 + (8 * i)), destinationArray5[i]));
            }
        }*/
    }

    @Test(enabled = false)
    public void typeConverter()
    {
        // TODO(Eric): Enable this test once this class is created
        // NOTE(Eric): This is used to return the Vector3TypeConverter from Design
        // Vector3TypeConverter converter = TypeDescriptor.GetConverter(typeof(Vector3));
        // Locale invariantCulture = Locale.ROOT;

        // assertTrue(new Vector3(32, 64), converter.ConvertFromString(null, invariantCulture, "32, 64"));
        // assertTrue(new Vector3(0.5f, 2.75f), converter.ConvertFromString(null, invariantCulture, "0.5, 2.75"));
        // assertTrue(new Vector3(1024.5f, 2048.75f), converter.ConvertFromString(null, invariantCulture, "1024.5, 2048.75"));
        // assertTrue("32, 64", converter.ConvertToString(null, invariantCulture, new Vector3(32, 64)));
        // assertTrue("0.5, 2.75", converter.ConvertToString(null, invariantCulture, new Vector3(0.5f, 2.75f)));
        // assertTrue("1024.5, 2048.75", converter.ConvertToString(null, invariantCulture, new Vector3(1024.5f, 2048.75f)));

        // Locale otherCulture = new Locale("el-GR");

        // assertTrue(new Vector3(1024.5f, 2048.75f), converter.ConvertFromString(null, otherCulture, "1024,5; 2048,75"));
        // assertTrue("1024,5; 2048,75", converter.ConvertToString(null, otherCulture, new Vector3(1024.5f, 2048.75f)));
    }

    @Test
    public void hashCodeTest()
    {
        // Checking for overflows in hash calculation.
        Vector3 max = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 min = new Vector3(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        assertNotEquals(max.hashCode(), Vector3.Zero().hashCode());
        assertNotEquals(min.hashCode(), Vector3.Zero().hashCode());

        // Common values
        Vector3 a = new Vector3(0f, 0f, 0f);
        assertEquals(a.hashCode(), Vector3.Zero().hashCode());
        assertNotEquals(a.hashCode(), Vector3.One().hashCode());

        // Individual properties alter hash
        Vector3 xa = new Vector3(2f, 1f, 5f);
        Vector3 xb = new Vector3(3f, 1f, 5f);
        Vector3 ya = new Vector3(1f, 2f, 5f);
        Vector3 yb = new Vector3(1f, 3f, 5f);
        Vector3 za = new Vector3(2f, 1f, 5f);
        Vector3 zb = new Vector3(2f, 1f, 7f);
        assertNotEquals(xa.hashCode(), xb.hashCode(), "Different properties should change hash.");
        assertNotEquals(ya.hashCode(), yb.hashCode(), "Different properties should change hash.");
        assertNotEquals(za.hashCode(), zb.hashCode(), "Different properties should change hash.");
        assertNotEquals(xa.hashCode(), ya.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(xb.hashCode(), yb.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(ya.hashCode(), za.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(yb.hashCode(), zb.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(xa.hashCode(), yb.hashCode());
        assertNotEquals(ya.hashCode(), xb.hashCode());
    }

    @Test
    public void toStringTest()
    {
        assertTrue(new Vector3(10, -20, 3.5f).toString().equals("{X:10.0 Y:-20.0 Z:3.5}"));
    }

}
