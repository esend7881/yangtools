/*
 * (C) Copyright 2016 Pantheon Technologies, s.r.o. and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opendaylight.yangtools.triemap;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MainNode<K, V> extends BasicNode {

    public static final AtomicReferenceFieldUpdater<MainNode, MainNode> updater = AtomicReferenceFieldUpdater.newUpdater (MainNode.class, MainNode.class, "prev");

    public volatile MainNode<K, V> prev = null;

    public abstract int cachedSize (Object ct);

    public boolean CAS_PREV (final MainNode<K, V> oldval, final MainNode<K, V> nval) {
        return updater.compareAndSet (this, oldval, nval);
    }

    public void WRITE_PREV (final MainNode<K, V> nval) {
        updater.set (this, nval);
    }

    // do we need this? unclear in the javadocs...
    // apparently not - volatile reads are supposed to be safe
    // regardless of whether there are concurrent ARFU updates
    public MainNode<K, V> READ_PREV () {
        return updater.get (this);
    }

}