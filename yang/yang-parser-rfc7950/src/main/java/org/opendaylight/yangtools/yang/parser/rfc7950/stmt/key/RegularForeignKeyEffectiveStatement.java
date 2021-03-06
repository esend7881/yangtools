/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt.key;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.KeyStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.SchemaNodeIdentifier;

final class RegularForeignKeyEffectiveStatement extends AbstractKeyEffectiveStatement.Foreign {
    private final @NonNull ImmutableList<? extends EffectiveStatement<?, ?>> substatements;

    RegularForeignKeyEffectiveStatement(final KeyStatement declared, final Collection<SchemaNodeIdentifier> argument,
            final ImmutableList<? extends EffectiveStatement<?, ?>> substatements) {
        super(declared, argument);
        this.substatements = requireNonNull(substatements);
    }

    @Override
    public ImmutableList<? extends EffectiveStatement<?, ?>> effectiveSubstatements() {
        return substatements;
    }
}
