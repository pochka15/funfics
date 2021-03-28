package com.pochka15.funfics.configs;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Component which runs the mass indexation for the current search session on startup.
 */
@Component
public class StartupMassIndexation implements ApplicationListener<ApplicationReadyEvent> {
    private final EntityManagerFactory entityManagerFactory;

    @Value("${enableStartupMassIndexation}")
    private boolean enableIndexation;

    public StartupMassIndexation(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (enableIndexation) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            SearchSession searchSession = Search.session(entityManager);
            searchSession.massIndexer()
                    .start()
                    .exceptionally(throwable -> {
                        System.err.println("Mass indexing failed!");
                        return null;
                    });
        }
    }
}
