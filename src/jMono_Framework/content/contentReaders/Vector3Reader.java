package jMono_Framework.content.contentReaders;

import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;
import jMono_Framework.math.Vector3;

public class Vector3Reader extends ContentTypeReader<Vector3>
{
	public Vector3Reader()
	{
		super(Vector3.class);
	}

	@Override
	protected Vector3 read(ContentReader input, Vector3 existingInstance)
	{
		return input.readVector3();
	}
}
