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
package org.tdmx.lib.zone.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Random;

import javax.inject.Named;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tdmx.lib.common.domain.PageSpecifier;
import org.tdmx.lib.common.domain.ProcessingState;
import org.tdmx.lib.common.domain.ProcessingStatus;
import org.tdmx.lib.control.datasource.ThreadLocalPartitionIdProvider;
import org.tdmx.lib.control.domain.TestDataGeneratorInput;
import org.tdmx.lib.control.domain.TestDataGeneratorOutput;
import org.tdmx.lib.control.job.TestDataGenerator;
import org.tdmx.lib.control.service.MockDatabasePartitionInstaller;
import org.tdmx.lib.zone.domain.Channel;
import org.tdmx.lib.zone.domain.ChannelAuthorization;
import org.tdmx.lib.zone.domain.ChannelAuthorizationSearchCriteria;
import org.tdmx.lib.zone.domain.ChannelMessage;
import org.tdmx.lib.zone.domain.FlowQuota;
import org.tdmx.lib.zone.domain.MessageStatus;
import org.tdmx.lib.zone.domain.MessageStatusSearchCriteria;
import org.tdmx.lib.zone.domain.Zone;
import org.tdmx.lib.zone.domain.ZoneFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/org/tdmx/test-context.xml")
public class ChannelServiceRepositoryUnitTest {

	@Autowired
	private TestDataGenerator dataGenerator;

	@Autowired
	@Named("tdmx.lib.zone.ThreadLocalPartitionIdProvider")
	private ThreadLocalPartitionIdProvider zonePartitionIdProvider;
	@Autowired
	private ChannelService channelService;

	private TestDataGeneratorInput input;
	private TestDataGeneratorOutput data;

	private Zone zone;

	@Before
	public void doSetup() throws Exception {

		input = new TestDataGeneratorInput("zone.apex." + System.currentTimeMillis(),
				MockDatabasePartitionInstaller.ZP1_S1);
		input.setNumZACs(1);
		input.setNumDomains(1);
		input.setNumServicesPerDomain(1);
		input.setNumDACsPerDomain(1);
		input.setNumAddressesPerDomain(1);
		input.setNumUsersPerAddress(1);

		data = dataGenerator.setUp(input);

		zone = data.getZone();

		zonePartitionIdProvider.setPartitionId(input.getZonePartitionId());
	}

	@After
	public void doTeardown() {
		zonePartitionIdProvider.clearPartitionId();

		dataGenerator.tearDown(input, data);

	}

	@Test
	public void testAutoWire() throws Exception {
		assertNotNull(dataGenerator);
		assertNotNull(channelService);
	}

	@Test
	public void testFindById_NotFound() throws Exception {
		Channel c = channelService.findById(new Random().nextLong(), false, false);
		assertNull(c);
		c = channelService.findById(new Random().nextLong(), false, true);
		assertNull(c);
		c = channelService.findById(new Random().nextLong(), true, false);
		assertNull(c);
		c = channelService.findById(new Random().nextLong(), true, true);
		assertNull(c);
	}

	@Test
	public void testSearchCA_None() throws Exception {
		ChannelAuthorizationSearchCriteria criteria = new ChannelAuthorizationSearchCriteria(new PageSpecifier(0, 999));
		List<Channel> channels = channelService.search(zone, criteria);
		assertNotNull(channels);
		assertEquals(1, channels.size());
	}

	@Test
	public void testSearchCA_OriginAll() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorizationSearchCriteria criteria = new ChannelAuthorizationSearchCriteria(new PageSpecifier(0, 999));
		criteria.getOrigin().setLocalName(ca.getChannel().getOrigin().getLocalName());
		criteria.getOrigin().setDomainName(ca.getChannel().getOrigin().getDomainName());

