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
package org.tdmx.lib.zone.dao;

import java.util.List;

import org.tdmx.lib.zone.domain.Domain;
import org.tdmx.lib.zone.domain.Service;
import org.tdmx.lib.zone.domain.ServiceSearchCriteria;
import org.tdmx.lib.zone.domain.Zone;

/**
 * DAO for the Service Entity.
 * 
 * @author Peter
 * 
 */
public interface ServiceDao {

	public void persist(Service value);

	public void delete(Service value);

	public Service merge(Service value);

	public Service loadById(Long id);

	public Service loadByName(Domain domain, String serviceName);

	public List<Service> search(Zone zone, ServiceSearchCriteria criteria);
}
