12
https://raw.githubusercontent.com/Pingvin235/bgerp/master/src/ru/bgcrm/dynamic/model/CompilationMessage.java
package ru.bgcrm.dynamic.model;

public class CompilationMessage
{
	private String source;
	private String message;
	private long line;
	private long column;
	
	public String getMessage()
    {
    	return message;
    }
	
	public void setMessage( String message )
    {
    	this.message = message;
    }
	
	public long getLine()
    {
    	return line;
    }
	
	public void setLine( long line )
    {
    	this.line = line;
    }
	
	public long getColumn()
    {
    	return column;
    }
	
	public void setColumn( long column )
    {
    	this.column = column;
    }
	
	public void setSource( String source )
    {
	    this.source = source;
    }
	
	public String getSource()
    {
	    return source;
    }
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder( 200 );
		
		if( source != null )
		{
    		result.append( source );
    		result.append( ":" );
    		result.append( line );
    		result.append( ":" );
    		result.append( column );
    		result.append( "\t" );
		}	
		result.append( message );
				
		return result.toString();
	}
}
