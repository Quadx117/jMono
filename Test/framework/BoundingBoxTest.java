package framework;

import static org.testng.Assert.*;
import jMono_Framework.BoundingBox;
import jMono_Framework.BoundingSphere;
import jMono_Framework.ContainmentType;
import jMono_Framework.math.Vector3;

import org.testng.annotations.Test;

public class BoundingBoxTest
{
    @Test
    public void boxContainsVector3Test()
    {
        BoundingBox box = new BoundingBox(Vector3.Zero(), Vector3.One());

        assertTrue(box.contains(Vector3.One().negate()).equals(ContainmentType.Disjoint));
        assertTrue(box.contains(new Vector3(0.5f, 0.5f, -1f)).equals(ContainmentType.Disjoint));
        assertTrue(box.contains(Vector3.Zero()).equals(ContainmentType.Contains));
        assertTrue(box.contains(new Vector3(0f, 0, 0.5f)).equals(ContainmentType.Contains));
        assertTrue(box.contains(new Vector3(0f, 0.5f, 0.5f)).equals(ContainmentType.Contains));
        assertTrue(box.contains(Vector3.One()).equals(ContainmentType.Contains));
        assertTrue(box.contains(new Vector3(1f, 1, 0.5f)).equals(ContainmentType.Contains));
        assertTrue(box.contains(new Vector3(1f, 0.5f, 0.5f)).equals(ContainmentType.Contains));
        assertTrue(box.contains(new Vector3(0.5f, 0.5f, 0.5f)).equals(ContainmentType.Contains));
    }

    @Test
    public void BoxContainsIdenticalBox()
    {
        BoundingBox b1 = new BoundingBox(Vector3.Zero(), Vector3.One());
        BoundingBox b2 = new BoundingBox(Vector3.Zero(), Vector3.One());

        assertTrue(b1.contains(b2).equals(ContainmentType.Contains));
    }

    @Test
    public void BoundingBoxContainsBoundingSphere()
    {
        BoundingSphere testSphere = new BoundingSphere(Vector3.Zero(), 1);
        BoundingBox testBox = new BoundingBox(Vector3.One().negate(), Vector3.One());

        assertTrue(testBox.contains(testSphere).equals(ContainmentType.Contains));

        testSphere.center.subtract(Vector3.One());

        assertTrue(testBox.contains(testSphere).equals(ContainmentType.Intersects));

        testSphere.center.subtract(Vector3.One());

        assertTrue(testBox.contains(testSphere).equals(ContainmentType.Disjoint));
    }

}
