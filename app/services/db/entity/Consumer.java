package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;
import scala.util.parsing.json.JSON;

import java.io.IOException;
import java.util.Calendar;

public class Consumer {

    private long id;

    @JsonProperty( "ADDR" )
    private String ADDR;

    @JsonProperty( "APT" )
    private String APT;

    @JsonProperty( "BUY_AUTO_PARTS" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean BUY_AUTO_PARTS;

    @JsonProperty( "BUY_GARDENING" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean BUY_GARDENING;

    @JsonProperty( "BUY_HOME_GARD" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean BUY_HOME_GARD;

    @JsonProperty( "CC_USER" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean CC_USER;

    @JsonProperty( "CITY" )
    private String CITY;

    @JsonProperty( "CREDIT_LINES" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer CREDIT_LINES;

    @JsonProperty( "CREDIT_RANGE_NEW" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer CREDIT_RANGE_NEW;

    @JsonProperty( "CREDIT_RATING" )
    private String CREDIT_RATING;

    @JsonProperty( "DOB_DAY" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer DOB_DAY;

    @JsonProperty( "DOB_MON" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer DOB_MON;

    @JsonProperty( "DOB_YR" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer DOB_YR;

    @JsonProperty( "DONR_CHARITABLE" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_CHARITABLE;

    @JsonProperty( "DONR_MAIL_ORD" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_MAIL_ORD;

    @JsonProperty( "DONR_POL" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_POL;

    @JsonProperty( "DONR_POL_CONS" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_POL_CONS;

    @JsonProperty( "DONR_POL_LIB" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_POL_LIB;

    @JsonProperty( "DONR_RELIG" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_RELIG;

    @JsonProperty( "DONOR_VETS" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean DONR_VETS;

    @JsonProperty( "ETHNIC_AFR_AMER_PROF" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean ETHNIC_AFR_AMER_PROF;

    @JsonProperty( "PROP_DWEL_TYPE" )
    private String DWELL_TYP;

    @JsonProperty( "EDUC" )
    private String EDUC;

    @JsonProperty( "ETHNIC_ASSIM" )
    private String ETHNIC_ASSIM;

    @JsonProperty( "ETHNIC_CODE" )
    private String ETHNIC_CODE;

    @JsonProperty( "ETHNIC_CONF" )
    private String ETHNIC_CONF;

    @JsonProperty( "ETHNIC_GRP" )
    private String ETHNIC_GRP;

    @JsonProperty( "ETHNIC_HISP_CNTRY" )
    private String ETHNIC_HISP_CNTRY;

    @JsonProperty( "ETHNIC_LANG" )
    private String ETHNIC_LANG;

    @JsonProperty( "ETHNIC_RELIG" )
    private String ETHNIC_RELIG;

    @JsonProperty( "FIPS_CTY" )
    private String FIPS_CTY;

    @JsonProperty( "FN" )
    private String FN;

    @JsonProperty( "GENDER" )
    private String GENDER;

    @JsonProperty( "GENS" )
    private String GENS;

    @JsonProperty( "HH_INCOME" )
    private String HH_INCOME;

    @JsonProperty( "HH_MARITAL_STAT" )
    private String HH_MARITAL_STAT;

    @JsonProperty( "HH_SIZE" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer HH_SIZE;

    @JsonProperty( "HOME_OWNER" )
    private String HOME_OWNR;

    @JsonProperty( "HOME_OWNR_SRC" )
    private String HOME_OWNR_SRC;

    @JsonProperty( "INF_HH_RANK" )
    private String INF_HH_RANK;

    @JsonProperty( "INT_HOB_SWEEPS" )
    private String INT_HOB_SWEEPS;

    @JsonProperty( "INT_TRAV_CASINO" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean INT_TRAV_CASINO;

    @JsonProperty( "INT_TRAV_GENL" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean INT_TRAV_GENL;

    @JsonProperty( "LIFE_DIY" )
    private String LIFE_DIY;

    @JsonProperty( "LIFE_HOME" )
    private String LIFE_HOME;

    @JsonProperty( "LATITUDE" )
    @JsonDeserialize( using = ConsumerFloatDeserializer.class )
    private Float LATITUDE;

    @JsonProperty( "LN" )
    private String LN;

    @JsonProperty( "LONGITUDE" )
    @JsonDeserialize( using = ConsumerFloatDeserializer.class )
    private Float LONGITUDE;

    @JsonProperty( "LOR" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer LOR;

    @JsonProperty( "MI" )
    private String MI;

    @JsonProperty( "NAME_PRE" )
    private String NAME_PRE;

    @JsonProperty( "NET_WORTH" )
    private String NET_WORTH;

    @JsonProperty( "NUM_ADULTS" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer NUM_ADULTS;

    @JsonProperty( "NUM_KIDS" )
    @JsonDeserialize( using = IntegerDeserializer.class )
    private Integer NUM_KIDS;

    @JsonProperty( "PETS" )
    private String PETS;

    @JsonProperty( "PETS_CAT" )
    private String PETS_CAT;

    @JsonProperty( "PETS_DOG" )
    private String PETS_DOG;

    @JsonProperty( "PHONE" )
    private String PHONE;

    private Integer AREA_CODE;

    @JsonProperty( "PRES_KIDS" )
    private String PRES_KIDS;

    @JsonProperty( "PROP_TYPE" )
    private String PROP_TYPE;

    @JsonProperty( "PROP_AC" )
    private String PROP_AC;

    @JsonProperty( "PROP_FUEL" )
    private String PROP_FUEL;

    @JsonProperty( "PROP_POOL" )
    private String PROP_POOL;

    @JsonProperty( "ST" )
    private String ST;

    @JsonProperty( "TIME_ZN" )
    private String TIME_ZN;

    @JsonProperty( "VEN_IN_HH" )
    @JsonDeserialize( using = BooleanDeserializer.class )
    private Boolean VET_IN_HH;

    @JsonProperty( "Z4" )
    private String Z4;

    @JsonProperty( "ZIP" )
    private String ZIP;

    @JsonProperty( "ZIP4" )
    private String ZIP4;

    @JsonProperty( "PROP_SEWER" )
    private String PROP_SEWER;

    @JsonProperty( "PROP_WATER" )
    private String PROP_WATER;

    @JsonProperty( "PROP_BLD_YR" )
    private String PROP_BLD_YR;

    @JsonProperty( "PROP_CUR_HOME_VAL" )
    private String PROP_CUR_HOME_VAL;

    private String COUNTY;

    private Long DOB_DATE;

    private Integer ST_CODE;

    private Integer ZIP_CODE;

    private Integer phoneType;

    private Integer age;

    private Boolean dnc;

    private String data;

    public Consumer() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public String getAPT() {
        return APT;
    }

    public void setAPT(String APT) {
        this.APT = APT;
    }

    public Boolean getBUY_AUTO_PARTS() {
        return BUY_AUTO_PARTS;
    }

    public void setBUY_AUTO_PARTS(Boolean BUY_AUTO_PARTS) {
        this.BUY_AUTO_PARTS = BUY_AUTO_PARTS;
    }

    public Boolean getBUY_GARDENING() {
        return BUY_GARDENING;
    }

    public void setBUY_GARDENING(Boolean BUY_GARDENING) {
        this.BUY_GARDENING = BUY_GARDENING;
    }

    public Boolean getBUY_HOME_GARD() {
        return BUY_HOME_GARD;
    }

    public void setBUY_HOME_GARD(Boolean BUY_HOME_GARD) {
        this.BUY_HOME_GARD = BUY_HOME_GARD;
    }

    public Boolean getCC_USER() {
        return CC_USER;
    }

    public void setCC_USER(Boolean CC_USER) {
        this.CC_USER = CC_USER;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public Integer getCREDIT_LINES() {
        return CREDIT_LINES;
    }

    public void setCREDIT_LINES(Integer CREDIT_LINES) {
        this.CREDIT_LINES = CREDIT_LINES;
    }

    public Integer getCREDIT_RANGE_NEW() {
        return CREDIT_RANGE_NEW;
    }

    public void setCREDIT_RANGE_NEW(Integer CREDIT_RANGE_NEW) {
        this.CREDIT_RANGE_NEW = CREDIT_RANGE_NEW;
    }

    public String getCREDIT_RATING() {
        return CREDIT_RATING;
    }

    public void setCREDIT_RATING(String CREDIT_RATING) {
        this.CREDIT_RATING = CREDIT_RATING;
    }

    public Integer getDOB_DAY() {
        return DOB_DAY;
    }

    public void setDOB_DAY(Integer DOB_DAY) {
        this.DOB_DAY = DOB_DAY;
    }

    public Integer getDOB_MON() {
        return DOB_MON;
    }

    public void setDOB_MON(Integer DOB_MON) {
        this.DOB_MON = DOB_MON;
    }

    public Integer getDOB_YR() {
        return DOB_YR;
    }

    public void setDOB_YR(Integer DOB_YR) {
        this.DOB_YR = DOB_YR;
    }

    public Boolean getDONR_CHARITABLE() {
        return DONR_CHARITABLE;
    }

    public void setDONR_CHARITABLE(Boolean DONR_CHARITABLE) {
        this.DONR_CHARITABLE = DONR_CHARITABLE;
    }

    public Boolean getDONR_MAIL_ORD() {
        return DONR_MAIL_ORD;
    }

    public void setDONR_MAIL_ORD(Boolean DONR_MAIL_ORD) {
        this.DONR_MAIL_ORD = DONR_MAIL_ORD;
    }

    public Boolean getDONR_POL() {
        return DONR_POL;
    }

    public void setDONR_POL(Boolean DONR_POL) {
        this.DONR_POL = DONR_POL;
    }

    public Boolean getDONR_POL_CONS() {
        return DONR_POL_CONS;
    }

    public void setDONR_POL_CONS(Boolean DONR_POL_CONS) {
        this.DONR_POL_CONS = DONR_POL_CONS;
    }

    public Boolean getDONR_POL_LIB() {
        return DONR_POL_LIB;
    }

    public void setDONR_POL_LIB(Boolean DONR_POL_LIB) {
        this.DONR_POL_LIB = DONR_POL_LIB;
    }

    public Boolean getDONR_RELIG() {
        return DONR_RELIG;
    }

    public void setDONR_RELIG(Boolean DONR_RELIG) {
        this.DONR_RELIG = DONR_RELIG;
    }

    public Boolean getDONR_VETS() {
        return DONR_VETS;
    }

    public void setDONR_VETS(Boolean DONR_VETS) {
        this.DONR_VETS = DONR_VETS;
    }

    public String getDWELL_TYP() {
        return DWELL_TYP;
    }

    public void setDWELL_TYP(String DWELL_TYP) {
        this.DWELL_TYP = DWELL_TYP;
    }

    public String getEDUC() {
        return EDUC;
    }

    public void setEDUC(String EDUC) {
        this.EDUC = EDUC;
    }

    public String getETHNIC_ASSIM() {
        return ETHNIC_ASSIM;
    }

    public void setETHNIC_ASSIM(String ETHNIC_ASSIM) {
        this.ETHNIC_ASSIM = ETHNIC_ASSIM;
    }

    public String getETHNIC_CODE() {
        return ETHNIC_CODE;
    }

    public void setETHNIC_CODE(String ETHNIC_CODE) {
        this.ETHNIC_CODE = ETHNIC_CODE;
    }

    public String getETHNIC_CONF() {
        return ETHNIC_CONF;
    }

    public void setETHNIC_CONF(String ETHNIC_CONF) {
        this.ETHNIC_CONF = ETHNIC_CONF;
    }

    public String getETHNIC_GRP() {
        return ETHNIC_GRP;
    }

    public void setETHNIC_GRP(String ETHNIC_GRP) {
        this.ETHNIC_GRP = ETHNIC_GRP;
    }

    public String getETHNIC_HISP_CNTRY() {
        return ETHNIC_HISP_CNTRY;
    }

    public void setETHNIC_HISP_CNTRY(String ETHNIC_HISP_CNTRY) {
        this.ETHNIC_HISP_CNTRY = ETHNIC_HISP_CNTRY;
    }

    public String getETHNIC_LANG() {
        return ETHNIC_LANG;
    }

    public void setETHNIC_LANG(String ETHNIC_LANG) {
        this.ETHNIC_LANG = ETHNIC_LANG;
    }

    public String getETHNIC_RELIG() {
        return ETHNIC_RELIG;
    }

    public void setETHNIC_RELIG(String ETHNIC_RELIG) {
        this.ETHNIC_RELIG = ETHNIC_RELIG;
    }

    public String getFIPS_CTY() {
        return FIPS_CTY;
    }

    public void setFIPS_CTY(String FIPS_CTY) {
        this.FIPS_CTY = FIPS_CTY;
    }

    public String getFN() {
        return FN;
    }

    public void setFN(String FN) {
        this.FN = FN;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getGENS() {
        return GENS;
    }

    public void setGENS(String GENS) {
        this.GENS = GENS;
    }

    public String getHH_INCOME() {
        return HH_INCOME;
    }

    public void setHH_INCOME(String HH_INCOME) {
        this.HH_INCOME = HH_INCOME;
    }

    public String getHH_MARITAL_STAT() {
        return HH_MARITAL_STAT;
    }

    public void setHH_MARITAL_STAT(String HH_MARITAL_STAT) {
        this.HH_MARITAL_STAT = HH_MARITAL_STAT;
    }

    public Integer getHH_SIZE() {
        return HH_SIZE;
    }

    public void setHH_SIZE(Integer HH_SIZE) {
        this.HH_SIZE = HH_SIZE;
    }

    public String getHOME_OWNR() {
        return HOME_OWNR;
    }

    public void setHOME_OWNR(String HOME_OWNR) {
        this.HOME_OWNR = HOME_OWNR;
    }

    public String getHOME_OWNR_SRC() {
        return HOME_OWNR_SRC;
    }

    public void setHOME_OWNR_SRC(String HOME_OWNR_SRC) {
        this.HOME_OWNR_SRC = HOME_OWNR_SRC;
    }

    public String getINF_HH_RANK() {
        return INF_HH_RANK;
    }

    public void setINF_HH_RANK(String INF_HH_RANK) {
        this.INF_HH_RANK = INF_HH_RANK;
    }

    public String getINT_HOB_SWEEPS() {
        return INT_HOB_SWEEPS;
    }

    public void setINT_HOB_SWEEPS(String INT_HOB_SWEEPS) {
        this.INT_HOB_SWEEPS = INT_HOB_SWEEPS;
    }

    public Boolean getINT_TRAV_CASINO() {
        return INT_TRAV_CASINO;
    }

    public void setINT_TRAV_CASINO(Boolean INT_TRAV_CASINO) {
        this.INT_TRAV_CASINO = INT_TRAV_CASINO;
    }

    public Boolean getINT_TRAV_GENL() {
        return INT_TRAV_GENL;
    }

    public void setINT_TRAV_GENL(Boolean INT_TRAV_GENL) {
        this.INT_TRAV_GENL = INT_TRAV_GENL;
    }

    public String getLIFE_DIY() {
        return LIFE_DIY;
    }

    public void setLIFE_DIY(String LIFE_DIY) {
        this.LIFE_DIY = LIFE_DIY;
    }

    public String getLIFE_HOME() {
        return LIFE_HOME;
    }

    public void setLIFE_HOME(String LIFE_HOME) {
        this.LIFE_HOME = LIFE_HOME;
    }

    public String getLN() {
        return LN;
    }

    public void setLN(String LN) {
        this.LN = LN;
    }

    public Integer getLOR() {
        return LOR;
    }

    public void setLOR(Integer LOR) {
        this.LOR = LOR;
    }

    public String getMI() {
        return MI;
    }

    public void setMI(String MI) {
        this.MI = MI;
    }

    public String getNAME_PRE() {
        return NAME_PRE;
    }

    public void setNAME_PRE(String NAME_PRE) {
        this.NAME_PRE = NAME_PRE;
    }

    public String getNET_WORTH() {
        return NET_WORTH;
    }

    public void setNET_WORTH(String NET_WORTH) {
        this.NET_WORTH = NET_WORTH;
    }

    public Integer getNUM_ADULTS() {
        return NUM_ADULTS;
    }

    public void setNUM_ADULTS(Integer NUM_ADULTS) {
        this.NUM_ADULTS = NUM_ADULTS;
    }

    public Integer getNUM_KIDS() {
        return NUM_KIDS;
    }

    public void setNUM_KIDS(Integer NUM_KIDS) {
        this.NUM_KIDS = NUM_KIDS;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public Integer getAREA_CODE() {
        if ( PHONE == null || PHONE.length() == 0 ) {
            return null;
        } else {
            try { return Integer.parseInt( PHONE.substring( 0, 3 ) ); }
            catch ( Exception e ) { return  null; }
        }
    }

    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getPRES_KIDS() {
        return PRES_KIDS;
    }

    public void setPRES_KIDS(String PRES_KIDS) {
        this.PRES_KIDS = PRES_KIDS;
    }

    public String getPROP_TYPE() {
        return PROP_TYPE;
    }

    public void setPROP_TYPE(String PROP_TYPE) {
        this.PROP_TYPE = PROP_TYPE;
    }

    public String getPROP_AC() {
        return PROP_AC;
    }

    public void setPROP_AC(String PROP_AC) {
        this.PROP_AC = PROP_AC;
    }

    public String getPROP_FUEL() {
        return PROP_FUEL;
    }

    public void setPROP_FUEL(String PROP_FUEL) {
        this.PROP_FUEL = PROP_FUEL;
    }

    public String getPROP_POOL() {
        return PROP_POOL;
    }

    public void setPROP_POOL(String PROP_POOL) {
        this.PROP_POOL = PROP_POOL;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getTIME_ZN() {
        return TIME_ZN;
    }

    public void setTIME_ZN(String TIME_ZN) {
        this.TIME_ZN = TIME_ZN;
    }

    public Boolean getVET_IN_HH() {
        return VET_IN_HH;
    }

    public void setVET_IN_HH(Boolean VET_IN_HH) {
        this.VET_IN_HH = VET_IN_HH;
    }

    public String getZIP() {
        return ZIP;
    }

    public void setZIP(String ZIP) {
        this.ZIP = ZIP;
    }

    public String getZ4() {
        return Z4;
    }

    public void setZ4( String z4 ) {
        Z4 = z4;
    }

    public String getZIP4() {
        return ZIP4 != null ? ZIP4 : Z4;
    }

    public void setZIP4(String ZIP4) {
        this.ZIP4 = ZIP4;
    }

    public String getCOUNTY() {return COUNTY;}
    public void setCOUNTY(String COUNTY) { this.COUNTY = COUNTY; }

    public Integer getST_CODE() {
        if ( ST != null ) {
            for (int i = 0; i < Application.STATE_CODES.length; i++ ) {
                if ( ST.equals( Application.STATE_CODES[ i ] ) ) {
                    return i;
                }
            }
        }

        return null;
    }
    public void setST_CODE(Integer ST_CODE) { this.ST_CODE = ST_CODE; }

    public Integer getZIP_CODE() {
        if ( ZIP != null ) {
            try { return Integer.parseInt( ZIP.split( "-" )[ 0 ] ); }
            catch ( Exception e ) { /*noop*/ }
        }

        return null;
    }
    public void setZIP_CODE(Integer ZIP_CODE) { this.ZIP_CODE = ZIP_CODE; }

    public Long getDOB_DATE() {
        if ( getDOB_YR() != null ) {
            Calendar calendar = Calendar.getInstance();
            calendar.set( Calendar.YEAR, getDOB_YR() );
            calendar.set( Calendar.MONTH, getDOB_MON() );
            calendar.set( Calendar.DAY_OF_MONTH, getDOB_DAY() );

            return calendar.getTimeInMillis();
        } else {
            return null;
        }
    }

    public void setDOB_DATE(Long DOB_DATE) { this.DOB_DATE = DOB_DATE; }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getETHNIC_AFR_AMER_PROF() {
        return ETHNIC_AFR_AMER_PROF;
    }

    public void setETHNIC_AFR_AMER_PROF(Boolean ETHNIC_AFR_AMER_PROF) {
        this.ETHNIC_AFR_AMER_PROF = ETHNIC_AFR_AMER_PROF;
    }

    public Float getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(Float LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public Float getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(Float LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getPETS() {
        return PETS;
    }

    public void setPETS(String PETS) {
        this.PETS = PETS;
    }

    public String getPETS_CAT() {
        return PETS_CAT;
    }

    public void setPETS_CAT(String PETS_CAT) {
        this.PETS_CAT = PETS_CAT;
    }

    public String getPETS_DOG() {
        return PETS_DOG;
    }

    public void setPETS_DOG(String PETS_DOG) {
        this.PETS_DOG = PETS_DOG;
    }

    public String getPROP_SEWER() {
        return PROP_SEWER;
    }

    public void setPROP_SEWER(String PROP_SEWER) {
        this.PROP_SEWER = PROP_SEWER;
    }

    public String getPROP_WATER() {
        return PROP_WATER;
    }

    public void setPROP_WATER(String PROP_WATER) {
        this.PROP_WATER = PROP_WATER;
    }

    public String getPROP_BLD_YR() {
        return PROP_BLD_YR;
    }

    public void setPROP_BLD_YR(String PROP_BLD_YR) {
        this.PROP_BLD_YR = PROP_BLD_YR;
    }

    public String getPROP_CUR_HOME_VAL() {
        return PROP_CUR_HOME_VAL;
    }

    public void setPROP_CUR_HOME_VAL(String PROP_CUR_HOME_VAL) {
        this.PROP_CUR_HOME_VAL = PROP_CUR_HOME_VAL;
    }

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class BooleanDeserializer extends JsonDeserializer {

        public BooleanDeserializer() {}

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return p.getValueAsString( "" ).trim().equals( "Y" );
        }
    }

    public static class NullableIntegerDeserializer extends JsonDeserializer {

        public NullableIntegerDeserializer() {}

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString( "" ).trim();
            if ( value.length()  == 0 || "U".equals( value ) ) {
                return null;
            } else {
                if (value.contains("-")) {
                    value = value.split("-")[0];
                }

                if (value.contains(".")) {
                    value = value.split(".")[0];
                }

                try {
                    return Integer.parseInt(value);
                } catch (Exception e) {e.printStackTrace();}
            }

            return null;
        }
    }

    public static class IntegerDeserializer extends JsonDeserializer {

        public IntegerDeserializer() {}

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString( "" ).trim();
            if ( value.length()  == 0 || "U".equals( value ) ) {
                return -1;
            } else {
                if (value.contains("-")) {
                    value = value.split("-")[0];
                }

                if (value.contains(".")) {
                    value = value.split(".")[0];
                }

                try {
                    return Integer.parseInt(value);
                } catch (Exception e) {e.printStackTrace();}
            }

            return -1;
        }
    }

    private static class ConsumerFloatDeserializer extends JsonDeserializer {

        public ConsumerFloatDeserializer() {}

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString( "" ).trim();
            if ( value.length() == 0 ) {
                return -1.0f;
            } else {
                try { return Float.parseFloat( value ); }
                catch( Exception e ) { e.printStackTrace(); }

                return -1.0f;
            }
        }
    }

}
