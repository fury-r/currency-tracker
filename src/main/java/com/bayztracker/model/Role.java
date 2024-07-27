package com.bayztracker.model;

import javax.persistence.*;

@Entity(name = "roles")
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
    private long id;

    @Column
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
