package framework;

import static org.testng.Assert.*;
import jMono_Framework.BoundingBox;
import jMono_Framework.BoundingFrustum;
import jMono_Framework.ContainmentType;
import jMono_Framework.Ray;
import jMono_Framework.math.MathHelper;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Vector3;

import org.testng.annotations.Test;

public class BoundingFrustumTest
{
    @Test
    public void BoundingFrustumToBoundingBoxTests()
    {
        Matrix view = Matrix.createLookAt(new Vector3(0, 0, 5), Vector3.Zero(), Vector3.Up());
        Matrix projection = Matrix.createPerspectiveFieldOfView(MathHelper.PiOver4, 1, 1, 100);
        BoundingFrustum testFrustum = new BoundingFrustum(Matrix.multiply(view, projection));

        BoundingBox bbox1 = new BoundingBox(new Vector3(0, 0, 0), new Vector3(1, 1, 1));
        assertTrue(testFrustum.contains(bbox1).equals(ContainmentType.Contains));
        assertTrue(testFrustum.intersects(bbox1));

        BoundingBox bbox2 = new BoundingBox(new Vector3(-1000, -1000, -1000),
                                            new Vector3(1000, 1000, 1000));
        assertTrue(testFrustum.contains(bbox2).equals(ContainmentType.Intersects));
        assertTrue(testFrustum.intersects(bbox2));

        BoundingBox bbox3 = new BoundingBox(new Vector3(-1000, -1000, -1000),
                                            new Vector3(0, 0, 0));
        assertTrue(testFrustum.contains(bbox3).equals(ContainmentType.Intersects));
        assertTrue(testFrustum.intersects(bbox3));

        BoundingBox bbox4 = new BoundingBox(new Vector3(-1000, -1000, -1000),
                                            new Vector3(-500, -500, -500));
        assertTrue(testFrustum.contains(bbox4).equals(ContainmentType.Disjoint));
        assertFalse(testFrustum.intersects(bbox4));
    }

    @Test
    public void BoundingFrustumToBoundingFrustumTests()
    {
        Matrix view = Matrix.createLookAt(new Vector3(0, 0, 5), Vector3.Zero(), Vector3.Up());
        Matrix projection = Matrix.createPerspectiveFieldOfView(MathHelper.PiOver4, 1, 1, 100);
        BoundingFrustum testFrustum = new BoundingFrustum(Matrix.multiply(view, projection));

        // Same frustum.
        assertTrue(testFrustum.contains(testFrustum).equals(ContainmentType.Contains));
        assertTrue(testFrustum.intersects(testFrustum));

        BoundingFrustum otherFrustum = new BoundingFrustum(Matrix.identity());

        // Smaller frustum contained entirely inside.
        Matrix view2 = Matrix.createLookAt(new Vector3(0, 0, 4), Vector3.Zero(), Vector3.Up());
        Matrix projection2 = Matrix.createPerspectiveFieldOfView(MathHelper.PiOver4, 1, 1, 50);
        otherFrustum.setMatrix(Matrix.multiply(view2, projection2));

        assertTrue(testFrustum.contains(otherFrustum).equals(ContainmentType.Contains));
        assertTrue(testFrustum.intersects(otherFrustum));

        // Same size frustum, pointing in the same direction and offset by a small amount.
        otherFrustum.setMatrix(Matrix.multiply(view2, projection));

        assertTrue(testFrustum.contains(otherFrustum).equals(ContainmentType.Intersects));
        assertTrue(testFrustum.intersects(otherFrustum));

        // Same size frustum, pointing in the opposite direction and not overlapping.
        Matrix view3 = Matrix.createLookAt(new Vector3(0, 0, 6), new Vector3(0, 0, 7), Vector3.Up());
        otherFrustum.setMatrix(Matrix.multiply(view3, projection));

        assertTrue(testFrustum.contains(otherFrustum).equals(ContainmentType.Disjoint));
        assertFalse(testFrustum.intersects(otherFrustum));

        // Larger frustum, entirely containing test frustum.
        Matrix view4 = Matrix.createLookAt(new Vector3(0, 0, 10), Vector3.Zero(), Vector3.Up());
        Matrix projection4 = Matrix.createPerspectiveFieldOfView(MathHelper.PiOver4, 1, 1, 1000);
        otherFrustum.setMatrix(Matrix.multiply(view4, projection4));

        assertTrue(testFrustum.contains(otherFrustum).equals(ContainmentType.Intersects));
        assertTrue(testFrustum.intersects(otherFrustum));

        BoundingFrustum bf =
                new BoundingFrustum(Matrix.multiply(Matrix.createLookAt(new Vector3(0, 1, 1),
                                                                        new Vector3(0, 0, 0),
                                                                        Vector3.Up()),
                                                    Matrix.createPerspectiveFieldOfView(MathHelper.PiOver4,
                                                                                        1.3f, 0.1f, 1000.0f)));
        Ray ray = new Ray(new Vector3(0, 0.5f, 0.5f), new Vector3(0, 0, 0));
        Ray ray2 = new Ray(new Vector3(0, 1.0f, 1.0f), new Vector3(0, 0, 0));
        Float value = bf.intersects(ray);
        Float value2 = bf.intersects(ray2);
        assertEquals(0.0f, value);
        assertEquals(null, value2);
    }
}
