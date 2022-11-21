package com.example;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import lombok.Data;
import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;

@Data
@Entity
@RevisionEntity(UserRevisionListener.class)
public class UserRevEntity extends DefaultRevisionEntity {

    private String username;

}
