package com.alibou.security.drug;

import com.alibou.security.drug.reference.*;
import com.alibou.security.drug.reference.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReferenceService {

    private final DrugRepository drugRepository;
    private final InnRepository innRepository;
    private final Atc1Repository atc1Repository;
    private final Atc2Repository atc2Repository;
    private final Atc3Repository atc3Repository;
    private final DosageRepository dosageRepository;
    private final DrugFormRepository drugFormRepository;
    private final ManufacturingCompanyRepository manufacturingCompanyRepository;
    private final PackQuantityRepository packQuantityRepository;
    private final SegmentRepository segmentRepository;
    private final TradeNameRepository tradeNameRepository;


    @Transactional
    public void setAll() {
        saveIfNotExists(
                drugRepository.findDistinctInn(),
                innRepository.findAllNames(),
                innRepository::saveAll,
                name -> {
                    Inn inn = new Inn();
                    inn.setName(name);
                    return inn;
                },
                "INN"
        );

        saveIfNotExists(
                drugRepository.findDistinctSegment(),
                segmentRepository.findAllNames(),
                segmentRepository::saveAll,
                name -> {
                    Segment segment = new Segment();
                    segment.setName(name);
                    return segment;
                },
                "Segment"
        );

        saveIfNotExists(
                drugRepository.findDistinctTradeName(),
                tradeNameRepository.findAllNames(),
                tradeNameRepository::saveAll,
                name -> {
                    TradeName tradeName = new TradeName();
                    tradeName.setName(name);
                    return tradeName;
                },
                "Trade Name"
        );

        saveIfNotExists(
                drugRepository.findDistinctManufacturingCompany(),
                manufacturingCompanyRepository.findAllNames(),
                manufacturingCompanyRepository::saveAll,
                name -> {
                    ManufacturingCompany mc = new ManufacturingCompany();
                    mc.setName(name);
                    return mc;
                },
                "Manufacturing Company"
        );

        saveIfNotExists(
                drugRepository.findDistinctDrugForm(),
                drugFormRepository.findAllNames(),
                drugFormRepository::saveAll,
                name -> {
                    DrugForm form = new DrugForm();
                    form.setName(name);
                    return form;
                },
                "Drug Form"
        );

        saveIfNotExists(
                drugRepository.findDistinctDosage(),
                dosageRepository.findAllNames(),
                dosageRepository::saveAll,
                name -> {
                    Dosage dosage = new Dosage();
                    dosage.setName(name);
                    return dosage;
                },
                "Dosage"
        );

        saveIfNotExists(
                drugRepository.findDistinctPackQuantity(),
                packQuantityRepository.findAllNames(),
                packQuantityRepository::saveAll,
                name -> {
                    PackQuantity pq = new PackQuantity();
                    pq.setName(name);
                    return pq;
                },
                "Pack Quantity"
        );

        saveIfNotExists(
                drugRepository.findDistinctAtc1(),
                atc1Repository.findAllNames(),
                atc1Repository::saveAll,
                name -> {
                    Atc1 atc = new Atc1();
                    atc.setName(name);
                    return atc;
                },
                "ATC1"
        );

        saveIfNotExists(
                drugRepository.findDistinctAtc2(),
                atc2Repository.findAllNames(),
                atc2Repository::saveAll,
                name -> {
                    Atc2 atc = new Atc2();
                    atc.setName(name);
                    return atc;
                },
                "ATC2"
        );

        saveIfNotExists(
                drugRepository.findDistinctAtc3(),
                atc3Repository.findAllNames(),
                atc3Repository::saveAll,
                name -> {
                    Atc3 atc = new Atc3();
                    atc.setName(name);
                    return atc;
                },
                "ATC3"
        );

    }

    private <T> void saveIfNotExists(
            List<String> newValues,
            List<String> existingValues,
            Consumer<List<T>> saver,
            Function<String, T> entityFactory,
            String label
    ) {
        Set<String> existingSet = new HashSet<>(existingValues);

        List<T> toSave = newValues.stream()
                .filter(val -> val != null && !existingSet.contains(val))
                .map(entityFactory)
                .toList();

        if (!toSave.isEmpty()) {
            saver.accept(toSave);
            log.info("Saved {} new entries for {}", toSave.size(), label);
        } else {
            log.info("No new entries for {}", label);
        }
    }


    public List<Inn> getAllInn() {
        return innRepository.findAll();
    }

    public List<Atc1> getAllAtc1() {
        return atc1Repository.findAll();
    }

    public List<Atc2> getAllAtc2() {
        return atc2Repository.findAll();
    }

    public List<Atc3> getAllAtc3() {
        return atc3Repository.findAll();
    }

    public List<Dosage> getAllDosage() {
        return dosageRepository.findAll();
    }

    public List<DrugForm> getAllDrugForm() {
        return drugFormRepository.findAll();
    }

    public List<ManufacturingCompany> getAllManufacturingCompany() {
        return manufacturingCompanyRepository.findAll();
    }

    public List<PackQuantity> getAllPackQuantity() {
        return packQuantityRepository.findAll();
    }

    public List<Segment> getAllSegment() {
        return segmentRepository.findAll();
    }

    public List<TradeName> getAllTradeName() {
        return tradeNameRepository.findAll();
    }
}
