package org.tdmx.console.application.domain;


/**
 * A ServiceProvider.
 * 
 * @author Peter
 *
 */
public class ServiceProviderDO extends AbstractDO {

	//-------------------------------------------------------------------------
	//PUBLIC CONSTANTS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PROTECTED AND PRIVATE VARIABLES AND CONSTANTS
	//-------------------------------------------------------------------------
	private String subjectIdentifier;
	private Integer version;
	
	private String mrsHostname;
	private Integer mrsPort;
	private ConnectionTestResultDO mrsStatus;
	private HttpProxyDO mrsProxy;

	private String masHostname;
	private Integer masPort;
	private ConnectionTestResultDO masStatus;
	private HttpProxyDO masProxy;

	private String mosHostname;
	private Integer mosPort;
	private ConnectionTestResultDO mosStatus;
	private HttpProxyDO mosProxy;

	private String mdsHostname;
	private Integer mdsPort;
	private ConnectionTestResultDO mdsStatus;
	private HttpProxyDO mdsProxy;
	
	//-------------------------------------------------------------------------
	//CONSTRUCTORS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PUBLIC METHODS
	//-------------------------------------------------------------------------
	
    //-------------------------------------------------------------------------
	//PROTECTED METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PRIVATE METHODS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	//PUBLIC ACCESSORS (GETTERS / SETTERS)
	//-------------------------------------------------------------------------

	public String getSubjectIdentifier() {
		return subjectIdentifier;
	}

	public void setSubjectIdentifier(String subjectIdentifier) {
		this.subjectIdentifier = subjectIdentifier;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getMrsHostname() {
		return mrsHostname;
	}

	public void setMrsHostname(String mrsHostname) {
		this.mrsHostname = mrsHostname;
	}

	public Integer getMrsPort() {
		return mrsPort;
	}

	public void setMrsPort(Integer mrsPort) {
		this.mrsPort = mrsPort;
	}

	public ConnectionTestResultDO getMrsStatus() {
		return mrsStatus;
	}

	public void setMrsStatus(ConnectionTestResultDO mrsStatus) {
		this.mrsStatus = mrsStatus;
	}

	public String getMasHostname() {
		return masHostname;
	}

	public void setMasHostname(String masHostname) {
		this.masHostname = masHostname;
	}

	public Integer getMasPort() {
		return masPort;
	}

	public void setMasPort(Integer masPort) {
		this.masPort = masPort;
	}


	public ConnectionTestResultDO getMasStatus() {
		return masStatus;
	}

	public void setMasStatus(ConnectionTestResultDO masStatus) {
		this.masStatus = masStatus;
	}

	public String getMosHostname() {
		return mosHostname;
	}

	public void setMosHostname(String mosHostname) {
		this.mosHostname = mosHostname;
	}

	public Integer getMosPort() {
		return mosPort;
	}

	public void setMosPort(Integer mosPort) {
		this.mosPort = mosPort;
	}


	public ConnectionTestResultDO getMosStatus() {
		return mosStatus;
	}

	public void setMosStatus(ConnectionTestResultDO mosStatus) {
		this.mosStatus = mosStatus;
	}

	public String getMdsHostname() {
		return mdsHostname;
	}

	public void setMdsHostname(String mdsHostname) {
		this.mdsHostname = mdsHostname;
	}

	public Integer getMdsPort() {
		return mdsPort;
	}

	public void setMdsPort(Integer mdsPort) {
		this.mdsPort = mdsPort;
	}

	public ConnectionTestResultDO getMdsStatus() {
		return mdsStatus;
	}

	public void setMdsStatus(ConnectionTestResultDO mdsStatus) {
		this.mdsStatus = mdsStatus;
	}

	public HttpProxyDO getMrsProxy() {
		return mrsProxy;
	}

	public void setMrsProxy(HttpProxyDO mrsProxy) {
		this.mrsProxy = mrsProxy;
	}

	public HttpProxyDO getMasProxy() {
		return masProxy;
	}

	public void setMasProxy(HttpProxyDO masProxy) {
		this.masProxy = masProxy;
	}

	public HttpProxyDO getMosProxy() {
		return mosProxy;
	}

	public void setMosProxy(HttpProxyDO mosProxy) {
		this.mosProxy = mosProxy;
	}

	public HttpProxyDO getMdsProxy() {
		return mdsProxy;
	}

	public void setMdsProxy(HttpProxyDO mdsProxy) {
		this.mdsProxy = mdsProxy;
	}

	
}
