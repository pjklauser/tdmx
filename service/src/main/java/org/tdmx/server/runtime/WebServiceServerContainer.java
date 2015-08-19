/*
 * TDMX - Trusted Domain Messaging eXchange
 * 
 * Enterprise B2B messaging between separate corporations via interoperable cloud service providers.
 * 
 * Copyright (C) 2014 Peter Klauser (http://tdmx.org)
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 */
package org.tdmx.server.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CRL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.AsyncNCSARequestLog;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tdmx.server.ws.security.HSTSHandler;
import org.tdmx.server.ws.security.NotFoundHandler;
import org.tdmx.server.ws.security.RequireClientCertificateFilter;
import org.tdmx.server.ws.security.SessionProhibitionFilter;

public class WebServiceServerContainer implements ServerContainer {

	// -------------------------------------------------------------------------
	// PUBLIC CONSTANTS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	// -------------------------------------------------------------------------
	private static final Logger log = LoggerFactory.getLogger(WebServiceServerContainer.class);

	private static final int MILLIS_IN_ONE_SECOND = 1000;
	private static final String HTTP_1_1 = "http/1.1";
	private static final String HTTPS = "https";

	private ServerRuntimeContextService runtimeContext;

	private Filter agentAuthorizationFilter;

	private TrustManagerProvider trustProvider;
	private List<Manageable> manageables;

	// -------------------------------------------------------------------------
	// CONSTRUCTORS
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// PUBLIC METHODS
	// -------------------------------------------------------------------------

