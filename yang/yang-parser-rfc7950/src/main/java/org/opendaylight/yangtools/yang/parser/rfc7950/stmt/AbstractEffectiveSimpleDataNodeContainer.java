/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.AugmentationTarget;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;
import org.opendaylight.yangtools.yang.model.api.RevisionAwareXPath;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.meta.DeclaredStatement;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.WhenEffectiveStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.CopyHistory;
import org.opendaylight.yangtools.yang.parser.spi.meta.CopyType;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;

public abstract class AbstractEffectiveSimpleDataNodeContainer<D extends DeclaredStatement<QName>> extends
        AbstractEffectiveDocumentedDataNodeContainer<QName, D> implements AugmentationTarget, DataSchemaNode {

    private final ImmutableSet<AugmentationSchemaNode> augmentations;
    private final RevisionAwareXPath whenCondition;
    private final @NonNull SchemaPath path;
    private final boolean configuration;
    private final boolean addedByUses;
    private final boolean augmenting;

    protected AbstractEffectiveSimpleDataNodeContainer(final StmtContext<QName, D, ?> ctx) {
        super(ctx);

        this.path = ctx.getSchemaPath().get();
        this.configuration = ctx.isConfiguration();

        whenCondition = findFirstEffectiveSubstatementArgument(WhenEffectiveStatement.class).orElse(null);

        // initSubstatementCollectionsAndFields

        Set<AugmentationSchemaNode> augmentationsInit = new LinkedHashSet<>();
        for (EffectiveStatement<?, ?> effectiveStatement : effectiveSubstatements()) {
            if (effectiveStatement instanceof AugmentationSchemaNode) {
                augmentationsInit.add((AugmentationSchemaNode) effectiveStatement);
            }
        }
        this.augmentations = ImmutableSet.copyOf(augmentationsInit);

        // initCopyType
        final CopyHistory copyTypesFromOriginal = ctx.getCopyHistory();
        if (copyTypesFromOriginal.contains(CopyType.ADDED_BY_USES_AUGMENTATION)) {
            this.augmenting = true;
            this.addedByUses = true;
        } else {
            this.augmenting = copyTypesFromOriginal.contains(CopyType.ADDED_BY_AUGMENTATION);
            this.addedByUses = copyTypesFromOriginal.contains(CopyType.ADDED_BY_USES);
        }
    }

    @Override
    public QName getQName() {
        return path.getLastComponent();
    }

    @Override
    public SchemaPath getPath() {
        return path;
    }

    @Deprecated
    @Override
    public boolean isAugmenting() {
        return augmenting;
    }

    @Deprecated
    @Override
    public boolean isAddedByUses() {
        return addedByUses;
    }

    @Override
    public boolean isConfiguration() {
        return configuration;
    }

    @Override
    public Collection<? extends AugmentationSchemaNode> getAvailableAugmentations() {
        return augmentations;
    }

    @Override
    public final Optional<RevisionAwareXPath> getWhenCondition() {
        return Optional.ofNullable(whenCondition);
    }
}
