package org.ichnaea.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A Superclass implementation of an Entity which has a stable entity identifier.
 *
 * @author Tomás Sánchez
 * @version 3.0
 */
public abstract class PersistentEntity implements Serializable {

    static final long serialVersionUID = 1L;

    protected Long id;

    protected LocalDateTime createdAt = LocalDateTime.now();

    protected LocalDateTime lastUpdatedAt;

    public String getCreationDate() {
        return createdAt.toLocalDate().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        return values;
    }
}
