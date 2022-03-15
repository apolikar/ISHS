package irl.lyit.DublinSmartHouseSearch.dao;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