		List<Channel> channels = channelService.search(zone, criteria);
		assertNotNull(channels);
		assertEquals(1, channels.size());
	}

	@Test
	public void testSearchCA_DestinationAll() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorizationSearchCriteria criteria = new ChannelAuthorizationSearchCriteria(new PageSpecifier(0, 999));
		criteria.getDestination().setLocalName(ca.getChannel().getDestination().getLocalName());
		criteria.getDestination().setDomainName(ca.getChannel().getDestination().getDomainName());
		criteria.getDestination().setServiceName(ca.getChannel().getDestination().getServiceName());

		List<Channel> channels = channelService.search(zone, criteria);
		assertNotNull(channels);
		assertEquals(1, channels.size());
	}

	@Test
	public void testSearchCA_OriginAndDestinationAll() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorizationSearchCriteria criteria = new ChannelAuthorizationSearchCriteria(new PageSpecifier(0, 999));
		criteria.getOrigin().setLocalName(ca.getChannel().getOrigin().getLocalName());
		criteria.getOrigin().setDomainName(ca.getChannel().getOrigin().getDomainName());
		criteria.getDestination().setLocalName(ca.getChannel().getDestination().getLocalName());
		criteria.getDestination().setDomainName(ca.getChannel().getDestination().getDomainName());
		criteria.getDestination().setServiceName(ca.getChannel().getDestination().getServiceName());

		List<Channel> channels = channelService.search(zone, criteria);
		assertNotNull(channels);
		assertEquals(1, channels.size());
	}

	@Test
	public void testSearchStatus_RelayPendingMessages() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		MessageStatusSearchCriteria criteria = new MessageStatusSearchCriteria(new PageSpecifier(0, 999));
		criteria.getOrigin().setLocalName(ca.getChannel().getOrigin().getLocalName());
		criteria.getOrigin().setDomainName(ca.getChannel().getOrigin().getDomainName());
		criteria.getDestination().setLocalName(ca.getChannel().getDestination().getLocalName());
		criteria.getDestination().setDomainName(ca.getChannel().getDestination().getDomainName());
		criteria.getDestination().setServiceName(ca.getChannel().getDestination().getServiceName());
		criteria.setMessageStatus(MessageStatus.SUBMITTED);
		criteria.setProcessingStatus(ProcessingStatus.PENDING);

		List<ChannelMessage> messages = channelService.search(zone, criteria);
		assertNotNull(messages);
		// currently the test data factory creates the messages without pending status
		assertEquals(0, messages.size());
	}

	@Test
	public void testSearchStatusIds_RelayPendingMessages() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		MessageStatusSearchCriteria criteria = new MessageStatusSearchCriteria(new PageSpecifier(0, 999));
		criteria.getOrigin().setLocalName(ca.getChannel().getOrigin().getLocalName());
		criteria.getOrigin().setDomainName(ca.getChannel().getOrigin().getDomainName());
		criteria.getDestination().setLocalName(ca.getChannel().getDestination().getLocalName());
		criteria.getDestination().setDomainName(ca.getChannel().getDestination().getDomainName());
		criteria.getDestination().setServiceName(ca.getChannel().getDestination().getServiceName());
		criteria.setMessageStatus(MessageStatus.SUBMITTED);
		criteria.setProcessingStatus(ProcessingStatus.PENDING);

		List<Long> messages = channelService.getStatusReferences(zone, criteria, 100);
		assertNotNull(messages);
		// currently the test data factory creates the messages without pending status TODO
		assertEquals(0, messages.size());
	}

	@Test
	public void testLookup_FindByChannel() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorization storedCA = channelService.findByChannel(zone, ca.getChannel().getDomain(),
				ca.getChannel().getOrigin(), ca.getChannel().getDestination());
		assertNotNull(storedCA);
	}

	@Test
	public void testSearchCA_UnknownZone() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorizationSearchCriteria criteria = new ChannelAuthorizationSearchCriteria(new PageSpecifier(0, 999));
		criteria.getOrigin().setLocalName(ca.getChannel().getOrigin().getLocalName());

		Zone gugus = new Zone(zone.getAccountZoneId(), zone.getZoneApex());
		gugus.setId(new Random().nextLong());

		List<Channel> channels = channelService.search(gugus, criteria);
		assertNotNull(channels);
		assertEquals(0, channels.size());
	}

	@Test
	public void testUpdateChannel_DSProcessingState() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorization storedCA = channelService.findByChannel(zone, ca.getChannel().getDomain(),
				ca.getChannel().getOrigin(), ca.getChannel().getDestination());
		assertNotNull(storedCA);

		Channel c = storedCA.getChannel();
		assertNotNull(c);

		ProcessingState error = ProcessingState.error(1, "unit test");

		channelService.updateStatusDestinationSession(c.getId(), error);

		Channel storedC = channelService.findById(c.getId(), false, false);
		assertNotNull(storedC);

		assertEquals(error.getErrorCode(), storedC.getProcessingState().getErrorCode());
		assertEquals(error.getErrorMessage(), storedC.getProcessingState().getErrorMessage());
		assertEquals(error.getStatus(), storedC.getProcessingState().getStatus());
		assertEquals(error.getTimestamp(), storedC.getProcessingState().getTimestamp());
	}

	@Test
	public void testUpdateStatus_ChannelMessageProcessingState() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorization storedCA = channelService.findByChannel(zone, ca.getChannel().getDomain(),
				ca.getChannel().getOrigin(), ca.getChannel().getDestination());
		assertNotNull(storedCA);

		Channel c = storedCA.getChannel();
		assertNotNull(c);

		ProcessingState success = ProcessingState.none();

		ChannelMessage cm = ZoneFacade.createChannelMessage("" + System.currentTimeMillis(), zone, c, success);
		channelService.create(cm);
		assertNotNull(cm.getId());

		ProcessingState error = ProcessingState.error(1, "unit test");
		assertNotNull(cm);

		channelService.updateMessageProcessingState(cm.getState().getId(), error);

		ChannelMessage storedCm = channelService.findByMessageId(cm.getId());
		assertNotNull(storedCm);

		assertEquals(error.getErrorCode(), storedCm.getState().getProcessingState().getErrorCode());
		assertEquals(error.getErrorMessage(), storedCm.getState().getProcessingState().getErrorMessage());
		assertEquals(error.getStatus(), storedCm.getState().getProcessingState().getStatus());
		assertEquals(error.getTimestamp(), storedCm.getState().getProcessingState().getTimestamp());
	}

	@Test
	public void testUpdateCA_ChannelAuthorizationProcessingState() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorization storedCA = channelService.findByChannel(zone, ca.getChannel().getDomain(),
				ca.getChannel().getOrigin(), ca.getChannel().getDestination());
		assertNotNull(storedCA);

		Channel c = storedCA.getChannel();
		assertNotNull(c);

		ProcessingState error = ProcessingState.error(1, "unit test");

		channelService.updateStatusChannelAuthorization(c.getId(), error);

		Channel storedC = channelService.findById(c.getId(), false, true);
		assertNotNull(storedC);

		storedCA = storedC.getAuthorization();

		assertEquals(error.getErrorCode(), storedCA.getProcessingState().getErrorCode());
		assertEquals(error.getErrorMessage(), storedCA.getProcessingState().getErrorMessage());
		assertEquals(error.getStatus(), storedCA.getProcessingState().getStatus());
		assertEquals(error.getTimestamp(), storedCA.getProcessingState().getTimestamp());
	}

	@Test
	public void testUpdateFQ_ChannelFlowQuotaProcessingState() throws Exception {
		ChannelAuthorization ca = data.getDomains().get(0).getAuths().get(0);

		ChannelAuthorization storedCA = channelService.findByChannel(zone, ca.getChannel().getDomain(),
				ca.getChannel().getOrigin(), ca.getChannel().getDestination());
		assertNotNull(storedCA);

		Channel c = storedCA.getChannel();
		assertNotNull(c);

		ProcessingState error = ProcessingState.error(1, "unit test");

		channelService.updateStatusFlowQuota(c.getQuota().getId(), error);

		Channel storedC = channelService.findById(c.getId(), true, false);
		assertNotNull(storedC);

		FlowQuota storedFQ = storedC.getQuota();

		assertEquals(error.getErrorCode(), storedFQ.getProcessingState().getErrorCode());
		assertEquals(error.getErrorMessage(), storedFQ.getProcessingState().getErrorMessage());
		assertEquals(error.getStatus(), storedFQ.getProcessingState().getStatus());
		assertEquals(error.getTimestamp(), storedFQ.getProcessingState().getTimestamp());
	}

}