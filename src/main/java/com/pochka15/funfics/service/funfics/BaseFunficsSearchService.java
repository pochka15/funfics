package com.pochka15.funfics.service.funfics;

import com.pochka15.funfics.converter.funfic.FunficToDtoConverter;
import com.pochka15.funfics.domain.funfic.Funfic;
import com.pochka15.funfics.dto.funfic.FunficDto;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseFunficsSearchService implements FunficsSearchService {
    private final FunficToDtoConverter funficToDtoConverter;

    @PersistenceContext
    private final EntityManager entityManager;

    public BaseFunficsSearchService(FunficToDtoConverter funficToDtoConverter, EntityManager entityManager) {
        this.funficToDtoConverter = funficToDtoConverter;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<FunficDto> searchByName(String matchingName) {
        return foundFunfics(matchingName).stream()
                .map(funficToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    private List<Funfic> foundFunfics(String matchingName) {
        return Search.session(entityManager)
                .search(Funfic.class)
                .where(f -> f.match()
                        .field("name")
                        .matching(matchingName)
                        .fuzzy())
                .fetch(20)
                .hits();
    }
}
