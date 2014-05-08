/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.impl.codec;

import org.opendaylight.yangtools.yang.binding.BindingCodec;
import org.opendaylight.yangtools.yang.data.api.Node;

public interface DomCodec<I> extends BindingCodec<Node<?>, ValueWithQName<I>>{
    @Override
    Node<?> serialize(ValueWithQName<I> input);

    @Override
    ValueWithQName<I> deserialize(Node<?> input);
}
