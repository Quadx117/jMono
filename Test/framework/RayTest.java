package framework;

import static org.testng.Assert.*;
import jMono_Framework.BoundingBox;
import jMono_Framework.Ray;
import jMono_Framework.math.Vector3;

import org.testng.annotations.Test;

public class RayTest
{
    @Test
    public void boundingBoxIntersects()
    {
        // Our test box.
        BoundingBox box = new BoundingBox();
        box.min = new Vector3(-10, -20, -30);
        box.max = new Vector3(10, 20, 30);
        Vector3 center = (Vector3.add(box.max, box.min)).multiply(0.5f);

        // Test misses.
        assertNull(new Ray(Vector3.subtract(center, Vector3.UnitX().multiply(40)), Vector3.UnitX().negate()).intersects(box));
        assertNull(new Ray(Vector3.add(center, Vector3.UnitX().multiply(40)), Vector3.UnitX()).intersects(box));
        assertNull(new Ray(Vector3.subtract(center, Vector3.UnitY().multiply(40)), Vector3.UnitY().negate()).intersects(box));
        assertNull(new Ray(Vector3.add(center, Vector3.UnitY().multiply(40)), Vector3.UnitY()).intersects(box));
        assertNull(new Ray(Vector3.subtract(center, Vector3.UnitZ().multiply(40)), Vector3.UnitZ().negate()).intersects(box));
        assertNull(new Ray(Vector3.add(center, Vector3.UnitZ().multiply(40)), Vector3.UnitZ()).intersects(box));

        // Test middle of each face.
        assertTrue(30.0f == new Ray(Vector3.subtract(center, Vector3.UnitX().multiply(40)), Vector3.UnitX()).intersects(box));
        assertTrue(30.0f == new Ray(Vector3.add(center, Vector3.UnitX().multiply(40)), Vector3.UnitX().negate()).intersects(box));
        assertTrue(20.0f == new Ray(Vector3.subtract(center, Vector3.UnitY().multiply(40)), Vector3.UnitY()).intersects(box));
        assertTrue(20.0f == new Ray(Vector3.add(center, Vector3.UnitY().multiply(40)), Vector3.UnitY().negate()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.subtract(center, Vector3.UnitZ().multiply(40)), Vector3.UnitZ()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.add(center, Vector3.UnitZ().multiply(40)), Vector3.UnitZ().negate()).intersects(box));

        // Test the corners along each axis.
        assertTrue(10.0f == new Ray(Vector3.subtract(box.min, Vector3.UnitX().multiply(10)), Vector3.UnitX()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.subtract(box.min, Vector3.UnitY().multiply(10)), Vector3.UnitY()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.subtract(box.min, Vector3.UnitZ().multiply(10)), Vector3.UnitZ()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.add(box.max, Vector3.UnitX().multiply(10)), Vector3.UnitX().negate()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.add(box.max, Vector3.UnitY().multiply(10)), Vector3.UnitY().negate()).intersects(box));
        assertTrue(10.0f == new Ray(Vector3.add(box.max, Vector3.UnitZ().multiply(10)), Vector3.UnitZ().negate()).intersects(box));

        // Test inside out.
        assertTrue(0.0f == new Ray(center, Vector3.UnitX()).intersects(box));
        assertTrue(0.0f == new Ray(center, Vector3.UnitX().negate()).intersects(box));
        assertTrue(0.0f == new Ray(center, Vector3.UnitY()).intersects(box));
        assertTrue(0.0f == new Ray(center, Vector3.UnitY().negate()).intersects(box));
        assertTrue(0.0f == new Ray(center, Vector3.UnitZ()).intersects(box));
        assertTrue(0.0f == new Ray(center, Vector3.UnitZ().negate()).intersects(box));
    }
}
