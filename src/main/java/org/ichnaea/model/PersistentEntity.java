package org.ichnaea.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A Superclass implementation of an Entity which has a stable entity identifier.
 *
 * @author Tomás Sánchez
 * @version 3.0
 */
@Getter
@Setter
public abstract class PersistentEntity implements Serializable {

    static final long serialVersionUID = 1L;

    protected Long id;

    protected LocalDateTime createdAt = LocalDateTime.now();

    protected LocalDateTime lastUpdatedAt;

    public String getCreationDate() {
        return createdAt.toLocalDate().toString();
    }
}
