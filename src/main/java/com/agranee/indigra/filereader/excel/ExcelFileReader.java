/*
 * copyright Agranee Solutions Ltd.
 */
package com.agranee.indigra.filereader.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agranee.indigra.dto.IndigraDataObject;
import com.agranee.indigra.filereader.FileReader;
import com.agranee.indigra.filereader.exception.IndigraBusinessException;
import com.agranee.indigra.main.IndigraConstants;
import com.agranee.indigra.utils.IndigraUtils;

/**
 * Excel File Rwader
 * @author tjotwani
 *
 */
public class ExcelFileReader implements FileReader
{

	private Map<String, String> buildDataMap = null;

	public Map<String, String> getBuildData() throws IndigraBusinessException
	{

		if ( buildDataMap == null || buildDataMap.size() < 1 )
		{
			throw new IndigraBusinessException( "The Build Information is either not populated or some error occured during readin them.", null );
		}
		return buildDataMap;
	}

	public List<IndigraDataObject> readFile( File file ) throws IndigraBusinessException
	{
		List<IndigraDataObject> data = null;
		Workbook wb = null; //<-Interface, accepts both HSSF and XSSF.
		try
		{

			String fileExt = IndigraUtils.getFileExt( file );
			if ( "xls".equalsIgnoreCase( fileExt ) )
			{
				wb = new HSSFWorkbook( new FileInputStream( file ) );
			}
			else if ( "xlsx".equalsIgnoreCase( fileExt ) )
			{
				wb = new XSSFWorkbook( new FileInputStream( file ) );
			}
			else
			{
				throw new IllegalArgumentException( "Received file does not have a standard excel extension." );
			}

			Sheet sheet = wb.getSheetAt( 0 );
			Row row;
			Cell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();

			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows
			for ( int i = 0; i < 10 || i < rows; i++ )
			{
				row = sheet.getRow( i );
				if ( row != null )
				{
					tmp = sheet.getRow( i ).getPhysicalNumberOfCells();
					if ( tmp > cols )
						cols = tmp;
				}
			}

			data = new ArrayList<IndigraDataObject>();
			IndigraDataObject tempObj = null;

			buildDataMap = new HashMap<String, String>();

			for ( int r = 0; r < 4; r++ )
			{
				row = sheet.getRow( r );
				if ( row != null )
				{
					//get the cell value
					cell = row.getCell( 1 );

					switch ( r )
					{
						case 0:
							buildDataMap.put( IndigraConstants.BUILD_NAME, cell.getStringCellValue() );
							break;
						case 1:
							buildDataMap.put( IndigraConstants.PLAN_ID, "" + (int) cell.getNumericCellValue() );
							break;
						case 2:
							buildDataMap.put( IndigraConstants.PLATFORM_NAME, cell.getStringCellValue() );
							break;
						case 3:
							buildDataMap.put( IndigraConstants.STORIES_FILE, cell.getStringCellValue() );
							break;
					}
				}
			}

			//start the loop by 5 to ignore the header and build info
			for ( int r = 5; r < rows; r++ )
			{
				row = sheet.getRow( r );
				if ( row != null )
				{
					tempObj = new IndigraDataObject();
					for ( int c = 0; c < cols; c++ )
					{
						cell = row.getCell( (short) c );
						if ( cell != null )
						{
							switch ( c )
							{
								case 0:
									tempObj.setSerialNumber( "" + (int) cell.getNumericCellValue() );
									break;
								case 1:
									tempObj.setTestCaseNumber( cell.getStringCellValue() );
									break;
								case 2:
									tempObj.setModule( cell.getStringCellValue() );
									break;
								case 3:
									tempObj.setSubModule( cell.getStringCellValue() );
									break;
								case 4:
									tempObj.setTestCaseTag( cell.getStringCellValue() );
									break;
								case 5:
									tempObj.setTestCaseTitle( cell.getStringCellValue() );
									break;
								case 6:
									if ( "Y".equalsIgnoreCase( cell.getStringCellValue().trim() ) )
									{
										tempObj.setExecute( true );
									}
									else
									{
										tempObj.setExecute( false );
									}
									break;
							} // end Switch

						} // end cell null check
					}// end for loop for cell
						//add the object to List
					data.add( tempObj );
				}// end row null check
			}// end for loop for row
		}
		catch ( Exception ioe )
		{
			throw new IndigraBusinessException( "Some Error Occured", ioe );
		}
		finally
		{
			if ( wb != null )
			{
				try
				{
					wb.close();
				}
				catch ( IOException e )
				{
					throw new IndigraBusinessException( "Could not close File", e );
				}
			}
		}
		return data;
	}

}
