package org.tdmx.console.application.search.match;

import java.text.DateFormat;
import java.util.Calendar;

public class MatchValueFormatter {

	//-------------------------------------------------------------------------
	//PUBLIC CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	public static String getNumber( Long number ) {
		if ( number == null ) {
			return null;
		}
		return number.toString();
	}
	
	public static String getTime( Integer time ) {
		if ( time == null ) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.add(Calendar.SECOND, time);
		return DateFormat.getTimeInstance().format(cal.getTime());
	}
	
	public static String getDate( Long date ) {
		if ( date == null ) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		return DateFormat.getDateInstance().format(cal.getTime());
	}
	
	public static String getDateTime( Object[] dateTime ) {
		if ( dateTime == null ) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis((Long)dateTime[0]);
		cal.add(Calendar.SECOND, (Integer)dateTime[1]);
		return DateFormat.getDateTimeInstance().format(cal.getTime());
	}
	
	public static String getDateTimeTS( Long dateTimeTs ) {
		if ( dateTimeTs == null ) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateTimeTs);
		return DateFormat.getDateTimeInstance().format(cal.getTime());
	}
	
	//TODO other field normalizations
	
    //-------------------------------------------------------------------------
	//PROTECTED METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PRIVATE METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PUBLIC ACCESSORS (GETTERS / SETTERS)
	//-------------------------------------------------------------------------

}
