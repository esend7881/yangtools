/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt.belongs_to;

import org.opendaylight.yangtools.yang.model.api.stmt.BelongsToEffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.BelongsToStatement;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.AbstractDeclaredEffectiveStatement;

abstract class AbstractBelongsToEffectiveStatement
        extends AbstractDeclaredEffectiveStatement.DefaultArgument<String, BelongsToStatement>
        implements BelongsToEffectiveStatement {
    AbstractBelongsToEffectiveStatement(final BelongsToStatement declared) {
        super(declared);
    }
}
