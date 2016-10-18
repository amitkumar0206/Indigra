/*
 * copyright Agranee Solutions Ltd.
 */
package com.agranee.indigra.filereader;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.agranee.indigra.dto.IndigraDataObject;
import com.agranee.indigra.filereader.exception.IndigraBusinessException;

/**
 * Data Objevct to Load XML file
 * @author tjotwani
 *
 */
public interface FileReader
{
	public Map<String, String> getBuildData() throws IndigraBusinessException;

	public List<IndigraDataObject> readFile( File file ) throws IndigraBusinessException;
}
