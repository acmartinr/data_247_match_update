package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;

public class BusinessDetailed {

    private long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Company")
    private String company;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Address2")
    private String address2;

    @JsonProperty("City")
    private String city;

    @JsonProperty("State")
    private String state;

    @JsonProperty("Zip")
    private String zip;

    @JsonProperty("County_Code")
    private String countyCode;

    @JsonProperty("Telephone_Number")
    private String phone;

    @JsonProperty("Toll_Free_Number")
    private String tollFreePhone;

    @JsonProperty("Fax_Number")
    private String fax;

    @JsonProperty("Name_First")
    private String nameFirst;

    @JsonProperty("Name_Middle_Initial")
    private String middleName;

    @JsonProperty("Name_Last")
    private String nameLast;

    @JsonProperty("Ethnic_Code")
    private String ethnicCode;

    @JsonProperty("Ethnic_Group")
    private String ethnicGroup;

    @JsonProperty("Language_Code")
    private String languageCode;

    @JsonProperty("Religion_Code")
    private String religionCode;

    @JsonProperty("Web_Address")
    private String www;

    @JsonProperty("Total_Employees_Corp_Wide")
    private Integer totalEmployees;

    @JsonProperty("Employees_on_Site")
    private Integer employeesOnSite;

    @JsonProperty("Total_Revenue_Corp_Wide")
    private Integer totalRevenue;

    @JsonProperty("Revenue_at_Site")
    private Integer revenue;

    @JsonProperty("Year_Founded")
    private Integer yearFounded;

    @JsonProperty("MinorityOwned")
    private Integer minorityOwned;

    @JsonProperty("SmallBusiness")
    private Integer smallBusiness;

    @JsonProperty("LargeBusiness")
    private Integer largeBusiness;

    @JsonProperty("HomeBusiness")
    private Integer homeBusiness;

    @JsonProperty("ImportExport")
    private Integer importExport;

    @JsonProperty("PublicCompany")
    private Integer publicCompany;

    @JsonProperty("Headquarters_Branch")
    private Integer headquartersBranch;

    @JsonProperty("StockExchange")
    private Integer stockExchange;

    @JsonProperty("FranchiseFlag")
    private Integer franchiseFlag;

    @JsonProperty("IndividualFirm_Code")
    private Integer individualFirmCode;

    @JsonProperty("SIC6_1")
    private String SIC_CODE;

    @JsonProperty("YearAppearedinYP")
    private Integer appearedYear;

    @JsonProperty("FemaleOwnedorOperated")
    private Integer femaleOwnedorOperated;

    @JsonProperty("WhiteCollarCode")
    private Integer whiteCollarCode;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Title_Full")
    private String title;

    @JsonProperty("Phone_Contact")
    private String phoneContact;

    @JsonProperty("Credit_Score")
    private String creditScore;

    private Integer ZIP_CODE;

    @JsonProperty("Area_Code")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer AREA_CODE;

    private Integer phoneType;

    private Boolean dnc;

    private Integer ST_CODE;

    private String data;
    @JsonProperty( "SIC" )
    private Integer SIC;

    private String county;

    private String industry;

    // MOCK objects

    @JsonProperty("SCF")
    private String mock1;

    @JsonProperty("Zip4")
    private String mock2;

    @JsonProperty("DPBC")
    private String mock3;

    @JsonProperty("Zip9")
    private String mock4;

    @JsonProperty("Carrier_Route")
    private String mock5;

    @JsonProperty("Zip_CRRT")
    private String mock6;

    @JsonProperty("Zip4_Valid_Flag")
    private String mock7;

    @JsonProperty("Line_of_Travel")
    private String mock8;

    @JsonProperty("Latitude")
    private String mock9;

    @JsonProperty("Longitude")
    private String mock10;

    @JsonProperty("Lat_Long_Type")
    private String mock11;

    @JsonProperty("Mail_Score")
    private String mock12;

    @JsonProperty("CBSA_Code")
    private String mock13;

    @JsonProperty("MSA")
    private String mock14;

    @JsonProperty("CSA_Code")
    private String mock15;

