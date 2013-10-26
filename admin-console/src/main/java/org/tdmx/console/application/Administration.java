package org.tdmx.console.application;

import java.util.List;

import org.tdmx.console.application.job.BackgroundJob;
import org.tdmx.console.application.service.ObjectRegistry;
import org.tdmx.console.application.service.ProblemRegistry;
import org.tdmx.console.application.service.ProxyService;

public interface Administration {

	public ProblemRegistry getProblemRegistry();
	public ObjectRegistry getObjectRegistry();
	public List<BackgroundJob> getBackgroundJobs();
	public ProxyService getProxyService();
	
}
