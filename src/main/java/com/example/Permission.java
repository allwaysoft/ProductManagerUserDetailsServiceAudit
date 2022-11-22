package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

@Data
@Entity
@DynamicUpdate
@Audited
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String uri;
    private String method;

    @Override
    public String toString() {
        return this.name;
    }
}
