/*
 * copyright Agranee Solutions Ltd.
 */
package com.agranee.indigra.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.agranee.indigra.dto.IndigraDataObject;
import com.agranee.indigra.filereader.FileReader;
import com.agranee.indigra.filereader.FileReaderFactory;
import com.agranee.indigra.filereader.exception.IndigraBusinessException;
import com.agranee.indigra.utils.IndigraUtils;

public final class Indigra
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static int batchSize = 10;

	public static String environmentName = null;
	public static final String COMMAND_PREFIX =
			" \"\" build multithread chrome \"$STORIES_FILE\" \"$BUILD_NAME\" \"$PLATFORM_NAME\" \"$PLAN_ID\" 0 \"+restApi\" 1 \"";
	public static final String COMMAND_SUFFIX = "\" 0 \"d_fr\" 0 \"k_en\" 0 \"k_fr\" 0 \"c_en\" 0 \"c_fr\" 0 \"m_en\" 0 \"m_fr\" 0 \"spf\"";

	public static void main( String[] args )
	{
		List<IndigraDataObject> list = null;

		try
		{
			if ( args == null || args.length < 2 )
			{
				LOGGER.error( "Error: Invalid Number of Parameters. Usage: indigra [fileName] [environmentName] -b[batchSize(optional)]" );
				System.exit( 0 );
			}
			else if ( args.length > 2 )
			{
				String argument = null;
				String argumentType = null;

				for ( int i = 1; i < args.length; ++i )
				{
					argument = args[ i ];
					if ( argument != null && !"".equals( argument.trim() ) )
					{
						argumentType = argument.substring( argument.indexOf( "-b" ) + 1 );
						if ( argument.startsWith( "-b" ) )
						{
							batchSize = Integer.valueOf( argumentType );
						}
						else
						{
							throw new IndigraBusinessException( "Invalid Parameter Type. " + argumentType, null );
						}
					}
				}
			}

			File file = new File( args[ 0 ] );
			environmentName = args[ 1 ];

			FileReader reader;

			String fileType = IndigraUtils.getFileExt( file );
			reader = FileReaderFactory.getInstance().getReader( fileType );

			//get list of Data from File
			list = reader.readFile( file );
			Map<String, String> buildDataMap = reader.getBuildData();

			//filter out records not supposed to run
			IndigraUtils.removeNonExecutable( list );

			List<String> batchList = IndigraUtils.createBatchOfTags( list, batchSize );

			String commandPrefix = getCommandPrefix( buildDataMap );
			String command = null;
			for ( String tag : batchList )
			{
				command = environmentName + commandPrefix + tag + COMMAND_SUFFIX;
				LOGGER.info( "Command Run: " + command );

				Process process = Runtime.getRuntime().exec( command );

				BufferedReader inStreamReader = new BufferedReader(
						new InputStreamReader( process.getInputStream() ) );

				while ( inStreamReader.readLine() != null )
				{
					LOGGER.info( inStreamReader.readLine() );
				}
			}

		}
		catch ( IndigraBusinessException e )
		{
			LOGGER.error( getStackTraceString( e ) );
		}
		catch ( IOException e )
		{
			LOGGER.error( getStackTraceString( e ) );
		}

	}

	private static String getCommandPrefix( Map<String, String> buildDataMap )
	{
		String str = COMMAND_PREFIX.replace( "$" + IndigraConstants.BUILD_NAME, buildDataMap.get( IndigraConstants.BUILD_NAME ) );
		str = str.replace( "$" + IndigraConstants.PLAN_ID, buildDataMap.get( IndigraConstants.PLAN_ID ) );
		str = str.replace( "$" + IndigraConstants.PLATFORM_NAME, buildDataMap.get( IndigraConstants.PLATFORM_NAME ) );
		str = str.replace( "$" + IndigraConstants.STORIES_FILE, buildDataMap.get( IndigraConstants.STORIES_FILE ) );
		return str;
	}

	private static String getStackTraceString( Exception e )
	{
		StackTraceElement[] stackTraceArr = e.getStackTrace();

		StringBuilder builder = new StringBuilder( "\n" );
		for ( StackTraceElement element : stackTraceArr )
		{
			builder.append( "\t" )
					.append( element.toString() )
					.append( "\n" );
		}
		return builder.toString();
	}

}
