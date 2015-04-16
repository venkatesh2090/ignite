/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cache.query;

import org.apache.ignite.*;
import org.apache.ignite.cache.query.*;
import org.apache.ignite.internal.processors.cache.*;
import org.apache.ignite.lang.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * Per-projection queries object returned to user.
 */
public class CacheQueriesProxy<K, V> implements CacheQueries<K, V>, Externalizable {
    /** */
    private static final long serialVersionUID = 0L;

    /** */
    private GridCacheGateway<K, V> gate;

    /** */
    private CacheProjectionContext<K, V> prj;

    /** */
    private CacheQueries<K, V> delegate;

    /**
     * Required by {@link Externalizable}.
     */
    public CacheQueriesProxy() {
        // No-op.
    }

    /**
     * Create cache queries implementation.
     *
     * @param cctx Context.
     * @param prj Optional cache projection.
     * @param delegate Delegate object.
     */
    public CacheQueriesProxy(GridCacheContext<K, V> cctx, @Nullable CacheProjectionContext<K, V> prj,
        CacheQueries<K, V> delegate) {
        assert cctx != null;
        assert delegate != null;

        gate = cctx.gate();

        this.prj = prj;
        this.delegate = delegate;
    }

    /**
     * Gets cache projection.
     *
     * @return Cache projection.
     */
    public CacheProjection<K, V> projection() {
        return prj;
    }

    /** {@inheritDoc} */
    @Override public CacheQuery<List<?>> createSqlFieldsQuery(String qry) {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.createSqlFieldsQuery(qry);
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public CacheQuery<Map.Entry<K, V>> createFullTextQuery(String clsName, String search) {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.createFullTextQuery(clsName, search);
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public CacheQuery<Map.Entry<K, V>> createScanQuery(@Nullable IgniteBiPredicate<K, V> filter) {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.createScanQuery(filter);
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public <R> CacheQuery<R> createSpiQuery() {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.createSpiQuery();
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public QueryCursor<List<?>> execute(String space, GridCacheTwoStepQuery qry) {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.execute(space, qry);
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public QueryCursor<List<?>> executeTwoStepQuery(String space, String sqlQry, Object[] params) {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.executeTwoStepQuery(space, sqlQry, params);
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public QueryMetrics metrics() {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.metrics();
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public Collection<GridCacheSqlMetadata> sqlMetadata() throws IgniteCheckedException {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.sqlMetadata();
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public CacheQuery<List<?>> createSqlFieldsQuery(String qry, boolean incMeta) {
        CacheProjectionContext<K, V> prev = gate.enter(prj);

        try {
            return delegate.createSqlFieldsQuery(qry, incMeta);
        }
        finally {
            gate.leave(prev);
        }
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(prj);
        out.writeObject(delegate);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        prj = (CacheProjectionContext<K, V>)in.readObject();
        delegate = (CacheQueries<K, V>)in.readObject();

        gate = prj.context().gate();
    }
}
