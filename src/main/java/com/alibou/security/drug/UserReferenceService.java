package com.alibou.security.drug;

import com.alibou.security.drug.reference.*;
import com.alibou.security.drug.reference.repository.*;
import com.alibou.security.useraccess.UserAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReferenceService {

    private final UserAccessRepository userAccessRepository;
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
    private final PersonWithTradingLicenseRepository personWithTradingLicenseRepository;
    private final PersonInterestedInRegistrationGeorgiaStandRepository personInterestedInRegistrationGeorgiaStandRepository;
    private final InterestedPartyRepository interestedPartyRepository;
    private final RxOtcRepository rxOtcRepository;
    private final ModeOfRegistrationRepository modeOfRegistrationRepository;
    private final SkuRepository skuRepository;
    private final PriceSourceRepository priceSourceRepository;


    public List<Inn> getAllInnByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "INN");
        return innRepository.findByIdIn(ids);
    }


    public List<Atc1> getAllAtc1ByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "ATC1");
        return atc1Repository.findByIdIn(ids);
    }


    public List<Atc2> getAllAtc2ByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "ATC2");
        return atc2Repository.findByIdIn(ids);
    }


    public List<Atc3> getAllAtc3ByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "ATC3");
        return atc3Repository.findByIdIn(ids);
    }


    public List<Dosage> getAllDosageByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "DOSAGE");
        return dosageRepository.findByIdIn(ids);
    }


    public List<DrugForm> getAllDrugFormByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "DRUG_FORM");
        return drugFormRepository.findByIdIn(ids);
    }


    public List<ManufacturingCompany> getAllManufacturingCompanyByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "MANUFACTURING_COMPANY");
        return manufacturingCompanyRepository.findByIdIn(ids);
    }


    public List<PackQuantity> getAllPackQuantityByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "PACK_QUANTITY");
        return packQuantityRepository.findByIdIn(ids);
    }


    public List<Segment> getAllSegmentByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "SEGMENT");
        return segmentRepository.findByIdIn(ids);
    }


    public List<TradeName> getAllTradeNameByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "TRADE_NAME");
        return tradeNameRepository.findByIdIn(ids);
    }

    public List<PersonWithTradingLicense> getAllPersonWithTradingLicenseByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "PERSON_WITH_TRADING_LICENSE");
        return personWithTradingLicenseRepository.findByIdIn(ids);
    }

    public List<PersonInterestedInRegistrationGeorgiaStand> getAllPersonInterestedInRegistrationGeorgiaStandByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "PERSON_INTERESTED_IN_REGISTRATION_GEORGIA_STAND");
        return personInterestedInRegistrationGeorgiaStandRepository.findByIdIn(ids);
    }

    public List<InterestedParty> getAllInterestedPartyByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "INTERESTED_PARTY");
        return interestedPartyRepository.findByIdIn(ids);
    }

    public List<RxOtc> getAllRxOtcByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "RX_OTC");
        return rxOtcRepository.findByIdIn(ids);
    }

    public List<ModeOfRegistration> getAllModeOfRegistrationByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "MODE_OF_REGISTRATION");
        return modeOfRegistrationRepository.findByIdIn(ids);
    }

    public List<Sku> getAllSkuByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "SKU");
        return skuRepository.findByIdIn(ids);
    }

    public List<PriceSource> getAllPriceSourceByAccess(Long userId) {
        List<Long> ids = userAccessRepository.findRefIdsByUserIdAndRefType(userId, "PRICE_SOURCE");
        return priceSourceRepository.findByIdIn(ids);
    }

}

