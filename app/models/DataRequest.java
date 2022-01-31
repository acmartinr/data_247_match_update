package models;

import java.util.LinkedList;
import java.util.List;

public class DataRequest {

    private List< Integer > areaCodes = new LinkedList();
    private String stateAreacode = "";
    private List< Integer > omittedAreaCodes = new LinkedList();
    private List< String > cities = new LinkedList();
    private List< String > omittedCities = new LinkedList();
    private List< String > states = new LinkedList();
    private List< String > omittedStates = new LinkedList();
    private List< String > zipCodes = new LinkedList();
    private List< Integer > omittedZipCodes = new LinkedList();
    private List< String > counties = new LinkedList();
    private List<String> consumerCarriers;
    private List< String > ages = new LinkedList();
    private List< String > genders = new LinkedList();
    private List< String > educations = new LinkedList();
    private List< String > netWorth = new LinkedList();
    private List< String > creditRating = new LinkedList();
    private List< String > creditLines = new LinkedList();
    private List< String > creditRanges = new LinkedList();
    private List< String > ethnicityGroups = new LinkedList();
    private List< String > ethnicityLanguages = new LinkedList();
    private List< String > ethnicityReligions = new LinkedList();
    private List< String > householdIncome = new LinkedList();
    private List< String > householdSize = new LinkedList();
    private List< String > residenceType = new LinkedList();
    private List< String > residenceOwnership = new LinkedList();
    private List< String > residenceVeteran = new LinkedList();
    private List< String > residenceLength = new LinkedList();
    private List< String > residenceMarital = new LinkedList();
    private List< String > residenceChildren = new LinkedList();
    private List< String > interests = new LinkedList();
    private List< Integer > selectedLists = new LinkedList();
    private List< Integer > uploadedLists = new LinkedList();
    private List< String  > sales = new LinkedList();
    private List< String  > employeeCount = new LinkedList();
    private List< String > titles = new LinkedList();
    private List< String > industries = new LinkedList();
    private List< String > sources = new LinkedList();
    private List< Integer > phoneTypes = new LinkedList();
    private int dataType;
    private String keyword;
    private List< String > columns = new LinkedList();
    private String tableName;
    private boolean unique;

    private String countNote;
    private boolean confirmed;

    public boolean isBusinessMatch() {
        return businessMatch;
    }

    public void setBusinessMatch(boolean businessMatch) {
        this.businessMatch = businessMatch;
    }


    private boolean detailedBusinessMatch;
    private boolean healthBuyersMatch;

    public String getStateAreacode() {
        return stateAreacode;
    }

    public void setStateAreacode(String stateAreacode) {
        this.stateAreacode = stateAreacode;
    }
    public boolean isHealthBuyersMatch() {
        return healthBuyersMatch;
    }

    public void setHealthBuyersMatch(boolean healthBuyersMatch) {
        this.healthBuyersMatch = healthBuyersMatch;
    }



    public boolean isHealthInsuranceMatch() {
        return healthInsuranceMatch;
    }

    public void setHealthInsuranceMatch(boolean healthInsuranceMatch) {
        this.healthInsuranceMatch = healthInsuranceMatch;
    }


    public boolean isConsumerMatch2018() {
        return consumerMatch2018;
    }

    public void setConsumerMatch2018(boolean consumerMatch2018) {
        this.consumerMatch2018 = consumerMatch2018;
    }

    public boolean isRemoveCorps() {
        return removeCorps;
    }

    public void setRemoveCorps(boolean removeCorps) {
        this.removeCorps = removeCorps;
    }

    private boolean removeCorps;
    private boolean consumerMatch2018;
    private boolean healthInsuranceMatch;

    private boolean businessMatch;
    private boolean businessMatch2;

    private boolean everydataMatch;
    private boolean whoisMatch;

    public boolean isConsumerMatch2019() {
        return consumerMatch2019;
    }

    public void setConsumerMatch2019(boolean consumerMatch2019) {
        this.consumerMatch2019 = consumerMatch2019;
    }

    private boolean consumerMatch2019;

    public boolean isBusinessMatch2() {
        return businessMatch2;
    }

    public void setBusinessMatch2(boolean businessMatch2) {
        this.businessMatch2 = businessMatch2;
    }

