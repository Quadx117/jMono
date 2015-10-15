package gameCore.content.contentReaders;

import gameCore.content.ContentReader;
import gameCore.content.ContentTypeReader;
import gameCore.math.Vector3;

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
