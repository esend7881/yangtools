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

/**
 * An immutable TrieMap.
 *
 * @author Robert Varga
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @deprecated use {@link tech.pantheon.triemap.ImmutableTrieMap} instead.
 */
@Deprecated
public final class ImmutableTrieMap<K, V> extends TrieMap<K, V> {
    private static final long serialVersionUID = 1L;

    ImmutableTrieMap(final tech.pantheon.triemap.ImmutableTrieMap<K, V> delegate) {
        super(delegate);
    }

    @Override
    public TrieMap<K, V> mutableSnapshot() {
        return new MutableTrieMap<>(delegate().mutableSnapshot());
    }

    @Override
    public ImmutableTrieMap<K, V> immutableSnapshot() {
        return this;
    }
}
