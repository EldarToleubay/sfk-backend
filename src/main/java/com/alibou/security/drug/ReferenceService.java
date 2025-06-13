package com.alibou.security.drug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReferenceService {

    private final DrugRepository drugRepository;
    private final InnRepository innRepository;


    public List<Inn> setAll() {
        List<String> strings = drugRepository.findDistinctInn();
        List<Inn> inns = new ArrayList<>();
        for (String string : strings) {
            Inn inn = new Inn();
            inn.setName(string);
            inns.add(inn);
        }
        log.info("All inns in drugs: {}", inns.size());
        innRepository.saveAll(inns);
        return inns;

    }

    public List<Inn> getAll() {
        return innRepository.findAll();
    }
}