    public boolean isWhoisMatch() {
        return whoisMatch;
    }

    public void setWhoisMatch(boolean whoisMatch) {
        this.whoisMatch = whoisMatch;
    }

    public boolean isEverydataMatch() {
        return everydataMatch;
    }

    public void setEverydataMatch(boolean everydataMatch) {
        this.everydataMatch = everydataMatch;
    }

    public boolean isInstagramMatch() {
        return instagramMatch;
    }

    public void setInstagramMatch(boolean instagramMatch) {
        this.instagramMatch = instagramMatch;
    }

    private boolean instagramMatch;
    private boolean uniqueEmails;
    private boolean filterDNC;
    private boolean filterEmptyPhone;
    private boolean filterEmail;
    private boolean filterBusinessEmail;
    private boolean uniqueBusinessName;
    private boolean filterWebsite;

    public String getCountNote() {
        return countNote;
    }

    public void setCountNote(String countNote) {
        this.countNote = countNote;
    }

    private List<String> creditScores = new LinkedList();
    private List<String> companyTypes = new LinkedList();

    private List<Integer> sics;
    private Integer fromSic;
    private Integer toSic;
    private List<String> keywords = new LinkedList();
    private List<List<String>> keywordsColumns = new LinkedList();
    private List<String> sections = new LinkedList();
    private List<Long> dates = new LinkedList();
    private List<String> timeZones = new LinkedList();
    private List<Long> datime = new LinkedList();
    private List<String> maritalStatuses = new LinkedList();
    private List<String> ethnicCodes = new LinkedList();
    private List<String> languageCodes = new LinkedList();
    private List<String> ethnicGroups = new LinkedList();
    private List<String> religionCodes = new LinkedList();
    private List<String> hispanicCountryCodes = new LinkedList();
    private List<String> properties = new LinkedList();
    private List<String> propertyType = new LinkedList();
    private List<String> ownerType = new LinkedList();
    private List<String> lengthOfResidence = new LinkedList();
    private List<String> numberOfPersonInLivingUnit = new LinkedList();
    private List<String> numberOfChildren = new LinkedList();
    private List<String> inferredHouseHoldRank = new LinkedList();
    private List<String> numberOfAdults = new LinkedList();
    private List<String> generationsInHouseHold = new LinkedList();
    private List<String> sewer = new LinkedList();
    private List<String> water = new LinkedList();
    private List<String> occupationGroups = new LinkedList();
    private List<String> personEducations = new LinkedList();
    private List<String> personOccupations = new LinkedList();
    private List<String> businessOwners = new LinkedList();
    private List<String> estimatedIncome = new LinkedList();
    private List<String> netWorthes = new LinkedList();
    private List<String> propertyOwned = new LinkedList();
    private List<String> homePurchasePrices = new LinkedList();
    private List<Long> homePurchasedDates = new LinkedList();
    private List<Long> homeYearBuilt = new LinkedList();
    private List<String> estimatedCurrentHomeValueCodes = new LinkedList();
    private List<String> mortgageAmountInThousands = new LinkedList();
    private List<String> mortgageLenderNames = new LinkedList();
    private List<String> mortgageRate = new LinkedList();
    private List<String> mortgageRateTypes = new LinkedList();
    private List<String> mortgageLoanTypes = new LinkedList();
    private List<String> transactionTypes = new LinkedList();
    private List<String> refinanceAmountInThousands = new LinkedList();
    private List<String> refinanceLeaderNames = new LinkedList();
    private List<Long> deedDatesOfRefinance = new LinkedList();
    private List<String> refinanceRateTypes = new LinkedList();
    private List<String> refinanceLoanTypes = new LinkedList();
    private List<String> censusMedianHouseHoldIncome = new LinkedList();
    private List<String> censusMedianHomeValue = new LinkedList();
    private List<String> craIncomeClassificationCodes = new LinkedList();
    private List<Long> purchaseMortgageDates = new LinkedList();
    private List<String> mostRecentLenderCodes = new LinkedList();
    private List<String> purchaseLenderNames = new LinkedList();
    private List<String> mostRecentMortgageInterestRates = new LinkedList();
    private List<String> loanToValues = new LinkedList();
    private List<String> rating = new LinkedList();
    private List<String> activeLines = new LinkedList();
    private List<String> range = new LinkedList();
    private List<Integer> agesRange = new LinkedList();
    private List<String> dobs = new LinkedList();
    private List<String> categories = new LinkedList();
    private List<Long> yearsRange = new LinkedList();
    private List<String> models = new LinkedList();
    private List<String> makes = new LinkedList();
    private List<Integer> numberOfSources = new LinkedList();
    private List<String> dpv = new LinkedList();
    private List<String> childrenAgeGender = new LinkedList();

