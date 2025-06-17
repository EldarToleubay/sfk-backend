package com.alibou.security.drug;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final DrugService drugService;
    private final ReferenceService referenceService;

    @Transactional
    public void importDrugsAsync(InputStream inputStream) throws Exception {
        drugService.removeAllDrugs();
        drugService.importExcel(inputStream);
        referenceService.setAll();
    }
}
