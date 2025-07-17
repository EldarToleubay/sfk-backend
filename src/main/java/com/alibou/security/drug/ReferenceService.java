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

    // New Repositories
    private final PersonWithTradingLicenseRepository personWithTradingLicenseRepository;
    private final PersonInterestedInRegistrationGeorgiaStandRepository personInterestedInRegistrationGeorgiaStandRepository;
    private final InterestedPartyRepository interestedPartyRepository;
    private final RxOtcRepository rxOtcRepository;
    private final ModeOfRegistrationRepository modeOfRegistrationRepository;
    private final SkuRepository skuRepository;
    private final PriceSourceRepository priceSourceRepository;

    @Transactional
    public void setAll() {
        saveIfNotExists(drugRepository.findDistinctInn(), innRepository.findAllNames(), innRepository::saveAll, name -> {
            Inn entity = new Inn();
            entity.setName(name);
            return entity;
        }, "INN");

        saveIfNotExists(drugRepository.findDistinctSegment(), segmentRepository.findAllNames(), segmentRepository::saveAll, name -> {
            Segment entity = new Segment();
            entity.setName(name);
            return entity;
        }, "Segment");

        saveIfNotExists(drugRepository.findDistinctTradeName(), tradeNameRepository.findAllNames(), tradeNameRepository::saveAll, name -> {
            TradeName entity = new TradeName();
            entity.setName(name);
            return entity;
        }, "Trade Name");

        saveIfNotExists(drugRepository.findDistinctManufacturingCompany(), manufacturingCompanyRepository.findAllNames(), manufacturingCompanyRepository::saveAll, name -> {
            ManufacturingCompany entity = new ManufacturingCompany();
            entity.setName(name);
            return entity;
        }, "Manufacturing Company");

        saveIfNotExists(drugRepository.findDistinctDrugForm(), drugFormRepository.findAllNames(), drugFormRepository::saveAll, name -> {
            DrugForm entity = new DrugForm();
            entity.setName(name);
            return entity;
        }, "Drug Form");

        saveIfNotExists(drugRepository.findDistinctDosage(), dosageRepository.findAllNames(), dosageRepository::saveAll, name -> {
            Dosage entity = new Dosage();
            entity.setName(name);
            return entity;
        }, "Dosage");

        saveIfNotExists(drugRepository.findDistinctPackQuantity(), packQuantityRepository.findAllNames(), packQuantityRepository::saveAll, name -> {
            PackQuantity entity = new PackQuantity();
            entity.setName(name);
            return entity;
        }, "Pack Quantity");

        saveIfNotExists(drugRepository.findDistinctAtc1(), atc1Repository.findAllNames(), atc1Repository::saveAll, name -> {
            Atc1 entity = new Atc1();
            entity.setName(name);
            return entity;
        }, "ATC1");

        saveIfNotExists(drugRepository.findDistinctAtc2(), atc2Repository.findAllNames(), atc2Repository::saveAll, name -> {
            Atc2 entity = new Atc2();
            entity.setName(name);
            return entity;
        }, "ATC2");

        saveIfNotExists(drugRepository.findDistinctAtc3(), atc3Repository.findAllNames(), atc3Repository::saveAll, name -> {
            Atc3 entity = new Atc3();
            entity.setName(name);
            return entity;
        }, "ATC3");

        // ✅ New: Add save logic for new references
        saveIfNotExists(drugRepository.findDistinctPersonWithTradingLicense(), personWithTradingLicenseRepository.findAllNames(), personWithTradingLicenseRepository::saveAll, name -> {
            PersonWithTradingLicense entity = new PersonWithTradingLicense();
            entity.setName(name);
            return entity;
        }, "Person With Trading License");

        saveIfNotExists(drugRepository.findDistinctPersonInterestedInRegistrationGeorgiaStand(), personInterestedInRegistrationGeorgiaStandRepository.findAllNames(), personInterestedInRegistrationGeorgiaStandRepository::saveAll, name -> {
            PersonInterestedInRegistrationGeorgiaStand entity = new PersonInterestedInRegistrationGeorgiaStand();
            entity.setName(name);
            return entity;
        }, "Person Interested in Registration Georgia Stand");

        saveIfNotExists(drugRepository.findDistinctInterestedParty(), interestedPartyRepository.findAllNames(), interestedPartyRepository::saveAll, name -> {
            InterestedParty entity = new InterestedParty();
            entity.setName(name);
            return entity;
        }, "Interested Party");

        saveIfNotExists(drugRepository.findDistinctRxOtc(), rxOtcRepository.findAllNames(), rxOtcRepository::saveAll, name -> {
            RxOtc entity = new RxOtc();
            entity.setName(name);
            return entity;
        }, "Rx/OTC");

        saveIfNotExists(drugRepository.findDistinctModeOfRegistration(), modeOfRegistrationRepository.findAllNames(), modeOfRegistrationRepository::saveAll, name -> {
            ModeOfRegistration entity = new ModeOfRegistration();
            entity.setName(name);
            return entity;
        }, "Mode of Registration");

        saveIfNotExists(drugRepository.findDistinctSku(), skuRepository.findAllNames(), skuRepository::saveAll, name -> {
            Sku entity = new Sku();
            entity.setName(name);
            return entity;
        }, "SKU");

        saveIfNotExists(drugRepository.findDistinctPriceSource(), priceSourceRepository.findAllNames(), priceSourceRepository::saveAll, name -> {
            PriceSource entity = new PriceSource();
            entity.setName(name);
            return entity;
        }, "Price Source");
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

    // Existing getters...

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

    // ✅ New getters

    public List<PersonWithTradingLicense> getAllPersonWithTradingLicense() {
        return personWithTradingLicenseRepository.findAll();
    }

    public List<PersonInterestedInRegistrationGeorgiaStand> getAllPersonInterestedInRegistrationGeorgiaStand() {
        return personInterestedInRegistrationGeorgiaStandRepository.findAll();
    }

    public List<InterestedParty> getAllInterestedParty() {
        return interestedPartyRepository.findAll();
    }

    public List<RxOtc> getAllRxOtc() {
        return rxOtcRepository.findAll();
    }

    public List<ModeOfRegistration> getAllModeOfRegistration() {
        return modeOfRegistrationRepository.findAll();
    }

    public List<Sku> getAllSku() {
        return skuRepository.findAll();
    }

    public List<PriceSource> getAllPriceSource() {
        return priceSourceRepository.findAll();
    }
}
