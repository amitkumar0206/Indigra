package com.agranee.indigra.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import com.agranee.indigra.dto.IndigraDataObject;

public class IndigraUtils
{
	public static String getFileExt( File file )
	{
		return file.getName().substring( file.getName().lastIndexOf( "." ) + 1 );
	}

	public static void removeNonExecutable( List<IndigraDataObject> list )
	{
		Iterator<IndigraDataObject> itr = list.iterator();

		while ( itr.hasNext() )
		{
			if ( !( itr.next().isExecute() ) )
			{
				itr.remove();
			}
		}
	}

	public static List<String> createBatchOfTags( final List<IndigraDataObject> list, final int batchSize )
	{
		List<String> batchList = new ArrayList<String>();

		StringJoiner builder = new StringJoiner( "," );

		int i = 0;

		for ( IndigraDataObject dataObject : list )
		{
			++i;
			builder.add( "+" + dataObject.getTestCaseTag() );
			if ( i == batchSize )
			{
				//add the tags
				batchList.add( builder.toString() );
				builder = new StringJoiner( "," );
				i = 0;
			}
		}

		if ( i > 0 && i < batchSize )
		{
			batchList.add( builder.toString() );
		}

		return batchList;
	}
}
