/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.api.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The <code>ResourceMetadata</code> interface defines the API for the
 * metadata of a Sling {@link Resource}. Essentially the resource's metadata is
 * just a map of objects indexed by string keys.
 * <p>
 * The actual contents of the meta data map is implementation specific with the
 * exception of the {@link #RESOLUTION_PATH sling.resolutionPath} property which
 * must be provided by all implementations and contain the part of the request
 * URI used to resolve the resource. The type of this property value is defined
 * to be <code>String</code>.
 * <p>
 * Note, that the prefix <em>sling.</em> to key names is reserved for the
 * Sling implementation.
 *
 * Once a resource is returned by the {@link ResourceResolver}, the resource
 * metadata is made read-only and therefore can't be changed by client code!
 */
public class ResourceMetadata extends HashMap<String, Object> {

    private static final long serialVersionUID = 4692666752269523738L;

    /**
     * The name of the required property providing the part of the request URI
     * which was used to the resolve the resource to which the meta data
     * instance belongs (value is "sling.resolutionPath").
     */
    public static final String RESOLUTION_PATH = "sling.resolutionPath";

    /**
     * The name of the required property providing the part of the request URI
     * which was not used to the resolve the resource to which the meta data
     * instance belongs (value is "sling.resolutionPathInfo"). The value of this
     * property concatenated to the value of the
     * {@link #RESOLUTION_PATH sling.resolutionPath} property returns the
     * original request URI leading to the resource.
     * <p>
     * This property is optional. If missing, it should be assumed equal to an
     * empty string.
     *
     * @since 2.0.4
     */
    public static final String RESOLUTION_PATH_INFO = "sling.resolutionPathInfo";

    /**
     * The name of the optional property providing the content type of the
     * resource if the resource is streamable (value is "sling.contentType").
     * This property may be missing if the resource is not streamable or if the
     * content type is not known.
     */
    public static final String CONTENT_TYPE = "sling.contentType";

    /**
     * The name of the optional property providing the content length of the
     * resource if the resource is streamable (value is "sling.contentLength").
     * This property may be missing if the resource is not streamable or if the
     * content length is not known.
     * <p>
     * Note, that unlike the other properties, this property may be set only
     * after the resource has successfully been adapted to an
     * <code>InputStream</code> for performance reasons.
     */
    public static final String CONTENT_LENGTH = "sling.contentLength";

    /**
     * The name of the optional property providing the character encoding of the
     * resource if the resource is streamable and contains character data (value
     * is "sling.characterEncoding"). This property may be missing if the
     * resource is not streamable or if the character encoding is not known.
     */
    public static final String CHARACTER_ENCODING = "sling.characterEncoding";

    /**
     * Returns the creation time of this resource in the repository in
     * milliseconds (value is "sling.creationTime"). The type of this property
     * is <code>java.lang.Long</code>. The property may be missing if the
     * resource is not streamable or if the creation time is not known.
     */
    public static final String CREATION_TIME = "sling.creationTime";

    /**
     * Returns the last modification time of this resource in the repository in
     * milliseconds (value is "sling.modificationTime"). The type of this
     * property is <code>java.lang.Long</code>. The property may be missing
     * if the resource is not streamable or if the last modification time is not
     * known.
     */
    public static final String MODIFICATION_TIME = "sling.modificationTime";

    /**
     * Returns whether the resource resolver should continue to search for a
     * resource.
     * A resource provider can set this flag to indicate that the resource
     * resolver should search for a provider with a lower priority. If it
     * finds a resource using such a provider, that resource is returned
     * instead. If none is found this resource is returned.
     * This flag should never be manipulated by application code!
     * The value of this property has no meaning, the resource resolver
     * just checks whether this flag is set or not.
     * @since 2.2
     */
    public static final String INTERNAL_CONTINUE_RESOLVING = ":org.apache.sling.resource.internal.continue.resolving";


    /**
     * This map contains the real metadata.
     * A call to {@link #makeReadOnly()} makes it read-only
     */
    private Map<String, Object> dataMap = new HashMap<String, Object>();

    /**
     * Sets the {@link #CHARACTER_ENCODING} property to <code>encoding</code>
     * if not <code>null</code>.
     */
    public void setCharacterEncoding(final String encoding) {
        if (encoding != null) {
            put(CHARACTER_ENCODING, encoding);
        }
    }

    /**
     * Returns the {@link #CHARACTER_ENCODING} property if not <code>null</code>
     * and a <code>String</code> instance. Otherwise <code>null</code> is
     * returned.
     */
    public String getCharacterEncoding() {
        Object value = get(CHARACTER_ENCODING);
        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    /**
     * Sets the {@link #CONTENT_TYPE} property to <code>contentType</code> if
     * not <code>null</code>.
     */
    public void setContentType(final String contentType) {
        if (contentType != null) {
            put(CONTENT_TYPE, contentType);
        }
    }

    /**
     * Returns the {@link #CONTENT_TYPE} property if not <code>null</code> and
     * a <code>String</code> instance. Otherwise <code>null</code> is
     * returned.
     */
    public String getContentType() {
        Object value = get(CONTENT_TYPE);
        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    /**
     * Sets the {@link #CONTENT_LENGTH} property to <code>contentType</code>
     * if not <code>null</code>.
     */
    public void setContentLength(final long contentLength) {
        if (contentLength > 0) {
            put(CONTENT_LENGTH, contentLength);
        }
    }

    /**
     * Returns the {@link #CONTENT_LENGTH} property if not <code>null</code>
     * and a <code>long</code>. Otherwise <code>-1</code> is returned.
     */
    public long getContentLength() {
        Object value = get(CONTENT_LENGTH);
        if (value instanceof Long) {
            return (Long) value;
        }

        return -1;
    }

    /**
     * Sets the {@link #CREATION_TIME} property to <code>creationTime</code>
     * if not negative.
     */
    public void setCreationTime(final long creationTime) {
        if (creationTime >= 0) {
            put(CREATION_TIME, creationTime);
        }
    }

    /**
     * Returns the {@link #CREATION_TIME} property if not <code>null</code>
     * and a <code>long</code>. Otherwise <code>-1</code> is returned.
     */
    public long getCreationTime() {
        Object value = get(CREATION_TIME);
        if (value instanceof Long) {
            return (Long) value;
        }

        return -1;
    }

    /**
     * Sets the {@link #MODIFICATION_TIME} property to
     * <code>modificationTime</code> if not negative.
     */
    public void setModificationTime(final long modificationTime) {
        if (modificationTime >= 0) {
            put(MODIFICATION_TIME, modificationTime);
        }
    }

    /**
     * Returns the {@link #MODIFICATION_TIME} property if not <code>null</code>
     * and a <code>long</code>. Otherwise <code>-1</code> is returned.
     */
    public long getModificationTime() {
        Object value = get(MODIFICATION_TIME);
        if (value instanceof Long) {
            return (Long) value;
        }

        return -1;
    }

    /**
     * Sets the {@link #RESOLUTION_PATH} property to <code>resolutionPath</code>
     * if not <code>null</code>.
     */
    public void setResolutionPath(final String resolutionPath) {
        if (resolutionPath != null) {
            put(RESOLUTION_PATH, resolutionPath);
        }
    }

    /**
     * Returns the {@link #RESOLUTION_PATH} property if not <code>null</code>
     * and a <code>String</code> instance. Otherwise <code>null</code> is
     * returned.
     */
    public String getResolutionPath() {
        Object value = get(RESOLUTION_PATH);
        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    /**
     * Sets the {@link #RESOLUTION_PATH_INFO} property to
     * <code>resolutionPathInfo</code> if not <code>null</code>.
     */
    public void setResolutionPathInfo(final String resolutionPathInfo) {
        if (resolutionPathInfo != null) {
            put(RESOLUTION_PATH_INFO, resolutionPathInfo);
        }
    }

    /**
     * Returns the {@link #RESOLUTION_PATH_INFO} property if not
     * <code>null</code> and a <code>String</code> instance. Otherwise
     * <code>null</code> is returned.
     */
    public String getResolutionPathInfo() {
        Object value = get(RESOLUTION_PATH_INFO);
        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    /**
     * Make this object read-only. All method calls trying to modify this object
     * result in an exception!
     */
    public void makeReadOnly() {
        this.dataMap = Collections.unmodifiableMap(this.dataMap);
    }

    @Override
    public void clear() {
        this.dataMap.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.dataMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.dataMap.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return this.dataMap.entrySet();
    }

    @Override
    public Object get(final Object key) {
        return this.dataMap.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.dataMap.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.dataMap.keySet();
    }

    @Override
    public Object put(final String key, final Object value) {
        return this.dataMap.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> m) {
        this.dataMap.putAll(m);
    }

    @Override
    public Object remove(final Object key) {
        return this.dataMap.remove(key);
    }

    @Override
    public int size() {
        return this.dataMap.size();
    }

    @Override
    public Collection<Object> values() {
        return this.dataMap.values();
    }

    @Override
    public String toString() {
        return "Resource Metadata: " + this.dataMap.toString();
    }
}
