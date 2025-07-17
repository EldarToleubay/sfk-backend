package com.alibou.security.drug;


import com.alibou.security.drug.reference.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/inn")
    public List<Inn> inn() {
        return referenceService.getAllInn();
    }


    @GetMapping("/atc1")
    public List<Atc1> atc1s() {
        return referenceService.getAllAtc1();
    }

    @GetMapping("/atc2")
    public List<Atc2> atc2s() {
        return referenceService.getAllAtc2();
    }

    @GetMapping("/atc3")
    public List<Atc3> atc3s() {
        return referenceService.getAllAtc3();
    }


    @GetMapping("/dosage")
    public List<Dosage> dosages() {
        return referenceService.getAllDosage();
    }


    @GetMapping("/drug-form")
    public List<DrugForm> drugForms() {
        return referenceService.getAllDrugForm();
    }


    @GetMapping("/manufacturing-company")
    public List<ManufacturingCompany> manufacturingCompanies() {
        return referenceService.getAllManufacturingCompany();
    }


    @GetMapping("/pack-quantities")
    public List<PackQuantity> packQuantities() {
        return referenceService.getAllPackQuantity();
    }


    @GetMapping("/segment")
    public List<Segment> segment() {
        return referenceService.getAllSegment();
    }

    @GetMapping("/trade-name")
    public List<TradeName> tradeName() {
        return referenceService.getAllTradeName();
    }


    @Operation(tags = "Новые справочники")
    @GetMapping("/person-with-trading-license")
    public List<PersonWithTradingLicense> personWithTradingLicense() {
        return referenceService.getAllPersonWithTradingLicense();
    }

    @Operation(tags = "Новые справочники")
    @GetMapping("/person-interested-in-registration-georgia-stand")
    public List<PersonInterestedInRegistrationGeorgiaStand> personInterestedInRegistrationGeorgiaStand() {
        return referenceService.getAllPersonInterestedInRegistrationGeorgiaStand();
    }

    @Operation(tags = "Новые справочники")
    @GetMapping("/interested-party")
    public List<InterestedParty> interestedParty() {
        return referenceService.getAllInterestedParty();
    }
    @Operation(tags = "Новые справочники")
    @GetMapping("/rx-otc")
    public List<RxOtc> rxOtc() {
        return referenceService.getAllRxOtc();
    }
    @Operation(tags = "Новые справочники")
    @GetMapping("/mode-of-registration")
    public List<ModeOfRegistration> modeOfRegistration() {
        return referenceService.getAllModeOfRegistration();
    }

    @Operation(tags = "Новые справочники")
    @GetMapping("/sku")
    public List<Sku> sku() {
        return referenceService.getAllSku();
    }

    @Operation(tags = "Новые справочники")
    @GetMapping("/price-source")
    public List<PriceSource> priceSource() {
        return referenceService.getAllPriceSource();
    }







}
