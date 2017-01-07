package jMono_Framework.content.contentReaders;

import jMono_Framework.content.ContentReader;
import jMono_Framework.content.ContentTypeReader;
import jMono_Framework.graphics.effect.Effect;

import java.util.Locale;
import java.util.stream.Stream;

public class EffectReader extends ContentTypeReader<Effect>
{
	public EffectReader()
    {
		super(Effect.class);
    }
	
	static String [] supportedExtensions = new String[]  {".fxg"};
	
	public static String normalize(String FileName)
	{
        return ContentTypeReader.normalize(FileName, supportedExtensions);
	}
	
	// TODO: Do I need an Ellipsis (...) here (c# params keyword)
	private static String tryFindAnyCased(String search, String[] arr, String[] extensions)
	{
		return Stream.of(arr).filter(s -> Stream.of(extensions).anyMatch(ext -> s.toLowerCase(Locale.US) == (search.toLowerCase(Locale.US) + ext))).findFirst().get();
	}
	
	private static boolean contains(String search, String[] arr)
	{
		return Stream.of(arr).anyMatch(s -> s == search);
	}

	@Override
    protected Effect read(ContentReader input, Effect existingInstance)
    {
        int count = input.readInt32();
        
        Effect effect = new Effect(input.getGraphicsDevice(),input.readBytes(count));
        effect.name = input.getAssetName();
        
        return effect;
    }
}
