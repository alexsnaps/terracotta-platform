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

package org.terracotta.voltron.proxy.server.messages;

import org.terracotta.voltron.proxy.client.messages.MessageListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Alex Snaps
 */
public abstract class MessageFiring {

  private static final MessageListener FAKE = new MessageListener() {
    @Override
    public void onMessage(final Object message) {
      // no op;
    }
  };

  private final ConcurrentMap<Class<?>, MessageListener> listeners = new ConcurrentHashMap<Class<?>, MessageListener>();

  public MessageFiring(Class<?>... messageTypes) {
    for (Class<?> messageType : messageTypes) {
      this.listeners.put(messageType, FAKE);
    }
  }

  protected void fire(Object message) {
    listeners.get(message.getClass()).onMessage(message);
  }

  public <T> void registerListener(Class<T> messageType, MessageListener<T> listener) {
    if(!listeners.replace(messageType, FAKE, listener)) {
      throw new IllegalStateException();
    }
  }
}
