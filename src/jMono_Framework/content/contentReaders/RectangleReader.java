package jMono_Framework.content.contentReaders;

import jMono_Framework.Rectangle;
import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;

public class RectangleReader extends ContentTypeReader<Rectangle>
{
	public RectangleReader()
	{
		super(Rectangle.class);
	}

	@Override
	protected Rectangle read(ContentReader input, Rectangle existingInstance)
	{
		int left = input.readInt32();
		int top = input.readInt32();
		int width = input.readInt32();
		int height = input.readInt32();
		return new Rectangle(left, top, width, height);
	}
}
