package com.alibou.security.drug;

import com.alibou.security.drug.reference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/user-access/reference")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserReferenceController {

    private final UserReferenceService referenceService;

    @GetMapping("/inn")
    public List<Inn> getAccessibleInn(@RequestParam Long userId) {
        return referenceService.getAllInnByAccess(userId);
    }

    @GetMapping("/atc1")
    public List<Atc1> getAccessibleAtc1(@RequestParam Long userId) {
        System.out.println(userId);
        return referenceService.getAllAtc1ByAccess(userId);
    }

    @GetMapping("/atc2")
    public List<Atc2> getAccessibleAtc2(@RequestParam Long userId) {
        return referenceService.getAllAtc2ByAccess(userId);
    }

    @GetMapping("/atc3")
    public List<Atc3> getAccessibleAtc3(@RequestParam Long userId) {
        return referenceService.getAllAtc3ByAccess(userId);
    }

    @GetMapping("/dosage")
    public List<Dosage> getAccessibleDosage(@RequestParam Long userId) {
        return referenceService.getAllDosageByAccess(userId);
    }

    @GetMapping("/drug-form")
    public List<DrugForm> getAccessibleDrugForms(@RequestParam Long userId) {
        return referenceService.getAllDrugFormByAccess(userId);
    }

    @GetMapping("/manufacturing-company")
    public List<ManufacturingCompany> getAccessibleManufacturingCompanies(@RequestParam Long userId) {
        return referenceService.getAllManufacturingCompanyByAccess(userId);
    }

    @GetMapping("/pack-quantities")
    public List<PackQuantity> getAccessiblePackQuantities(@RequestParam Long userId) {
        return referenceService.getAllPackQuantityByAccess(userId);
    }

    @GetMapping("/segment")
    public List<Segment> getAccessibleSegments(@RequestParam Long userId) {
        return referenceService.getAllSegmentByAccess(userId);
    }

    @GetMapping("/trade-name")
    public List<TradeName> getAccessibleTradeNames(@RequestParam Long userId) {
        return referenceService.getAllTradeNameByAccess(userId);
    }

    @GetMapping("/person-with-trading-license")
    public List<PersonWithTradingLicense> getAccessiblePersonWithTradingLicense(@RequestParam Long userId) {
        return referenceService.getAllPersonWithTradingLicenseByAccess(userId);
    }

    @GetMapping("/person-interested-in-registration-georgia-stand")
    public List<PersonInterestedInRegistrationGeorgiaStand> getAccessiblePersonInterestedInRegistrationGeorgiaStand(@RequestParam Long userId) {
        return referenceService.getAllPersonInterestedInRegistrationGeorgiaStandByAccess(userId);
    }

    @GetMapping("/interested-party")
    public List<InterestedParty> getAccessibleInterestedParty(@RequestParam Long userId) {
        return referenceService.getAllInterestedPartyByAccess(userId);
    }

    @GetMapping("/rx-otc")
    public List<RxOtc> getAccessibleRxOtc(@RequestParam Long userId) {
        return referenceService.getAllRxOtcByAccess(userId);
    }

    @GetMapping("/mode-of-registration")
    public List<ModeOfRegistration> getAccessibleModeOfRegistration(@RequestParam Long userId) {
        return referenceService.getAllModeOfRegistrationByAccess(userId);
    }

    @GetMapping("/sku")
    public List<Sku> getAccessibleSku(@RequestParam Long userId) {
        return referenceService.getAllSkuByAccess(userId);
    }

    @GetMapping("/price-source")
    public List<PriceSource> getAccessiblePriceSource(@RequestParam Long userId) {
        return referenceService.getAllPriceSourceByAccess(userId);
    }

}