	@Override
	public void runUntilStopped(String[] args) {
		// Create a basic jetty server object without declaring the port. Since we are configuring connectors
		// directly we'll be setting ports on those connectors.
		Server server = new Server();

		TrustingSslContextFactory sslExt = new TrustingSslContextFactory();
		// we trust client certs known to our ApiName, with security in servlet "filters"
		// sslContextFactory.setCertAlias("server");
		sslExt.setRenegotiationAllowed(runtimeContext.isRenegotiationAllowed());
		// we don't NEED client auth if we want to co-host a Restfull API on this server.
		sslExt.setWantClientAuth(true);

		sslExt.setIncludeCipherSuites(runtimeContext.getHttpsCiphers());
		sslExt.setIncludeProtocols(runtimeContext.getHttpsProtocols());
		sslExt.setKeyStoreType("jks");
		sslExt.setKeyStorePath(runtimeContext.getKeyStoreFile());
		sslExt.setKeyStorePassword(runtimeContext.getKeyStorePassword());
		// TODO check if needed
		// sslContextFactory.setKeyManagerPassword("changeme");

		// HTTPS Configuration
		// A new HttpConfiguration object is needed for the next connector and you can pass the old one as an
		// argument to effectively clone the contents. On this HttpConfiguration object we add a
		// SecureRequestCustomizer which is how a new connector is able to resolve the https connection before
		// handing control over to the Jetty Server.
		HttpConfiguration httpsConfigExt = new HttpConfiguration();
		httpsConfigExt.setSecureScheme(HTTPS);
		httpsConfigExt.setSecurePort(runtimeContext.getHttpsPort());
		httpsConfigExt.setOutputBufferSize(32768);
		httpsConfigExt.addCustomizer(new SecureRequestCustomizer());

		// HTTPS connector
		// We create a second ServerConnector, passing in the http configuration we just made along with the
		// previously created ssl context factory. Next we set the port and a longer idle timeout.
		ServerConnector httpsExt = new ServerConnector(server, new SslConnectionFactory(sslExt, HTTP_1_1),
				new HttpConnectionFactory(httpsConfigExt));
		httpsExt.setPort(runtimeContext.getHttpsPort());
		httpsExt.setHost(runtimeContext.getServerAddress());
		httpsExt.setIdleTimeout(runtimeContext.getConnectionIdleTimeoutSec() * MILLIS_IN_ONE_SECOND);

		TrustingSslContextFactory sslInt = new TrustingSslContextFactory();
		// we trust all client certs, with security in servlet "filters"
		// sslContextFactory.setCertAlias("server");
		sslInt.setRenegotiationAllowed(runtimeContext.isRenegotiationAllowed());
		// we don't want client auth on internal apis
		sslInt.setWantClientAuth(false);

		sslInt.setIncludeCipherSuites(runtimeContext.getHttpsCiphers());
		sslInt.setIncludeProtocols(runtimeContext.getHttpsProtocols());
		sslInt.setKeyStoreType("jks");
		sslInt.setKeyStorePath(runtimeContext.getKeyStoreFile());
		sslInt.setKeyStorePassword(runtimeContext.getKeyStorePassword());

		// HTTPS Configuration
		// A new HttpConfiguration object is needed for the next connector and you can pass the old one as an
		// argument to effectively clone the contents. On this HttpConfiguration object we add a
		// SecureRequestCustomizer which is how a new connector is able to resolve the https connection before
		// handing control over to the Jetty Server.
		HttpConfiguration httpsConfigInt = new HttpConfiguration();
		httpsConfigInt.setSecureScheme(HTTPS);
		httpsConfigInt.setSecurePort(runtimeContext.getHttpsPort() + 1);
		httpsConfigInt.setOutputBufferSize(32768);
		httpsConfigInt.addCustomizer(new SecureRequestCustomizer());

		// HTTPS connector
		ServerConnector httpsInt = new ServerConnector(server, new SslConnectionFactory(sslInt, HTTP_1_1),
				new HttpConnectionFactory(httpsConfigInt));
		httpsInt.setPort(runtimeContext.getHttpsPort() + 1);
		httpsInt.setHost(runtimeContext.getServerAddress());
		httpsInt.setIdleTimeout(runtimeContext.getConnectionIdleTimeoutSec() * MILLIS_IN_ONE_SECOND);

		// Here you see the server having multiple connectors registered with it, now requests can flow into the server
		// from both http and https urls to their respective ports and be processed accordingly by jetty. A simple
		// handler is also registered with the server so the example has something to pass requests off to.

		// Set the connectors
		server.setConnectors(new Connector[] { httpsExt, httpsInt });

		// The following section adds some handlers, deployers and webapp providers.
		// See: http://www.eclipse.org/jetty/documentation/current/advanced-embedding.html for details.

		// Setup handlers
		HandlerCollection handlers = new HandlerCollection();
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		HSTSHandler hstsHandler = new HSTSHandler();
		NotFoundHandler notfoundHandler = new NotFoundHandler();

		handlers.setHandlers(new Handler[] { hstsHandler, contexts, requestLogHandler, notfoundHandler });

		StatisticsHandler stats = new StatisticsHandler();
		stats.setHandler(handlers);

		server.setHandler(stats);

		NCSARequestLog requestLog = new AsyncNCSARequestLog();
		requestLog.setFilename("jetty-yyyy_mm_dd.log");
		requestLog.setExtended(true);
		requestLog.setRetainDays(7);
		requestLogHandler.setRequestLog(requestLog);

		ServletContextHandler wsContext = new ServletContextHandler(ServletContextHandler.NO_SECURITY
				| ServletContextHandler.NO_SESSIONS);
		wsContext.setContextPath("/api");
		// Setup Spring context
		wsContext.addEventListener(new org.springframework.web.context.ContextLoaderListener());
		wsContext.setInitParameter("parentContextKey", "applicationContext");
		wsContext.setInitParameter("locatorFactorySelector", "classpath*:beanRefContext.xml");
		wsContext.setInitParameter("contextConfigLocation", "classpath:/ws-context.xml");

		// Add filters
		FilterHolder sf = new FilterHolder();
		sf.setFilter(new SessionProhibitionFilter());
		wsContext.addFilter(sf, "/*", EnumSet.allOf(DispatcherType.class));

		FilterHolder cf = new FilterHolder();
		cf.setFilter(new RequireClientCertificateFilter());
		wsContext.addFilter(cf, "/*", EnumSet.allOf(DispatcherType.class));

		FilterHolder fh = new FilterHolder();
		fh.setFilter(getAgentAuthorizationFilter());
		wsContext.addFilter(fh, "/v1.0/sp/mds/*", EnumSet.of(DispatcherType.REQUEST));
		wsContext.addFilter(fh, "/v1.0/sp/mos/*", EnumSet.of(DispatcherType.REQUEST));
		wsContext.addFilter(fh, "/v1.0/sp/zas/*", EnumSet.of(DispatcherType.REQUEST));
		// the MRS endpoint is filtered by ServiceProviderFilter TODO

		// Add servlets
		ServletHolder sh = new ServletHolder(CXFServlet.class);
		sh.setInitOrder(1);
		wsContext.addServlet(sh, "/*");

		ServletContextHandler rsContext = new ServletContextHandler(ServletContextHandler.NO_SECURITY
				| ServletContextHandler.NO_SESSIONS);
		rsContext.setContextPath("/rs");
		// Setup Spring context
		rsContext.addEventListener(new org.springframework.web.context.ContextLoaderListener());
		rsContext.setInitParameter("parentContextKey", "applicationContext");
		rsContext.setInitParameter("locatorFactorySelector", "classpath*:beanRefContext.xml");
		rsContext.setInitParameter("contextConfigLocation", "classpath:/rs-context.xml");

		// Add filters
		/*
		 * FilterHolder rsfh = new FilterHolder(); rsfh.setFilter(getAgentAuthorizationFilter());
		 * rsContext.addFilter(rsfh, "/sas/*", EnumSet.of(DispatcherType.REQUEST));
		 */
		// Add servlets
		ServletHolder rsSh = new ServletHolder(CXFServlet.class);
		rsSh.setInitOrder(1);
		rsContext.addServlet(rsSh, "/*");

		contexts.addHandler(wsContext);
		contexts.addHandler(rsContext);
		try {
			// init jobs
			initManageables();

			// Start the server
			server.start();

			Thread monitor = new MonitorThread(server, runtimeContext.getStopPort(), runtimeContext.getStopCommand(),
					runtimeContext.getStopAddress());
			monitor.start();

			startManageables();

			// Wait for the server to be stopped by the MonitorThread.
			server.join();

		} catch (InterruptedException ie) {
			log.warn("Container running thread interrupted.", ie);
		} catch (Exception e) {
			log.error("Starting failed.", e);
		}
		// Exiting here will terminate the application.
	}