    @JsonProperty("Metro_Micro_Code")
    private String mock16;

    @JsonProperty("Census_Tract")
    private String mock17;

    @JsonProperty("Census_Block_Group")
    private String mock18;

    @JsonProperty("Telephone_Number_Sequence")
    private String mock19;

    @JsonProperty("Name_Gender")
    private String mock20;

    @JsonProperty("Name_Prefix")
    private String mock21;

    @JsonProperty("Name_Suffix")
    private String mock22;

    @JsonProperty("Title_Code_1")
    private String mock23;

    @JsonProperty("Title_Code_2")
    private String mock24;

    @JsonProperty("Title_Code_3")
    private String mock25;

    @JsonProperty("Title_Code_4")
    private String mock26;

    @JsonProperty("Total_Employees_Code")
    private String mock27;

    @JsonProperty("Employees_on_Site_Code")
    private String mock28;

    @JsonProperty("Total_Revenue_Code_Corp_Wide")
    private String mock29;

    @JsonProperty("Revenue_at_Site_Code")
    private String mock30;

    @JsonProperty("NAICS_1")
    private String mock31;

    @JsonProperty("NAICS_2")
    private String mock32;

    @JsonProperty("NAICS_3")
    private String mock33;

    @JsonProperty("NAICS_4")
    private String mock34;

    @JsonProperty("SIC8_1")
    private String mock35;

    @JsonProperty("SIC8_2")
    private String mock36;

    @JsonProperty("SIC8_3")
    private String mock37;

    @JsonProperty("SIC8_4")
    private String mock38;

    @JsonProperty("SIC6_2")
    private String mock39;

    @JsonProperty("SIC6_3")
    private String mock40;

    @JsonProperty("SIC6_4")
    private String mock41;

    @JsonProperty("SIC6_5")
    private String mock42;

    @JsonProperty("StockTicker")
    private String mock43;

    @JsonProperty("FortuneRank")
    private String mock44;

    @JsonProperty("ProfessionalAmount")
    private String mock45;

    @JsonProperty("ProfessionalFlag")
    private String mock46;

    @JsonProperty("Ad_Size")
    private String mock47;

    @JsonProperty("CityPopulation")
    private String mock48;

    @JsonProperty("ParentForeignEntity")
    private String mock49;

    @JsonProperty("GovernmentType")
    private String mock50;

    @JsonProperty("Database_Site_ID")
    private String mock51;

    @JsonProperty("Database_individual_ID")
    private String mock52;

    @JsonProperty("Individual_Sequence")
    private String mock53;

    @JsonProperty("Phone_Present_Flag")
    private String mock54;

    @JsonProperty("Name_Present_Flag")
    private String mock55;

    @JsonProperty("Web_Present_Flag")
    private String mock56;

    @JsonProperty("Fax_Present_Flag")
    private String mock57;

    @JsonProperty("Residential_Business_Code")
    private String mock58;

    @JsonProperty("PO_BOX_Flag")
    private String mock59;

    @JsonProperty("COMPANY_Present_Flag")
    private String mock60;

    @JsonProperty("Email_Present_Flag")
    private String mock61;

    @JsonProperty("Ind_Src1")
    private String mock62;

    @JsonProperty("Ind_Src2")
    private String mock63;

    @JsonProperty("Ind_Src3")
    private String mock64;

    @JsonProperty("Ind_Src4")
    private String mock65;

    @JsonProperty("Ind_Src5")
    private String mock66;

    @JsonProperty("Ind_Src6")
    private String mock67;

    @JsonProperty("Ind_Src7")
    private String mock68;

    @JsonProperty("Ind_Src8")
    private String mock69;

    @JsonProperty("Ind_Src9")
    private String mock70;

    @JsonProperty("Ind_Src10")
    private String mock71;

    @JsonProperty("Site_Src1")
    private String mock72;

    @JsonProperty("Site_Src2")
    private String mock73;

    @JsonProperty("Site_Src3")
    private String mock74;

    @JsonProperty("Site_Src4")
    private String mock75;

    @JsonProperty("Site_Src5")
    private String mock76;

    @JsonProperty("Site_Src6")
    private String mock77;