    private List<String> carriersBrands = new LinkedList();
    private List<String> domainSources = new LinkedList();

    private List<String> carriers = new LinkedList();
    private List<String> carriersPhones = new LinkedList();
    private List<String> facebookGenders = new LinkedList();

    private List<String> facebookJobs = new LinkedList();
    private List<String> facebookStatus = new LinkedList();

    private List<String> facebookHLName = new LinkedList();

    private boolean blackListMatch;

    private boolean consumerMatch;
    private boolean craigslistMatch;
    private boolean directoryMatch;
    private boolean optinMatch;
    private boolean businessDetailedMatch;
    private boolean callLeadsMatch;


    private boolean facebookMatch;

    private boolean localNumbers;


    private boolean fbHispanicLName;

    public DataRequest() {}

    public boolean isLocalNumbers() {
        return localNumbers;
    }

    public void setLocalNumbers(boolean localNumbers) {
        this.localNumbers = localNumbers;
    }

    public boolean isFbHispanicLName() {
        return fbHispanicLName;
    }

    public void setFbHispanicLName(boolean fbHispanicLName) {
        this.fbHispanicLName = fbHispanicLName;
    }

    public List<Integer> getAreaCodes() { return areaCodes; }
    public void setAreaCodes(List<Integer> areaCodes) { this.areaCodes = areaCodes; }

    public List<String> getCities() { return cities; }
    public void setCities(List<String> cities) { this.cities = cities; }

    public List<String> getStates() { return states; }
    public void setStates(List<String> states) { this.states = states; }

    public List<String> getZipCodes() { return zipCodes; }
    public void setZipCodes(List<String> zipCodes) { this.zipCodes = zipCodes; }

    public List<String> getCounties() { return counties; }
    public void setCounties(List<String> counties) { this.counties = counties; }


    public boolean isFacebookMatch() {
        return facebookMatch;
    }

    public void setFacebookMatch(boolean facebookMatch) {
        this.facebookMatch = facebookMatch;
    }

    public List<String> getFacebookHLName() {
        return facebookHLName;
    }

    public void setFacebookHLName(List<String> facebookHLName) {
        this.facebookHLName = facebookHLName;
    }
    public boolean isConfirmed() {
        return confirmed;
    }
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<String> getAges() { return ages; }
    public void setAges(List<String> ages) { this.ages = ages; }

    public List<String> getGenders() { return genders; }
    public void setGenders(List<String> genders) { this.genders = genders; }

    public List<String> getFacebookJobs() {
        return facebookJobs;
    }


    public List<String> getConsumerCarriers() {
        return consumerCarriers;
    }

    public void setConsumerCarriers(List<String> consumerCarriers) {
        this.consumerCarriers = consumerCarriers;
    }

    public void setFacebookJobs(List<String> facebookJobs) {
        this.facebookJobs = facebookJobs;
    }
    public List<String> getEducations() { return educations; }
    public void setEducations(List<String> educations) { this.educations = educations; }

    public List<String> getCarriersBrands() {
        return carriersBrands;
    }
    public List<Long> getDatime() {
        return datime;
    }

    public void setgetDatime(List<Long> datime) {
        this.datime = datime;
    }

    public List<String> getFacebookGenders() {
        return facebookGenders;
    }

    public void setFacebookGenders(List<String> facebookGenders) {
        this.facebookGenders = facebookGenders;
    }

    public List<String> getFacebookStatus() {
        return facebookStatus;
    }

    public void setFacebookStatus(List<String> facebookStatus) {
        this.facebookStatus = facebookStatus;
    }
    public void setCarriersBrands(List<String> carriersBrands) {
        this.carriersBrands = carriersBrands;
    }

