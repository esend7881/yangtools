/*
 * Copyright (c) 2017 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt.status;

import org.opendaylight.yangtools.yang.model.api.Status;
import org.opendaylight.yangtools.yang.model.api.YangStmtMapping;
import org.opendaylight.yangtools.yang.model.api.stmt.StatusEffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.StatusStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractStatementSupport;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;
import org.opendaylight.yangtools.yang.parser.spi.meta.SubstatementValidator;
import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;

public final class StatusStatementSupport
        extends AbstractStatementSupport<Status, StatusStatement, StatusEffectiveStatement> {
    private static final SubstatementValidator SUBSTATEMENT_VALIDATOR = SubstatementValidator.builder(YangStmtMapping
        .STATUS)
        .build();
    private static final StatusStatementSupport INSTANCE = new StatusStatementSupport();

    private StatusStatementSupport() {
        super(YangStmtMapping.STATUS);
    }

    public static StatusStatementSupport getInstance() {
        return INSTANCE;
    }

    @Override
    public Status parseArgumentValue(final StmtContext<?, ?, ?> ctx, final String value) {
        switch (value) {
            case "current":
                return Status.CURRENT;
            case "deprecated":
                return Status.DEPRECATED;
            case "obsolete":
                return Status.OBSOLETE;
            default:
                throw new SourceException(ctx.getStatementSourceReference(),
                    "Invalid status '%s', must be one of 'current', 'deprecated' or 'obsolete'", value);
        }
    }

    @Override
    public StatusStatement createDeclared(final StmtContext<Status, StatusStatement, ?> ctx) {
        return new StatusStatementImpl(ctx);
    }

    @Override
    public StatusEffectiveStatement createEffective(
            final StmtContext<Status, StatusStatement, StatusEffectiveStatement> ctx) {
        return new StatusEffectiveStatementImpl(ctx);
    }

    @Override
    public String internArgument(final String rawArgument) {
        if ("current".equals(rawArgument)) {
            return "current";
        } else if ("deprecated".equals(rawArgument)) {
            return "deprecated";
        } else if ("obsolete".equals(rawArgument)) {
            return "obsolete";
        } else {
            return rawArgument;
        }
    }

    @Override
    protected SubstatementValidator getSubstatementValidator() {
        return SUBSTATEMENT_VALIDATOR;
    }
}