	// -------------------------------------------------------------------------
	// PROTECTED METHODS
	// -------------------------------------------------------------------------

	void initManageables() {
		for (Manageable m : getManageables()) {
			m.initialize();
		}
	}

	void startManageables() {
		for (Manageable m : getManageables()) {
			m.start();
		}
	}

	void stopManageables() {
		for (Manageable m : getManageables()) {
			m.stop();
		}
	}

	// -------------------------------------------------------------------------
	// PRIVATE METHODS
	// -------------------------------------------------------------------------

	private class TrustingSslContextFactory extends SslContextFactory {
		@Override
		protected TrustManager[] getTrustManagers(KeyStore trustStore, Collection<? extends CRL> crls) throws Exception {
			return new TrustManager[] { getTrustProvider().getTrustManager() };
		}
	}

	private class MonitorThread extends Thread {
		private final Logger log = LoggerFactory.getLogger(MonitorThread.class);

		private final ServerSocket socket;
		private final Server server;
		private final String stopCommand;

		public MonitorThread(Server s, int port, String stopCommand, String localAddr) throws IOException {
			setDaemon(true);
			setName("ServerContainer#StopMonitor");
			this.stopCommand = stopCommand;
			this.server = s;
			this.socket = new ServerSocket(port, 1, InetAddress.getByName(localAddr));
		}

		@Override
		public void run() {
			boolean stopServer = false;
			while (!stopServer) {
				Socket accept = null;
				try {
					log.info("accepting stop connections on " + socket.getLocalPort());
					accept = socket.accept();
					log.info("accepted a stop connection.");
					BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream(),
							Charset.forName("UTF-8")));
					// read just a single line
					String line = reader.readLine();
					// IMPROVEMENT: read only enough bytes to cover the content of the stopCommand + \n
					if (line != null && line.indexOf(stopCommand) != -1) {
						stopServer = true;
						log.info("recieved stop command. Stopping container ...");
					}
				} catch (IOException e) {
					log.warn("Problem reading from stop client.", e);
				} finally {
					if (accept != null) {
						try {
							accept.close();
						} catch (IOException e) {
							log.warn("Unable to close stop client connection.", e);
						}
					}
				}

				// safety that we don't loop too quickly.
				if (!stopServer) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException ie) {
						// ignored
					}
				}
			}

			// stop all dependents
			stopManageables();

			try {
				server.stop();
			} catch (Exception e) {
				log.error("Unable to stop container.", e);
			}

			try {
				socket.close();
			} catch (IOException e) {
				log.warn("Unable to close stop server socket.", e);
			}
		}
	}

	// -------------------------------------------------------------------------
	// PUBLIC ACCESSORS (GETTERS / SETTERS)
	// -------------------------------------------------------------------------

	public Filter getAgentAuthorizationFilter() {
		return agentAuthorizationFilter;
	}

	public void setAgentAuthorizationFilter(Filter agentAuthorizationFilter) {
		this.agentAuthorizationFilter = agentAuthorizationFilter;
	}

	public TrustManagerProvider getTrustProvider() {
		return trustProvider;
	}

	public void setTrustProvider(TrustManagerProvider trustProvider) {
		this.trustProvider = trustProvider;
	}

	public List<Manageable> getManageables() {
		if (manageables == null) {
			manageables = new ArrayList<>();
		}
		return manageables;
	}

	public void setManageables(List<Manageable> manageables) {
		this.manageables = manageables;
	}

}