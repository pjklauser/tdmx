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
package org.tdmx.lib.control.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tdmx.lib.common.domain.Job;
import org.tdmx.lib.common.domain.PageSpecifier;
import org.tdmx.lib.console.domain.ControlJobFacade;
import org.tdmx.lib.control.domain.ControlJob;
import org.tdmx.lib.control.domain.ControlJobSearchCriteria;
import org.tdmx.lib.control.domain.ControlJobStatus;
import org.tdmx.lib.control.job.JobConverter;
import org.tdmx.service.control.task.dao.ZoneTransferCommand;
import org.tdmx.service.control.task.dao.ZoneTransferTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
// @TransactionConfiguration(transactionManager="tdmx.lib.control.TransactionManager")
// @Transactional("ControlDB")
public class ControlJobServiceRepositoryUnitTest {

	@Autowired
	private JobConverter<ZoneTransferTask> cmdConverter;

	@Autowired
	private ControlJobService service;
	@Autowired
	private ObjectIdService idService;

	private Long jobId;

	@Before
	public void doSetup() throws Exception {
		ZoneTransferCommand cmd = new ZoneTransferCommand();
		cmd.setPassword("pwd");
		cmd.setUsername("un");
		ZoneTransferTask task = new ZoneTransferTask();
		task.setCommand(cmd);

		Job j = new Job();
		j.setType(cmdConverter.getType());
		cmdConverter.setData(j, task);

		ControlJob je = ControlJobFacade.createImmediateJob(idService.getNextObjectId(), ControlJobStatus.NEW, j);

		jobId = je.getId();

		service.createOrUpdate(je);
	}

	@After
	public void doTeardown() {
		ControlJob je = service.findById(jobId);
		if (je != null) {
			service.delete(je);
		}
	}

	@Test
	public void testAutoWire() throws Exception {
		assertNotNull(service);
	}

	@Test
	public void testSearch_Status() throws Exception {
		ControlJobSearchCriteria sc = new ControlJobSearchCriteria(new PageSpecifier(0, 10));
		sc.setStatus(ControlJobStatus.ERR);
		List<ControlJob> l = service.search(sc);
		assertEquals(0, l.size());

		sc.setStatus(ControlJobStatus.NEW);
		l = service.search(sc);
		assertEquals(1, l.size());
	}

	@Test
	public void testSearch_TypeStatus() throws Exception {
		ControlJobSearchCriteria sc = new ControlJobSearchCriteria(new PageSpecifier(0, 10));
		sc.setJobType(cmdConverter.getType());
		sc.setStatus(ControlJobStatus.ERR);
		List<ControlJob> l = service.search(sc);
		assertEquals(0, l.size());

		sc.setStatus(ControlJobStatus.NEW);
		l = service.search(sc);
		assertEquals(1, l.size());
	}

	@Test
	public void testSearch_TypeTimeStatus() throws Exception {
		ControlJobSearchCriteria sc = new ControlJobSearchCriteria(new PageSpecifier(0, 10));
		sc.setJobType(cmdConverter.getType());
		sc.setScheduledTimeBefore(new Date());
		sc.setStatus(ControlJobStatus.ERR);
		List<ControlJob> l = service.search(sc);
		assertEquals(0, l.size());

		sc.setStatus(ControlJobStatus.NEW);
		l = service.search(sc);
		assertEquals(1, l.size());
	}

	@Test
	public void testLookup_NotFound() throws Exception {
		ControlJob je = service.findById(new Random().nextLong());
		assertNull(je);
	}

	@Test
	public void testReserveAndModify() throws Exception {
		List<ControlJob> runnable = service.reserve(1);
		assertEquals(1, runnable.size());

		Thread.sleep(1000);

		ControlJob j = runnable.get(0);
		assertEquals(jobId, j.getId());
		assertEquals(ControlJobStatus.RUN, j.getStatus());

		j.setStatus(ControlJobStatus.OK);
		service.createOrUpdate(j);

		j = service.findById(jobId);
		assertEquals(ControlJobStatus.OK, j.getStatus());
	}

}