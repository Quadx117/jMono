package framework;

import static org.testng.Assert.*;
import jMono_Framework.BoundingSphere;
import jMono_Framework.ContainmentType;
import jMono_Framework.math.Vector3;

import org.testng.annotations.Test;

import utilities.BoundingSphereComparer;

public class BoundingSphereTest
{
    @Test
    public void BoundingSphereTests()
    {
        BoundingSphere zeroPoint = BoundingSphere.createFromPoints(new Vector3[] { Vector3.Zero() });
        assertTrue(new BoundingSphere().equals(zeroPoint));

        BoundingSphere onePoint = BoundingSphere.createFromPoints(new Vector3[] { Vector3.One() });
        assertTrue(new BoundingSphere(Vector3.One(), 0).equals(onePoint));

        BoundingSphere twoPoint = BoundingSphere.createFromPoints(new Vector3[] { Vector3.Zero(),
                                                                                 Vector3.One() });
        assertTrue(new BoundingSphere(new Vector3(0.5f, 0.5f, 0.5f), 0.8660254f).equals(twoPoint));

        BoundingSphere threePoint = BoundingSphere.createFromPoints(new Vector3[] { new Vector3(0, 0, 0),
                                                                                   new Vector3(-1, 0, 0),
                                                                                   new Vector3(1, 1, 1) });
        assertTrue(BoundingSphereComparer.Epsilon.equals(new BoundingSphere(new Vector3(0, 0.5f, 0.5f),
                                                                            1.224745f),
                                                         threePoint));

        Vector3[] eightPointTestInput = new Vector3[]
        {
         new Vector3(54.58071f, 124.9063f, 56.0016f),
         new Vector3(54.52138f, 124.9063f, 56.13985f),
         new Vector3(54.52208f, 124.8235f, 56.14014f),
         new Vector3(54.5814f, 124.8235f, 56.0019f),
         new Vector3(1145.415f, 505.913f, -212.5173f),
         new Vector3(611.4731f, 505.9535f, 1031.893f),
         new Vector3(617.7462f, -239.7422f, 1034.584f),
         new Vector3(1151.687f, -239.7035f, -209.8246f)
        };
        BoundingSphere eightPoint = BoundingSphere.createFromPoints(eightPointTestInput);
        for (int i = 0; i < eightPointTestInput.length; i++)
        {
            assertTrue(eightPoint.contains(eightPointTestInput[i]) != ContainmentType.Disjoint);
        }

        assertThrows(IllegalArgumentException.class,
                     () -> BoundingSphere.createFromPoints(new Vector3[] {}));
    }
}
