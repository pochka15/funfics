package com.pochka15.funfics.dto.form;

import lombok.Data;

import java.util.List;

@Data
public class DeleteFunficsForm {
    private List<Long> funficIds;
}
