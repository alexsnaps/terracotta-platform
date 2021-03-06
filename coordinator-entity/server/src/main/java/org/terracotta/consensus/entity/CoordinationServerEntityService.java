/*
 * The contents of this file are subject to the Terracotta Public License Version
 * 2.0 (the "License"); You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://terracotta.org/legal/terracotta-public-license.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Covered Software is Connection API.
 *
 * The Initial Developer of the Covered Software is
 * Terracotta, Inc., a Software AG company
 */

package org.terracotta.consensus.entity;

import org.terracotta.consensus.entity.server.LeaderElector;
import org.terracotta.entity.BasicServiceConfiguration;
import org.terracotta.entity.ClientCommunicator;
import org.terracotta.entity.ClientDescriptor;
import org.terracotta.entity.EntityMessage;
import org.terracotta.entity.EntityResponse;
import org.terracotta.entity.PassiveServerEntity;
import org.terracotta.entity.ServerEntityService;
import org.terracotta.entity.ServiceRegistry;

/**
 * @author Alex Snaps
 */
public class CoordinationServerEntityService implements ServerEntityService<CoordinationServerEntity, PassiveServerEntity<EntityMessage, EntityResponse>> {
  
  private static final String ENTITY_CLASS_NAME = "org.terracotta.consensus.entity.client.CoordinationClientEntity";

  public long getVersion() {
    return Versions.LATEST.version();
  }

  public boolean handlesEntityType(final String s) {
    return ENTITY_CLASS_NAME.equals(s);
  }

  public CoordinationServerEntity createActiveEntity(final ServiceRegistry serviceRegistry, final byte[] bytes) {
    if (bytes != null && bytes.length > 0) {
      throw new IllegalArgumentException("No config expected here!");
    }
    
    ClientCommunicator communicator = serviceRegistry.getService(new BasicServiceConfiguration<ClientCommunicator>(ClientCommunicator.class));
    
    return new CoordinationServerEntity(new LeaderElector<String, ClientDescriptor>(new ClientDescriptorPermitFactory()), communicator);
  }

  public PassiveServerEntity<EntityMessage, EntityResponse> createPassiveEntity(final ServiceRegistry serviceRegistry, final byte[] bytes) {
    throw new UnsupportedOperationException("Implement me!");
  }
}
