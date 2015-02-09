package org.tdmx.server.rs.sas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "account")
@XmlType(name = "Account")
public class AccountResource {

	private Long id;
	private String accountId;
	private String firstname;
	private String lastname;
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String value) {
		this.firstname = value;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String value) {
		this.lastname = value;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String value) {
		this.email = value;
	}

}