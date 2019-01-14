package framework.math;

import static org.testng.Assert.*;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector3;
import jMono_Framework.math.Vector4;

import org.testng.annotations.Test;

import utilities.Vector4Comparer;

public class Vector4Test
{
    @Test
    public void constructors()
    {
        Vector4 expectedResult = new Vector4(1, 2, 3, 4);

        Vector4 expectedResult2 = new Vector4(2.2f, 2.2f, 2.2f, 2.2f);

        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(), Vector4.Zero()));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(1, 2, 3, 4), expectedResult));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(new Vector2(1, 2), 3, 4), expectedResult));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(new Vector3(1, 2, 3), 4), expectedResult));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(expectedResult), expectedResult));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(2.2f), expectedResult2));
    }

    @Test
    public void properties()
    {
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(0, 0, 0, 0), Vector4.Zero()));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(1, 1, 1, 1), Vector4.One()));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(1, 0, 0, 0), Vector4.UnitX()));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(0, 1, 0, 0), Vector4.UnitY()));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(0, 0, 1, 0), Vector4.UnitZ()));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(0, 0, 0, 1), Vector4.UnitW()));
    }

    @Test
    public void dotProduct()
    {
        Vector4 vector1 = new Vector4(1, 2, 3, 4);
        Vector4 vector2 = new Vector4(0.5f, 1.1f, -3.8f, 1.2f);
        float expectedResult = -3.89999962f;

        assertEquals(Vector4.dot(vector1, vector2), expectedResult, 0);

        // TODO(Eric): This method is not implemented in java (need a wrapper on the primitive).
        // float result;
        // Vector4.dot(vector1, vector2, result);

        // assertEquals(result, expectedResult);
    }

    @Test
    public void add()
    {
        Vector4 vector = new Vector4(1, 2, 3, 4);

        // Test add 0.0
        Vector4 expectedResult = new Vector4(vector);
        assertTrue(new Vector4(vector).add(Vector4.Zero()).equals(expectedResult));
        assertTrue(Vector4.add(Vector4.Zero(), vector).equals(expectedResult));
        assertTrue(Vector4.add(vector, Vector4.Zero()).equals(expectedResult));

        // Test add 2
        Vector4 scaledVec = new Vector4(vector.x + 2, vector.y + 2, vector.z + 2, vector.w + 2);
        assertTrue(new Vector4(vector).add(new Vector4(2)).equals(scaledVec));
        assertTrue(Vector4.add(new Vector4(2), vector).equals(scaledVec));
        assertTrue(Vector4.add(vector, new Vector4(2)).equals(scaledVec));
        assertTrue(Vector4.add(vector, new Vector4(2.0f)).equals(scaledVec));
        assertTrue(Vector4.add(new Vector4(2), vector).equals(Vector4.add(vector, new Vector4(2.0f))));
        assertTrue(Vector4.add(new Vector4(2), vector).equals(Vector4.add(vector, new Vector4(2))));

        Vector4 vector2 = new Vector4(2, 2, 2, 2);

        // Test two vectors addition.
        assertTrue(new Vector4(vector.x + vector2.x, vector.y + vector2.y, vector.z + vector2.z, vector.w + vector2.w).equals(Vector4.add(vector, vector2)));
        assertTrue(Vector4.add(vector2, vector).equals(new Vector4(vector.x + vector2.x, vector.y + vector2.y, vector.z + vector2.z, vector.w + vector2.w)));
        assertTrue(Vector4.add(vector, vector2).equals(Vector4.add(vector, vector2)));

        Vector4 refVec = new Vector4();

        // Overloads comparison
        Vector4 vector3 = Vector4.add(vector, vector2);
        Vector4.add(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void subtract()
    {
        Vector4 vector = new Vector4(1, 2, 3, 4);

        // Test subtract 0.0
        Vector4 expectedResult = new Vector4(vector);
        assertTrue(new Vector4(vector).subtract(Vector4.Zero()).equals(expectedResult));
        assertTrue(Vector4.subtract(Vector4.Zero(), vector).equals(Vector4.negate(expectedResult)));
        assertTrue(Vector4.subtract(vector, Vector4.Zero()).equals(expectedResult));

        // Test subtract 2
        Vector4 scaledVec = new Vector4(vector.x - 2, vector.y - 2, vector.z - 2, vector.w - 2);
        Vector4 scaledVec2 = new Vector4(2 - vector.x, 2 - vector.y, 2 - vector.z, 2 - vector.w);
        assertTrue(new Vector4(vector).subtract(new Vector4(2)).equals(scaledVec));
        assertTrue(Vector4.subtract(new Vector4(2), vector).equals(scaledVec2));
        assertTrue(Vector4.subtract(vector, new Vector4(2)).equals(scaledVec));
        assertTrue(Vector4.subtract(vector, new Vector4(2.0f)).equals(scaledVec));
        assertTrue(Vector4.subtract(vector, new Vector4(2)).equals(Vector4.subtract(vector, new Vector4(2.0f))));
        assertTrue(Vector4.subtract(vector, new Vector4(2)).equals(Vector4.subtract(vector, new Vector4(2))));

        Vector4 vector2 = new Vector4(2, 2, 2, 2);

        // Test two vectors subtraction.
        assertTrue(new Vector4(vector.x - vector2.x, vector.y - vector2.y, vector.z - vector2.z, vector.w - vector2.w).equals(Vector4.subtract(vector, vector2)));
        assertTrue(Vector4.subtract(vector2, vector).equals(new Vector4(vector2.x - vector.x, vector2.y - vector.y, vector2.z - vector.z, vector2.w - vector.w)));
        assertFalse(Vector4.subtract(vector, vector2).equals(Vector4.subtract(vector2, vector)));

        Vector4 refVec = new Vector4();

        // Overloads comparison
        Vector4 vector3 = Vector4.subtract(vector, vector2);
        Vector4.subtract(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void multiply()
    {
        Vector4 vector = new Vector4(1, 2, 3, 4);

        // Test 0.0 scale.
        assertTrue(new Vector4(vector).multiply(0).equals(Vector4.Zero()));
        assertTrue(new Vector4(vector).multiply(Vector4.Zero()).equals(Vector4.Zero()));
        assertTrue(Vector4.multiply(vector, 0).equals(Vector4.Zero()));
        assertTrue(Vector4.multiply(vector, 0).equals(Vector4.multiply(vector, 0.0f)));

        // Test 1.0 scale.
        Vector4 expectedResult = new Vector4(vector);
        assertTrue(vector.multiply(1).equals(expectedResult));
        assertTrue(vector.multiply(Vector4.One()).equals(expectedResult));
        assertTrue(Vector4.multiply(vector, 1).equals(expectedResult));
        assertTrue(Vector4.multiply(vector, 1).equals(Vector4.multiply(vector, 1.0f)));

        Vector4 scaledVec = Vector4.multiply(vector, 2);

        // Test 2.0 scale.
        assertTrue(new Vector4(vector).multiply(2).equals(scaledVec));
        assertTrue(new Vector4(vector).multiply(new Vector4(2)).equals(scaledVec));
        assertTrue(Vector4.multiply(vector, 2).equals(scaledVec));
        assertTrue(Vector4.multiply(vector, 2.0f).equals(scaledVec));
        assertTrue(Vector4.multiply(vector, 2).equals(Vector4.multiply(vector, 2.0f)));
        assertTrue(Vector4.multiply(vector, 2).equals(Vector4.multiply(vector, 2)));

        scaledVec = Vector4.multiply(vector, 0.999f);

        // Test 0.999 scale.
        assertTrue(new Vector4(vector).multiply(0.999f).equals(scaledVec));
        assertTrue(new Vector4(vector).multiply(new Vector4(0.999f)).equals(scaledVec));
        assertTrue(Vector4.multiply(vector, 0.999f).equals(scaledVec));
        assertTrue(Vector4.multiply(vector, 0.999f).equals(Vector4.multiply(vector, 0.999f)));

        Vector4 vector2 = new Vector4(2, 2, 2, 2);

        // Test two vectors multiplication.
        assertTrue(new Vector4(vector.x * vector2.x, vector.y * vector2.y, vector.z * vector2.z, vector.w * vector2.w).equals(Vector4.multiply(vector, vector2)));
        assertTrue(Vector4.multiply(vector2, vector).equals(new Vector4(vector.x * vector2.x, vector.y * vector2.y, vector.z * vector2.z, vector.w * vector2.w)));
        assertTrue(Vector4.multiply(vector, vector2).equals(Vector4.multiply(vector, vector2)));

        Vector4 refVec = new Vector4();

        // Overloads comparison
        Vector4 vector3 = Vector4.multiply(vector, vector2);
        Vector4.multiply(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));

        vector3 = Vector4.multiply(vector, 2);
        Vector4.multiply(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void divide()
    {
        final Vector4 vector = new Vector4(1, 2, 3, 4);

        // Test 0.0 scale.
        Vector4 expectedResult = new Vector4(Float.POSITIVE_INFINITY);
        assertTrue(new Vector4(vector).divide(0).equals(expectedResult));
        assertTrue(new Vector4(vector).divide(Vector4.Zero()).equals(expectedResult));
        assertTrue(Vector4.divide(vector, 0).equals(expectedResult));
        assertTrue(Vector4.divide(vector, 0).equals(Vector4.divide(vector, 0.0f)));

        // Test 1.0 scale.
        expectedResult = new Vector4(vector);
        assertTrue(vector.divide(1).equals(expectedResult));
        assertTrue(vector.divide(Vector4.One()).equals(expectedResult));
        assertTrue(Vector4.divide(vector, 1).equals(expectedResult));
        assertTrue(Vector4.divide(vector, 1).equals(Vector4.divide(vector, 1.0f)));

        Vector4 scaledVec = Vector4.divide(vector, 2);

        // Test 2.0 scale.
        assertTrue(new Vector4(vector).divide(2).equals(scaledVec));
        assertTrue(new Vector4(vector).divide(new Vector4(2)).equals(scaledVec));
        assertTrue(Vector4.divide(vector, 2).equals(scaledVec));
        assertTrue(Vector4.divide(vector, 2.0f).equals(scaledVec));
        assertTrue(Vector4.divide(vector, 2).equals(Vector4.divide(vector, 2.0f)));
        assertTrue(Vector4.divide(vector, 2).equals(Vector4.divide(vector, 2)));

        scaledVec = Vector4.divide(vector, 0.999f);

        // Test 0.999 scale.
        assertTrue(new Vector4(vector).divide(0.999f).equals(scaledVec));
        assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(vector).divide(new Vector4(0.999f)), scaledVec));
        assertTrue(Vector4.divide(vector, 0.999f).equals(scaledVec));
        assertTrue(Vector4.divide(vector, 0.999f).equals(Vector4.divide(vector, 0.999f)));

        Vector4 vector2 = new Vector4(2, 2, 2, 2);

        // Test two vectors multiplication.
        assertTrue(new Vector4(vector.x / vector2.x, vector.y / vector2.y, vector.z / vector2.z, vector.w / vector2.w).equals(Vector4.divide(vector, vector2)));
        assertTrue(Vector4.divide(vector2, vector).equals(new Vector4(vector2.x / vector.x, vector2.y / vector.y, vector2.z / vector.z, vector2.w / vector.w)));
        assertTrue(Vector4.divide(vector, vector2).equals(Vector4.divide(vector, vector2)));

        Vector4 refVec = new Vector4();

        // Overloads comparison
        Vector4 vector3 = Vector4.divide(vector, vector2);
        Vector4.divide(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));

        vector3 = Vector4.divide(vector, 2);
        Vector4.divide(vector, vector2, refVec);
        assertTrue(vector3.equals(refVec));
    }

    @Test
    public void distanceSquared()
    {
        Vector4 v1 = new Vector4(0.1f, 100.0f, -5.5f, 3.0f);
        Vector4 v2 = new Vector4(1.1f, -2.0f, 5.5f, 4.8f);
        float d = Vector4.distanceSquared(v1, v2);
        float expectedResult = 10529.24f;
        assertEquals(expectedResult, d);
    }

    @Test
    public void normalize()
    {
        Vector4 v1 = new Vector4(-10.5f, 0.2f, 100.0f, 4.8f);
        Vector4 v2 = new Vector4(-10.5f, 0.2f, 100.0f, 4.8f);
        v1.normalize();
        Vector4 expectedResult = new Vector4(-0.104306940274f, 0.001986798862f, 0.993399431186f, 0.047683172696f);
        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult, v1));
        v2 = Vector4.normalize(v2);
        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult, v2));
    }

    @Test
    public void hermite()
    {
        Vector4 t1 = new Vector4(1.40625f, 1.40625f, 0.2f, 0.92f);
        Vector4 t2 = new Vector4(2.662375f, 2.26537514f, 10.0f, 2f);

        Vector4 v1 = new Vector4(1, 2, 3, 4);
        Vector4 v2 = new Vector4(-1.3f, 0.1f, 30.0f, 365.20f);
        float a = 2.234f;

        Vector4 expected = new Vector4(39.0311f, 34.65557f, -132.5473f, -2626.85938f);
        Vector4 result1 = Vector4.hermite(v1, t1, v2, t2, a);
        assertTrue(Vector4Comparer.Epsilon.equals(result1, expected));

        Vector4 result2 = new Vector4();

        Vector4.hermite(v1, t1, v2, t2, a, result2);

        assertTrue(Vector4Comparer.Epsilon.equals(result1, result2));
    }

    @Test
    public void length()
    {
        Vector4 vector1 = new Vector4(1, 2, 3, 4);
        assertEquals(5.477226f, vector1.length(), 0);
    }

    @Test
    public void lengthSquared()
    {
        Vector4 vector1 = new Vector4(1, 2, 3, 4);
        assertEquals(30, vector1.lengthSquared(), 0);
    }

    // TODO(Eric): Add catmullRom test for Vector4
    @Test(enabled = false)
    public void catmullRom()
    {
/*        Vector4 expectedResult = new Vector4(5.1944f, 6.1944f);
        Vector4 v1 = new Vector4(1, 2);
        Vector4 v2 = new Vector4(3, 4);
        Vector4 v3 = new Vector4(5, 6);
        Vector4 v4 = new Vector4(7, 8);
        float value = 1.0972f;

        Vector4 result = new Vector4();
        Vector4.catmullRom(v1, v2, v3, v4, value, result);

        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult, Vector4.catmullRom(v1, v2, v3, v4, value)));
        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult, result));*/
    }

    // TODO(Eric): Add transform test for Vector4
    @Test(enabled = false)
    public void transform()
    {
        // STANDARD OVERLOADS TEST
/*
        Vector4 expectedResult1 = new Vector4(51, 58, 65);
        Vector4 expectedResult2 = new Vector4(33, -14, -1);

        Vector4 v1 = new Vector4(1, 2, 3, 4);
        Matrix m1 = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Vector4 v2 = new Vector4(1, 2, 3, 4);
        Quaternion q1 = new Quaternion(2, 3, 4, 5);

        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult1, Vector4.transform(v1, m1)));
        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult2, Vector4.transform(v2, q1)));

        // OUTPUT OVERLOADS TEST

        Vector4 result1 = new Vector4();
        Vector4 result2 = new Vector4();

        Vector4.transform(v1, m1, result1);
        Vector4.transform(v2, q1, result2);

        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult1, result1));
        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult2, result2));

        // TRANSFORM ON LIST (MATRIX)
        {
            Vector4[] sourceList1 = new Vector4[10];
            Vector4[] desinationList1 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList1[i] = (new Vector4(1 + i, 1 + i, 1 + i));
                desinationList1[i] = new Vector4();
            }

            Vector4.transform(sourceList1, 0, m1, desinationList1, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(desinationList1[i], new Vector4(19 + (6 * i), 22 + (8 * i))));
            }
        }
        // TRANSFORM ON LIST (MATRIX)(DESTINATION & SOURCE)
        {
            Vector4[] sourceList2 = new Vector4[10];
            Vector4[] desinationList2 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList2[i] = (new Vector4(1 + i, 1 + i, 1 + i));
                desinationList2[i] = new Vector4();
            }

            Vector4.transform(sourceList2, 2, m1, desinationList2, 1, 3);

            assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), desinationList2[0]));

            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(31, 38), desinationList2[1]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(37, 46), desinationList2[2]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(43, 54), desinationList2[3]));

            for (int i = 4; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), desinationList2[i]));
            }
        }
        // TRANSFORM ON LIST (MATRIX)(SIMPLE)
        {
            Vector4[] sourceList3 = new Vector4[10];
            Vector4[] desinationList3 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList3[i] = (new Vector4(1 + i, 1 + i, 1 + i));
                desinationList3[i] = new Vector4();
            }

            Vector4.transform(sourceList3, m1, desinationList3);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(desinationList3[i], new Vector4(19 + (6 * i), 22 + (8 * i))));
            }
        }
        // TRANSFORM ON LIST (QUATERNION)
        {
            Vector4[] sourceList4 = new Vector4[10];
            Vector4[] desinationList4 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList4[i] = (new Vector4(1 + i, 1 + i, 1 + i));
                desinationList4[i] = new Vector4();
            }

            Vector4.transform(sourceList4, 0, q3, desinationList4, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(-45 + (-45 * i), 9 + (9 * i)), desinationList4[i]));
            }
        }
        // TRANSFORM ON LIST (QUATERNION)(DESTINATION & SOURCE)
        {
            Vector4[] sourceList5 = new Vector4[10];
            Vector4[] desinationList5 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList5[i] = (new Vector4(1 + i, 1 + i, 1 + i));
                desinationList5[i] = new Vector4();
            }

            Vector4.transform(sourceList5, 2, q3, desinationList5, 1, 3);

            assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), desinationList5[0]));

            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(-135, 27), desinationList5[1]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(-180, 36), desinationList5[2]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(-225, 45), desinationList5[3]));

            for (int i = 4; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), desinationList5[i]));
            }
        }
        // TRANSFORM ON LIST (QUATERNION)(SIMPLE)
        {
            Vector4[] sourceList6 = new Vector4[10];
            Vector4[] desinationList6 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceList6[i] = (new Vector4(1 + i, 1 + i, 1 + i));
                desinationList6[i] = new Vector4();
            }

            Vector4.transform(sourceList6, q3, desinationList6);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(-45 + (-45 * i), 9 + (9 * i)), desinationList6[i]));
            }
        }*/
    }

    // TODO(Eric): Add transformNormal for Vector4
    @Test(enabled = false)
    public void transformNormal()
    {
/*        Vector4 normal = new Vector4(1.5f, 2.5f);
        Matrix matrix = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Vector4 expectedResult1 = new Vector4(14, 18);
        Vector4 expectedResult2 = new Vector4(expectedResult1);

        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult1, Vector4.transformNormal(normal, matrix)));

        Vector4 result = new Vector4();
        Vector4.transformNormal(normal, matrix, result);

        assertTrue(Vector4Comparer.Epsilon.equals(expectedResult2, result));

        // TRANSFORM ON LIST
        {
            Vector4[] sourceArray1 = new Vector4[10];
            Vector4[] destinationArray1 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceArray1[i] = new Vector4(i, i);
                destinationArray1[i] = new Vector4();
            }

            Vector4.transformNormal(sourceArray1, 0, matrix, destinationArray1, 0, 10);

            for (int i = 0; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(0 + (6 * i), 0 + (8 * i)), destinationArray1[i]));
            }
        }
        // TRANSFORM ON LIST(SOURCE OFFSET)
        {
            Vector4[] sourceArray2 = new Vector4[10];
            Vector4[] destinationArray2 = new Vector4[10];

            for (int i = 0; i < 10; i++)
            {
                sourceArray2[i] = new Vector4(i, i);
                destinationArray2[i] = new Vector4();
            }

            Vector4.transformNormal(sourceArray2, 5, matrix, destinationArray2, 0, 5);

            for (int i = 0; i < 5; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(30 + (6 * i), 40 + (8 * i)), destinationArray2[i]));
            }

            for (int i = 5; i < 10; i++)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), destinationArray2[i]));
            }
        }
        // TRANSFORM ON LIST(DESTINATION OFFSET)
        {
            Vector4[] sourceArray3 = new Vector4[10];
            Vector4[] destinationArray3 = new Vector4[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray3[i] = new Vector4(i, i);
                destinationArray3[i] = new Vector4();
            }

            Vector4.transformNormal(sourceArray3, 0, matrix, destinationArray3, 5, 5);

            for (int i = 0; i < 6; ++i)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), destinationArray3[i]));
            }

            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(6, 8), destinationArray3[6]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(12, 16), destinationArray3[7]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(18, 24), destinationArray3[8]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(24, 32), destinationArray3[9]));
        }
        // TRANSFORM ON LIST(DESTINATION & SOURCE)
        {
            Vector4[] sourceArray4 = new Vector4[10];
            Vector4[] destinationArray4 = new Vector4[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray4[i] = new Vector4(i, i);
                destinationArray4[i] = new Vector4();
            }

            Vector4.transformNormal(sourceArray4, 2, matrix, destinationArray4, 3, 6);

            for (int i = 0; i < 3; ++i)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), destinationArray4[i]));
            }

            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(12, 16), destinationArray4[3]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(18, 24), destinationArray4[4]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(24, 32), destinationArray4[5]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(30, 40), destinationArray4[6]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(36, 48), destinationArray4[7]));
            assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(42, 56), destinationArray4[8]));

            assertTrue(Vector4Comparer.Epsilon.equals(Vector4.Zero(), destinationArray4[9]));
        }
        // TRANSFORM ON LIST(SIMPLE)
        {
            Vector4[] sourceArray5 = new Vector4[10];
            Vector4[] destinationArray5 = new Vector4[10];

            for (int i = 0; i < 10; ++i)
            {
                sourceArray5[i] = new Vector4(i, i);
                destinationArray5[i] = new Vector4();
            }

            Vector4.transformNormal(sourceArray5, matrix, destinationArray5);

            for (int i = 0; i < 10; ++i)
            {
                assertTrue(Vector4Comparer.Epsilon.equals(new Vector4(0 + (6 * i), 0 + (8 * i)), destinationArray5[i]));
            }
        }*/
    }

    @Test(enabled = false)
    public void typeConverter()
    {
        // TODO(Eric): Enable this test once this class is created
        // NOTE(Eric): This is used to return the Vector4TypeConverter from Design
        // Vector4TypeConverter converter = TypeDescriptor.GetConverter(typeof(Vector4));
        // Locale invariantCulture = Locale.ROOT;

        // assertTrue(new Vector4(32, 64), converter.ConvertFromString(null, invariantCulture, "32, 64"));
        // assertTrue(new Vector4(0.5f, 2.75f), converter.ConvertFromString(null, invariantCulture, "0.5, 2.75"));
        // assertTrue(new Vector4(1024.5f, 2048.75f), converter.ConvertFromString(null, invariantCulture, "1024.5, 2048.75"));
        // assertTrue("32, 64", converter.ConvertToString(null, invariantCulture, new Vector4(32, 64)));
        // assertTrue("0.5, 2.75", converter.ConvertToString(null, invariantCulture, new Vector4(0.5f, 2.75f)));
        // assertTrue("1024.5, 2048.75", converter.ConvertToString(null, invariantCulture, new Vector4(1024.5f, 2048.75f)));

        // Locale otherCulture = new Locale("el-GR");

        // assertTrue(new Vector4(1024.5f, 2048.75f), converter.ConvertFromString(null, otherCulture, "1024,5; 2048,75"));
        // assertTrue("1024,5; 2048,75", converter.ConvertToString(null, otherCulture, new Vector4(1024.5f, 2048.75f)));
    }

    @Test
    public void hashCodeTest()
    {
        // Checking for overflows in hash calculation.
        Vector4 max = new Vector4(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector4 min = new Vector4(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        assertNotEquals(max.hashCode(), Vector4.Zero().hashCode());
        assertNotEquals(min.hashCode(), Vector4.Zero().hashCode());

        // Common values
        Vector4 a = new Vector4(0f, 0f, 0f, 0f);
        assertEquals(a.hashCode(), Vector4.Zero().hashCode());
        assertNotEquals(a.hashCode(), Vector4.One().hashCode());

        // Individual properties alter hash
        Vector4 xa = new Vector4(2f, 1f, 5f, 8f);
        Vector4 xb = new Vector4(3f, 1f, 5f, 8f);
        Vector4 ya = new Vector4(1f, 2f, 5f, 8f);
        Vector4 yb = new Vector4(1f, 3f, 5f, 8f);
        Vector4 za = new Vector4(2f, 1f, 5f, 8f);
        Vector4 zb = new Vector4(2f, 1f, 7f, 8f);
        Vector4 wa = new Vector4(4f, 1f, 5f, 8f);
        Vector4 wb = new Vector4(4f, 1f, 5f, 9f);
        assertNotEquals(xa.hashCode(), xb.hashCode(), "Different properties should change hash.");
        assertNotEquals(ya.hashCode(), yb.hashCode(), "Different properties should change hash.");
        assertNotEquals(za.hashCode(), zb.hashCode(), "Different properties should change hash.");
        assertNotEquals(wa.hashCode(), wb.hashCode(), "Different properties should change hash.");
        assertNotEquals(xa.hashCode(), ya.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(xb.hashCode(), yb.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(ya.hashCode(), za.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(yb.hashCode(), zb.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(za.hashCode(), wa.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(zb.hashCode(), wb.hashCode(), "Identical values on different properties should have different hashes.");
        assertNotEquals(xa.hashCode(), yb.hashCode());
        assertNotEquals(ya.hashCode(), xb.hashCode());
    }

    @Test
    public void toStringTest()
    {
        assertTrue(new Vector4(10, 20, 3.5f, -100).toString().equals("{X:10.0 Y:20.0 Z:3.5 W:-100.0}"));
    }

}
