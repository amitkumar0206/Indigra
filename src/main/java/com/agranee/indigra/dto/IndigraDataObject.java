package com.agranee.indigra.dto;

import java.util.StringJoiner;

public class IndigraDataObject
{
	private String serialNumber;
	private String testCaseNumber;
	private String module;
	private String subModule;
	private String testCaseTag;
	private String testCaseTitle;
	private boolean execute;

	public String getTestCaseNumber()
	{
		return testCaseNumber;
	}

	public void setTestCaseNumber( String testCaseNumber )
	{
		this.testCaseNumber = testCaseNumber;
	}

	public String getTestCaseTag()
	{
		return testCaseTag;
	}

	public void setTestCaseTag( String testCaseTag )
	{
		this.testCaseTag = testCaseTag;
	}

	public String getTestCaseTitle()
	{
		return testCaseTitle;
	}

	public void setTestCaseTitle( String testCaseTitle )
	{
		this.testCaseTitle = testCaseTitle;
	}

	public boolean isExecute()
	{
		return execute;
	}

	public void setExecute( boolean execute )
	{
		this.execute = execute;
	}

	public String getSerialNumber()
	{
		return serialNumber;
	}

	public void setSerialNumber( String serialNumber )
	{
		this.serialNumber = serialNumber;
	}

	public String getModule()
	{
		return module;
	}

	public void setModule( String module )
	{
		this.module = module;
	}

	public String getSubModule()
	{
		return subModule;
	}

	public void setSubModule( String subModule )
	{
		this.subModule = subModule;
	}

	@Override
	public String toString()
	{
		StringJoiner joiner = new StringJoiner( "," );
		joiner.add( "\n IndigraDataObject [serialNumber=" + serialNumber )
				.add( "testCaseNumber=" + testCaseNumber )
				.add( "module=" + module )
				.add( "subModule=" + subModule )
				.add( "testCaseTag=" + testCaseTag )
				.add( "testCaseTitle=" + testCaseTitle )
				.add( "execute=" + execute + "]" );

		return joiner.toString();
	}

}