    public List<String> getDomainSources() {
        return domainSources;
    }

    public void setDomainSources(List<String> domainSources) {
        this.domainSources = domainSources;
    }
    public List<String> getNetWorth() { return netWorth; }
    public void setNetWorth(List<String> netWorth) { this.netWorth = netWorth; }

    public List<String> getCreditRating() { return creditRating; }
    public void setCreditRating(List<String> creditRating) { this.creditRating = creditRating; }

    public List<String> getCreditLines() { return creditLines; }
    public void setCreditLines(List<String> creditLines) { this.creditLines = creditLines; }

    public List<String> getCreditRanges() { return creditRanges; }
    public void setCreditRanges(List<String> creditRanges) { this.creditRanges = creditRanges; }

    public List<String> getEthnicityGroups() { return ethnicityGroups; }
    public void setEthnicityGroups(List<String> ethnicityGroups) { this.ethnicityGroups = ethnicityGroups; }

    public List<String> getEthnicityLanguages() { return ethnicityLanguages; }
    public void setEthnicityLanguages(List<String> ethnicityLanguages) { this.ethnicityLanguages = ethnicityLanguages; }

    public List<String> getEthnicityReligions() { return ethnicityReligions; }
    public void setEthnicityReligions(List<String> ethnicityReligions) { this.ethnicityReligions = ethnicityReligions; }

    public List<String> getHouseholdIncome() { return householdIncome; }
    public void setHouseholdIncome(List<String> householdIncome) { this.householdIncome = householdIncome; }

    public List<String> getHouseholdSize() { return householdSize; }
    public void setHouseholdSize( List<String> householdSize ) { this.householdSize = householdSize; }

    public List<String> getResidenceType() { return residenceType; }
    public void setResidenceType(List<String> residenceType) { this.residenceType = residenceType; }

    public List<String> getResidenceOwnership() { return residenceOwnership; }
    public void setResidenceOwnership(List<String> residenceOwnership) { this.residenceOwnership = residenceOwnership; }

    public List<String> getResidenceVeteran() { return residenceVeteran; }
    public void setResidenceVeteran(List<String> residenceVeteran) { this.residenceVeteran = residenceVeteran; }

    public List<String> getResidenceLength() { return residenceLength; }
    public void setResidenceLength(List<String> residenceLength) { this.residenceLength = residenceLength; }

    public List<String> getResidenceMarital() { return residenceMarital; }
    public void setResidenceMarital(List<String> residenceMarital) { this.residenceMarital = residenceMarital; }

    public List<String> getResidenceChildren() { return residenceChildren; }
    public void setResidenceChildren(List<String> residenceChildren) { this.residenceChildren = residenceChildren; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }

    public int getDataType() { return dataType; }
    public void setDataType( int dataType ) { this.dataType = dataType; }

    public List< String > getSales() { return sales; }
    public void setSales( List< String > sales ) { this.sales = sales; }

    public List< String > getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount( List< String > employeeCount ) { this.employeeCount = employeeCount; }

    public List< String > getTitles() { return titles; }
    public void setTitles( List< String > titles ) { this.titles = titles; }

    public List< String > getIndustries() { return industries; }
    public void setIndustries( List< String > industries ) { this.industries = industries; }

    public String getKeyword() { return keyword; }
    public void setKeyword( String keyword ) { this.keyword = keyword; }

    public List< String > getColumns() { return columns; }
    public void setColumns( List< String > columns ) { this.columns = columns; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName ) { this.tableName = tableName; }

    public List< Integer > getUploadedLists() { return uploadedLists; }
    public void setUploadedLists( List< Integer > uploadedLists ) { this.uploadedLists = uploadedLists; }

