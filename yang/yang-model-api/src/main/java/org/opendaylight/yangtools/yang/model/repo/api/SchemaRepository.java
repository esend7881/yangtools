/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.model.repo.api;

import com.google.common.annotations.Beta;
import com.google.common.util.concurrent.ListenableFuture;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;

/**
 * Interface exposed by repository implementations. A schema repository is a logically centralized place for model
 * storage and creation of {@link SchemaContext} instances.
 */
@Beta
public interface SchemaRepository {
    /**
     * Returns {@link SchemaContextFactory} with supplied configuration.
     *
     * @param config configuration of schema context factory.
     * @return schema context factory.
     */
    @NonNull EffectiveModelContextFactory createEffectiveModelContextFactory(
            @NonNull SchemaContextFactoryConfiguration config);

    /**
     * Returns {@link SchemaContextFactory} with {@link SchemaContextFactoryConfiguration#getDefault()}.
     *
     * @return schema context factory.
     */
    default @NonNull EffectiveModelContextFactory createEffectiveModelContextFactory() {
        return createEffectiveModelContextFactory(SchemaContextFactoryConfiguration.getDefault());
    }

    <T extends SchemaSourceRepresentation> @NonNull ListenableFuture<T> getSchemaSource(@NonNull SourceIdentifier id,
            @NonNull Class<T> represetation);
}
