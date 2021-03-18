package com.pochka15.funfics.service.funfics;

import com.pochka15.funfics.dto.funfic.FunficDto;

import java.util.List;

public interface FunficsSearchService {
    List<FunficDto> searchByName(String name);
}
