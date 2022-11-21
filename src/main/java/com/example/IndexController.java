package com.example;

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
    public String index3() {
        System.out.println("entityManager=" + entityManager);
        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery()
                .forRevisionsOfEntity(Product.class, false, true);

        query.add(AuditEntity.revisionProperty("username").eq("admin"));
        //This return a list of array triplets of changes concerning the specified revision.
        // The array triplet contains the entity, entity revision information and at last the revision type.
        List<Object[]> revs = (List<Object[]>) query.getResultList();
        for (Object[] rev : revs) {
            Product product = (Product) rev[0];
            UserRevEntity userRevEntity = (UserRevEntity) rev[1];
            RevisionType type = (RevisionType) rev[2];
            System.out.println(product);
            System.out.println(userRevEntity);
            System.out.println(userRevEntity.getId());
            System.out.println(userRevEntity.getRevisionDate());
            System.out.println(userRevEntity.getUsername());
            System.out.println(type);
        }

        System.out.println();

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
