package com.example;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class IndexController {

    @Autowired
    ProductRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index2")
    public String index2() {
        return "index2";
    }

    @GetMapping("/index3")
    public String index3(Model model) {

//In this case we want the entity revision information object, which is the second object of the array.
        return "index";
    }

    @GetMapping("/header")
    public String header() {
        return "fragments/header";
    }

    @GetMapping("/footer")
    public String footer() {
        return "fragments/footer";
    }

    @GetMapping("/sidebar")
    public String sidebar() {
        return "fragments/sidebar";
    }
}
