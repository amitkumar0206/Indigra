/*
 * copyright Agranee Solutions Ltd.
 */
package com.agranee.indigra.filereader;

import com.agranee.indigra.filereader.excel.ExcelFileReader;
import com.agranee.indigra.filereader.exception.IndigraBusinessException;

public class FileReaderFactory
{
	private static FileReaderFactory obj = null;

	private FileReaderFactory()
	{
	}

	public static synchronized FileReaderFactory getInstance()
	{
		if ( obj == null )
		{
			obj = new FileReaderFactory();
		}
		return obj;
	}

	public FileReader getReader( String fileType ) throws IndigraBusinessException
	{
		FileReader fileReader = null;
		if ( fileType == null || "".equals( fileType ) || "xls".equalsIgnoreCase( fileType ) || "xlsx".equalsIgnoreCase( fileType ) )
		{
			//default File Reader
			fileReader = new ExcelFileReader();
		}
		else if ( "csv".equalsIgnoreCase( fileType ) )
		{
			throw new IndigraBusinessException( "FileType : CSV is not supported yet.", null );
		}
		else
		{
			throw new IndigraBusinessException( "FileType : " + fileType + " is not supported yet.", null );
		}

		return fileReader;
	}
}
