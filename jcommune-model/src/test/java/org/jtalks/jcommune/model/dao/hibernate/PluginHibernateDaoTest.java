/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.model.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.jcommune.model.PersistedObjectsFactory;
import org.jtalks.jcommune.model.dao.PluginDao;
import org.jtalks.jcommune.model.entity.PluginConfiguration;
import org.jtalks.jcommune.model.entity.PluginConfigurationProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.*;

/**
 * @author Anuar Nurmakanov
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/jcommune/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PluginHibernateDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private PluginDao pluginDao;
    private Session session;

    @BeforeMethod
    public void init() {
        this.session = sessionFactory.getCurrentSession();
        PersistedObjectsFactory.setSession(session);
    }

    /*===== Common methods =====*/

    @Test
    public void getShouldReturnPluginById() {
        PluginConfiguration pluginConfiguration = PersistedObjectsFactory.getDefaultPlugin();

        PluginConfiguration foundPluginConfiguration = pluginDao.get(pluginConfiguration.getId());

        assertNotNull(foundPluginConfiguration);
        assertEquals(foundPluginConfiguration.getId(), pluginConfiguration.getId(),
                "Get should return pluginConfiguration by it ID, so found pluginConfiguration must have the same ID as passed to get.");
    }

    @Test
    public void getWithPassedIdOfNonExistPluginShouldReturnNull() {
        PluginConfiguration nonExistPluginConfiguration = pluginDao.get(-788888L);

        assertNull(nonExistPluginConfiguration, "PluginConfiguration doesn't exist, so get must return null");
    }

    @Test
    public void saveOrUpdateShouldUpdatePluginProperties() {
        String newPluginName = "Poulpe pluginConfiguration";
        PluginConfiguration pluginConfiguration = PersistedObjectsFactory.getDefaultPlugin();
        pluginConfiguration.setName(newPluginName);

        pluginDao.saveOrUpdate(pluginConfiguration);
        session.evict(pluginConfiguration);
        PluginConfiguration updatedPluginConfiguration = (PluginConfiguration) session.get(PluginConfiguration.class, pluginConfiguration.getId());

        assertEquals(updatedPluginConfiguration.getName(), newPluginName, "After update pluginConfiguration properties must be updated.");
    }

    @Test
    public void saveOrUpdateShouldSaveNewPlugin() {
        PluginConfiguration newPluginConfiguration = new PluginConfiguration("New PluginConfiguration", true, Collections.<PluginConfigurationProperty> emptyList());

        pluginDao.saveOrUpdate(newPluginConfiguration);
        session.evict(newPluginConfiguration);
        PluginConfiguration savedPluginConfiguration = (PluginConfiguration) session.get(PluginConfiguration.class, newPluginConfiguration.getId());

        assertNotNull(savedPluginConfiguration, "PluginConfiguration should be found after persisting to database.");
    }

    @Test(expectedExceptions = org.springframework.dao.DataIntegrityViolationException.class)
    public void saveOrUpdateWithNullValuesShouldNotSavePlugin() {
        PluginConfiguration pluginConfiguration = PersistedObjectsFactory.getDefaultPlugin();
        session.save(pluginConfiguration);

        pluginConfiguration.setName(null);
        pluginDao.saveOrUpdate(pluginConfiguration);
    }

    @Test
    public void testGetByName() throws NotFoundException {
        PluginConfiguration pluginConfiguration = PersistedObjectsFactory.getDefaultPlugin();
        session.save(pluginConfiguration);

        PluginConfiguration actual = pluginDao.get(pluginConfiguration.getName());

        assertEquals(actual, pluginConfiguration);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testGetByNonExistingName() throws NotFoundException {
        pluginDao.get("Some fake name");
    }
}