    @JsonProperty("Site_Src7")
    private String mock78;

    @JsonProperty("Site_Src8")
    private String mock79;

    @JsonProperty("Site_Src9")
    private String mock80;

    @JsonProperty("Site_Src10")
    private String mock81;

    @JsonProperty("Other_Company_Name")
    private String mock82;

    @JsonProperty("Status")
    private String mock83;

    @JsonProperty("TransactionDate")
    private String mock84;

    @JsonProperty("TransactionType")
    private String mock85;

    @JsonProperty("Database_Individual_ID")
    private String mock86;

    @JsonProperty("Mobile_Phone")
    private String mock87;

    // end MOCK objects

    public BusinessDetailed() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTollFreePhone() {
        return tollFreePhone;
    }

    public void setTollFreePhone(String tollFreePhone) {
        this.tollFreePhone = tollFreePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getEthnicCode() {
        return ethnicCode;
    }

    public void setEthnicCode(String ethnicCode) {
        this.ethnicCode = ethnicCode;
    }

    public String getEthnicGroup() {
        return ethnicGroup;
    }

    public void setEthnicGroup(String ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public Integer getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(Integer totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public Integer getEmployeesOnSite() {
        return employeesOnSite;
    }

    public void setEmployeesOnSite(Integer employeesOnSite) {
        this.employeesOnSite = employeesOnSite;
    }

    public Integer getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Integer totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(Integer yearFounded) {
        this.yearFounded = yearFounded;
    }

    public Integer getMinorityOwned() {
        return minorityOwned;
    }

    public void setMinorityOwned(Integer minorityOwned) {
        this.minorityOwned = minorityOwned;
    }

    public Integer getSmallBusiness() {
        return smallBusiness;
    }

    public void setSmallBusiness(Integer smallBusiness) {
        this.smallBusiness = smallBusiness;
    }

    public Integer getLargeBusiness() {
        return largeBusiness;
    }

    public void setLargeBusiness(Integer largeBusiness) {
        this.largeBusiness = largeBusiness;
    }

    public Integer getHomeBusiness() {
        return homeBusiness;
    }

    public void setHomeBusiness(Integer homeBusiness) {
        this.homeBusiness = homeBusiness;
    }

    public Integer getImportExport() {
        return importExport;
    }

    public void setImportExport(Integer importExport) {
        this.importExport = importExport;
    }

    public Integer getPublicCompany() {
        return publicCompany;
    }

    public void setPublicCompany(Integer publicCompany) {
        this.publicCompany = publicCompany;
    }

    public Integer getHeadquartersBranch() {
        return headquartersBranch;
    }

    public void setHeadquartersBranch(Integer headquartersBranch) {
        this.headquartersBranch = headquartersBranch;
    }

    public Integer getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(Integer stockExchange) {
        this.stockExchange = stockExchange;
    }

    public Integer getFranchiseFlag() {
        return franchiseFlag;
    }

    public void setFranchiseFlag(Integer franchiseFlag) {
        this.franchiseFlag = franchiseFlag;
    }

    public Integer getIndividualFirmCode() {
        return individualFirmCode;
    }

    public void setIndividualFirmCode(Integer individualFirmCode) {
        this.individualFirmCode = individualFirmCode;
    }

    public String getSIC_CODE() {
        return SIC_CODE;
    }

    public void setSIC_CODE(String SIC_CODE) {
        this.SIC_CODE = SIC_CODE;
    }

    public Integer getAppearedYear() {
        return appearedYear;
    }

    public void setAppearedYear(Integer appearedYear) {
        this.appearedYear = appearedYear;
    }

    public Integer getFemaleOwnedorOperated() {
        return femaleOwnedorOperated;
    }

    public void setFemaleOwnedorOperated(Integer femaleOwnedorOperated) {
        this.femaleOwnedorOperated = femaleOwnedorOperated;
    }

    public Integer getWhiteCollarCode() {
        return whiteCollarCode;
    }

    public void setWhiteCollarCode(Integer whiteCollarCode) {
        this.whiteCollarCode = whiteCollarCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title != null ? title.toUpperCase() : null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public String getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }

    public Integer getZIP_CODE() {
        if (zip != null) {
            try { return Integer.parseInt( zip.split( "-" )[ 0 ] ); }
            catch (Exception e) { /*noop*/ }
        }

        return null;
    }

    public void setZIP_CODE(Integer ZIP_CODE) {
        this.ZIP_CODE = ZIP_CODE;
    }

    public Integer getAREA_CODE() {
        if (phone == null || phone.length() == 0) {
            return null;
        } else {
            try { return Integer.parseInt(phone.substring(0, 3)); }
            catch (Exception e) { return  null; }
        }
    }

    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public Integer getSIC() {
        if (SIC == null) {
            try { SIC = Integer.parseInt(getSIC_CODE()); }
            catch (Exception e) {/*e.printStackTrace();*/}
        }

        return SIC;
    }

    public void setSIC(Integer SIC) {
        this.SIC = SIC;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getST_CODE() {
        if (state != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++) {
                if (state.equals(Application.STATE_CODES[i])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setST_CODE(Integer ST_CODE) {
        this.ST_CODE = ST_CODE;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    // MOCK getters and setters

    public String getMock1() {
        return mock1;
    }

    public void setMock1(String mock1) {
        this.mock1 = mock1;
    }

    public String getMock2() {
        return mock2;
    }

    public void setMock2(String mock2) {
        this.mock2 = mock2;
    }

    public String getMock3() {
        return mock3;
    }

    public void setMock3(String mock3) {
        this.mock3 = mock3;
    }

    public String getMock4() {
        return mock4;
    }

    public void setMock4(String mock4) {
        this.mock4 = mock4;
    }

    public String getMock5() {
        return mock5;
    }

    public void setMock5(String mock5) {
        this.mock5 = mock5;
    }

    public String getMock6() {
        return mock6;
    }

    public void setMock6(String mock6) {
        this.mock6 = mock6;
    }

    public String getMock7() {
        return mock7;
    }

    public void setMock7(String mock7) {
        this.mock7 = mock7;
    }

    public String getMock8() {
        return mock8;
    }

    public void setMock8(String mock8) {
        this.mock8 = mock8;
    }

    public String getMock9() {
        return mock9;
    }

    public void setMock9(String mock9) {
        this.mock9 = mock9;
    }

    public String getMock10() {
        return mock10;
    }

    public void setMock10(String mock10) {
        this.mock10 = mock10;
    }

    public String getMock11() {
        return mock11;
    }

    public void setMock11(String mock11) {
        this.mock11 = mock11;
    }

    public String getMock12() {
        return mock12;
    }

    public void setMock12(String mock12) {
        this.mock12 = mock12;
    }

    public String getMock13() {
        return mock13;
    }

    public void setMock13(String mock13) {
        this.mock13 = mock13;
    }

    public String getMock14() {
        return mock14;
    }

    public void setMock14(String mock14) {
        this.mock14 = mock14;
    }

    public String getMock15() {
        return mock15;
    }

    public void setMock15(String mock15) {
        this.mock15 = mock15;
    }

    public String getMock16() {
        return mock16;
    }

    public void setMock16(String mock16) {
        this.mock16 = mock16;
    }

    public String getMock17() {
        return mock17;
    }

    public void setMock17(String mock17) {
        this.mock17 = mock17;
    }

    public String getMock18() {
        return mock18;
    }

    public void setMock18(String mock18) {
        this.mock18 = mock18;
    }

    public String getMock19() {
        return mock19;
    }

    public void setMock19(String mock19) {
        this.mock19 = mock19;
    }

    public String getMock20() {
        return mock20;
    }

    public void setMock20(String mock20) {
        this.mock20 = mock20;
    }

    public String getMock21() {
        return mock21;
    }

    public void setMock21(String mock21) {
        this.mock21 = mock21;
    }

    public String getMock22() {
        return mock22;
    }

    public void setMock22(String mock22) {
        this.mock22 = mock22;
    }

    public String getMock23() {
        return mock23;
    }

    public void setMock23(String mock23) {
        this.mock23 = mock23;
    }

    public String getMock24() {
        return mock24;
    }

    public void setMock24(String mock24) {
        this.mock24 = mock24;
    }

    public String getMock25() {
        return mock25;
    }

    public void setMock25(String mock25) {
        this.mock25 = mock25;
    }

    public String getMock26() {
        return mock26;
    }

    public void setMock26(String mock26) {
        this.mock26 = mock26;
    }

    public String getMock27() {
        return mock27;
    }

    public void setMock27(String mock27) {
        this.mock27 = mock27;
    }

    public String getMock28() {
        return mock28;
    }

    public void setMock28(String mock28) {
        this.mock28 = mock28;
    }

    public String getMock29() {
        return mock29;
    }

    public void setMock29(String mock29) {
        this.mock29 = mock29;
    }

    public String getMock30() {
        return mock30;
    }

    public void setMock30(String mock30) {
        this.mock30 = mock30;
    }

    public String getMock31() {
        return mock31;
    }

    public void setMock31(String mock31) {
        this.mock31 = mock31;
    }

    public String getMock32() {
        return mock32;
    }

    public void setMock32(String mock32) {
        this.mock32 = mock32;
    }

    public String getMock33() {
        return mock33;
    }

    public void setMock33(String mock33) {
        this.mock33 = mock33;
    }

    public String getMock34() {
        return mock34;
    }

    public void setMock34(String mock34) {
        this.mock34 = mock34;
    }

    public String getMock35() {
        return mock35;
    }

    public void setMock35(String mock35) {
        this.mock35 = mock35;
    }

    public String getMock36() {
        return mock36;
    }

    public void setMock36(String mock36) {
        this.mock36 = mock36;
    }

    public String getMock37() {
        return mock37;
    }

    public void setMock37(String mock37) {
        this.mock37 = mock37;
    }

    public String getMock38() {
        return mock38;
    }

    public void setMock38(String mock38) {
        this.mock38 = mock38;
    }

    public String getMock39() {
        return mock39;
    }

    public void setMock39(String mock39) {
        this.mock39 = mock39;
    }

    public String getMock40() {
        return mock40;
    }

    public void setMock40(String mock40) {
        this.mock40 = mock40;
    }

    public String getMock41() {
        return mock41;
    }

    public void setMock41(String mock41) {
        this.mock41 = mock41;
    }

    public String getMock42() {
        return mock42;
    }

    public void setMock42(String mock42) {
        this.mock42 = mock42;
    }

    public String getMock43() {
        return mock43;
    }

    public void setMock43(String mock43) {
        this.mock43 = mock43;
    }

    public String getMock44() {
        return mock44;
    }

    public void setMock44(String mock44) {
        this.mock44 = mock44;
    }

    public String getMock45() {
        return mock45;
    }

    public void setMock45(String mock45) {
        this.mock45 = mock45;
    }

    public String getMock46() {
        return mock46;
    }

    public void setMock46(String mock46) {
        this.mock46 = mock46;
    }

    public String getMock47() {
        return mock47;
    }

    public void setMock47(String mock47) {
        this.mock47 = mock47;
    }

    public String getMock48() {
        return mock48;
    }

    public void setMock48(String mock48) {
        this.mock48 = mock48;
    }

    public String getMock49() {
        return mock49;
    }

    public void setMock49(String mock49) {
        this.mock49 = mock49;
    }

    public String getMock50() {
        return mock50;
    }

    public void setMock50(String mock50) {
        this.mock50 = mock50;
    }

    public String getMock51() {
        return mock51;
    }

    public void setMock51(String mock51) {
        this.mock51 = mock51;
    }

    public String getMock52() {
        return mock52;
    }

    public void setMock52(String mock52) {
        this.mock52 = mock52;
    }

    public String getMock53() {
        return mock53;
    }

    public void setMock53(String mock53) {
        this.mock53 = mock53;
    }

    public String getMock54() {
        return mock54;
    }

    public void setMock54(String mock54) {
        this.mock54 = mock54;
    }

    public String getMock55() {
        return mock55;
    }

    public void setMock55(String mock55) {
        this.mock55 = mock55;
    }

    public String getMock56() {
        return mock56;
    }

    public void setMock56(String mock56) {
        this.mock56 = mock56;
    }

    public String getMock57() {
        return mock57;
    }

    public void setMock57(String mock57) {
        this.mock57 = mock57;
    }

    public String getMock58() {
        return mock58;
    }

    public void setMock58(String mock58) {
        this.mock58 = mock58;
    }

    public String getMock59() {
        return mock59;
    }

    public void setMock59(String mock59) {
        this.mock59 = mock59;
    }

    public String getMock60() {
        return mock60;
    }

    public void setMock60(String mock60) {
        this.mock60 = mock60;
    }

    public String getMock61() {
        return mock61;
    }

    public void setMock61(String mock61) {
        this.mock61 = mock61;
    }

    public String getMock62() {
        return mock62;
    }

    public void setMock62(String mock62) {
        this.mock62 = mock62;
    }

    public String getMock63() {
        return mock63;
    }

    public void setMock63(String mock63) {
        this.mock63 = mock63;
    }

    public String getMock64() {
        return mock64;
    }

    public void setMock64(String mock64) {
        this.mock64 = mock64;
    }

    public String getMock65() {
        return mock65;
    }

    public void setMock65(String mock65) {
        this.mock65 = mock65;
    }

    public String getMock66() {
        return mock66;
    }

    public void setMock66(String mock66) {
        this.mock66 = mock66;
    }

    public String getMock67() {
        return mock67;
    }

    public void setMock67(String mock67) {
        this.mock67 = mock67;
    }

    public String getMock68() {
        return mock68;
    }

    public void setMock68(String mock68) {
        this.mock68 = mock68;
    }

    public String getMock69() {
        return mock69;
    }

    public void setMock69(String mock69) {
        this.mock69 = mock69;
    }

    public String getMock70() {
        return mock70;
    }

    public void setMock70(String mock70) {
        this.mock70 = mock70;
    }

    public String getMock71() {
        return mock71;
    }

    public void setMock71(String mock71) {
        this.mock71 = mock71;
    }

    public String getMock72() {
        return mock72;
    }

    public void setMock72(String mock72) {
        this.mock72 = mock72;
    }

    public String getMock73() {
        return mock73;
    }

    public void setMock73(String mock73) {
        this.mock73 = mock73;
    }

    public String getMock74() {
        return mock74;
    }

    public void setMock74(String mock74) {
        this.mock74 = mock74;
    }

    public String getMock75() {
        return mock75;
    }

    public void setMock75(String mock75) {
        this.mock75 = mock75;
    }

    public String getMock76() {
        return mock76;
    }

    public void setMock76(String mock76) {
        this.mock76 = mock76;
    }

    public String getMock77() {
        return mock77;
    }

    public void setMock77(String mock77) {
        this.mock77 = mock77;
    }

    public String getMock78() {
        return mock78;
    }

    public void setMock78(String mock78) {
        this.mock78 = mock78;
    }

    public String getMock79() {
        return mock79;
    }

    public void setMock79(String mock79) {
        this.mock79 = mock79;
    }

    public String getMock80() {
        return mock80;
    }

    public void setMock80(String mock80) {
        this.mock80 = mock80;
    }

    public String getMock81() {
        return mock81;
    }

    public void setMock81(String mock81) {
        this.mock81 = mock81;
    }

    public String getMock82() {
        return mock82;
    }

    public void setMock82(String mock82) {
        this.mock82 = mock82;
    }

    public String getMock83() {
        return mock83;
    }

    public void setMock83(String mock83) {
        this.mock83 = mock83;
    }

    public String getMock84() {
        return mock84;
    }

    public void setMock84(String mock84) {
        this.mock84 = mock84;
    }

    public String getMock85() {
        return mock85;
    }

    public void setMock85(String mock85) {
        this.mock85 = mock85;
    }

    public String getMock86() {
        return mock86;
    }

    public void setMock86(String mock86) {
        this.mock86 = mock86;
    }

    public String getMock87() {
        return mock87;
    }

    public void setMock87(String mock87) {
        this.mock87 = mock87;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    // end MOCK getters and setters
}
