/*
 *(c) Copyright QNX Software Systems Ltd. 2002.
 * All Rights Reserved.
 * 
 */
package org.eclipse.cdt.debug.internal.core.model;

import org.eclipse.cdt.debug.core.cdi.CDIException;
import org.eclipse.cdt.debug.core.cdi.event.ICDIEvent;
import org.eclipse.cdt.debug.core.cdi.event.ICDIEventListener;
import org.eclipse.cdt.debug.core.cdi.model.ICDISignal;
import org.eclipse.cdt.debug.core.model.ICSignal;
import org.eclipse.debug.core.DebugException;

/**
 * Enter type comment.
 * 
 * @since: Jan 31, 2003
 */
public class CSignal extends CDebugElement implements ICSignal, ICDIEventListener
{
	private ICDISignal fCDISignal;

	/**
	 * Constructor for CSignal.
	 * @param target
	 */
	public CSignal( CDebugTarget target, ICDISignal cdiSignal )
	{
		super( target );
		fCDISignal = cdiSignal;
		getCDISession().getEventManager().addEventListener( this );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#getDescription()
	 */
	public String getDescription()
	{
		return getCDISignal().getDescription();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#getName()
	 */
	public String getName()
	{
		return getCDISignal().getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#isPassEnabled()
	 */
	public boolean isPassEnabled()
	{
		return !getCDISignal().isIgnore();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#isStopEnabled()
	 */
	public boolean isStopEnabled()
	{
		return getCDISignal().isStopSet();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#setPassEnabled(boolean)
	 */
	public void setPassEnabled( boolean enable ) throws DebugException
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#setStopEnabled(boolean)
	 */
	public void setStopEnabled( boolean enable ) throws DebugException
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.cdi.event.ICDIEventListener#handleDebugEvent(ICDIEvent)
	 */
	public void handleDebugEvent( ICDIEvent event )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#dispose()
	 */
	public void dispose()
	{
		getCDISession().getEventManager().removeEventListener( this );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.core.model.ICSignal#signal()
	 */
	public void signal() throws DebugException
	{
		try
		{
			getCDISignal().signal();
		}
		catch( CDIException e )
		{
			targetRequestFailed( e.getMessage(), null );
		}
	}
	
	protected ICDISignal getCDISignal()
	{
		return fCDISignal;
	}
}