    public List<Integer> getPhoneTypes() { return phoneTypes; }
    public void setPhoneTypes(List<Integer> phoneTypes) { this.phoneTypes = phoneTypes; }

    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }

    public boolean isUnique() { return unique; }
    public void setUnique(boolean unique) { this.unique = unique; }

    public List<Integer> getSics() { return sics; }
    public void setSics(List<Integer> sics) { this.sics = sics; }

    public Integer getFromSic() { return fromSic; }
    public void setFromSic(Integer fromSic) { this.fromSic = fromSic; }

    public Integer getToSic() { return toSic; }
    public void setToSic(Integer toSic) { this.toSic = toSic; }

    public List<Integer> getSelectedLists() { return selectedLists; }
    public void setSelectedLists(List<Integer> selectedLists) { this.selectedLists = selectedLists; }

    public List<String> getKeywords() {
        if (getKeyword() != null) {
            List<String> result = new LinkedList();
            result.addAll(keywords);
            result.add(getKeyword());

            return result;
        } else {
            return keywords;
        }
    }

    public void setKeywords(List<String> keywords) { this.keywords = keywords; }

    public List<List<String>> getKeywordsColumns() {
        if (getColumns() != null) {
            List<List<String>> result = new LinkedList();
            result.addAll(keywordsColumns);
            result.add(getColumns());

            return result;
        } else {
            return keywordsColumns;
        }
    }

    public void setKeywordsColumns(List<List<String>> keywordsColumns) { this.keywordsColumns = keywordsColumns; }

    public List<String> getSections() { return sections; }
    public void setSections(List<String> sections) { this.sections = sections; }

    public List<Long> getDates() { return dates; }
    public void setDates(List<Long> dates) { this.dates = dates; }

    public boolean isFilterDNC() { return filterDNC; }
    public void setFilterDNC(boolean filterDNC) { this.filterDNC = filterDNC; }

    public List<String> getTimeZones() { return timeZones; }
    public void setTimeZones(List<String> timeZones) { this.timeZones = timeZones; }

    public List<String> getMaritalStatuses() {
        return maritalStatuses;
    }

    public void setMaritalStatuses(List<String> maritalStatuses) {
        this.maritalStatuses = maritalStatuses;
    }

    public List<String> getEthnicCodes() {
        return ethnicCodes;
    }

    public void setEthnicCodes(List<String> ethnicCodes) {
        this.ethnicCodes = ethnicCodes;
    }

    public List<String> getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(List<String> languageCodes) {
        this.languageCodes = languageCodes;
    }

    public List<String> getEthnicGroups() {
        return ethnicGroups;
    }

    public void setEthnicGroups(List<String> ethnicGroups) {
        this.ethnicGroups = ethnicGroups;
    }

    public List<String> getReligionCodes() {
        return religionCodes;
    }

    public void setReligionCodes(List<String> religionCodes) {
        this.religionCodes = religionCodes;
    }

    public List<String> getHispanicCountryCodes() {
        return hispanicCountryCodes;
    }

    public void setHispanicCountryCodes(List<String> hispanicCountryCodes) {
        this.hispanicCountryCodes = hispanicCountryCodes;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public List<String> getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(List<String> propertyType) {
        this.propertyType = propertyType;
    }

    public List<String> getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(List<String> ownerType) {
        this.ownerType = ownerType;
    }

    public List<String> getLengthOfResidence() {
        return lengthOfResidence;
    }

    public void setLengthOfResidence(List<String> lengthOfResidence) {
        this.lengthOfResidence = lengthOfResidence;
    }

    public List<String> getNumberOfPersonInLivingUnit() {
        return numberOfPersonInLivingUnit;
    }

    public void setNumberOfPersonInLivingUnit(List<String> numberOfPersonInLivingUnit) {
        this.numberOfPersonInLivingUnit = numberOfPersonInLivingUnit;
    }

    public List<String> getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(List<String> numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public List<String> getInferredHouseHoldRank() {
        return inferredHouseHoldRank;
    }

    public void setInferredHouseHoldRank(List<String> inferredHouseHoldRank) {
        this.inferredHouseHoldRank = inferredHouseHoldRank;
    }

    public List<String> getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(List<String> numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public List<String> getGenerationsInHouseHold() {
        return generationsInHouseHold;
    }

    public void setGenerationsInHouseHold(List<String> generationsInHouseHold) {
        this.generationsInHouseHold = generationsInHouseHold;
    }

    public List<String> getSewer() {
        return sewer;
    }

    public void setSewer(List<String> sewer) {
        this.sewer = sewer;
    }

    public List<String> getWater() {
        return water;
    }

    public void setWater(List<String> water) {
        this.water = water;
    }

    public List<String> getOccupationGroups() {
        return occupationGroups;
    }

    public void setOccupationGroups(List<String> occupationGroups) {
        this.occupationGroups = occupationGroups;
    }

    public List<String> getPersonEducations() {
        return personEducations;
    }

    public void setPersonEducations(List<String> personEducations) {
        this.personEducations = personEducations;
    }

    public List<String> getPersonOccupations() {
        return personOccupations;
    }

    public void setPersonOccupations(List<String> personOccupations) {
        this.personOccupations = personOccupations;
    }

    public List<String> getBusinessOwners() {
        return businessOwners;
    }

    public void setBusinessOwners(List<String> businessOwners) {
        this.businessOwners = businessOwners;
    }

    public List<String> getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(List<String> estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public List<String> getNetWorthes() {
        return netWorthes;
    }

    public void setNetWorthes(List<String> netWorthes) {
        this.netWorthes = netWorthes;
    }

    public List<String> getPropertyOwned() {
        return propertyOwned;
    }

    public void setPropertyOwned(List<String> propertyOwned) {
        this.propertyOwned = propertyOwned;
    }

    public List<String> getHomePurchasePrices() {
        return homePurchasePrices;
    }

    public void setHomePurchasePrices(List<String> homePurchasePrices) {
        this.homePurchasePrices = homePurchasePrices;
    }

    public List<Long> getHomePurchasedDates() {
        return homePurchasedDates;
    }

    public void setHomePurchasedDates(List<Long> homePurchasedDates) {
        this.homePurchasedDates = homePurchasedDates;
    }

    public List<Long> getHomeYearBuilt() {
        return homeYearBuilt;
    }

    public void setHomeYearBuilt(List<Long> homeYearBuilt) {
        this.homeYearBuilt = homeYearBuilt;
    }

    public List<String> getEstimatedCurrentHomeValueCodes() {
        return estimatedCurrentHomeValueCodes;
    }

    public void setEstimatedCurrentHomeValueCodes(List<String> estimatedCurrentHomeValueCodes) {
        this.estimatedCurrentHomeValueCodes = estimatedCurrentHomeValueCodes;
    }

    public List<String> getMortgageAmountInThousands() {
        return mortgageAmountInThousands;
    }

    public void setMortgageAmountInThousands(List<String> mortgageAmountInThousands) {
        this.mortgageAmountInThousands = mortgageAmountInThousands;
    }

    public List<String> getMortgageLenderNames() {
        return mortgageLenderNames;
    }

    public void setMortgageLenderNames(List<String> mortgageLenderNames) {
        this.mortgageLenderNames = mortgageLenderNames;
    }

    public List<String> getMortgageRate() {
        return mortgageRate;
    }

    public void setMortgageRate(List<String> mortgageRate) {
        this.mortgageRate = mortgageRate;
    }

    public List<String> getMortgageRateTypes() {
        return mortgageRateTypes;
    }

    public void setMortgageRateTypes(List<String> mortgageRateTypes) {
        this.mortgageRateTypes = mortgageRateTypes;
    }

    public List<String> getMortgageLoanTypes() {
        return mortgageLoanTypes;
    }

    public void setMortgageLoanTypes(List<String> mortgageLoanTypes) {
        this.mortgageLoanTypes = mortgageLoanTypes;
    }

    public List<String> getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(List<String> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public List<String> getRefinanceAmountInThousands() {
        return refinanceAmountInThousands;
    }

    public void setRefinanceAmountInThousands(List<String> refinanceAmountInThousands) {
        this.refinanceAmountInThousands = refinanceAmountInThousands;
    }

    public List<String> getRefinanceLeaderNames() {
        return refinanceLeaderNames;
    }

    public void setRefinanceLeaderNames(List<String> refinanceLeaderNames) {
        this.refinanceLeaderNames = refinanceLeaderNames;
    }

    public List<Long> getDeedDatesOfRefinance() {
        return deedDatesOfRefinance;
    }

    public void setDeedDatesOfRefinance(List<Long> deedDatesOfRefinance) {
        this.deedDatesOfRefinance = deedDatesOfRefinance;
    }

    public List<String> getRefinanceRateTypes() {
        return refinanceRateTypes;
    }

    public void setRefinanceRateTypes(List<String> refinanceRateTypes) {
        this.refinanceRateTypes = refinanceRateTypes;
    }

    public List<String> getRefinanceLoanTypes() {
        return refinanceLoanTypes;
    }

    public void setRefinanceLoanTypes(List<String> refinanceLoanTypes) {
        this.refinanceLoanTypes = refinanceLoanTypes;
    }

    public List<String> getCensusMedianHouseHoldIncome() {
        return censusMedianHouseHoldIncome;
    }

    public void setCensusMedianHouseHoldIncome(List<String> censusMedianHouseHoldIncome) {
        this.censusMedianHouseHoldIncome = censusMedianHouseHoldIncome;
    }

    public List<String> getCensusMedianHomeValue() {
        return censusMedianHomeValue;
    }

    public void setCensusMedianHomeValue(List<String> censusMedianHomeValue) {
        this.censusMedianHomeValue = censusMedianHomeValue;
    }

    public List<String> getCraIncomeClassificationCodes() {
        return craIncomeClassificationCodes;
    }

    public void setCraIncomeClassificationCodes(List<String> craIncomeClassificationCodes) {
        this.craIncomeClassificationCodes = craIncomeClassificationCodes;
    }

    public List<Long> getPurchaseMortgageDates() {
        return purchaseMortgageDates;
    }

    public void setPurchaseMortgageDates(List<Long> purchaseMortgageDates) {
        this.purchaseMortgageDates = purchaseMortgageDates;
    }

    public List<String> getMostRecentLenderCodes() {
        return mostRecentLenderCodes;
    }

    public void setMostRecentLenderCodes(List<String> mostRecentLenderCodes) {
        this.mostRecentLenderCodes = mostRecentLenderCodes;
    }

    public List<String> getPurchaseLenderNames() {
        return purchaseLenderNames;
    }

    public void setPurchaseLenderNames(List<String> purchaseLenderNames) {
        this.purchaseLenderNames = purchaseLenderNames;
    }

    public List<String> getMostRecentMortgageInterestRates() {
        return mostRecentMortgageInterestRates;
    }

    public void setMostRecentMortgageInterestRates(List<String> mostRecentMortgageInterestRates) {
        this.mostRecentMortgageInterestRates = mostRecentMortgageInterestRates;
    }

    public List<String> getLoanToValues() {
        return loanToValues;
    }

    public void setLoanToValues(List<String> loanToValues) {
        this.loanToValues = loanToValues;
    }

    public List<String> getRating() {
        return rating;
    }

    public void setRating(List<String> rating) {
        this.rating = rating;
    }

    public List<String> getActiveLines() {
        return activeLines;
    }

    public void setActiveLines(List<String> activeLines) {
        this.activeLines = activeLines;
    }

    public List<String> getRange() {
        return range;
    }

    public void setRange(List<String> range) {
        this.range = range;
    }

    public List<Integer> getAgesRange() {
        return agesRange;
    }

    public void setAgesRange(List<Integer> agesRange) {
        this.agesRange = agesRange;
    }

    public boolean isFilterEmptyPhone() {
        return filterEmptyPhone;
    }

    public void setFilterEmptyPhone(boolean filterEmptyPhone) {
        this.filterEmptyPhone = filterEmptyPhone;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public boolean isUniqueBusinessName() {
        return uniqueBusinessName;
    }

    public void setUniqueBusinessName(boolean uniqueBusinessName) {
        this.uniqueBusinessName = uniqueBusinessName;
    }

    public boolean isFilterEmail() {
        return filterEmail;
    }

    public void setFilterEmail(boolean filterEmail) {
        this.filterEmail = filterEmail;
    }

    public List<Integer> getOmittedZipCodes() {
        return omittedZipCodes;
    }

    public void setOmittedZipCodes(List<Integer> omittedZipCodes) {
        this.omittedZipCodes = omittedZipCodes;
    }

    public List<String> getOmittedCities() {
        return omittedCities;
    }

    public void setOmittedCities(List<String> omittedCities) {
        this.omittedCities = omittedCities;
    }

    public List<Long> getYearsRange() {
        return yearsRange;
    }

    public void setYearsRange(List<Long> yearsRange) {
        this.yearsRange = yearsRange;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    public List<String> getMakes() {
        return makes;
    }

    public void setMakes(List<String> makes) {
        this.makes = makes;
    }

    public List<Integer> getOmittedAreaCodes() {
        return omittedAreaCodes;
    }

    public void setOmittedAreaCodes(List<Integer> omittedAreaCodes) {
        this.omittedAreaCodes = omittedAreaCodes;
    }

    public List<String> getOmittedStates() {
        return omittedStates;
    }

    public void setOmittedStates(List<String> omittedStates) {
        this.omittedStates = omittedStates;
    }

    public List<Integer> getNumberOfSources() {
        return numberOfSources;
    }

    public void setNumberOfSources(List<Integer> numberOfSources) {
        this.numberOfSources = numberOfSources;
    }

    public List<String> getDpv() {
        return dpv;
    }

    public void setDpv(List<String> dpv) {
        this.dpv = dpv;
    }

    public List<String> getChildrenAgeGender() {
        return childrenAgeGender;
    }

    public void setChildrenAgeGender(List<String> childrenAgeGender) {
        this.childrenAgeGender = childrenAgeGender;
    }

    public List<String> getCarriers() {
        return carriers;
    }

    public void setCarriers(List<String> carriers) {
        this.carriers = carriers;
    }

    public List<String> getCarriersPhones() {
        return carriersPhones;
    }

    public void setCarriersPhones(List<String> carriersPhones) {
        this.carriersPhones = carriersPhones;
    }

    public boolean isBlackListMatch() {
        return blackListMatch;
    }

    public void setBlackListMatch(boolean blackListMatch) {
        this.blackListMatch = blackListMatch;
    }

    public List<String> getDobs() {
        return dobs;
    }

    public void setDobs(List<String> dobs) {
        this.dobs = dobs;
    }

    public boolean isConsumerMatch() {
        return consumerMatch;
    }

    public void setConsumerMatch(boolean consumerMatch) {
        this.consumerMatch = consumerMatch;
    }

    public boolean isCraigslistMatch() {
        return craigslistMatch;
    }

    public void setCraigslistMatch(boolean craigslistMatch) {
        this.craigslistMatch = craigslistMatch;
    }

    public List<String> getCreditScores() {
        return creditScores;
    }

    public void setCreditScores(List<String> creditScores) {
        this.creditScores = creditScores;
    }

    public List<String> getCompanyTypes() {
        return companyTypes;
    }

    public void setCompanyTypes(List<String> companyTypes) {
        this.companyTypes = companyTypes;
    }

    public boolean isDirectoryMatch() {
        return directoryMatch;
    }

    public void setDirectoryMatch(boolean directoryMatch) {
        this.directoryMatch = directoryMatch;
    }

    public boolean isFilterBusinessEmail() {
        return filterBusinessEmail;
    }

    public void setFilterBusinessEmail(boolean filterBusinessEmail) {
        this.filterBusinessEmail = filterBusinessEmail;
    }

    public boolean isOptinMatch() {
        return optinMatch;
    }

    public void setOptinMatch(boolean optinMatch) {
        this.optinMatch = optinMatch;
    }

    public boolean isBusinessDetailedMatch() {
        return businessDetailedMatch;
    }

    public void setBusinessDetailedMatch(boolean businessDetailedMatch) {
        this.businessDetailedMatch = businessDetailedMatch;
    }

    public boolean isUniqueEmails() {
        return uniqueEmails;
    }

    public void setUniqueEmails(boolean uniqueEmails) {
        this.uniqueEmails = uniqueEmails;
    }

    public boolean isFilterWebsite() {
        return filterWebsite;
    }

    public void setFilterWebsite(boolean filterWebsite) {
        this.filterWebsite = filterWebsite;
    }

    public boolean isCallLeadsMatch() {
        return callLeadsMatch;
    }

    public void setCallLeadsMatch(boolean callLeadsMatch) {
        this.callLeadsMatch = callLeadsMatch;
    }

    public static class Entity {

        private String key;
        private Object value;
        private String relationship;

        public Entity() {}

        public Entity( String key, Object value, String relationship ) {
            this.key = key;
            this.value = value;
            this.relationship = relationship;
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }

        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }

        public String getRelationship() { return relationship; }
        public void setRelationship( String relationship ) { this.relationship = relationship; }
    }
}
