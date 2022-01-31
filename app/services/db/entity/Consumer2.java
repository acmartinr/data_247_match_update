package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;

import java.util.Calendar;

public class Consumer2 {

    private long id;

    @JsonProperty("PERSONFIRSTNAME")
    private String PERSONFIRSTNAME;

    @JsonProperty("PERSONMIDDLEINITIAL")
    private String PERSONMIDDLEINITIAL;

    @JsonProperty("PERSONLASTNAME")
    private String PERSONLASTNAME;

    @JsonProperty("PRIMARYADDRESS")
    private String PRIMARYADDRESS;

    @JsonProperty("SECONDARYADDRESS")
    private String SECONDARYADDRESS;

    @JsonProperty("STATE")
    private String STATE;

    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer ST_CODE;

    @JsonProperty("ZIPCODE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer ZIPCODE;

    @JsonProperty("ZIP_4")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer ZIP_4;

    @JsonProperty("COUNTYCODE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer COUNTYCODE;

    @JsonProperty("COUNTYNAME")
    private String COUNTYNAME;

    @JsonProperty("CITYNAME")
    private String CITYNAME;

    @JsonProperty("DWELLINGTYPE")
    private String DWELLINGTYPE; //VALUES?

    @JsonProperty("AREACODE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer AREACODE;

    @JsonProperty("PHONE")
    private String PHONE;

    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PHONETYPE;

    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean DNC = true;

    @JsonProperty("ESTIMATEDINCOMECODE")
    private String ESTIMATEDINCOMECODE; //VALUES?

    @JsonProperty("HOMEOWNERPROBABILITYMODEL")
    private String HOMEOWNERPROBABILITYMODEL; //VALUES?

    @JsonProperty("LENGTHOFRESIDENCE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer LENGTHOFRESIDENCE;

    @JsonProperty("NUMBEROFPERSONSINLIVINGUNIT")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer NUMBEROFPERSONSINLIVINGUNIT;

    @JsonProperty("PRESENCEOFCHILDREN")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PRESENCEOFCHILDREN;

    @JsonProperty("NUMBEROFCHILDREN")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer NUMBEROFCHILDREN;

    @JsonProperty("PERSONGENDER")
    private String PERSONGENDER;

    @JsonProperty("PERSONDATEOFBIRTHYEAR")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PERSONDATEOFBIRTHYEAR;

    @JsonProperty("PERSONDATEOFBIRTHMONTH")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PERSONDATEOFBIRTHMONTH;

    @JsonProperty("PERSONDATEOFBIRTHDAY")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PERSONDATEOFBIRTHDAY;

    private Long PERSONDATEOFBIRTHDATE;

    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PERSONAGE;

    @JsonProperty("PERSONEXACTAGE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PERSONEXACTAGE;

    @JsonProperty("PERSONMARITALSTATUS")
    private String PERSONMARITALSTATUS; //VALUES?

    @JsonProperty("INFERREDAGE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer INFERREDAGE;

    @JsonProperty("OCCUPATIONGROUP")
    private String OCCUPATIONGROUP;


    @JsonProperty("carrier_name")
    private String carrier_name;



    @JsonProperty("type")
    private String type;

    @JsonProperty("wireless")
    private String wireless;

    @JsonProperty("PERSONOCCUPATION")
    private String PERSONOCCUPATION;

    @JsonProperty("ETHNICCODE")
    private String ETHNICCODE;

    @JsonProperty("LANGUAGECODE")
    private String LANGUAGECODE;

    @JsonProperty("ETHNICGROUP")
    private String ETHNICGROUP;

    @JsonProperty("RELIGIONCODE")
    private String RELIGIONCODE;

    @JsonProperty("HISPANICCOUNTRYCODE")
    private String HISPANICCOUNTRYCODE;

    @JsonProperty("PERSONEDUCATION")
    private String PERSONEDUCATION;

    @JsonProperty("BUSINESSOWNER")
    private String BUSINESSOWNER;

    @JsonProperty("INFERREDHOUSEHOLDRANK")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer INFERREDHOUSEHOLDRANK;

    @JsonProperty("NUMBEROFADULTS")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer NUMBEROFADULTS;

    @JsonProperty("GENERATIONSINHOUSEHOLD")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer GENERATIONSINHOUSEHOLD;

    @JsonProperty("PRESENCEOFCREDITCARD")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PRESENCEOFCREDITCARD;

    @JsonProperty("PRESENCEOFGOLDORPLATINUMCREDITCARD")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PRESENCEOFGOLDORPLATINUMCREDITCARD;

    @JsonProperty("PRESENCEOFPREMIUMCREDITCARD")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PRESENCEOFPREMIUMCREDITCARD;

    @JsonProperty("CREDITRATING")
    private String CREDITRATING;

    @JsonProperty("INVESTMENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTMENT;

    @JsonProperty("INVESTMENTSTOCKSECURITIES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTMENTSTOCKSECURITIES;

    @JsonProperty("NETWORTH")
    private String NETWORTH;

    @JsonProperty("NUMBEROFLINESOFCREDIT")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer NUMBEROFLINESOFCREDIT;

    @JsonProperty("CREDIT_RANGEOFNEWCREDIT")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer CREDIT_RANGEOFNEWCREDIT;

    @JsonProperty("TRAVELANDENTERTAINMENTCARDHOLDER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVELANDENTERTAINMENTCARDHOLDER;

    @JsonProperty("CREDITCARDUSER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CREDITCARDUSER;

    @JsonProperty("CREDITCARDNEWISSUE")
    private String CREDITCARDNEWISSUE;

    @JsonProperty("INVESTING_ACTIVE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTING_ACTIVE;

    @JsonProperty("INVESTMENTSPERSONAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTMENTSPERSONAL;

    @JsonProperty("INVESTMENTSREALESTATE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTMENTSREALESTATE;

    @JsonProperty("INVESTINGFINANCEGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTINGFINANCEGROUPING;

    @JsonProperty("INVESTMENTSFOREIGN")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INVESTMENTSFOREIGN;

    @JsonProperty("INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED")
    private String INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED;

    @JsonProperty("VALUEHUNTER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean VALUEHUNTER;

    @JsonProperty("OPPORTUNITYSEEKERS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean OPPORTUNITYSEEKERS;

    @JsonProperty("NEWSANDFINANCIAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean NEWSANDFINANCIAL;

    @JsonProperty("AUTOMOTIVEBUFF")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean AUTOMOTIVEBUFF;

    @JsonProperty("BOOKREADER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean BOOKREADER;

    @JsonProperty("COMPUTEROWNER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COMPUTEROWNER;

    @JsonProperty("COOKINGENTHUSIAST")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COOKINGENTHUSIAST;

    @JsonProperty("DO_IT_YOURSELFERS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean DO_IT_YOURSELFERS;

    @JsonProperty("EXERCISEENTHUSIAST")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EXERCISEENTHUSIAST;

    @JsonProperty("GARDENER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GARDENER;

    @JsonProperty("GOLFENTHUSIASTS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GOLFENTHUSIASTS;

    @JsonProperty("HOMEDECORATINGENTHUSIAST")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOMEDECORATINGENTHUSIAST;

    @JsonProperty("OUTDOORENTHUSIAST")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean OUTDOORENTHUSIAST;

    @JsonProperty("OUTDOORSPORTSLOVER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean OUTDOORSPORTSLOVER;

    @JsonProperty("PHOTOGRAPHY")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PHOTOGRAPHY;

    @JsonProperty("TRAVELER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVELER;

    @JsonProperty("PETS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PETS;

    @JsonProperty("CATS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CATS;

    @JsonProperty("DOGS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean DOGS;

    @JsonProperty("MAILRESPONDER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MAILRESPONDER;

    @JsonProperty("SWEEPSTAKES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SWEEPSTAKES;

    @JsonProperty("RELIGIOUSMAGAZINE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean RELIGIOUSMAGAZINE;

    @JsonProperty("MALEMERCHBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MALEMERCHBUYER;

    @JsonProperty("FEMALEMERCHBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean FEMALEMERCHBUYER;

    @JsonProperty("CRAFTS_HOBBMERCHBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CRAFTS_HOBBMERCHBUYER;

    @JsonProperty("GARDENING_FARMINGBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GARDENING_FARMINGBUYER;

    @JsonProperty("BOOKBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean BOOKBUYER;

    @JsonProperty("COLLECT_SPECIALFOODSBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COLLECT_SPECIALFOODSBUYER;

    @JsonProperty("RELIGIOUSCONTRIBUTOR")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean RELIGIOUSCONTRIBUTOR;

    @JsonProperty("POLITICALCONTRIBUTOR")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean POLITICALCONTRIBUTOR;

    @JsonProperty("CHARITABLE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CHARITABLE;

    @JsonProperty("DONATESTOENVIRONMENTALCAUSES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean DONATESTOENVIRONMENTALCAUSES;

    @JsonProperty("VETERANINHOUSEHOLD")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean VETERANINHOUSEHOLD;

    @JsonProperty("HIGHTECHLEADER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HIGHTECHLEADER;

    @JsonProperty("SMOKER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SMOKER;

    @JsonProperty("MAILORDERBUYER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MAILORDERBUYER;

    @JsonProperty("ONLINEPURCHASINGINDICATOR")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean ONLINEPURCHASINGINDICATOR;

    @JsonProperty("APPARELWOMENS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean APPARELWOMENS;

    @JsonProperty("YOUNGWOMENSAPPAREL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean YOUNGWOMENSAPPAREL;

    @JsonProperty("APPARELMENS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean APPARELMENS;

    @JsonProperty("APPARELMENSBIGANDTALL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean APPARELMENSBIGANDTALL;

    @JsonProperty("YOUNGMENSAPPAREL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean YOUNGMENSAPPAREL;

    @JsonProperty("APPARELCHILDRENS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean APPARELCHILDRENS;

    @JsonProperty("HEALTHANDBEAUTY")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HEALTHANDBEAUTY;

    @JsonProperty("BEAUTYCOSMETICS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean BEAUTYCOSMETICS;

    @JsonProperty("JEWELRY")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean JEWELRY;

    @JsonProperty("LUGGAGE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean LUGGAGE;

    @JsonProperty("POLITICALCONSERVATIVECHARITABLEDONATION")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean POLITICALCONSERVATIVECHARITABLEDONATION;

    @JsonProperty("POLITICALLIBERALCHARITABLEDONATION")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean POLITICALLIBERALCHARITABLEDONATION;

    @JsonProperty("VETERANSCHARITABLEDONATION")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean VETERANSCHARITABLEDONATION;

    @JsonProperty("SINGLEPARENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SINGLEPARENT;

    @JsonProperty("SENIORADULTINHOUSEHOLD")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SENIORADULTINHOUSEHOLD;

    @JsonProperty("EQUESTRIAN")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EQUESTRIAN;

    @JsonProperty("CAREERIMPROVEMENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CAREERIMPROVEMENT;

    @JsonProperty("WORKINGWOMAN")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean WORKINGWOMAN;

    @JsonProperty("AFRICANAMERICANPROFESSIONALS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean AFRICANAMERICANPROFESSIONALS;

    @JsonProperty("SOHOINDICATOR")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SOHOINDICATOR;

    @JsonProperty("CAREER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CAREER;

    @JsonProperty("READINGSCIENCEFICTION")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean READINGSCIENCEFICTION;

    @JsonProperty("READINGAUDIOBOOKS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean READINGAUDIOBOOKS;

    @JsonProperty("HISTORYMILITARY")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HISTORYMILITARY;

    @JsonProperty("CURRENTAFFAIRSPOLITICS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CURRENTAFFAIRSPOLITICS;

    @JsonProperty("SCIENCESPACE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SCIENCESPACE;

    @JsonProperty("EDUCATIONONLINE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EDUCATIONONLINE;

    @JsonProperty("GAMING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GAMING;

    @JsonProperty("COMPUTINGHOMEOFFICEGENERAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COMPUTINGHOMEOFFICEGENERAL;

    @JsonProperty("ELECTRONICSCOMPUTINGANDHOMEOFFICE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean ELECTRONICSCOMPUTINGANDHOMEOFFICE;

    @JsonProperty("HIGHENDAPPLIANCES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HIGHENDAPPLIANCES;

    @JsonProperty("INTENDTOPURCHASEHDTVSATELLITEDISH")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INTENDTOPURCHASEHDTVSATELLITEDISH;

    @JsonProperty("TVCABLE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TVCABLE;

    @JsonProperty("GAMESVIDEOGAMES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GAMESVIDEOGAMES;

    @JsonProperty("TVSATELLITEDISH")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TVSATELLITEDISH;

    @JsonProperty("COMPUTERS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COMPUTERS;

    @JsonProperty("GAMESCOMPUTERGAMES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GAMESCOMPUTERGAMES;

    @JsonProperty("CONSUMERELECTRONICS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CONSUMERELECTRONICS;

    @JsonProperty("MOVIEMUSICGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MOVIEMUSICGROUPING;

    @JsonProperty("ELECTRONICSCOMPUTERSGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean ELECTRONICSCOMPUTERSGROUPING;

    @JsonProperty("TELECOMMUNICATIONS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TELECOMMUNICATIONS;

    @JsonProperty("ARTS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean ARTS;

    @JsonProperty("MUSICALINSTRUMENTS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MUSICALINSTRUMENTS;

    @JsonProperty("COLLECTIBLESSTAMPS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COLLECTIBLESSTAMPS;

    @JsonProperty("COLLECTIBLESCOINS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COLLECTIBLESCOINS;

    @JsonProperty("COLLECTIBLESARTS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COLLECTIBLESARTS;

    @JsonProperty("COLLECTIBLESANTIQUES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COLLECTIBLESANTIQUES;

    @JsonProperty("COLLECTIBLESSPORTSMEMORABILIA")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COLLECTIBLESSPORTSMEMORABILIA;

    @JsonProperty("MILITARYMEMORABILIAWEAPONRY")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MILITARYMEMORABILIAWEAPONRY;

    @JsonProperty("AUTOWORK")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean AUTOWORK;

    @JsonProperty("WOODWORKING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean WOODWORKING;

    @JsonProperty("AVIATION")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean AVIATION;

    @JsonProperty("HOUSEPLANTS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOUSEPLANTS;

    @JsonProperty("HOMEANDGARDEN")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOMEANDGARDEN;

    @JsonProperty("HOMEIMPROVEMENTGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOMEIMPROVEMENTGROUPING;

    @JsonProperty("PHOTOGRAPHYANDVIDEOEQUIPMENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean PHOTOGRAPHYANDVIDEOEQUIPMENT;

    @JsonProperty("HOMEFURNISHINGSDECORATING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOMEFURNISHINGSDECORATING;

    @JsonProperty("HOMEIMPROVEMENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOMEIMPROVEMENT;

    @JsonProperty("INTENDTOPURCHASEHOMEIMPROVEMENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean INTENDTOPURCHASEHOMEIMPROVEMENT;

    @JsonProperty("FOODWINES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean FOODWINES;

    @JsonProperty("COOKINGGENERAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COOKINGGENERAL;

    @JsonProperty("COOKING - GOURMET")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COOKINGGOURMET;

    @JsonProperty("FOODSNATURAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean FOODSNATURAL;

    @JsonProperty("COOKINGFOODGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean COOKINGFOODGROUPING;

    @JsonProperty("GAMINGCASINO")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean GAMINGCASINO;

    @JsonProperty("TRAVELGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVELGROUPING;

    @JsonProperty("TRAVEL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVEL;

    @JsonProperty("TRAVELDOMESTIC")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVELDOMESTIC;

    @JsonProperty("TRAVELINTERNATIONAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVELINTERNATIONAL;

    @JsonProperty("TRAVELCRUISEVACATIONS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TRAVELCRUISEVACATIONS;

    @JsonProperty("UPSCALELIVING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean UPSCALELIVING;

    @JsonProperty("CULTURALARTISTICLIVING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CULTURALARTISTICLIVING;

    @JsonProperty("HIGH-TECH LIVING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HIGHTECHLIVING;

    @JsonProperty("EXERCISEHEALTHGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EXERCISEHEALTHGROUPING;

    @JsonProperty("EXERCISERUNNINGJOGGING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EXERCISERUNNINGJOGGING;

    @JsonProperty("EXERCISEWALKING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EXERCISEWALKING;

    @JsonProperty("EXERCISEAEROBIC")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EXERCISEAEROBIC;

    @JsonProperty("SPECTATORSPORTSTVSPORTS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPECTATORSPORTSTVSPORTS;

    @JsonProperty("SPECTATORSPORTSFOOTBALL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPECTATORSPORTSFOOTBALL;

    @JsonProperty("SPECTATORSPORTSBASEBALL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPECTATORSPORTSBASEBALL;

    @JsonProperty("SPECTATORSPORTSBASKETBALL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPECTATORSPORTSBASKETBALL;

    @JsonProperty("SPECTATORSPORTSHOCKEY")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPECTATORSPORTSHOCKEY;

    @JsonProperty("SPECTATORSPORTSSOCCER")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPECTATORSPORTSSOCCER;

    @JsonProperty("TENNIS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean TENNIS;

    @JsonProperty("SNOWSKIING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SNOWSKIING;

    @JsonProperty("MOTORCYCLING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MOTORCYCLING;

    @JsonProperty("NASCAR")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean NASCAR;

    @JsonProperty("BOATINGSAILING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean BOATINGSAILING;

    @JsonProperty("SCUBADIVING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SCUBADIVING;

    @JsonProperty("SPORTSANDLEISURE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPORTSANDLEISURE;

    @JsonProperty("HUNTING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HUNTING;

    @JsonProperty("FISHING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean FISHING;

    @JsonProperty("CAMPINGHIKING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean CAMPINGHIKING;

    @JsonProperty("HUNTINGSHOOTING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HUNTINGSHOOTING;

    @JsonProperty("SPORTSGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SPORTSGROUPING;

    @JsonProperty("OUTDOORSGROUPING")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean OUTDOORSGROUPING;

    @JsonProperty("HEALTHMEDICAL")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HEALTHMEDICAL;

    @JsonProperty("DIETINGWEIGHTLOSS")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean DIETINGWEIGHTLOSS;

    @JsonProperty("SELFIMPROVEMENT")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean SELFIMPROVEMENT;

    @JsonProperty("AUTOMOTIVEAUTOPARTSANDACCESSORIES")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean AUTOMOTIVEAUTOPARTSANDACCESSORIES;

    @JsonProperty("HOMESWIMMINGPOOLINDICATOR")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean HOMESWIMMINGPOOLINDICATOR;

    @JsonProperty("AIRCONDITIONING")
    private String AIRCONDITIONING;

    @JsonProperty("HOMEHEATINDICATOR")
    private String HOMEHEATINDICATOR;

    @JsonProperty("HOMEPURCHASEPRICE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer HOMEPURCHASEPRICE;

    @JsonProperty("HOMEPURCHASEDATEYEAR")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer HOMEPURCHASEDATEYEAR;

    @JsonProperty("HOMEPURCHASEDATEMONTH")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer HOMEPURCHASEDATEMONTH;

    @JsonProperty("HOMEPURCHASEDATEDAY")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer HOMEPURCHASEDATEDAY;

    private Long HOMEPURCHASEDATE;

    @JsonProperty("HOMEYEARBUILT")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer HOMEYEARBUILT;

    @JsonProperty("ESTIMATEDCURRENTHOMEVALUECODE")
    private String ESTIMATEDCURRENTHOMEVALUECODE;

    @JsonProperty("MORTGAGEAMOUNTINTHOUSANDS")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer MORTGAGEAMOUNTINTHOUSANDS;

    @JsonProperty("MORTGAGELENDERNAME")
    private String MORTGAGELENDERNAME;

    @JsonProperty("MORTGAGELENDERNAMEAVAILABLE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean MORTGAGELENDERNAMEAVAILABLE;

    @JsonProperty("MORTGAGERATE")
    private String MORTGAGERATE;

    @JsonProperty("MORTGAGERATETYPE")
    private String MORTGAGERATETYPE; //VALUES?

    @JsonProperty("MORTGAGELOANTYPE")
    private String MORTGAGELOANTYPE; //VALUES?

    @JsonProperty("TRANSACTIONTYPE")
    private String TRANSACTIONTYPE; //VALUES?

    @JsonProperty("DEEDDATEOFREFINANCEYEAR")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer DEEDDATEOFREFINANCEYEAR;

    @JsonProperty("DEEDDATEOFREFINANCEMONTH")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer DEEDDATEOFREFINANCEMONTH;

    @JsonProperty("DEEDDATEOFREFINANCEDAY")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer DEEDDATEOFREFINANCEDAY;

    private Long DEEDDATEOFREFINANCE;

    @JsonProperty("REFINANCEAMOUNTINTHOUSANDS")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer REFINANCEAMOUNTINTHOUSANDS;

    @JsonProperty("REFINANCELENDERNAME")
    private String REFINANCELENDERNAME;

    @JsonProperty("REFINANCELENDERNAMEAVAILABLE")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean REFINANCELENDERNAMEAVAILABLE;

    @JsonProperty("REFINANCERATETYPE")
    private String REFINANCERATETYPE; //VALUES?

    @JsonProperty("REFINANCELOANTYPE")
    private String REFINANCELOANTYPE; //VALUES?

    @JsonProperty("CENSUSMEDIANHOMEVALUE")
    private String CENSUSMEDIANHOMEVALUE;

    @JsonProperty("CENSUSMEDIANHOUSEHOLDINCOME")
    private String CENSUSMEDIANHOUSEHOLDINCOME;

    @JsonProperty("CRA_INCOMECLASSIFICATIONCODE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer CRA_INCOMECLASSIFICATIONCODE;

    @JsonProperty("PURCHASEMORTGAGEDATE")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer PURCHASEMORTGAGEDATE;

    @JsonProperty("MOSTRECENTLENDERCODE")
    private String MOSTRECENTLENDERCODE;

    @JsonProperty("PURCHASE LENDER NAME")
    private String PURCHASELENDERNAME;

    @JsonProperty("MOSTRECENTMORTGAGEINTERESTRATE")
    private String MOSTRECENTMORTGAGEINTERESTRATE;

    @JsonProperty("SEWER")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer SEWER;

    @JsonProperty("WATER")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer WATER;

    @JsonProperty("LOANTOVALUE")
    private String LOANTOVALUE;

    @JsonProperty("PASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE")
    private String PASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE;

    @JsonProperty("EMAILFLAG")
    @JsonDeserialize(using = Consumer.BooleanDeserializer.class)
    private Boolean EMAILFLAG;

    private String HASH;

    private String data;

    // MOCK FIELDS FOR TESTING
    @JsonProperty("FEFFSRUNIQUEKEY")
    private String SRUNIQUEKEY;

    @JsonProperty("ADDRESSID")
    private String ADDRESSID;

    @JsonProperty("INDIVIDUALID")
    private String INDIVIDUALID;

    @JsonProperty("PERSONSURNAMESUFFIX")
    private String PERSONSURNAMESUFFIX;

    @JsonProperty("PERSONTITLEOFRESPECT")
    private String PERSONTITLEOFRESPECT;

    @JsonProperty("HOUSENUMBER")
    private String HOUSENUMBER;

    @JsonProperty("PREDIRECTION")
    private String PREDIRECTION;

    @JsonProperty("STREETNAME")
    private String STREETNAME;

    @JsonProperty("STREETSUFFIX")
    private String STREETSUFFIX;

    @JsonProperty("POSTDIRECTION")
    private String POSTDIRECTION;

    @JsonProperty("UNITDESIGNATOR")
    private String UNITDESIGNATOR;

    @JsonProperty("UNITDESIGNATORNUMBER")
    private String UNITDESIGNATORNUMBER;

    @JsonProperty("DEL_POINT_CHECK_DIGIT")
    private String DEL_POINT_CHECK_DIGIT;

    @JsonProperty("MSA")
    private String MSA;

    @JsonProperty("CITYNAMEABBR")
    private String CITYNAMEABBR;

    @JsonProperty("CARRIER_ROUTE")
    private String CARRIER_ROUTE;

    @JsonProperty("CENSUSTRACT")
    private String CENSUSTRACT;

    @JsonProperty("CENSUSBLOCK")
    private String CENSUSBLOCK;

    @JsonProperty("LATITUDE")
    private String LATITUDE;

    @JsonProperty("LONGITUDE")
    private String LONGITUDE;

    @JsonProperty("TIMEZONE")
    private String TIMEZONE;

    @JsonProperty("XAXIS")
    private String XAXIS;

    @JsonProperty("YAXIS")
    private String YAXIS;

    @JsonProperty("ZAXIS")
    private String ZAXIS;

    @JsonProperty("DPV_CODE")
    private String DPV_CODE;

    @JsonProperty("NUMBEROFSOURCES")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer NUMBEROFSOURCES;

    @JsonProperty("SECONDARYADDRESSPRESENT")
    private String SECONDARYADDRESSPRESENT;

    @JsonProperty("LIVINGUNITID")
    private String LIVINGUNITID;

    @JsonProperty("RDID")
    private String RDID;

    @JsonProperty("LENGTHOFRESIDENCECODE")
    private String LENGTHOFRESIDENCECODE;

    @JsonProperty("CHILDRENAGE00_02")
    private String CHILDRENAGE00_02;

    @JsonProperty("CHILDRENAGE00_02MALE")
    private String CHILDRENAGE00_02MALE;

    @JsonProperty("CHILDRENAGE00_02FEMALE")
    private String CHILDRENAGE00_02FEMALE;

    @JsonProperty("CHILDRENAGE00_02UNKNOWN")
    private String CHILDRENAGE00_02UNKNOWN;

    @JsonProperty("CHILDRENAGE03_05")
    private String CHILDRENAGE03_05;

    @JsonProperty("CHILDRENAGE03_05MALE")
    private String CHILDRENAGE03_05MALE;

    @JsonProperty("CHILDRENAGE03_05FEMALE")
    private String CHILDRENAGE03_05FEMALE;

    @JsonProperty("CHILDRENAGE03_05UNKNOWN")
    private String CHILDRENAGE03_05UNKNOWN;

    @JsonProperty("CHILDRENAGE06_10")
    private String CHILDRENAGE06_10;

    @JsonProperty("CHILDRENAGE06_10MALE")
    private String CHILDRENAGE06_10MALE;

    @JsonProperty("CHILDRENAGE06_10FEMALE")
    private String CHILDRENAGE06_10FEMALE;

    @JsonProperty("CHILDRENAGE06_10UNKNOWN")
    private String CHILDRENAGE06_10UNKNOWN;

    @JsonProperty("CHILDRENAGE11_15")
    private String CHILDRENAGE11_15;

    @JsonProperty("CHILDRENAGE11_15MALE")
    private String CHILDRENAGE11_15MALE;

    @JsonProperty("CHILDRENAGE11_15FEMALE")
    private String CHILDRENAGE11_15FEMALE;

    @JsonProperty("CHILDRENAGE11_15UNKNOWN")
    private String CHILDRENAGE11_15UNKNOWN;

    @JsonProperty("CHILDRENAGE16_17")
    private String CHILDRENAGE16_17;

    @JsonProperty("CHILDRENAGE16_17MALE")
    private String CHILDRENAGE16_17MALE;

    @JsonProperty("CHILDRENAGE16_17FEMALE")
    private String CHILDRENAGE16_17FEMALE;

    @JsonProperty("CHILDRENAGE16_17UNKNOWN")
    private String CHILDRENAGE16_17UNKNOWN;

    @JsonProperty("PERSONAGECODE")
    private String PERSONAGECODE;

    @JsonProperty("MALES_18_24")
    private String MALES_18_24;

    @JsonProperty("FEMALES_18_24")
    private String FEMALES_18_24;

    @JsonProperty("UNKNOWNGENDER_18_24")
    private String UNKNOWNGENDER_18_24;

    @JsonProperty("MALES_25_34")
    private String MALES_25_34;

    @JsonProperty("FEMALES_25_34")
    private String FEMALES_25_34;

    @JsonProperty("UNKNOWNGENDER_25_34")
    private String UNKNOWNGENDER_25_34;

    @JsonProperty("MALES_35_44")
    private String MALES_35_44;

    @JsonProperty("FEMALES_35_44")
    private String FEMALES_35_44;

    @JsonProperty("UNKNOWNGENDER_35_44")
    private String UNKNOWNGENDER_35_44;

    @JsonProperty("MALES_45_54")
    private String MALES_45_54;

    @JsonProperty("FEMALES_45_54")
    private String FEMALES_45_54;

    @JsonProperty("UNKNOWNGENDER_45_54")
    private String UNKNOWNGENDER_45_54;

    @JsonProperty("MALES_55_64")
    private String MALES_55_64;

    @JsonProperty("FEMALES_55_64")
    private String FEMALES_55_64;

    @JsonProperty("UNKNOWNGENDER_55_64")
    private String UNKNOWNGENDER_55_64;

    @JsonProperty("MALES_65_74")
    private String MALES_65_74;

    @JsonProperty("FEMALES_65_74")
    private String FEMALES_65_74;

    @JsonProperty("UNKNOWNGENDER_65_74")
    private String UNKNOWNGENDER_65_74;

    @JsonProperty("MALES_75_PLUS")
    private String MALES_75_PLUS;

    @JsonProperty("FEMALES_75_PLUS")
    private String FEMALES_75_PLUS;

    @JsonProperty("UNKNOWNGENDER_75_PLUS")
    private String UNKNOWNGENDER_75_PLUS;

    @JsonProperty("ETHNICCONFIDENCECODE")
    private String ETHNICCONFIDENCECODE;

    @JsonProperty("PRESENCEOFUPSCALERETAILCARD")
    private String PRESENCEOFUPSCALERETAILCARD;

    @JsonProperty("PRESENCEOFBANKCARD")
    private String PRESENCEOFBANKCARD;

    @JsonProperty("GASDEPTRETAILCARDHOLDER")
    private String GASDEPTRETAILCARDHOLDER;

    @JsonProperty("AMERICANEXPRESSCARD")
    private String AMERICANEXPRESSCARD;

    @JsonProperty("AMERICANEXPRESSGOLDPREMIUM")
    private String AMERICANEXPRESSGOLDPREMIUM;

    @JsonProperty("DISCOVERGOLDPREMIUM")
    private String DISCOVERGOLDPREMIUM;

    @JsonProperty("DISCOVERREGULAR")
    private String DISCOVERREGULAR;

    @JsonProperty("GASOLINEORRETAILCARDGOLDPREMIUM")
    private String GASOLINEORRETAILCARDGOLDPREMIUM;

    @JsonProperty("GASOLINE OR RETAIL CARD REGULAR")
    private String GASOLINEORRETAILCARDREGULAR;

    @JsonProperty("MASTERCARDGOLDPREMIUM")
    private String MASTERCARDGOLDPREMIUM;

    @JsonProperty("MASTERCARDREGULAR")
    private String MASTERCARDREGULAR;

    @JsonProperty("VISAGOLDPREMIUM")
    private String VISAGOLDPREMIUM;

    @JsonProperty("VISAREGULAR")
    private String VISAREGULAR;

    @JsonProperty("CREDIT CARD INDICATOR")
    private String CREDITCARDINDICATOR;

    @JsonProperty("BANK CARD HOLDER")
    private String BANKCARDHOLDER;

    @JsonProperty("GAS/DEPARTMENT/RETAIL CARD HOLDER")
    private String GASDEPARTMENTRETAILCARDHOLDER;

    @JsonProperty("CREDITCARDHOLDERUNKNOWNTYPE")
    private String CREDITCARDHOLDERUNKNOWNTYPE;

    @JsonProperty("PREMIUM CARD HOLDER")
    private String PREMIUMCARDHOLDER;

    @JsonProperty("UPSCALE (DEPARTMENT STORE) CARD HOLDER")
    private String UPSCALEDEPARTMENTSTORECARDHOLDER;

    @JsonProperty("BANK CARD - PRESENCE IN HOUSEHOLD")
    private String BANKCARDPRESENCEINHOUSEHOLD;

    @JsonProperty("ASSIMILATIONCODES")
    private String ASSIMILATIONCODES;

    @JsonProperty("MEMBERSHIPCLUB")
    private String MEMBERSHIPCLUB;

    @JsonProperty("RESPONDEDTOCATALOG")
    private String RESPONDEDTOCATALOG;

    @JsonProperty("HEALTH_INSTITUTIONCONTRIBUTOR")
    private String HEALTH_INSTITUTIONCONTRIBUTOR;

    @JsonProperty("GENERALCONTRIBUTOR")
    private String GENERALCONTRIBUTOR;

    @JsonProperty("DONATESBYMAIL")
    private String DONATESBYMAIL;

    @JsonProperty("HEAVYBUSINESSTRAVELERS")
    private String HEAVYBUSINESSTRAVELERS;

    @JsonProperty("APPARELWOMENSPETITE")
    private String APPARELWOMENSPETITE;

    @JsonProperty("APPARELWOMENSPLUSSIZES")
    private String APPARELWOMENSPLUSSIZES;

    @JsonProperty("COMMUNITY INVOLVEMENT - CAUSES SUPPORTED FINANCIALLY")
    private String COMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY;

    @JsonProperty("ANIMALWELFARECHARITABLEDONATION")
    private String ANIMALWELFARECHARITABLEDONATION;

    @JsonProperty("ARTSORCULTURALCHARITABLEDONATION")
    private String ARTSORCULTURALCHARITABLEDONATION;

    @JsonProperty("CHILDRENSCHARITABLEDONATION")
    private String CHILDRENSCHARITABLEDONATION;

    @JsonProperty("ENVIRONMENT OR WILDLIFE CHARITABLE DONATION")
    private String ENVIRONMENTORWILDLIFECHARITABLEDONATION;

    @JsonProperty("ENVIRONMENTALISSUESCHARITABLEDONATION")
    private String ENVIRONMENTALISSUESCHARITABLEDONATION;

    @JsonProperty("INTERNATIONALAIDCHARITABLEDONATION")
    private String INTERNATIONALAIDCHARITABLEDONATION;

    @JsonProperty("POLITICALCHARITABLEDONATION")
    private String POLITICALCHARITABLEDONATION;

    @JsonProperty("CHARITABLEDONATIONS_OTHER")
    private String CHARITABLEDONATIONS_OTHER;

    @JsonProperty("COMMUNITYCHARITIES")
    private String COMMUNITYCHARITIES;

    @JsonProperty("PARENTING")
    private String PARENTING;

    @JsonProperty("CHILDRENSAPPARELINFANTSANDTODDLERS")
    private String CHILDRENSAPPARELINFANTSANDTODDLERS;

    @JsonProperty("CHILDRENSLEARNINGANDACTIVITYTOYS")
    private String CHILDRENSLEARNINGANDACTIVITYTOYS;

    @JsonProperty("CHILDRENSPRODUCTSGENERALBABYCARE")
    private String CHILDRENSPRODUCTSGENERALBABYCARE;

    @JsonProperty("CHILDRENSPRODUCTSGENERALBACKTOSCHOOL")
    private String CHILDRENSPRODUCTSGENERALBACKTOSCHOOL;

    @JsonProperty("CHILDRENSPRODUCTSGENERAL")
    private String CHILDRENSPRODUCTSGENERAL;

    @JsonProperty("YOUNGADULTINHOUSEHOLD")
    private String YOUNGADULTINHOUSEHOLD;

    @JsonProperty("CHILDRENSINTERESTS")
    private String CHILDRENSINTERESTS;

    @JsonProperty("GRANDCHILDREN")
    private String GRANDCHILDREN;

    @JsonProperty("CHRISTIANFAMILIES")
    private String CHRISTIANFAMILIES;

    @JsonProperty("OTHERPETOWNER")
    private String OTHERPETOWNER;

    @JsonProperty("BOOKSANDMAGAZINESMAGAZINES")
    private String BOOKSANDMAGAZINESMAGAZINES;

    @JsonProperty("BOOKSANDMUSICBOOKS")
    private String BOOKSANDMUSICBOOKS;

    @JsonProperty("BOOKSANDMUSICBOOKSAUDIO")
    private String BOOKSANDMUSICBOOKSAUDIO;

    @JsonProperty("READINGGENERAL")
    private String READINGGENERAL;

    @JsonProperty("READING - RELIGIOUS / INSPIRATIONAL")
    private String READINGRELIGIOUSINSPIRATIONAL;

    @JsonProperty("READINGMAGAZINES")
    private String READINGMAGAZINES;

    @JsonProperty("READINGGROUPING")
    private String READINGGROUPING;

    @JsonProperty("RELIGIOUSINSPIRATIONAL")
    private String RELIGIOUSINSPIRATIONAL;

    @JsonProperty("MAGAZINES")
    private String MAGAZINES;

    @JsonProperty("DVDSVIDEOS")
    private String DVDSVIDEOS;

    @JsonProperty("ELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER")
    private String ELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER;

    @JsonProperty("MUSICHOMESTEREO")
    private String MUSICHOMESTEREO;

    @JsonProperty("MUSICPLAYER")
    private String MUSICPLAYER;

    @JsonProperty("MUSICCOLLECTOR")
    private String MUSICCOLLECTOR;

    @JsonProperty("MUSICAVIDLISTENER")
    private String MUSICAVIDLISTENER;

    @JsonProperty("MOVIECOLLECTOR")
    private String MOVIECOLLECTOR;

    @JsonProperty("ARTSANDANTIQUESANTIQUES")
    private String ARTSANDANTIQUESANTIQUES;

    @JsonProperty("ARTSANDANTIQUESART")
    private String ARTSANDANTIQUESART;

    @JsonProperty("THEATERPERFORMINGARTS")
    private String THEATERPERFORMINGARTS;

    @JsonProperty("COLLECTIBLESGENERAL")
    private String COLLECTIBLESGENERAL;

    @JsonProperty("COLLECTORAVID")
    private String COLLECTORAVID;

    @JsonProperty("COLLECTIBLESANDANTIQUESGROUPING")
    private String COLLECTIBLESANDANTIQUESGROUPING;

    @JsonProperty("LIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES")
    private String LIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES;

    @JsonProperty("SEWINGKNITTINGNEEDLEWORK")
    private String SEWINGKNITTINGNEEDLEWORK;

    @JsonProperty("CRAFTS")
    private String CRAFTS;

    @JsonProperty("GARDENING")
    private String GARDENING;

    @JsonProperty("GARDENING2")
    private String GARDENING2;

    @JsonProperty("GAMESBOARDGAMESPUZZLES")
    private String GAMESBOARDGAMESPUZZLES;

    @JsonProperty("HOMELIVING")
    private String HOMELIVING;

    @JsonProperty("DIYLIVING")
    private String DIYLIVING;

    @JsonProperty("SPORTYLIVING")
    private String SPORTYLIVING;

    @JsonProperty("HIGHBROW")
    private String HIGHBROW;

    @JsonProperty("COMMONLIVING")
    private String COMMONLIVING;

    @JsonProperty("PROFESSIONALLIVING")
    private String PROFESSIONALLIVING;

    @JsonProperty("BROADERLIVING")
    private String BROADERLIVING;

    @JsonProperty("SPECTATORSPORTSAUTOMOTORCYCLERACING")
    private String SPECTATORSPORTSAUTOMOTORCYCLERACING;

    @JsonProperty("RDI")
    private String RDI;

    @JsonProperty("HOMEPURCHASEPRICECODE")
    private String HOMEPURCHASEPRICECODE;

    @JsonProperty("MORTGAGEAMOUNTINTHOUSANDSCODE")
    private String MORTGAGEAMOUNTINTHOUSANDSCODE;

    @JsonProperty("REFINANCEAMOUNTINTHOUSANDSCODE")
    private String REFINANCEAMOUNTINTHOUSANDSCODE;

    @JsonProperty("MOSTRECENTMORTGAGEAMOUNT2ND")
    private String MOSTRECENTMORTGAGEAMOUNT2ND;

    @JsonProperty("PURCHASE2NDMORTGAGEAMOUNT")
    private String PURCHASE2NDMORTGAGEAMOUNT;

    @JsonProperty("MOSTRECENTMORTGAGEDATE2ND")
    private String MOSTRECENTMORTGAGEDATE2ND;

    @JsonProperty("MOSTRECENTMORTGAGE2NDLOANTYPECODE")
    private String MOSTRECENTMORTGAGE2NDLOANTYPECODE;

    @JsonProperty("PURCHASE2NDMORTGAGELOANTYPECODE")
    private String PURCHASE2NDMORTGAGELOANTYPECODE;

    @JsonProperty("MOSTRECENT2NDLENDERCODE")
    private String MOSTRECENT2NDLENDERCODE;

    @JsonProperty("PURCHASELENDERCODE")
    private String PURCHASELENDERCODE;

    @JsonProperty("MOSTRECENTLENDERNAME2ND")
    private String MOSTRECENTLENDERNAME2ND;


    @JsonProperty("MOSTRECENTMORTGAGE2NDINTERESTRATETYPE")
    private String MOSTRECENTMORTGAGE2NDINTERESTRATETYPE;

    @JsonProperty("PURCHASE2NDMORTGAGEINTERESTRATETYPE")
    private String PURCHASE2NDMORTGAGEINTERESTRATETYPE;

    @JsonProperty("MOSTRECENTMORTGAGE2NDINTERESTRATE")
    private String MOSTRECENTMORTGAGE2NDINTERESTRATE;

    @JsonProperty("PURCHASE2NDMORTGAGEINTERESTRATE")
    private String PURCHASE2NDMORTGAGEINTERESTRATE;

    @JsonProperty("NCOA_EFFECTIVE_DATE")
    private String NCOA_EFFECTIVE_DATE;

    @JsonProperty("DONOTCALL")
    private String DONOTCALL;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("Segment")
    private String Segment;

    @JsonProperty("SegmentID")
    private String SegmentID;

    @JsonProperty("UPDATE_SOURCE")
    private String UPDATE_SOURCE;

    @JsonProperty("web_src")
    private String web_src;

    @JsonProperty("reg_dt")
    private String reg_dt;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("EMAIL")
    private String email;

    private String website;

    @JsonProperty("URL")
    private String url;

    @JsonProperty("DATE")
    private String date;

    @JsonProperty("MAKE")
    private String make;

    @JsonProperty("MODEL")
    private String model;

    @JsonProperty("VIN")
    private String vin;

    @JsonProperty("YEAR")
    @JsonDeserialize(using = Consumer.NullableIntegerDeserializer.class)
    private Integer year;

    @JsonProperty("PHONE2")
    private String phone2;

    // END MOCK FIELDS

    // MOCK FIELDS GETTERS SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUPDATE_SOURCE() {
        return UPDATE_SOURCE;
    }

    public void setUPDATE_SOURCE(String UPDATE_SOURCE) {
        this.UPDATE_SOURCE = UPDATE_SOURCE;
    }

    public String getWeb_src() {
        return web_src;
    }

    public void setWeb_src(String web_src) {
        this.web_src = web_src;
    }

    public String getReg_dt() {
        return reg_dt;
    }

    public void setReg_dt(String reg_dt) {
        this.reg_dt = reg_dt;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSRUNIQUEKEY() {
        return SRUNIQUEKEY;
    }

    public void setSRUNIQUEKEY(String SRUNIQUEKEY) {
        this.SRUNIQUEKEY = SRUNIQUEKEY;
    }

    public String getADDRESSID() {
        return ADDRESSID;
    }

    public void setADDRESSID(String ADDRESSID) {
        this.ADDRESSID = ADDRESSID;
    }

    public String getINDIVIDUALID() {
        return INDIVIDUALID;
    }

    public void setINDIVIDUALID(String INDIVIDUALID) {
        this.INDIVIDUALID = INDIVIDUALID;
    }

    public String getPERSONSURNAMESUFFIX() {
        return PERSONSURNAMESUFFIX;
    }

    public void setPERSONSURNAMESUFFIX(String PERSONSURNAMESUFFIX) {
        this.PERSONSURNAMESUFFIX = PERSONSURNAMESUFFIX;
    }

    public String getPERSONTITLEOFRESPECT() {
        return PERSONTITLEOFRESPECT;
    }

    public void setPERSONTITLEOFRESPECT(String PERSONTITLEOFRESPECT) {
        this.PERSONTITLEOFRESPECT = PERSONTITLEOFRESPECT;
    }

    public String getHOUSENUMBER() {
        return HOUSENUMBER;
    }

    public void setHOUSENUMBER(String HOUSENUMBER) {
        this.HOUSENUMBER = HOUSENUMBER;
    }

    public String getPREDIRECTION() {
        return PREDIRECTION;
    }

    public void setPREDIRECTION(String PREDIRECTION) {
        this.PREDIRECTION = PREDIRECTION;
    }

    public String getSTREETNAME() {
        return STREETNAME;
    }

    public void setSTREETNAME(String STREETNAME) {
        this.STREETNAME = STREETNAME;
    }

    public String getSTREETSUFFIX() {
        return STREETSUFFIX;
    }

    public void setSTREETSUFFIX(String STREETSUFFIX) {
        this.STREETSUFFIX = STREETSUFFIX;
    }

    public String getPOSTDIRECTION() {
        return POSTDIRECTION;
    }

    public void setPOSTDIRECTION(String POSTDIRECTION) {
        this.POSTDIRECTION = POSTDIRECTION;
    }

    public String getUNITDESIGNATOR() {
        return UNITDESIGNATOR;
    }

    public void setUNITDESIGNATOR(String UNITDESIGNATOR) {
        this.UNITDESIGNATOR = UNITDESIGNATOR;
    }

    public String getUNITDESIGNATORNUMBER() {
        return UNITDESIGNATORNUMBER;
    }

    public void setUNITDESIGNATORNUMBER(String UNITDESIGNATORNUMBER) {
        this.UNITDESIGNATORNUMBER = UNITDESIGNATORNUMBER;
    }

    public String getDEL_POINT_CHECK_DIGIT() {
        return DEL_POINT_CHECK_DIGIT;
    }

    public void setDEL_POINT_CHECK_DIGIT(String DEL_POINT_CHECK_DIGIT) {
        this.DEL_POINT_CHECK_DIGIT = DEL_POINT_CHECK_DIGIT;
    }

    public String getMSA() {
        return MSA;
    }

    public void setMSA(String MSA) {
        this.MSA = MSA;
    }

    public String getCITYNAMEABBR() {
        return CITYNAMEABBR;
    }

    public void setCITYNAMEABBR(String CITYNAMEABBR) {
        this.CITYNAMEABBR = CITYNAMEABBR;
    }

    public String getCARRIER_ROUTE() {
        return CARRIER_ROUTE;
    }

    public void setCARRIER_ROUTE(String CARRIER_ROUTE) {
        this.CARRIER_ROUTE = CARRIER_ROUTE;
    }

    public String getCENSUSTRACT() {
        return CENSUSTRACT;
    }

    public void setCENSUSTRACT(String CENSUSTRACT) {
        this.CENSUSTRACT = CENSUSTRACT;
    }

    public String getCENSUSBLOCK() {
        return CENSUSBLOCK;
    }

    public void setCENSUSBLOCK(String CENSUSBLOCK) {
        this.CENSUSBLOCK = CENSUSBLOCK;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getTIMEZONE() {
        return TIMEZONE;
    }

    public void setTIMEZONE(String TIMEZONE) {
        this.TIMEZONE = TIMEZONE;
    }

    public String getXAXIS() {
        return XAXIS;
    }

    public void setXAXIS(String XAXIS) {
        this.XAXIS = XAXIS;
    }

    public String getYAXIS() {
        return YAXIS;
    }

    public void setYAXIS(String YAXIS) {
        this.YAXIS = YAXIS;
    }

    public String getZAXIS() {
        return ZAXIS;
    }

    public void setZAXIS(String ZAXIS) {
        this.ZAXIS = ZAXIS;
    }

    public String getDPV_CODE() {
        return DPV_CODE;
    }

    public void setDPV_CODE(String DPV_CODE) {
        this.DPV_CODE = DPV_CODE;
    }

    public Integer getNUMBEROFSOURCES() {
        return NUMBEROFSOURCES;
    }

    public void setNUMBEROFSOURCES(Integer NUMBEROFSOURCES) {
        this.NUMBEROFSOURCES = NUMBEROFSOURCES;
    }

    public String getSECONDARYADDRESSPRESENT() {
        return SECONDARYADDRESSPRESENT;
    }

    public void setSECONDARYADDRESSPRESENT(String SECONDARYADDRESSPRESENT) {
        this.SECONDARYADDRESSPRESENT = SECONDARYADDRESSPRESENT;
    }

    public String getLIVINGUNITID() {
        return LIVINGUNITID;
    }

    public void setLIVINGUNITID(String LIVINGUNITID) {
        this.LIVINGUNITID = LIVINGUNITID;
    }

    public String getRDID() {
        return RDID;
    }

    public void setRDID(String RDID) {
        this.RDID = RDID;
    }

    public String getLENGTHOFRESIDENCECODE() {
        return LENGTHOFRESIDENCECODE;
    }

    public void setLENGTHOFRESIDENCECODE(String LENGTHOFRESIDENCECODE) {
        this.LENGTHOFRESIDENCECODE = LENGTHOFRESIDENCECODE;
    }

    public String getCHILDRENAGE00_02() {
        return CHILDRENAGE00_02;
    }

    public void setCHILDRENAGE00_02(String CHILDRENAGE00_02) {
        this.CHILDRENAGE00_02 = CHILDRENAGE00_02;
    }

    public String getCHILDRENAGE00_02MALE() {
        return CHILDRENAGE00_02MALE;
    }

    public void setCHILDRENAGE00_02MALE(String CHILDRENAGE00_02MALE) {
        this.CHILDRENAGE00_02MALE = CHILDRENAGE00_02MALE;
    }

    public String getCHILDRENAGE00_02FEMALE() {
        return CHILDRENAGE00_02FEMALE;
    }

    public void setCHILDRENAGE00_02FEMALE(String CHILDRENAGE00_02FEMALE) {
        this.CHILDRENAGE00_02FEMALE = CHILDRENAGE00_02FEMALE;
    }

    public String getCHILDRENAGE00_02UNKNOWN() {
        return CHILDRENAGE00_02UNKNOWN;
    }

    public void setCHILDRENAGE00_02UNKNOWN(String CHILDRENAGE00_02UNKNOWN) {
        this.CHILDRENAGE00_02UNKNOWN = CHILDRENAGE00_02UNKNOWN;
    }

    public String getCHILDRENAGE03_05() {
        return CHILDRENAGE03_05;
    }

    public void setCHILDRENAGE03_05(String CHILDRENAGE03_05) {
        this.CHILDRENAGE03_05 = CHILDRENAGE03_05;
    }

    public String getCHILDRENAGE03_05MALE() {
        return CHILDRENAGE03_05MALE;
    }

    public void setCHILDRENAGE03_05MALE(String CHILDRENAGE03_05MALE) {
        this.CHILDRENAGE03_05MALE = CHILDRENAGE03_05MALE;
    }

    public String getCHILDRENAGE03_05FEMALE() {
        return CHILDRENAGE03_05FEMALE;
    }

    public void setCHILDRENAGE03_05FEMALE(String CHILDRENAGE03_05FEMALE) {
        this.CHILDRENAGE03_05FEMALE = CHILDRENAGE03_05FEMALE;
    }

    public String getCHILDRENAGE03_05UNKNOWN() {
        return CHILDRENAGE03_05UNKNOWN;
    }

    public void setCHILDRENAGE03_05UNKNOWN(String CHILDRENAGE03_05UNKNOWN) {
        this.CHILDRENAGE03_05UNKNOWN = CHILDRENAGE03_05UNKNOWN;
    }

    public String getCHILDRENAGE06_10() {
        return CHILDRENAGE06_10;
    }

    public void setCHILDRENAGE06_10(String CHILDRENAGE06_10) {
        this.CHILDRENAGE06_10 = CHILDRENAGE06_10;
    }

    public String getCHILDRENAGE06_10MALE() {
        return CHILDRENAGE06_10MALE;
    }

    public void setCHILDRENAGE06_10MALE(String CHILDRENAGE06_10MALE) {
        this.CHILDRENAGE06_10MALE = CHILDRENAGE06_10MALE;
    }

    public String getCHILDRENAGE06_10FEMALE() {
        return CHILDRENAGE06_10FEMALE;
    }

    public void setCHILDRENAGE06_10FEMALE(String CHILDRENAGE06_10FEMALE) {
        this.CHILDRENAGE06_10FEMALE = CHILDRENAGE06_10FEMALE;
    }

    public String getCHILDRENAGE06_10UNKNOWN() {
        return CHILDRENAGE06_10UNKNOWN;
    }

    public void setCHILDRENAGE06_10UNKNOWN(String CHILDRENAGE06_10UNKNOWN) {
        this.CHILDRENAGE06_10UNKNOWN = CHILDRENAGE06_10UNKNOWN;
    }

    public String getCHILDRENAGE11_15() {
        return CHILDRENAGE11_15;
    }

    public void setCHILDRENAGE11_15(String CHILDRENAGE11_15) {
        this.CHILDRENAGE11_15 = CHILDRENAGE11_15;
    }

    public String getCHILDRENAGE11_15MALE() {
        return CHILDRENAGE11_15MALE;
    }

    public void setCHILDRENAGE11_15MALE(String CHILDRENAGE11_15MALE) {
        this.CHILDRENAGE11_15MALE = CHILDRENAGE11_15MALE;
    }

    public String getCHILDRENAGE11_15FEMALE() {
        return CHILDRENAGE11_15FEMALE;
    }

    public void setCHILDRENAGE11_15FEMALE(String CHILDRENAGE11_15FEMALE) {
        this.CHILDRENAGE11_15FEMALE = CHILDRENAGE11_15FEMALE;
    }

    public String getCHILDRENAGE11_15UNKNOWN() {
        return CHILDRENAGE11_15UNKNOWN;
    }

    public void setCHILDRENAGE11_15UNKNOWN(String CHILDRENAGE11_15UNKNOWN) {
        this.CHILDRENAGE11_15UNKNOWN = CHILDRENAGE11_15UNKNOWN;
    }

    public String getCHILDRENAGE16_17() {
        return CHILDRENAGE16_17;
    }

    public void setCHILDRENAGE16_17(String CHILDRENAGE16_17) {
        this.CHILDRENAGE16_17 = CHILDRENAGE16_17;
    }

    public String getCHILDRENAGE16_17MALE() {
        return CHILDRENAGE16_17MALE;
    }

    public void setCHILDRENAGE16_17MALE(String CHILDRENAGE16_17MALE) {
        this.CHILDRENAGE16_17MALE = CHILDRENAGE16_17MALE;
    }

    public String getCHILDRENAGE16_17FEMALE() {
        return CHILDRENAGE16_17FEMALE;
    }

    public void setCHILDRENAGE16_17FEMALE(String CHILDRENAGE16_17FEMALE) {
        this.CHILDRENAGE16_17FEMALE = CHILDRENAGE16_17FEMALE;
    }

    public String getCHILDRENAGE16_17UNKNOWN() {
        return CHILDRENAGE16_17UNKNOWN;
    }

    public void setCHILDRENAGE16_17UNKNOWN(String CHILDRENAGE16_17UNKNOWN) {
        this.CHILDRENAGE16_17UNKNOWN = CHILDRENAGE16_17UNKNOWN;
    }

    public String getPERSONAGECODE() {
        return PERSONAGECODE;
    }

    public void setPERSONAGECODE(String PERSONAGECODE) {
        this.PERSONAGECODE = PERSONAGECODE;
    }

    public String getMALES_18_24() {
        return MALES_18_24;
    }

    public void setMALES_18_24(String MALES_18_24) {
        this.MALES_18_24 = MALES_18_24;
    }

    public String getFEMALES_18_24() {
        return FEMALES_18_24;
    }

    public void setFEMALES_18_24(String FEMALES_18_24) {
        this.FEMALES_18_24 = FEMALES_18_24;
    }

    public String getUNKNOWNGENDER_18_24() {
        return UNKNOWNGENDER_18_24;
    }

    public void setUNKNOWNGENDER_18_24(String UNKNOWNGENDER_18_24) {
        this.UNKNOWNGENDER_18_24 = UNKNOWNGENDER_18_24;
    }

    public String getMALES_25_34() {
        return MALES_25_34;
    }

    public void setMALES_25_34(String MALES_25_34) {
        this.MALES_25_34 = MALES_25_34;
    }

    public String getFEMALES_25_34() {
        return FEMALES_25_34;
    }

    public void setFEMALES_25_34(String FEMALES_25_34) {
        this.FEMALES_25_34 = FEMALES_25_34;
    }

    public String getUNKNOWNGENDER_25_34() {
        return UNKNOWNGENDER_25_34;
    }

    public void setUNKNOWNGENDER_25_34(String UNKNOWNGENDER_25_34) {
        this.UNKNOWNGENDER_25_34 = UNKNOWNGENDER_25_34;
    }

    public String getMALES_35_44() {
        return MALES_35_44;
    }

    public void setMALES_35_44(String MALES_35_44) {
        this.MALES_35_44 = MALES_35_44;
    }

    public String getFEMALES_35_44() {
        return FEMALES_35_44;
    }

    public void setFEMALES_35_44(String FEMALES_35_44) {
        this.FEMALES_35_44 = FEMALES_35_44;
    }

    public String getUNKNOWNGENDER_35_44() {
        return UNKNOWNGENDER_35_44;
    }

    public void setUNKNOWNGENDER_35_44(String UNKNOWNGENDER_35_44) {
        this.UNKNOWNGENDER_35_44 = UNKNOWNGENDER_35_44;
    }

    public String getMALES_45_54() {
        return MALES_45_54;
    }

    public void setMALES_45_54(String MALES_45_54) {
        this.MALES_45_54 = MALES_45_54;
    }

    public String getFEMALES_45_54() {
        return FEMALES_45_54;
    }

    public void setFEMALES_45_54(String FEMALES_45_54) {
        this.FEMALES_45_54 = FEMALES_45_54;
    }

    public String getUNKNOWNGENDER_45_54() {
        return UNKNOWNGENDER_45_54;
    }

    public void setUNKNOWNGENDER_45_54(String UNKNOWNGENDER_45_54) {
        this.UNKNOWNGENDER_45_54 = UNKNOWNGENDER_45_54;
    }

    public String getMALES_55_64() {
        return MALES_55_64;
    }

    public void setMALES_55_64(String MALES_55_64) {
        this.MALES_55_64 = MALES_55_64;
    }

    public String getFEMALES_55_64() {
        return FEMALES_55_64;
    }

    public void setFEMALES_55_64(String FEMALES_55_64) {
        this.FEMALES_55_64 = FEMALES_55_64;
    }

    public String getUNKNOWNGENDER_55_64() {
        return UNKNOWNGENDER_55_64;
    }

    public void setUNKNOWNGENDER_55_64(String UNKNOWNGENDER_55_64) {
        this.UNKNOWNGENDER_55_64 = UNKNOWNGENDER_55_64;
    }

    public String getMALES_65_74() {
        return MALES_65_74;
    }

    public void setMALES_65_74(String MALES_65_74) {
        this.MALES_65_74 = MALES_65_74;
    }

    public String getFEMALES_65_74() {
        return FEMALES_65_74;
    }

    public void setFEMALES_65_74(String FEMALES_65_74) {
        this.FEMALES_65_74 = FEMALES_65_74;
    }

    public String getUNKNOWNGENDER_65_74() {
        return UNKNOWNGENDER_65_74;
    }

    public void setUNKNOWNGENDER_65_74(String UNKNOWNGENDER_65_74) {
        this.UNKNOWNGENDER_65_74 = UNKNOWNGENDER_65_74;
    }

    public String getMALES_75_PLUS() {
        return MALES_75_PLUS;
    }

    public void setMALES_75_PLUS(String MALES_75_PLUS) {
        this.MALES_75_PLUS = MALES_75_PLUS;
    }

    public String getFEMALES_75_PLUS() {
        return FEMALES_75_PLUS;
    }

    public void setFEMALES_75_PLUS(String FEMALES_75_PLUS) {
        this.FEMALES_75_PLUS = FEMALES_75_PLUS;
    }

    public String getUNKNOWNGENDER_75_PLUS() {
        return UNKNOWNGENDER_75_PLUS;
    }

    public void setUNKNOWNGENDER_75_PLUS(String UNKNOWNGENDER_75_PLUS) {
        this.UNKNOWNGENDER_75_PLUS = UNKNOWNGENDER_75_PLUS;
    }

    public String getETHNICCONFIDENCECODE() {
        return ETHNICCONFIDENCECODE;
    }

    public void setETHNICCONFIDENCECODE(String ETHNICCONFIDENCECODE) {
        this.ETHNICCONFIDENCECODE = ETHNICCONFIDENCECODE;
    }

    public String getPRESENCEOFUPSCALERETAILCARD() {
        return PRESENCEOFUPSCALERETAILCARD;
    }

    public void setPRESENCEOFUPSCALERETAILCARD(String PRESENCEOFUPSCALERETAILCARD) {
        this.PRESENCEOFUPSCALERETAILCARD = PRESENCEOFUPSCALERETAILCARD;
    }

    public String getPRESENCEOFBANKCARD() {
        return PRESENCEOFBANKCARD;
    }

    public void setPRESENCEOFBANKCARD(String PRESENCEOFBANKCARD) {
        this.PRESENCEOFBANKCARD = PRESENCEOFBANKCARD;
    }

    public String getGASDEPTRETAILCARDHOLDER() {
        return GASDEPTRETAILCARDHOLDER;
    }

    public void setGASDEPTRETAILCARDHOLDER(String GASDEPTRETAILCARDHOLDER) {
        this.GASDEPTRETAILCARDHOLDER = GASDEPTRETAILCARDHOLDER;
    }

    public String getAMERICANEXPRESSCARD() {
        return AMERICANEXPRESSCARD;
    }

    public void setAMERICANEXPRESSCARD(String AMERICANEXPRESSCARD) {
        this.AMERICANEXPRESSCARD = AMERICANEXPRESSCARD;
    }

    public String getAMERICANEXPRESSGOLDPREMIUM() {
        return AMERICANEXPRESSGOLDPREMIUM;
    }

    public void setAMERICANEXPRESSGOLDPREMIUM(String AMERICANEXPRESSGOLDPREMIUM) {
        this.AMERICANEXPRESSGOLDPREMIUM = AMERICANEXPRESSGOLDPREMIUM;
    }

    public String getCarrier_name() {
        return carrier_name;
    }

    public void setCarrier_name(String carrier_name) {
        this.carrier_name = carrier_name;
    }
    public String getDISCOVERGOLDPREMIUM() {
        return DISCOVERGOLDPREMIUM;
    }

    public void setDISCOVERGOLDPREMIUM(String DISCOVERGOLDPREMIUM) {
        this.DISCOVERGOLDPREMIUM = DISCOVERGOLDPREMIUM;
    }

    public String getDISCOVERREGULAR() {
        return DISCOVERREGULAR;
    }

    public void setDISCOVERREGULAR(String DISCOVERREGULAR) {
        this.DISCOVERREGULAR = DISCOVERREGULAR;
    }

    public String getGASOLINEORRETAILCARDGOLDPREMIUM() {
        return GASOLINEORRETAILCARDGOLDPREMIUM;
    }

    public void setGASOLINEORRETAILCARDGOLDPREMIUM(String GASOLINEORRETAILCARDGOLDPREMIUM) {
        this.GASOLINEORRETAILCARDGOLDPREMIUM = GASOLINEORRETAILCARDGOLDPREMIUM;
    }

    public String getGASOLINEORRETAILCARDREGULAR() {
        return GASOLINEORRETAILCARDREGULAR;
    }

    public void setGASOLINEORRETAILCARDREGULAR(String GASOLINEORRETAILCARDREGULAR) {
        this.GASOLINEORRETAILCARDREGULAR = GASOLINEORRETAILCARDREGULAR;
    }

    public String getMASTERCARDGOLDPREMIUM() {
        return MASTERCARDGOLDPREMIUM;
    }

    public void setMASTERCARDGOLDPREMIUM(String MASTERCARDGOLDPREMIUM) {
        this.MASTERCARDGOLDPREMIUM = MASTERCARDGOLDPREMIUM;
    }

    public String getMASTERCARDREGULAR() {
        return MASTERCARDREGULAR;
    }

    public void setMASTERCARDREGULAR(String MASTERCARDREGULAR) {
        this.MASTERCARDREGULAR = MASTERCARDREGULAR;
    }

    public String getVISAGOLDPREMIUM() {
        return VISAGOLDPREMIUM;
    }

    public void setVISAGOLDPREMIUM(String VISAGOLDPREMIUM) {
        this.VISAGOLDPREMIUM = VISAGOLDPREMIUM;
    }

    public String getVISAREGULAR() {
        return VISAREGULAR;
    }

    public void setVISAREGULAR(String VISAREGULAR) {
        this.VISAREGULAR = VISAREGULAR;
    }

    public String getCREDITCARDINDICATOR() {
        return CREDITCARDINDICATOR;
    }

    public void setCREDITCARDINDICATOR(String CREDITCARDINDICATOR) {
        this.CREDITCARDINDICATOR = CREDITCARDINDICATOR;
    }

    public String getBANKCARDHOLDER() {
        return BANKCARDHOLDER;
    }

    public void setBANKCARDHOLDER(String BANKCARDHOLDER) {
        this.BANKCARDHOLDER = BANKCARDHOLDER;
    }

    public String getGASDEPARTMENTRETAILCARDHOLDER() {
        return GASDEPARTMENTRETAILCARDHOLDER;
    }

    public void setGASDEPARTMENTRETAILCARDHOLDER(String GASDEPARTMENTRETAILCARDHOLDER) {
        this.GASDEPARTMENTRETAILCARDHOLDER = GASDEPARTMENTRETAILCARDHOLDER;
    }

    public String getCREDITCARDHOLDERUNKNOWNTYPE() {
        return CREDITCARDHOLDERUNKNOWNTYPE;
    }

    public void setCREDITCARDHOLDERUNKNOWNTYPE(String CREDITCARDHOLDERUNKNOWNTYPE) {
        this.CREDITCARDHOLDERUNKNOWNTYPE = CREDITCARDHOLDERUNKNOWNTYPE;
    }

    public String getPREMIUMCARDHOLDER() {
        return PREMIUMCARDHOLDER;
    }

    public void setPREMIUMCARDHOLDER(String PREMIUMCARDHOLDER) {
        this.PREMIUMCARDHOLDER = PREMIUMCARDHOLDER;
    }

    public String getUPSCALEDEPARTMENTSTORECARDHOLDER() {
        return UPSCALEDEPARTMENTSTORECARDHOLDER;
    }

    public void setUPSCALEDEPARTMENTSTORECARDHOLDER(String UPSCALEDEPARTMENTSTORECARDHOLDER) {
        this.UPSCALEDEPARTMENTSTORECARDHOLDER = UPSCALEDEPARTMENTSTORECARDHOLDER;
    }

    public String getBANKCARDPRESENCEINHOUSEHOLD() {
        return BANKCARDPRESENCEINHOUSEHOLD;
    }

    public void setBANKCARDPRESENCEINHOUSEHOLD(String BANKCARDPRESENCEINHOUSEHOLD) {
        this.BANKCARDPRESENCEINHOUSEHOLD = BANKCARDPRESENCEINHOUSEHOLD;
    }

    public String getASSIMILATIONCODES() {
        return ASSIMILATIONCODES;
    }

    public void setASSIMILATIONCODES(String ASSIMILATIONCODES) {
        this.ASSIMILATIONCODES = ASSIMILATIONCODES;
    }

    public String getMEMBERSHIPCLUB() {
        return MEMBERSHIPCLUB;
    }

    public void setMEMBERSHIPCLUB(String MEMBERSHIPCLUB) {
        this.MEMBERSHIPCLUB = MEMBERSHIPCLUB;
    }

    public String getRESPONDEDTOCATALOG() {
        return RESPONDEDTOCATALOG;
    }

    public void setRESPONDEDTOCATALOG(String RESPONDEDTOCATALOG) {
        this.RESPONDEDTOCATALOG = RESPONDEDTOCATALOG;
    }

    public String getHEALTH_INSTITUTIONCONTRIBUTOR() {
        return HEALTH_INSTITUTIONCONTRIBUTOR;
    }

    public void setHEALTH_INSTITUTIONCONTRIBUTOR(String HEALTH_INSTITUTIONCONTRIBUTOR) {
        this.HEALTH_INSTITUTIONCONTRIBUTOR = HEALTH_INSTITUTIONCONTRIBUTOR;
    }

    public String getGENERALCONTRIBUTOR() {
        return GENERALCONTRIBUTOR;
    }

    public void setGENERALCONTRIBUTOR(String GENERALCONTRIBUTOR) {
        this.GENERALCONTRIBUTOR = GENERALCONTRIBUTOR;
    }

    public String getDONATESBYMAIL() {
        return DONATESBYMAIL;
    }

    public void setDONATESBYMAIL(String DONATESBYMAIL) {
        this.DONATESBYMAIL = DONATESBYMAIL;
    }

    public String getHEAVYBUSINESSTRAVELERS() {
        return HEAVYBUSINESSTRAVELERS;
    }

    public void setHEAVYBUSINESSTRAVELERS(String HEAVYBUSINESSTRAVELERS) {
        this.HEAVYBUSINESSTRAVELERS = HEAVYBUSINESSTRAVELERS;
    }

    public String getAPPARELWOMENSPETITE() {
        return APPARELWOMENSPETITE;
    }

    public void setAPPARELWOMENSPETITE(String APPARELWOMENSPETITE) {
        this.APPARELWOMENSPETITE = APPARELWOMENSPETITE;
    }

    public String getAPPARELWOMENSPLUSSIZES() {
        return APPARELWOMENSPLUSSIZES;
    }

    public void setAPPARELWOMENSPLUSSIZES(String APPARELWOMENSPLUSSIZES) {
        this.APPARELWOMENSPLUSSIZES = APPARELWOMENSPLUSSIZES;
    }

    public String getCOMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY() {
        return COMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY;
    }

    public void setCOMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY(String COMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY) {
        this.COMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY = COMMUNITYINVOLVEMENTCAUSESSUPPORTEDFINANCIALLY;
    }

    public String getANIMALWELFARECHARITABLEDONATION() {
        return ANIMALWELFARECHARITABLEDONATION;
    }

    public void setANIMALWELFARECHARITABLEDONATION(String ANIMALWELFARECHARITABLEDONATION) {
        this.ANIMALWELFARECHARITABLEDONATION = ANIMALWELFARECHARITABLEDONATION;
    }

    public String getARTSORCULTURALCHARITABLEDONATION() {
        return ARTSORCULTURALCHARITABLEDONATION;
    }

    public void setARTSORCULTURALCHARITABLEDONATION(String ARTSORCULTURALCHARITABLEDONATION) {
        this.ARTSORCULTURALCHARITABLEDONATION = ARTSORCULTURALCHARITABLEDONATION;
    }

    public String getCHILDRENSCHARITABLEDONATION() {
        return CHILDRENSCHARITABLEDONATION;
    }

    public void setCHILDRENSCHARITABLEDONATION(String CHILDRENSCHARITABLEDONATION) {
        this.CHILDRENSCHARITABLEDONATION = CHILDRENSCHARITABLEDONATION;
    }

    public String getENVIRONMENTORWILDLIFECHARITABLEDONATION() {
        return ENVIRONMENTORWILDLIFECHARITABLEDONATION;
    }

    public void setENVIRONMENTORWILDLIFECHARITABLEDONATION(String ENVIRONMENTORWILDLIFECHARITABLEDONATION) {
        this.ENVIRONMENTORWILDLIFECHARITABLEDONATION = ENVIRONMENTORWILDLIFECHARITABLEDONATION;
    }

    public String getENVIRONMENTALISSUESCHARITABLEDONATION() {
        return ENVIRONMENTALISSUESCHARITABLEDONATION;
    }

    public void setENVIRONMENTALISSUESCHARITABLEDONATION(String ENVIRONMENTALISSUESCHARITABLEDONATION) {
        this.ENVIRONMENTALISSUESCHARITABLEDONATION = ENVIRONMENTALISSUESCHARITABLEDONATION;
    }

    public String getINTERNATIONALAIDCHARITABLEDONATION() {
        return INTERNATIONALAIDCHARITABLEDONATION;
    }

    public void setINTERNATIONALAIDCHARITABLEDONATION(String INTERNATIONALAIDCHARITABLEDONATION) {
        this.INTERNATIONALAIDCHARITABLEDONATION = INTERNATIONALAIDCHARITABLEDONATION;
    }

    public String getPOLITICALCHARITABLEDONATION() {
        return POLITICALCHARITABLEDONATION;
    }

    public void setPOLITICALCHARITABLEDONATION(String POLITICALCHARITABLEDONATION) {
        this.POLITICALCHARITABLEDONATION = POLITICALCHARITABLEDONATION;
    }

    public String getCHARITABLEDONATIONS_OTHER() {
        return CHARITABLEDONATIONS_OTHER;
    }

    public void setCHARITABLEDONATIONS_OTHER(String CHARITABLEDONATIONS_OTHER) {
        this.CHARITABLEDONATIONS_OTHER = CHARITABLEDONATIONS_OTHER;
    }

    public String getCOMMUNITYCHARITIES() {
        return COMMUNITYCHARITIES;
    }

    public void setCOMMUNITYCHARITIES(String COMMUNITYCHARITIES) {
        this.COMMUNITYCHARITIES = COMMUNITYCHARITIES;
    }

    public String getPARENTING() {
        return PARENTING;
    }

    public void setPARENTING(String PARENTING) {
        this.PARENTING = PARENTING;
    }

    public String getCHILDRENSAPPARELINFANTSANDTODDLERS() {
        return CHILDRENSAPPARELINFANTSANDTODDLERS;
    }

    public void setCHILDRENSAPPARELINFANTSANDTODDLERS(String CHILDRENSAPPARELINFANTSANDTODDLERS) {
        this.CHILDRENSAPPARELINFANTSANDTODDLERS = CHILDRENSAPPARELINFANTSANDTODDLERS;
    }

    public String getCHILDRENSLEARNINGANDACTIVITYTOYS() {
        return CHILDRENSLEARNINGANDACTIVITYTOYS;
    }

    public void setCHILDRENSLEARNINGANDACTIVITYTOYS(String CHILDRENSLEARNINGANDACTIVITYTOYS) {
        this.CHILDRENSLEARNINGANDACTIVITYTOYS = CHILDRENSLEARNINGANDACTIVITYTOYS;
    }

    public String getCHILDRENSPRODUCTSGENERALBABYCARE() {
        return CHILDRENSPRODUCTSGENERALBABYCARE;
    }

    public void setCHILDRENSPRODUCTSGENERALBABYCARE(String CHILDRENSPRODUCTSGENERALBABYCARE) {
        this.CHILDRENSPRODUCTSGENERALBABYCARE = CHILDRENSPRODUCTSGENERALBABYCARE;
    }

    public String getCHILDRENSPRODUCTSGENERALBACKTOSCHOOL() {
        return CHILDRENSPRODUCTSGENERALBACKTOSCHOOL;
    }

    public void setCHILDRENSPRODUCTSGENERALBACKTOSCHOOL(String CHILDRENSPRODUCTSGENERALBACKTOSCHOOL) {
        this.CHILDRENSPRODUCTSGENERALBACKTOSCHOOL = CHILDRENSPRODUCTSGENERALBACKTOSCHOOL;
    }

    public String getCHILDRENSPRODUCTSGENERAL() {
        return CHILDRENSPRODUCTSGENERAL;
    }

    public void setCHILDRENSPRODUCTSGENERAL(String CHILDRENSPRODUCTSGENERAL) {
        this.CHILDRENSPRODUCTSGENERAL = CHILDRENSPRODUCTSGENERAL;
    }

    public String getYOUNGADULTINHOUSEHOLD() {
        return YOUNGADULTINHOUSEHOLD;
    }

    public void setYOUNGADULTINHOUSEHOLD(String YOUNGADULTINHOUSEHOLD) {
        this.YOUNGADULTINHOUSEHOLD = YOUNGADULTINHOUSEHOLD;
    }

    public String getCHILDRENSINTERESTS() {
        return CHILDRENSINTERESTS;
    }

    public void setCHILDRENSINTERESTS(String CHILDRENSINTERESTS) {
        this.CHILDRENSINTERESTS = CHILDRENSINTERESTS;
    }

    public String getGRANDCHILDREN() {
        return GRANDCHILDREN;
    }

    public void setGRANDCHILDREN(String GRANDCHILDREN) {
        this.GRANDCHILDREN = GRANDCHILDREN;
    }

    public String getCHRISTIANFAMILIES() {
        return CHRISTIANFAMILIES;
    }

    public void setCHRISTIANFAMILIES(String CHRISTIANFAMILIES) {
        this.CHRISTIANFAMILIES = CHRISTIANFAMILIES;
    }

    public String getOTHERPETOWNER() {
        return OTHERPETOWNER;
    }

    public void setOTHERPETOWNER(String OTHERPETOWNER) {
        this.OTHERPETOWNER = OTHERPETOWNER;
    }

    public String getBOOKSANDMAGAZINESMAGAZINES() {
        return BOOKSANDMAGAZINESMAGAZINES;
    }

    public void setBOOKSANDMAGAZINESMAGAZINES(String BOOKSANDMAGAZINESMAGAZINES) {
        this.BOOKSANDMAGAZINESMAGAZINES = BOOKSANDMAGAZINESMAGAZINES;
    }

    public String getBOOKSANDMUSICBOOKS() {
        return BOOKSANDMUSICBOOKS;
    }

    public void setBOOKSANDMUSICBOOKS(String BOOKSANDMUSICBOOKS) {
        this.BOOKSANDMUSICBOOKS = BOOKSANDMUSICBOOKS;
    }

    public String getBOOKSANDMUSICBOOKSAUDIO() {
        return BOOKSANDMUSICBOOKSAUDIO;
    }

    public void setBOOKSANDMUSICBOOKSAUDIO(String BOOKSANDMUSICBOOKSAUDIO) {
        this.BOOKSANDMUSICBOOKSAUDIO = BOOKSANDMUSICBOOKSAUDIO;
    }

    public String getREADINGGENERAL() {
        return READINGGENERAL;
    }

    public void setREADINGGENERAL(String READINGGENERAL) {
        this.READINGGENERAL = READINGGENERAL;
    }

    public String getREADINGRELIGIOUSINSPIRATIONAL() {
        return READINGRELIGIOUSINSPIRATIONAL;
    }

    public void setREADINGRELIGIOUSINSPIRATIONAL(String READINGRELIGIOUSINSPIRATIONAL) {
        this.READINGRELIGIOUSINSPIRATIONAL = READINGRELIGIOUSINSPIRATIONAL;
    }

    public String getREADINGMAGAZINES() {
        return READINGMAGAZINES;
    }

    public void setREADINGMAGAZINES(String READINGMAGAZINES) {
        this.READINGMAGAZINES = READINGMAGAZINES;
    }

    public String getREADINGGROUPING() {
        return READINGGROUPING;
    }

    public void setREADINGGROUPING(String READINGGROUPING) {
        this.READINGGROUPING = READINGGROUPING;
    }

    public String getRELIGIOUSINSPIRATIONAL() {
        return RELIGIOUSINSPIRATIONAL;
    }

    public void setRELIGIOUSINSPIRATIONAL(String RELIGIOUSINSPIRATIONAL) {
        this.RELIGIOUSINSPIRATIONAL = RELIGIOUSINSPIRATIONAL;
    }

    public String getMAGAZINES() {
        return MAGAZINES;
    }

    public void setMAGAZINES(String MAGAZINES) {
        this.MAGAZINES = MAGAZINES;
    }

    public String getDVDSVIDEOS() {
        return DVDSVIDEOS;
    }

    public void setDVDSVIDEOS(String DVDSVIDEOS) {
        this.DVDSVIDEOS = DVDSVIDEOS;
    }

    public String getELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER() {
        return ELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER;
    }

    public void setELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER(String ELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER) {
        this.ELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER = ELECTRONICSANDCOMPUTINGTVVIDEOMOVIEWATCHER;
    }

    public String getMUSICHOMESTEREO() {
        return MUSICHOMESTEREO;
    }

    public void setMUSICHOMESTEREO(String MUSICHOMESTEREO) {
        this.MUSICHOMESTEREO = MUSICHOMESTEREO;
    }

    public String getMUSICPLAYER() {
        return MUSICPLAYER;
    }

    public void setMUSICPLAYER(String MUSICPLAYER) {
        this.MUSICPLAYER = MUSICPLAYER;
    }

    public String getMUSICCOLLECTOR() {
        return MUSICCOLLECTOR;
    }

    public void setMUSICCOLLECTOR(String MUSICCOLLECTOR) {
        this.MUSICCOLLECTOR = MUSICCOLLECTOR;
    }

    public String getMUSICAVIDLISTENER() {
        return MUSICAVIDLISTENER;
    }

    public void setMUSICAVIDLISTENER(String MUSICAVIDLISTENER) {
        this.MUSICAVIDLISTENER = MUSICAVIDLISTENER;
    }

    public String getMOVIECOLLECTOR() {
        return MOVIECOLLECTOR;
    }

    public void setMOVIECOLLECTOR(String MOVIECOLLECTOR) {
        this.MOVIECOLLECTOR = MOVIECOLLECTOR;
    }

    public String getARTSANDANTIQUESANTIQUES() {
        return ARTSANDANTIQUESANTIQUES;
    }

    public void setARTSANDANTIQUESANTIQUES(String ARTSANDANTIQUESANTIQUES) {
        this.ARTSANDANTIQUESANTIQUES = ARTSANDANTIQUESANTIQUES;
    }

    public String getARTSANDANTIQUESART() {
        return ARTSANDANTIQUESART;
    }

    public void setARTSANDANTIQUESART(String ARTSANDANTIQUESART) {
        this.ARTSANDANTIQUESART = ARTSANDANTIQUESART;
    }

    public String getTHEATERPERFORMINGARTS() {
        return THEATERPERFORMINGARTS;
    }

    public void setTHEATERPERFORMINGARTS(String THEATERPERFORMINGARTS) {
        this.THEATERPERFORMINGARTS = THEATERPERFORMINGARTS;
    }

    public String getCOLLECTIBLESGENERAL() {
        return COLLECTIBLESGENERAL;
    }

    public void setCOLLECTIBLESGENERAL(String COLLECTIBLESGENERAL) {
        this.COLLECTIBLESGENERAL = COLLECTIBLESGENERAL;
    }

    public String getCOLLECTORAVID() {
        return COLLECTORAVID;
    }

    public void setCOLLECTORAVID(String COLLECTORAVID) {
        this.COLLECTORAVID = COLLECTORAVID;
    }

    public String getCOLLECTIBLESANDANTIQUESGROUPING() {
        return COLLECTIBLESANDANTIQUESGROUPING;
    }

    public void setCOLLECTIBLESANDANTIQUESGROUPING(String COLLECTIBLESANDANTIQUESGROUPING) {
        this.COLLECTIBLESANDANTIQUESGROUPING = COLLECTIBLESANDANTIQUESGROUPING;
    }

    public String getLIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES() {
        return LIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES;
    }

    public void setLIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES(String LIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES) {
        this.LIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES = LIFESTYLESINTERESTSANDPASSIONSCOLLECTIBLES;
    }

    public String getSEWINGKNITTINGNEEDLEWORK() {
        return SEWINGKNITTINGNEEDLEWORK;
    }

    public void setSEWINGKNITTINGNEEDLEWORK(String SEWINGKNITTINGNEEDLEWORK) {
        this.SEWINGKNITTINGNEEDLEWORK = SEWINGKNITTINGNEEDLEWORK;
    }

    public String getCRAFTS() {
        return CRAFTS;
    }

    public void setCRAFTS(String CRAFTS) {
        this.CRAFTS = CRAFTS;
    }

    public String getGARDENING() {
        return GARDENING;
    }

    public void setGARDENING(String GARDENING) {
        this.GARDENING = GARDENING;
    }

    public String getGARDENING2() {
        return GARDENING2;
    }

    public void setGARDENING2(String GARDENING2) {
        this.GARDENING2 = GARDENING2;
    }

    public String getGAMESBOARDGAMESPUZZLES() {
        return GAMESBOARDGAMESPUZZLES;
    }

    public void setGAMESBOARDGAMESPUZZLES(String GAMESBOARDGAMESPUZZLES) {
        this.GAMESBOARDGAMESPUZZLES = GAMESBOARDGAMESPUZZLES;
    }

    public String getHOMELIVING() {
        return HOMELIVING;
    }

    public void setHOMELIVING(String HOMELIVING) {
        this.HOMELIVING = HOMELIVING;
    }

    public String getDIYLIVING() {
        return DIYLIVING;
    }

    public void setDIYLIVING(String DIYLIVING) {
        this.DIYLIVING = DIYLIVING;
    }

    public String getSPORTYLIVING() {
        return SPORTYLIVING;
    }

    public void setSPORTYLIVING(String SPORTYLIVING) {
        this.SPORTYLIVING = SPORTYLIVING;
    }

    public String getHIGHBROW() {
        return HIGHBROW;
    }

    public void setHIGHBROW(String HIGHBROW) {
        this.HIGHBROW = HIGHBROW;
    }

    public String getCOMMONLIVING() {
        return COMMONLIVING;
    }

    public void setCOMMONLIVING(String COMMONLIVING) {
        this.COMMONLIVING = COMMONLIVING;
    }

    public String getPROFESSIONALLIVING() {
        return PROFESSIONALLIVING;
    }

    public void setPROFESSIONALLIVING(String PROFESSIONALLIVING) {
        this.PROFESSIONALLIVING = PROFESSIONALLIVING;
    }

    public String getBROADERLIVING() {
        return BROADERLIVING;
    }

    public void setBROADERLIVING(String BROADERLIVING) {
        this.BROADERLIVING = BROADERLIVING;
    }

    public String getSPECTATORSPORTSAUTOMOTORCYCLERACING() {
        return SPECTATORSPORTSAUTOMOTORCYCLERACING;
    }

    public void setSPECTATORSPORTSAUTOMOTORCYCLERACING(String SPECTATORSPORTSAUTOMOTORCYCLERACING) {
        this.SPECTATORSPORTSAUTOMOTORCYCLERACING = SPECTATORSPORTSAUTOMOTORCYCLERACING;
    }

    public String getRDI() {
        return RDI;
    }

    public void setRDI(String RDI) {
        this.RDI = RDI;
    }

    public String getHOMEPURCHASEPRICECODE() {
        return HOMEPURCHASEPRICECODE;
    }

    public void setHOMEPURCHASEPRICECODE(String HOMEPURCHASEPRICECODE) {
        this.HOMEPURCHASEPRICECODE = HOMEPURCHASEPRICECODE;
    }

    public String getMORTGAGEAMOUNTINTHOUSANDSCODE() {
        return MORTGAGEAMOUNTINTHOUSANDSCODE;
    }

    public void setMORTGAGEAMOUNTINTHOUSANDSCODE(String MORTGAGEAMOUNTINTHOUSANDSCODE) {
        this.MORTGAGEAMOUNTINTHOUSANDSCODE = MORTGAGEAMOUNTINTHOUSANDSCODE;
    }

    public String getREFINANCEAMOUNTINTHOUSANDSCODE() {
        return REFINANCEAMOUNTINTHOUSANDSCODE;
    }

    public void setREFINANCEAMOUNTINTHOUSANDSCODE(String REFINANCEAMOUNTINTHOUSANDSCODE) {
        this.REFINANCEAMOUNTINTHOUSANDSCODE = REFINANCEAMOUNTINTHOUSANDSCODE;
    }

    public String getMOSTRECENTMORTGAGEAMOUNT2ND() {
        return MOSTRECENTMORTGAGEAMOUNT2ND;
    }

    public void setMOSTRECENTMORTGAGEAMOUNT2ND(String MOSTRECENTMORTGAGEAMOUNT2ND) {
        this.MOSTRECENTMORTGAGEAMOUNT2ND = MOSTRECENTMORTGAGEAMOUNT2ND;
    }

    public String getPURCHASE2NDMORTGAGEAMOUNT() {
        return PURCHASE2NDMORTGAGEAMOUNT;
    }

    public void setPURCHASE2NDMORTGAGEAMOUNT(String PURCHASE2NDMORTGAGEAMOUNT) {
        this.PURCHASE2NDMORTGAGEAMOUNT = PURCHASE2NDMORTGAGEAMOUNT;
    }

    public String getMOSTRECENTMORTGAGEDATE2ND() {
        return MOSTRECENTMORTGAGEDATE2ND;
    }

    public void setMOSTRECENTMORTGAGEDATE2ND(String MOSTRECENTMORTGAGEDATE2ND) {
        this.MOSTRECENTMORTGAGEDATE2ND = MOSTRECENTMORTGAGEDATE2ND;
    }

    public String getMOSTRECENTMORTGAGE2NDLOANTYPECODE() {
        return MOSTRECENTMORTGAGE2NDLOANTYPECODE;
    }

    public void setMOSTRECENTMORTGAGE2NDLOANTYPECODE(String MOSTRECENTMORTGAGE2NDLOANTYPECODE) {
        this.MOSTRECENTMORTGAGE2NDLOANTYPECODE = MOSTRECENTMORTGAGE2NDLOANTYPECODE;
    }

    public String getPURCHASE2NDMORTGAGELOANTYPECODE() {
        return PURCHASE2NDMORTGAGELOANTYPECODE;
    }

    public void setPURCHASE2NDMORTGAGELOANTYPECODE(String PURCHASE2NDMORTGAGELOANTYPECODE) {
        this.PURCHASE2NDMORTGAGELOANTYPECODE = PURCHASE2NDMORTGAGELOANTYPECODE;
    }

    public String getMOSTRECENT2NDLENDERCODE() {
        return MOSTRECENT2NDLENDERCODE;
    }

    public void setMOSTRECENT2NDLENDERCODE(String MOSTRECENT2NDLENDERCODE) {
        this.MOSTRECENT2NDLENDERCODE = MOSTRECENT2NDLENDERCODE;
    }

    public String getPURCHASELENDERCODE() {
        return PURCHASELENDERCODE;
    }

    public void setPURCHASELENDERCODE(String PURCHASELENDERCODE) {
        this.PURCHASELENDERCODE = PURCHASELENDERCODE;
    }

    public String getMOSTRECENTLENDERNAME2ND() {
        return MOSTRECENTLENDERNAME2ND;
    }

    public void setMOSTRECENTLENDERNAME2ND(String MOSTRECENTLENDERNAME2ND) {
        this.MOSTRECENTLENDERNAME2ND = MOSTRECENTLENDERNAME2ND;
    }

    public String getMOSTRECENTMORTGAGE2NDINTERESTRATETYPE() {
        return MOSTRECENTMORTGAGE2NDINTERESTRATETYPE;
    }

    public void setMOSTRECENTMORTGAGE2NDINTERESTRATETYPE(String MOSTRECENTMORTGAGE2NDINTERESTRATETYPE) {
        this.MOSTRECENTMORTGAGE2NDINTERESTRATETYPE = MOSTRECENTMORTGAGE2NDINTERESTRATETYPE;
    }

    public String getPURCHASE2NDMORTGAGEINTERESTRATETYPE() {
        return PURCHASE2NDMORTGAGEINTERESTRATETYPE;
    }

    public void setPURCHASE2NDMORTGAGEINTERESTRATETYPE(String PURCHASE2NDMORTGAGEINTERESTRATETYPE) {
        this.PURCHASE2NDMORTGAGEINTERESTRATETYPE = PURCHASE2NDMORTGAGEINTERESTRATETYPE;
    }

    public String getMOSTRECENTMORTGAGE2NDINTERESTRATE() {
        return MOSTRECENTMORTGAGE2NDINTERESTRATE;
    }

    public void setMOSTRECENTMORTGAGE2NDINTERESTRATE(String MOSTRECENTMORTGAGE2NDINTERESTRATE) {
        this.MOSTRECENTMORTGAGE2NDINTERESTRATE = MOSTRECENTMORTGAGE2NDINTERESTRATE;
    }

    public String getPURCHASE2NDMORTGAGEINTERESTRATE() {
        return PURCHASE2NDMORTGAGEINTERESTRATE;
    }

    public void setPURCHASE2NDMORTGAGEINTERESTRATE(String PURCHASE2NDMORTGAGEINTERESTRATE) {
        this.PURCHASE2NDMORTGAGEINTERESTRATE = PURCHASE2NDMORTGAGEINTERESTRATE;
    }

    public String getNCOA_EFFECTIVE_DATE() {
        return NCOA_EFFECTIVE_DATE;
    }

    public void setNCOA_EFFECTIVE_DATE(String NCOA_EFFECTIVE_DATE) {
        this.NCOA_EFFECTIVE_DATE = NCOA_EFFECTIVE_DATE;
    }

    public String getDONOTCALL() {
        return DONOTCALL;
    }

    public void setDONOTCALL(String DONOTCALL) {
        this.DONOTCALL = DONOTCALL;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSegment() {
        return Segment;
    }

    public void setSegment(String segment) {
        Segment = segment;
    }

    public String getSegmentID() {
        return SegmentID;
    }

    public void setSegmentID(String segmentID) {
        SegmentID = segmentID;
    }

    // MOCK FIELDS GETTERS SETTERS

    public Consumer2() {}

    public String getPERSONFIRSTNAME() {
        return PERSONFIRSTNAME != null ? PERSONFIRSTNAME.trim() : null;
    }

    public void setPERSONFIRSTNAME(String PERSONFIRSTNAME) {
        if (PERSONFIRSTNAME != null) {
            this.PERSONFIRSTNAME = PERSONFIRSTNAME.trim();
        }
    }

    public String getPERSONMIDDLEINITIAL() {
        return PERSONMIDDLEINITIAL != null ? PERSONMIDDLEINITIAL.trim() : null;
    }

    public void setPERSONMIDDLEINITIAL(String PERSONMIDDLEINITIAL) {
        if (PERSONMIDDLEINITIAL != null) {
            this.PERSONMIDDLEINITIAL = PERSONMIDDLEINITIAL.trim();
        }
    }

    public String getPERSONLASTNAME() {
        return PERSONLASTNAME != null ? PERSONLASTNAME.trim() : null;
    }

    public void setPERSONLASTNAME(String PERSONLASTNAME) {
        if (PERSONLASTNAME != null) {
            this.PERSONLASTNAME = PERSONLASTNAME.trim();
        }
    }
    public String getWireless() {
        return wireless;
    }

    public void setWireless(String wireless) {
        this.wireless = wireless;
    }
    public String getPRIMARYADDRESS() {
        return PRIMARYADDRESS;
    }

    public void setPRIMARYADDRESS(String PRIMARYADDRESS) {
        this.PRIMARYADDRESS = PRIMARYADDRESS;
    }

    public String getSECONDARYADDRESS() {
        return SECONDARYADDRESS;
    }

    public void setSECONDARYADDRESS(String SECONDARYADDRESS) {
        this.SECONDARYADDRESS = SECONDARYADDRESS;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public Integer getST_CODE() {
        if ( STATE != null ) {
            for (int i = 0; i < Application.STATE_CODES.length; i++ ) {
                if ( STATE.equals( Application.STATE_CODES[ i ] ) ) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setST_CODE(Integer ST_CODE) {
        this.ST_CODE = ST_CODE;
    }

    public Integer getZIPCODE() {
        return ZIPCODE;
    }

    public void setZIPCODE(Integer ZIPCODE) {
        this.ZIPCODE = ZIPCODE;
    }

    public Integer getZIP_4() {
        return ZIP_4;
    }

    public void setZIP_4(Integer ZIP_4) {
        this.ZIP_4 = ZIP_4;
    }

    public Integer getCOUNTYCODE() {
        return COUNTYCODE;
    }

    public void setCOUNTYCODE(Integer COUNTYCODE) {
        this.COUNTYCODE = COUNTYCODE;
    }

    public String getCOUNTYNAME() {
        return COUNTYNAME;
    }

    public void setCOUNTYNAME(String COUNTYNAME) {
        this.COUNTYNAME = COUNTYNAME;
    }

    public String getCITYNAME() {
        return CITYNAME;
    }

    public void setCITYNAME(String CITYNAME) {
        this.CITYNAME = CITYNAME;
    }

    public String getDWELLINGTYPE() {
        return DWELLINGTYPE;
    }

    public void setDWELLINGTYPE(String DWELLINGTYPE) {
        this.DWELLINGTYPE = DWELLINGTYPE;
    }

    public Integer getAREACODE() {
        return AREACODE;
    }

    public void setAREACODE(Integer AREACODE) {
        this.AREACODE = AREACODE;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void updatePhone() {
        if (getAREACODE() != null && getPHONE() != null) {
            setPHONE(getAREACODE() + getPHONE());
           // setPHONE(getPHONE());
        }
    }

    public String getESTIMATEDINCOMECODE() {
        return ESTIMATEDINCOMECODE;
    }

    public void setESTIMATEDINCOMECODE(String ESTIMATEDINCOMECODE) {
        this.ESTIMATEDINCOMECODE = ESTIMATEDINCOMECODE;
    }

    public String getHOMEOWNERPROBABILITYMODEL() {
        return HOMEOWNERPROBABILITYMODEL;
    }

    public void setHOMEOWNERPROBABILITYMODEL(String HOMEOWNERPROBABILITYMODEL) {
        this.HOMEOWNERPROBABILITYMODEL = HOMEOWNERPROBABILITYMODEL;
    }

    public Integer getLENGTHOFRESIDENCE() {
        return LENGTHOFRESIDENCE;
    }

    public void setLENGTHOFRESIDENCE(Integer LENGTHOFRESIDENCE) {
        this.LENGTHOFRESIDENCE = LENGTHOFRESIDENCE;
    }

    public Integer getNUMBEROFPERSONSINLIVINGUNIT() {
        return NUMBEROFPERSONSINLIVINGUNIT;
    }

    public void setNUMBEROFPERSONSINLIVINGUNIT(Integer NUMBEROFPERSONSINLIVINGUNIT) {
        this.NUMBEROFPERSONSINLIVINGUNIT = NUMBEROFPERSONSINLIVINGUNIT;
    }

    public Boolean getPRESENCEOFCHILDREN() {
        return PRESENCEOFCHILDREN;
    }

    public void setPRESENCEOFCHILDREN(Boolean PRESENCEOFCHILDREN) {
        this.PRESENCEOFCHILDREN = PRESENCEOFCHILDREN;
    }

    public Integer getNUMBEROFCHILDREN() {
        return NUMBEROFCHILDREN;
    }

    public void setNUMBEROFCHILDREN(Integer NUMBEROFCHILDREN) {
        this.NUMBEROFCHILDREN = NUMBEROFCHILDREN;
    }

    public String getPERSONGENDER() {
        return PERSONGENDER;
    }

    public void setPERSONGENDER(String PERSONGENDER) {
        this.PERSONGENDER = PERSONGENDER;
    }

    public Integer getPERSONDATEOFBIRTHYEAR() {
        return PERSONDATEOFBIRTHYEAR;
    }

    public void setPERSONDATEOFBIRTHYEAR(Integer PERSONDATEOFBIRTHYEAR) {
        this.PERSONDATEOFBIRTHYEAR = PERSONDATEOFBIRTHYEAR;
    }

    public Integer getPERSONDATEOFBIRTHMONTH() {
        return PERSONDATEOFBIRTHMONTH;
    }

    public void setPERSONDATEOFBIRTHMONTH(Integer PERSONDATEOFBIRTHMONTH) {
        this.PERSONDATEOFBIRTHMONTH = PERSONDATEOFBIRTHMONTH;
    }

    public Integer getPERSONDATEOFBIRTHDAY() {
        return PERSONDATEOFBIRTHDAY;
    }

    public void setPERSONDATEOFBIRTHDAY(Integer PERSONDATEOFBIRTHDAY) {
        this.PERSONDATEOFBIRTHDAY = PERSONDATEOFBIRTHDAY;
    }

    public Integer getPERSONEXACTAGE() {
        return PERSONEXACTAGE;
    }

    public void setPERSONEXACTAGE(Integer PERSONEXACTAGE) {
        this.PERSONEXACTAGE = PERSONEXACTAGE;
    }

    public String getPERSONMARITALSTATUS() {
        return PERSONMARITALSTATUS;
    }

    public void setPERSONMARITALSTATUS(String PERSONMARITALSTATUS) {
        this.PERSONMARITALSTATUS = PERSONMARITALSTATUS;
    }

    public Integer getINFERREDAGE() {
        return INFERREDAGE;
    }

    public void setINFERREDAGE(Integer INFERREDAGE) {
        this.INFERREDAGE = INFERREDAGE;
    }

    public String getOCCUPATIONGROUP() {
        return OCCUPATIONGROUP;
    }

    public void setOCCUPATIONGROUP(String OCCUPATIONGROUP) {
        this.OCCUPATIONGROUP = OCCUPATIONGROUP;
    }

    public String getPERSONOCCUPATION() {
        return PERSONOCCUPATION;
    }

    public void setPERSONOCCUPATION(String PERSONOCCUPATION) {
        this.PERSONOCCUPATION = PERSONOCCUPATION;
    }

    public String getETHNICCODE() {
        return ETHNICCODE;
    }

    public void setETHNICCODE(String ETHNICCODE) {
        this.ETHNICCODE = ETHNICCODE;
    }

    public String getLANGUAGECODE() {
        return LANGUAGECODE;
    }

    public void setLANGUAGECODE(String LANGUAGECODE) {
        this.LANGUAGECODE = LANGUAGECODE;
    }

    public String getETHNICGROUP() {
        return ETHNICGROUP;
    }

    public void setETHNICGROUP(String ETHNICGROUP) {
        this.ETHNICGROUP = ETHNICGROUP;
    }

    public String getRELIGIONCODE() {
        return RELIGIONCODE;
    }

    public void setRELIGIONCODE(String RELIGIONCODE) {
        this.RELIGIONCODE = RELIGIONCODE;
    }

    public String getHISPANICCOUNTRYCODE() {
        return HISPANICCOUNTRYCODE;
    }

    public void setHISPANICCOUNTRYCODE(String HISPANICCOUNTRYCODE) {
        this.HISPANICCOUNTRYCODE = HISPANICCOUNTRYCODE;
    }

    public String getPERSONEDUCATION() {
        return PERSONEDUCATION;
    }

    public void setPERSONEDUCATION(String PERSONEDUCATION) {
        this.PERSONEDUCATION = PERSONEDUCATION;
    }

    public String getBUSINESSOWNER() {
        return BUSINESSOWNER;
    }

    public void setBUSINESSOWNER(String BUSINESSOWNER) {
        this.BUSINESSOWNER = BUSINESSOWNER;
    }

    public Integer getINFERREDHOUSEHOLDRANK() {
        return INFERREDHOUSEHOLDRANK;
    }

    public void setINFERREDHOUSEHOLDRANK(Integer INFERREDHOUSEHOLDRANK) {
        this.INFERREDHOUSEHOLDRANK = INFERREDHOUSEHOLDRANK;
    }

    public Integer getNUMBEROFADULTS() {
        return NUMBEROFADULTS;
    }

    public void setNUMBEROFADULTS(Integer NUMBEROFADULTS) {
        this.NUMBEROFADULTS = NUMBEROFADULTS;
    }

    public Integer getGENERATIONSINHOUSEHOLD() {
        return GENERATIONSINHOUSEHOLD;
    }

    public void setGENERATIONSINHOUSEHOLD(Integer GENERATIONSINHOUSEHOLD) {
        this.GENERATIONSINHOUSEHOLD = GENERATIONSINHOUSEHOLD;
    }

    public Boolean getPRESENCEOFCREDITCARD() {
        return PRESENCEOFCREDITCARD;
    }

    public void setPRESENCEOFCREDITCARD(Boolean PRESENCEOFCREDITCARD) {
        this.PRESENCEOFCREDITCARD = PRESENCEOFCREDITCARD;
    }

    public Boolean getPRESENCEOFGOLDORPLATINUMCREDITCARD() {
        return PRESENCEOFGOLDORPLATINUMCREDITCARD;
    }

    public void setPRESENCEOFGOLDORPLATINUMCREDITCARD(Boolean PRESENCEOFGOLDORPLATINUMCREDITCARD) {
        this.PRESENCEOFGOLDORPLATINUMCREDITCARD = PRESENCEOFGOLDORPLATINUMCREDITCARD;
    }

    public Boolean getPRESENCEOFPREMIUMCREDITCARD() {
        return PRESENCEOFPREMIUMCREDITCARD;
    }

    public void setPRESENCEOFPREMIUMCREDITCARD(Boolean PRESENCEOFPREMIUMCREDITCARD) {
        this.PRESENCEOFPREMIUMCREDITCARD = PRESENCEOFPREMIUMCREDITCARD;
    }

    public String getCREDITRATING() {
        return CREDITRATING;
    }

    public void setCREDITRATING(String CREDITRATING) {
        this.CREDITRATING = CREDITRATING;
    }

    public Boolean getINVESTMENT() {
        return INVESTMENT;
    }

    public void setINVESTMENT(Boolean INVESTMENT) {
        this.INVESTMENT = INVESTMENT;
    }

    public Boolean getINVESTMENTSTOCKSECURITIES() {
        return INVESTMENTSTOCKSECURITIES;
    }

    public void setINVESTMENTSTOCKSECURITIES(Boolean INVESTMENTSTOCKSECURITIES) {
        this.INVESTMENTSTOCKSECURITIES = INVESTMENTSTOCKSECURITIES;
    }

    public String getNETWORTH() {
        return NETWORTH;
    }

    public void setNETWORTH(String NETWORTH) {
        this.NETWORTH = NETWORTH;
    }

    public Integer getNUMBEROFLINESOFCREDIT() {
        return NUMBEROFLINESOFCREDIT;
    }

    public void setNUMBEROFLINESOFCREDIT(Integer NUMBEROFLINESOFCREDIT) {
        this.NUMBEROFLINESOFCREDIT = NUMBEROFLINESOFCREDIT;
    }

    public Integer getCREDIT_RANGEOFNEWCREDIT() {
        return CREDIT_RANGEOFNEWCREDIT;
    }

    public void setCREDIT_RANGEOFNEWCREDIT(Integer CREDIT_RANGEOFNEWCREDIT) {
        this.CREDIT_RANGEOFNEWCREDIT = CREDIT_RANGEOFNEWCREDIT;
    }

    public Boolean getTRAVELANDENTERTAINMENTCARDHOLDER() {
        return TRAVELANDENTERTAINMENTCARDHOLDER;
    }

    public void setTRAVELANDENTERTAINMENTCARDHOLDER(Boolean TRAVELANDENTERTAINMENTCARDHOLDER) {
        this.TRAVELANDENTERTAINMENTCARDHOLDER = TRAVELANDENTERTAINMENTCARDHOLDER;
    }

    public Boolean getCREDITCARDUSER() {
        return CREDITCARDUSER;
    }

    public void setCREDITCARDUSER(Boolean CREDITCARDUSER) {
        this.CREDITCARDUSER = CREDITCARDUSER;
    }

    public String getCREDITCARDNEWISSUE() {
        return CREDITCARDNEWISSUE;
    }

    public void setCREDITCARDNEWISSUE(String CREDITCARDNEWISSUE) {
        this.CREDITCARDNEWISSUE = CREDITCARDNEWISSUE;
    }

    public Boolean getINVESTING_ACTIVE() {
        return INVESTING_ACTIVE;
    }

    public void setINVESTING_ACTIVE(Boolean INVESTING_ACTIVE) {
        this.INVESTING_ACTIVE = INVESTING_ACTIVE;
    }

    public Boolean getINVESTMENTSPERSONAL() {
        return INVESTMENTSPERSONAL;
    }

    public void setINVESTMENTSPERSONAL(Boolean INVESTMENTSPERSONAL) {
        this.INVESTMENTSPERSONAL = INVESTMENTSPERSONAL;
    }

    public Boolean getINVESTMENTSREALESTATE() {
        return INVESTMENTSREALESTATE;
    }

    public void setINVESTMENTSREALESTATE(Boolean INVESTMENTSREALESTATE) {
        this.INVESTMENTSREALESTATE = INVESTMENTSREALESTATE;
    }

    public Boolean getINVESTINGFINANCEGROUPING() {
        return INVESTINGFINANCEGROUPING;
    }

    public void setINVESTINGFINANCEGROUPING(Boolean INVESTINGFINANCEGROUPING) {
        this.INVESTINGFINANCEGROUPING = INVESTINGFINANCEGROUPING;
    }

    public Boolean getINVESTMENTSFOREIGN() {
        return INVESTMENTSFOREIGN;
    }

    public void setINVESTMENTSFOREIGN(Boolean INVESTMENTSFOREIGN) {
        this.INVESTMENTSFOREIGN = INVESTMENTSFOREIGN;
    }

    public String getINVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED() {
        return INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED;
    }

    public void setINVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED(String INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED) {
        this.INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED = INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED;
    }

    public Boolean getVALUEHUNTER() {
        return VALUEHUNTER;
    }

    public void setVALUEHUNTER(Boolean VALUEHUNTER) {
        this.VALUEHUNTER = VALUEHUNTER;
    }

    public Boolean getOPPORTUNITYSEEKERS() {
        return OPPORTUNITYSEEKERS;
    }

    public void setOPPORTUNITYSEEKERS(Boolean OPPORTUNITYSEEKERS) {
        this.OPPORTUNITYSEEKERS = OPPORTUNITYSEEKERS;
    }

    public Boolean getNEWSANDFINANCIAL() {
        return NEWSANDFINANCIAL;
    }

    public void setNEWSANDFINANCIAL(Boolean NEWSANDFINANCIAL) {
        this.NEWSANDFINANCIAL = NEWSANDFINANCIAL;
    }

    public Boolean getAUTOMOTIVEBUFF() {
        return AUTOMOTIVEBUFF;
    }

    public void setAUTOMOTIVEBUFF(Boolean AUTOMOTIVEBUFF) {
        this.AUTOMOTIVEBUFF = AUTOMOTIVEBUFF;
    }

    public Boolean getBOOKREADER() {
        return BOOKREADER;
    }

    public void setBOOKREADER(Boolean BOOKREADER) {
        this.BOOKREADER = BOOKREADER;
    }

    public Boolean getCOMPUTEROWNER() {
        return COMPUTEROWNER;
    }

    public void setCOMPUTEROWNER(Boolean COMPUTEROWNER) {
        this.COMPUTEROWNER = COMPUTEROWNER;
    }

    public Boolean getCOOKINGENTHUSIAST() {
        return COOKINGENTHUSIAST;
    }

    public void setCOOKINGENTHUSIAST(Boolean COOKINGENTHUSIAST) {
        this.COOKINGENTHUSIAST = COOKINGENTHUSIAST;
    }

    public Boolean getDO_IT_YOURSELFERS() {
        return DO_IT_YOURSELFERS;
    }

    public void setDO_IT_YOURSELFERS(Boolean DO_IT_YOURSELFERS) {
        this.DO_IT_YOURSELFERS = DO_IT_YOURSELFERS;
    }

    public Boolean getEXERCISEENTHUSIAST() {
        return EXERCISEENTHUSIAST;
    }

    public void setEXERCISEENTHUSIAST(Boolean EXERCISEENTHUSIAST) {
        this.EXERCISEENTHUSIAST = EXERCISEENTHUSIAST;
    }

    public Boolean getGARDENER() {
        return GARDENER;
    }

    public void setGARDENER(Boolean GARDENER) {
        this.GARDENER = GARDENER;
    }

    public Boolean getGOLFENTHUSIASTS() {
        return GOLFENTHUSIASTS;
    }

    public void setGOLFENTHUSIASTS(Boolean GOLFENTHUSIASTS) {
        this.GOLFENTHUSIASTS = GOLFENTHUSIASTS;
    }

    public Boolean getHOMEDECORATINGENTHUSIAST() {
        return HOMEDECORATINGENTHUSIAST;
    }

    public void setHOMEDECORATINGENTHUSIAST(Boolean HOMEDECORATINGENTHUSIAST) {
        this.HOMEDECORATINGENTHUSIAST = HOMEDECORATINGENTHUSIAST;
    }

    public Boolean getOUTDOORENTHUSIAST() {
        return OUTDOORENTHUSIAST;
    }

    public void setOUTDOORENTHUSIAST(Boolean OUTDOORENTHUSIAST) {
        this.OUTDOORENTHUSIAST = OUTDOORENTHUSIAST;
    }

    public Boolean getOUTDOORSPORTSLOVER() {
        return OUTDOORSPORTSLOVER;
    }

    public void setOUTDOORSPORTSLOVER(Boolean OUTDOORSPORTSLOVER) {
        this.OUTDOORSPORTSLOVER = OUTDOORSPORTSLOVER;
    }

    public Boolean getPHOTOGRAPHY() {
        return PHOTOGRAPHY;
    }

    public void setPHOTOGRAPHY(Boolean PHOTOGRAPHY) {
        this.PHOTOGRAPHY = PHOTOGRAPHY;
    }

    public Boolean getTRAVELER() {
        return TRAVELER;
    }

    public void setTRAVELER(Boolean TRAVELER) {
        this.TRAVELER = TRAVELER;
    }

    public Boolean getPETS() {
        return PETS;
    }

    public void setPETS(Boolean PETS) {
        this.PETS = PETS;
    }

    public Boolean getCATS() {
        return CATS;
    }

    public void setCATS(Boolean CATS) {
        this.CATS = CATS;
    }

    public Boolean getDOGS() {
        return DOGS;
    }

    public void setDOGS(Boolean DOGS) {
        this.DOGS = DOGS;
    }

    public Boolean getMAILRESPONDER() {
        return MAILRESPONDER;
    }

    public void setMAILRESPONDER(Boolean MAILRESPONDER) {
        this.MAILRESPONDER = MAILRESPONDER;
    }

    public Boolean getSWEEPSTAKES() {
        return SWEEPSTAKES;
    }

    public void setSWEEPSTAKES(Boolean SWEEPSTAKES) {
        this.SWEEPSTAKES = SWEEPSTAKES;
    }

    public Boolean getRELIGIOUSMAGAZINE() {
        return RELIGIOUSMAGAZINE;
    }

    public void setRELIGIOUSMAGAZINE(Boolean RELIGIOUSMAGAZINE) {
        this.RELIGIOUSMAGAZINE = RELIGIOUSMAGAZINE;
    }

    public Boolean getMALEMERCHBUYER() {
        return MALEMERCHBUYER;
    }

    public void setMALEMERCHBUYER(Boolean MALEMERCHBUYER) {
        this.MALEMERCHBUYER = MALEMERCHBUYER;
    }

    public Boolean getFEMALEMERCHBUYER() {
        return FEMALEMERCHBUYER;
    }

    public void setFEMALEMERCHBUYER(Boolean FEMALEMERCHBUYER) {
        this.FEMALEMERCHBUYER = FEMALEMERCHBUYER;
    }

    public Boolean getCRAFTS_HOBBMERCHBUYER() {
        return CRAFTS_HOBBMERCHBUYER;
    }

    public void setCRAFTS_HOBBMERCHBUYER(Boolean CRAFTS_HOBBMERCHBUYER) {
        this.CRAFTS_HOBBMERCHBUYER = CRAFTS_HOBBMERCHBUYER;
    }

    public Boolean getGARDENING_FARMINGBUYER() {
        return GARDENING_FARMINGBUYER;
    }

    public void setGARDENING_FARMINGBUYER(Boolean GARDENING_FARMINGBUYER) {
        this.GARDENING_FARMINGBUYER = GARDENING_FARMINGBUYER;
    }

    public Boolean getBOOKBUYER() {
        return BOOKBUYER;
    }

    public void setBOOKBUYER(Boolean BOOKBUYER) {
        this.BOOKBUYER = BOOKBUYER;
    }

    public Boolean getCOLLECT_SPECIALFOODSBUYER() {
        return COLLECT_SPECIALFOODSBUYER;
    }

    public void setCOLLECT_SPECIALFOODSBUYER(Boolean COLLECT_SPECIALFOODSBUYER) {
        this.COLLECT_SPECIALFOODSBUYER = COLLECT_SPECIALFOODSBUYER;
    }

    public Boolean getRELIGIOUSCONTRIBUTOR() {
        return RELIGIOUSCONTRIBUTOR;
    }

    public void setRELIGIOUSCONTRIBUTOR(Boolean RELIGIOUSCONTRIBUTOR) {
        this.RELIGIOUSCONTRIBUTOR = RELIGIOUSCONTRIBUTOR;
    }

    public Boolean getPOLITICALCONTRIBUTOR() {
        return POLITICALCONTRIBUTOR;
    }

    public void setPOLITICALCONTRIBUTOR(Boolean POLITICALCONTRIBUTOR) {
        this.POLITICALCONTRIBUTOR = POLITICALCONTRIBUTOR;
    }

    public Boolean getCHARITABLE() {
        return CHARITABLE;
    }

    public void setCHARITABLE(Boolean CHARITABLE) {
        this.CHARITABLE = CHARITABLE;
    }

    public Boolean getDONATESTOENVIRONMENTALCAUSES() {
        return DONATESTOENVIRONMENTALCAUSES;
    }

    public void setDONATESTOENVIRONMENTALCAUSES(Boolean DONATESTOENVIRONMENTALCAUSES) {
        this.DONATESTOENVIRONMENTALCAUSES = DONATESTOENVIRONMENTALCAUSES;
    }

    public Boolean getVETERANINHOUSEHOLD() {
        return VETERANINHOUSEHOLD;
    }

    public void setVETERANINHOUSEHOLD(Boolean VETERANINHOUSEHOLD) {
        this.VETERANINHOUSEHOLD = VETERANINHOUSEHOLD;
    }

    public Boolean getHIGHTECHLEADER() {
        return HIGHTECHLEADER;
    }

    public void setHIGHTECHLEADER(Boolean HIGHTECHLEADER) {
        this.HIGHTECHLEADER = HIGHTECHLEADER;
    }

    public Boolean getSMOKER() {
        return SMOKER;
    }

    public void setSMOKER(Boolean SMOKER) {
        this.SMOKER = SMOKER;
    }

    public Boolean getMAILORDERBUYER() {
        return MAILORDERBUYER;
    }

    public void setMAILORDERBUYER(Boolean MAILORDERBUYER) {
        this.MAILORDERBUYER = MAILORDERBUYER;
    }

    public Boolean getONLINEPURCHASINGINDICATOR() {
        return ONLINEPURCHASINGINDICATOR;
    }

    public void setONLINEPURCHASINGINDICATOR(Boolean ONLINEPURCHASINGINDICATOR) {
        this.ONLINEPURCHASINGINDICATOR = ONLINEPURCHASINGINDICATOR;
    }

    public Boolean getAPPARELWOMENS() {
        return APPARELWOMENS;
    }

    public void setAPPARELWOMENS(Boolean APPARELWOMENS) {
        this.APPARELWOMENS = APPARELWOMENS;
    }

    public Boolean getYOUNGWOMENSAPPAREL() {
        return YOUNGWOMENSAPPAREL;
    }

    public void setYOUNGWOMENSAPPAREL(Boolean YOUNGWOMENSAPPAREL) {
        this.YOUNGWOMENSAPPAREL = YOUNGWOMENSAPPAREL;
    }

    public Boolean getAPPARELMENS() {
        return APPARELMENS;
    }

    public void setAPPARELMENS(Boolean APPARELMENS) {
        this.APPARELMENS = APPARELMENS;
    }

    public Boolean getAPPARELMENSBIGANDTALL() {
        return APPARELMENSBIGANDTALL;
    }

    public void setAPPARELMENSBIGANDTALL(Boolean APPARELMENSBIGANDTALL) {
        this.APPARELMENSBIGANDTALL = APPARELMENSBIGANDTALL;
    }

    public Boolean getYOUNGMENSAPPAREL() {
        return YOUNGMENSAPPAREL;
    }

    public void setYOUNGMENSAPPAREL(Boolean YOUNGMENSAPPAREL) {
        this.YOUNGMENSAPPAREL = YOUNGMENSAPPAREL;
    }

    public Boolean getAPPARELCHILDRENS() {
        return APPARELCHILDRENS;
    }

    public void setAPPARELCHILDRENS(Boolean APPARELCHILDRENS) {
        this.APPARELCHILDRENS = APPARELCHILDRENS;
    }

    public Boolean getHEALTHANDBEAUTY() {
        return HEALTHANDBEAUTY;
    }

    public void setHEALTHANDBEAUTY(Boolean HEALTHANDBEAUTY) {
        this.HEALTHANDBEAUTY = HEALTHANDBEAUTY;
    }

    public Boolean getBEAUTYCOSMETICS() {
        return BEAUTYCOSMETICS;
    }

    public void setBEAUTYCOSMETICS(Boolean BEAUTYCOSMETICS) {
        this.BEAUTYCOSMETICS = BEAUTYCOSMETICS;
    }

    public Boolean getJEWELRY() {
        return JEWELRY;
    }

    public void setJEWELRY(Boolean JEWELRY) {
        this.JEWELRY = JEWELRY;
    }

    public Boolean getLUGGAGE() {
        return LUGGAGE;
    }

    public void setLUGGAGE(Boolean LUGGAGE) {
        this.LUGGAGE = LUGGAGE;
    }

    public Boolean getPOLITICALCONSERVATIVECHARITABLEDONATION() {
        return POLITICALCONSERVATIVECHARITABLEDONATION;
    }

    public void setPOLITICALCONSERVATIVECHARITABLEDONATION(Boolean POLITICALCONSERVATIVECHARITABLEDONATION) {
        this.POLITICALCONSERVATIVECHARITABLEDONATION = POLITICALCONSERVATIVECHARITABLEDONATION;
    }

    public Boolean getPOLITICALLIBERALCHARITABLEDONATION() {
        return POLITICALLIBERALCHARITABLEDONATION;
    }

    public void setPOLITICALLIBERALCHARITABLEDONATION(Boolean POLITICALLIBERALCHARITABLEDONATION) {
        this.POLITICALLIBERALCHARITABLEDONATION = POLITICALLIBERALCHARITABLEDONATION;
    }

    public Boolean getVETERANSCHARITABLEDONATION() {
        return VETERANSCHARITABLEDONATION;
    }

    public void setVETERANSCHARITABLEDONATION(Boolean VETERANSCHARITABLEDONATION) {
        this.VETERANSCHARITABLEDONATION = VETERANSCHARITABLEDONATION;
    }

    public Boolean getSINGLEPARENT() {
        return SINGLEPARENT;
    }

    public void setSINGLEPARENT(Boolean SINGLEPARENT) {
        this.SINGLEPARENT = SINGLEPARENT;
    }

    public Boolean getSENIORADULTINHOUSEHOLD() {
        return SENIORADULTINHOUSEHOLD;
    }

    public void setSENIORADULTINHOUSEHOLD(Boolean SENIORADULTINHOUSEHOLD) {
        this.SENIORADULTINHOUSEHOLD = SENIORADULTINHOUSEHOLD;
    }

    public Boolean getEQUESTRIAN() {
        return EQUESTRIAN;
    }

    public void setEQUESTRIAN(Boolean EQUESTRIAN) {
        this.EQUESTRIAN = EQUESTRIAN;
    }

    public Boolean getCAREERIMPROVEMENT() {
        return CAREERIMPROVEMENT;
    }

    public void setCAREERIMPROVEMENT(Boolean CAREERIMPROVEMENT) {
        this.CAREERIMPROVEMENT = CAREERIMPROVEMENT;
    }

    public Boolean getWORKINGWOMAN() {
        return WORKINGWOMAN;
    }

    public void setWORKINGWOMAN(Boolean WORKINGWOMAN) {
        this.WORKINGWOMAN = WORKINGWOMAN;
    }

    public Boolean getAFRICANAMERICANPROFESSIONALS() {
        return AFRICANAMERICANPROFESSIONALS;
    }

    public void setAFRICANAMERICANPROFESSIONALS(Boolean AFRICANAMERICANPROFESSIONALS) {
        this.AFRICANAMERICANPROFESSIONALS = AFRICANAMERICANPROFESSIONALS;
    }

    public Boolean getSOHOINDICATOR() {
        return SOHOINDICATOR;
    }

    public void setSOHOINDICATOR(Boolean SOHOINDICATOR) {
        this.SOHOINDICATOR = SOHOINDICATOR;
    }

    public Boolean getCAREER() {
        return CAREER;
    }

    public void setCAREER(Boolean CAREER) {
        this.CAREER = CAREER;
    }

    public Boolean getREADINGSCIENCEFICTION() {
        return READINGSCIENCEFICTION;
    }

    public void setREADINGSCIENCEFICTION(Boolean READINGSCIENCEFICTION) {
        this.READINGSCIENCEFICTION = READINGSCIENCEFICTION;
    }

    public Boolean getREADINGAUDIOBOOKS() {
        return READINGAUDIOBOOKS;
    }

    public void setREADINGAUDIOBOOKS(Boolean READINGAUDIOBOOKS) {
        this.READINGAUDIOBOOKS = READINGAUDIOBOOKS;
    }

    public Boolean getHISTORYMILITARY() {
        return HISTORYMILITARY;
    }

    public void setHISTORYMILITARY(Boolean HISTORYMILITARY) {
        this.HISTORYMILITARY = HISTORYMILITARY;
    }

    public Boolean getCURRENTAFFAIRSPOLITICS() {
        return CURRENTAFFAIRSPOLITICS;
    }

    public void setCURRENTAFFAIRSPOLITICS(Boolean CURRENTAFFAIRSPOLITICS) {
        this.CURRENTAFFAIRSPOLITICS = CURRENTAFFAIRSPOLITICS;
    }

    public Boolean getSCIENCESPACE() {
        return SCIENCESPACE;
    }

    public void setSCIENCESPACE(Boolean SCIENCESPACE) {
        this.SCIENCESPACE = SCIENCESPACE;
    }

    public Boolean getEDUCATIONONLINE() {
        return EDUCATIONONLINE;
    }

    public void setEDUCATIONONLINE(Boolean EDUCATIONONLINE) {
        this.EDUCATIONONLINE = EDUCATIONONLINE;
    }

    public Boolean getGAMING() {
        return GAMING;
    }

    public void setGAMING(Boolean GAMING) {
        this.GAMING = GAMING;
    }

    public Boolean getCOMPUTINGHOMEOFFICEGENERAL() {
        return COMPUTINGHOMEOFFICEGENERAL;
    }

    public void setCOMPUTINGHOMEOFFICEGENERAL(Boolean COMPUTINGHOMEOFFICEGENERAL) {
        this.COMPUTINGHOMEOFFICEGENERAL = COMPUTINGHOMEOFFICEGENERAL;
    }

    public Boolean getELECTRONICSCOMPUTINGANDHOMEOFFICE() {
        return ELECTRONICSCOMPUTINGANDHOMEOFFICE;
    }

    public void setELECTRONICSCOMPUTINGANDHOMEOFFICE(Boolean ELECTRONICSCOMPUTINGANDHOMEOFFICE) {
        this.ELECTRONICSCOMPUTINGANDHOMEOFFICE = ELECTRONICSCOMPUTINGANDHOMEOFFICE;
    }

    public Boolean getHIGHENDAPPLIANCES() {
        return HIGHENDAPPLIANCES;
    }

    public void setHIGHENDAPPLIANCES(Boolean HIGHENDAPPLIANCES) {
        this.HIGHENDAPPLIANCES = HIGHENDAPPLIANCES;
    }

    public Boolean getINTENDTOPURCHASEHDTVSATELLITEDISH() {
        return INTENDTOPURCHASEHDTVSATELLITEDISH;
    }

    public void setINTENDTOPURCHASEHDTVSATELLITEDISH(Boolean INTENDTOPURCHASEHDTVSATELLITEDISH) {
        this.INTENDTOPURCHASEHDTVSATELLITEDISH = INTENDTOPURCHASEHDTVSATELLITEDISH;
    }

    public Boolean getTVCABLE() {
        return TVCABLE;
    }

    public void setTVCABLE(Boolean TVCABLE) {
        this.TVCABLE = TVCABLE;
    }

    public Boolean getGAMESVIDEOGAMES() {
        return GAMESVIDEOGAMES;
    }

    public void setGAMESVIDEOGAMES(Boolean GAMESVIDEOGAMES) {
        this.GAMESVIDEOGAMES = GAMESVIDEOGAMES;
    }

    public Boolean getTVSATELLITEDISH() {
        return TVSATELLITEDISH;
    }

    public void setTVSATELLITEDISH(Boolean TVSATELLITEDISH) {
        this.TVSATELLITEDISH = TVSATELLITEDISH;
    }

    public Boolean getCOMPUTERS() {
        return COMPUTERS;
    }

    public void setCOMPUTERS(Boolean COMPUTERS) {
        this.COMPUTERS = COMPUTERS;
    }

    public Boolean getGAMESCOMPUTERGAMES() {
        return GAMESCOMPUTERGAMES;
    }

    public void setGAMESCOMPUTERGAMES(Boolean GAMESCOMPUTERGAMES) {
        this.GAMESCOMPUTERGAMES = GAMESCOMPUTERGAMES;
    }

    public Boolean getCONSUMERELECTRONICS() {
        return CONSUMERELECTRONICS;
    }

    public void setCONSUMERELECTRONICS(Boolean CONSUMERELECTRONICS) {
        this.CONSUMERELECTRONICS = CONSUMERELECTRONICS;
    }

    public Boolean getMOVIEMUSICGROUPING() {
        return MOVIEMUSICGROUPING;
    }

    public void setMOVIEMUSICGROUPING(Boolean MOVIEMUSICGROUPING) {
        this.MOVIEMUSICGROUPING = MOVIEMUSICGROUPING;
    }

    public Boolean getELECTRONICSCOMPUTERSGROUPING() {
        return ELECTRONICSCOMPUTERSGROUPING;
    }

    public void setELECTRONICSCOMPUTERSGROUPING(Boolean ELECTRONICSCOMPUTERSGROUPING) {
        this.ELECTRONICSCOMPUTERSGROUPING = ELECTRONICSCOMPUTERSGROUPING;
    }

    public Boolean getTELECOMMUNICATIONS() {
        return TELECOMMUNICATIONS;
    }

    public void setTELECOMMUNICATIONS(Boolean TELECOMMUNICATIONS) {
        this.TELECOMMUNICATIONS = TELECOMMUNICATIONS;
    }

    public Boolean getARTS() {
        return ARTS;
    }

    public void setARTS(Boolean ARTS) {
        this.ARTS = ARTS;
    }

    public Boolean getMUSICALINSTRUMENTS() {
        return MUSICALINSTRUMENTS;
    }

    public void setMUSICALINSTRUMENTS(Boolean MUSICALINSTRUMENTS) {
        this.MUSICALINSTRUMENTS = MUSICALINSTRUMENTS;
    }

    public Boolean getCOLLECTIBLESSTAMPS() {
        return COLLECTIBLESSTAMPS;
    }

    public void setCOLLECTIBLESSTAMPS(Boolean COLLECTIBLESSTAMPS) {
        this.COLLECTIBLESSTAMPS = COLLECTIBLESSTAMPS;
    }

    public Boolean getCOLLECTIBLESCOINS() {
        return COLLECTIBLESCOINS;
    }

    public void setCOLLECTIBLESCOINS(Boolean COLLECTIBLESCOINS) {
        this.COLLECTIBLESCOINS = COLLECTIBLESCOINS;
    }

    public Boolean getCOLLECTIBLESARTS() {
        return COLLECTIBLESARTS;
    }

    public void setCOLLECTIBLESARTS(Boolean COLLECTIBLESARTS) {
        this.COLLECTIBLESARTS = COLLECTIBLESARTS;
    }

    public Boolean getCOLLECTIBLESANTIQUES() {
        return COLLECTIBLESANTIQUES;
    }

    public void setCOLLECTIBLESANTIQUES(Boolean COLLECTIBLESANTIQUES) {
        this.COLLECTIBLESANTIQUES = COLLECTIBLESANTIQUES;
    }

    public Boolean getCOLLECTIBLESSPORTSMEMORABILIA() {
        return COLLECTIBLESSPORTSMEMORABILIA;
    }

    public void setCOLLECTIBLESSPORTSMEMORABILIA(Boolean COLLECTIBLESSPORTSMEMORABILIA) {
        this.COLLECTIBLESSPORTSMEMORABILIA = COLLECTIBLESSPORTSMEMORABILIA;
    }

    public Boolean getMILITARYMEMORABILIAWEAPONRY() {
        return MILITARYMEMORABILIAWEAPONRY;
    }

    public void setMILITARYMEMORABILIAWEAPONRY(Boolean MILITARYMEMORABILIAWEAPONRY) {
        this.MILITARYMEMORABILIAWEAPONRY = MILITARYMEMORABILIAWEAPONRY;
    }

    public Boolean getAUTOWORK() {
        return AUTOWORK;
    }

    public void setAUTOWORK(Boolean AUTOWORK) {
        this.AUTOWORK = AUTOWORK;
    }

    public Boolean getWOODWORKING() {
        return WOODWORKING;
    }

    public void setWOODWORKING(Boolean WOODWORKING) {
        this.WOODWORKING = WOODWORKING;
    }

    public Boolean getAVIATION() {
        return AVIATION;
    }

    public void setAVIATION(Boolean AVIATION) {
        this.AVIATION = AVIATION;
    }

    public Boolean getHOUSEPLANTS() {
        return HOUSEPLANTS;
    }

    public void setHOUSEPLANTS(Boolean HOUSEPLANTS) {
        this.HOUSEPLANTS = HOUSEPLANTS;
    }

    public Boolean getHOMEANDGARDEN() {
        return HOMEANDGARDEN;
    }

    public void setHOMEANDGARDEN(Boolean HOMEANDGARDEN) {
        this.HOMEANDGARDEN = HOMEANDGARDEN;
    }

    public Boolean getHOMEIMPROVEMENTGROUPING() {
        return HOMEIMPROVEMENTGROUPING;
    }

    public void setHOMEIMPROVEMENTGROUPING(Boolean HOMEIMPROVEMENTGROUPING) {
        this.HOMEIMPROVEMENTGROUPING = HOMEIMPROVEMENTGROUPING;
    }

    public Boolean getPHOTOGRAPHYANDVIDEOEQUIPMENT() {
        return PHOTOGRAPHYANDVIDEOEQUIPMENT;
    }

    public void setPHOTOGRAPHYANDVIDEOEQUIPMENT(Boolean PHOTOGRAPHYANDVIDEOEQUIPMENT) {
        this.PHOTOGRAPHYANDVIDEOEQUIPMENT = PHOTOGRAPHYANDVIDEOEQUIPMENT;
    }

    public Boolean getHOMEFURNISHINGSDECORATING() {
        return HOMEFURNISHINGSDECORATING;
    }

    public void setHOMEFURNISHINGSDECORATING(Boolean HOMEFURNISHINGSDECORATING) {
        this.HOMEFURNISHINGSDECORATING = HOMEFURNISHINGSDECORATING;
    }

    public Boolean getHOMEIMPROVEMENT() {
        return HOMEIMPROVEMENT;
    }

    public void setHOMEIMPROVEMENT(Boolean HOMEIMPROVEMENT) {
        this.HOMEIMPROVEMENT = HOMEIMPROVEMENT;
    }

    public Boolean getINTENDTOPURCHASEHOMEIMPROVEMENT() {
        return INTENDTOPURCHASEHOMEIMPROVEMENT;
    }

    public void setINTENDTOPURCHASEHOMEIMPROVEMENT(Boolean INTENDTOPURCHASEHOMEIMPROVEMENT) {
        this.INTENDTOPURCHASEHOMEIMPROVEMENT = INTENDTOPURCHASEHOMEIMPROVEMENT;
    }

    public Boolean getFOODWINES() {
        return FOODWINES;
    }

    public void setFOODWINES(Boolean FOODWINES) {
        this.FOODWINES = FOODWINES;
    }

    public Boolean getCOOKINGGENERAL() {
        return COOKINGGENERAL;
    }

    public void setCOOKINGGENERAL(Boolean COOKINGGENERAL) {
        this.COOKINGGENERAL = COOKINGGENERAL;
    }

    public Boolean getCOOKINGGOURMET() {
        return COOKINGGOURMET;
    }

    public void setCOOKINGGOURMET(Boolean COOKINGGOURMET) {
        this.COOKINGGOURMET = COOKINGGOURMET;
    }

    public Boolean getFOODSNATURAL() {
        return FOODSNATURAL;
    }

    public void setFOODSNATURAL(Boolean FOODSNATURAL) {
        this.FOODSNATURAL = FOODSNATURAL;
    }

    public Boolean getCOOKINGFOODGROUPING() {
        return COOKINGFOODGROUPING;
    }

    public void setCOOKINGFOODGROUPING(Boolean COOKINGFOODGROUPING) {
        this.COOKINGFOODGROUPING = COOKINGFOODGROUPING;
    }

    public Boolean getGAMINGCASINO() {
        return GAMINGCASINO;
    }

    public void setGAMINGCASINO(Boolean GAMINGCASINO) {
        this.GAMINGCASINO = GAMINGCASINO;
    }

    public Boolean getTRAVELGROUPING() {
        return TRAVELGROUPING;
    }

    public void setTRAVELGROUPING(Boolean TRAVELGROUPING) {
        this.TRAVELGROUPING = TRAVELGROUPING;
    }

    public Boolean getTRAVEL() {
        return TRAVEL;
    }

    public void setTRAVEL(Boolean TRAVEL) {
        this.TRAVEL = TRAVEL;
    }

    public Boolean getTRAVELDOMESTIC() {
        return TRAVELDOMESTIC;
    }

    public void setTRAVELDOMESTIC(Boolean TRAVELDOMESTIC) {
        this.TRAVELDOMESTIC = TRAVELDOMESTIC;
    }

    public Boolean getTRAVELINTERNATIONAL() {
        return TRAVELINTERNATIONAL;
    }

    public void setTRAVELINTERNATIONAL(Boolean TRAVELINTERNATIONAL) {
        this.TRAVELINTERNATIONAL = TRAVELINTERNATIONAL;
    }

    public Boolean getTRAVELCRUISEVACATIONS() {
        return TRAVELCRUISEVACATIONS;
    }

    public void setTRAVELCRUISEVACATIONS(Boolean TRAVELCRUISEVACATIONS) {
        this.TRAVELCRUISEVACATIONS = TRAVELCRUISEVACATIONS;
    }

    public Boolean getUPSCALELIVING() {
        return UPSCALELIVING;
    }

    public void setUPSCALELIVING(Boolean UPSCALELIVING) {
        this.UPSCALELIVING = UPSCALELIVING;
    }

    public Boolean getCULTURALARTISTICLIVING() {
        return CULTURALARTISTICLIVING;
    }

    public void setCULTURALARTISTICLIVING(Boolean CULTURALARTISTICLIVING) {
        this.CULTURALARTISTICLIVING = CULTURALARTISTICLIVING;
    }

    public Boolean getHIGHTECHLIVING() {
        return HIGHTECHLIVING;
    }

    public void setHIGHTECHLIVING(Boolean HIGHTECHLIVING) {
        this.HIGHTECHLIVING = HIGHTECHLIVING;
    }

    public Boolean getEXERCISEHEALTHGROUPING() {
        return EXERCISEHEALTHGROUPING;
    }

    public void setEXERCISEHEALTHGROUPING(Boolean EXERCISEHEALTHGROUPING) {
        this.EXERCISEHEALTHGROUPING = EXERCISEHEALTHGROUPING;
    }

    public Boolean getEXERCISERUNNINGJOGGING() {
        return EXERCISERUNNINGJOGGING;
    }

    public void setEXERCISERUNNINGJOGGING(Boolean EXERCISERUNNINGJOGGING) {
        this.EXERCISERUNNINGJOGGING = EXERCISERUNNINGJOGGING;
    }

    public Boolean getEXERCISEWALKING() {
        return EXERCISEWALKING;
    }

    public void setEXERCISEWALKING(Boolean EXERCISEWALKING) {
        this.EXERCISEWALKING = EXERCISEWALKING;
    }

    public Boolean getEXERCISEAEROBIC() {
        return EXERCISEAEROBIC;
    }

    public void setEXERCISEAEROBIC(Boolean EXERCISEAEROBIC) {
        this.EXERCISEAEROBIC = EXERCISEAEROBIC;
    }

    public Boolean getSPECTATORSPORTSTVSPORTS() {
        return SPECTATORSPORTSTVSPORTS;
    }

    public void setSPECTATORSPORTSTVSPORTS(Boolean SPECTATORSPORTSTVSPORTS) {
        this.SPECTATORSPORTSTVSPORTS = SPECTATORSPORTSTVSPORTS;
    }

    public Boolean getSPECTATORSPORTSFOOTBALL() {
        return SPECTATORSPORTSFOOTBALL;
    }

    public void setSPECTATORSPORTSFOOTBALL(Boolean SPECTATORSPORTSFOOTBALL) {
        this.SPECTATORSPORTSFOOTBALL = SPECTATORSPORTSFOOTBALL;
    }

    public Boolean getSPECTATORSPORTSBASEBALL() {
        return SPECTATORSPORTSBASEBALL;
    }

    public void setSPECTATORSPORTSBASEBALL(Boolean SPECTATORSPORTSBASEBALL) {
        this.SPECTATORSPORTSBASEBALL = SPECTATORSPORTSBASEBALL;
    }

    public Boolean getSPECTATORSPORTSBASKETBALL() {
        return SPECTATORSPORTSBASKETBALL;
    }

    public void setSPECTATORSPORTSBASKETBALL(Boolean SPECTATORSPORTSBASKETBALL) {
        this.SPECTATORSPORTSBASKETBALL = SPECTATORSPORTSBASKETBALL;
    }

    public Boolean getSPECTATORSPORTSHOCKEY() {
        return SPECTATORSPORTSHOCKEY;
    }

    public void setSPECTATORSPORTSHOCKEY(Boolean SPECTATORSPORTSHOCKEY) {
        this.SPECTATORSPORTSHOCKEY = SPECTATORSPORTSHOCKEY;
    }

    public Boolean getSPECTATORSPORTSSOCCER() {
        return SPECTATORSPORTSSOCCER;
    }

    public void setSPECTATORSPORTSSOCCER(Boolean SPECTATORSPORTSSOCCER) {
        this.SPECTATORSPORTSSOCCER = SPECTATORSPORTSSOCCER;
    }

    public Boolean getTENNIS() {
        return TENNIS;
    }

    public void setTENNIS(Boolean TENNIS) {
        this.TENNIS = TENNIS;
    }

    public Boolean getSNOWSKIING() {
        return SNOWSKIING;
    }

    public void setSNOWSKIING(Boolean SNOWSKIING) {
        this.SNOWSKIING = SNOWSKIING;
    }

    public Boolean getMOTORCYCLING() {
        return MOTORCYCLING;
    }

    public void setMOTORCYCLING(Boolean MOTORCYCLING) {
        this.MOTORCYCLING = MOTORCYCLING;
    }

    public Boolean getNASCAR() {
        return NASCAR;
    }

    public void setNASCAR(Boolean NASCAR) {
        this.NASCAR = NASCAR;
    }

    public Boolean getBOATINGSAILING() {
        return BOATINGSAILING;
    }

    public void setBOATINGSAILING(Boolean BOATINGSAILING) {
        this.BOATINGSAILING = BOATINGSAILING;
    }

    public Boolean getSCUBADIVING() {
        return SCUBADIVING;
    }

    public void setSCUBADIVING(Boolean SCUBADIVING) {
        this.SCUBADIVING = SCUBADIVING;
    }

    public Boolean getSPORTSANDLEISURE() {
        return SPORTSANDLEISURE;
    }

    public void setSPORTSANDLEISURE(Boolean SPORTSANDLEISURE) {
        this.SPORTSANDLEISURE = SPORTSANDLEISURE;
    }

    public Boolean getHUNTING() {
        return HUNTING;
    }

    public void setHUNTING(Boolean HUNTING) {
        this.HUNTING = HUNTING;
    }

    public Boolean getFISHING() {
        return FISHING;
    }

    public void setFISHING(Boolean FISHING) {
        this.FISHING = FISHING;
    }

    public Boolean getCAMPINGHIKING() {
        return CAMPINGHIKING;
    }

    public void setCAMPINGHIKING(Boolean CAMPINGHIKING) {
        this.CAMPINGHIKING = CAMPINGHIKING;
    }

    public Boolean getHUNTINGSHOOTING() {
        return HUNTINGSHOOTING;
    }

    public void setHUNTINGSHOOTING(Boolean HUNTINGSHOOTING) {
        this.HUNTINGSHOOTING = HUNTINGSHOOTING;
    }

    public Boolean getSPORTSGROUPING() {
        return SPORTSGROUPING;
    }

    public void setSPORTSGROUPING(Boolean SPORTSGROUPING) {
        this.SPORTSGROUPING = SPORTSGROUPING;
    }

    public Boolean getOUTDOORSGROUPING() {
        return OUTDOORSGROUPING;
    }

    public void setOUTDOORSGROUPING(Boolean OUTDOORSGROUPING) {
        this.OUTDOORSGROUPING = OUTDOORSGROUPING;
    }

    public Boolean getHEALTHMEDICAL() {
        return HEALTHMEDICAL;
    }

    public void setHEALTHMEDICAL(Boolean HEALTHMEDICAL) {
        this.HEALTHMEDICAL = HEALTHMEDICAL;
    }

    public Boolean getDIETINGWEIGHTLOSS() {
        return DIETINGWEIGHTLOSS;
    }

    public void setDIETINGWEIGHTLOSS(Boolean DIETINGWEIGHTLOSS) {
        this.DIETINGWEIGHTLOSS = DIETINGWEIGHTLOSS;
    }

    public Boolean getSELFIMPROVEMENT() {
        return SELFIMPROVEMENT;
    }

    public void setSELFIMPROVEMENT(Boolean SELFIMPROVEMENT) {
        this.SELFIMPROVEMENT = SELFIMPROVEMENT;
    }

    public Boolean getAUTOMOTIVEAUTOPARTSANDACCESSORIES() {
        return AUTOMOTIVEAUTOPARTSANDACCESSORIES;
    }

    public void setAUTOMOTIVEAUTOPARTSANDACCESSORIES(Boolean AUTOMOTIVEAUTOPARTSANDACCESSORIES) {
        this.AUTOMOTIVEAUTOPARTSANDACCESSORIES = AUTOMOTIVEAUTOPARTSANDACCESSORIES;
    }

    public Boolean getHOMESWIMMINGPOOLINDICATOR() {
        return HOMESWIMMINGPOOLINDICATOR;
    }

    public void setHOMESWIMMINGPOOLINDICATOR(Boolean HOMESWIMMINGPOOLINDICATOR) {
        this.HOMESWIMMINGPOOLINDICATOR = HOMESWIMMINGPOOLINDICATOR;
    }

    public String getAIRCONDITIONING() {
        return AIRCONDITIONING;
    }

    public void setAIRCONDITIONING(String AIRCONDITIONING) {
        this.AIRCONDITIONING = AIRCONDITIONING;
    }

    public String getHOMEHEATINDICATOR() {
        return HOMEHEATINDICATOR;
    }

    public void setHOMEHEATINDICATOR(String HOMEHEATINDICATOR) {
        this.HOMEHEATINDICATOR = HOMEHEATINDICATOR;
    }

    public Integer getHOMEPURCHASEPRICE() {
        return HOMEPURCHASEPRICE;
    }

    public void setHOMEPURCHASEPRICE(Integer HOMEPURCHASEPRICE) {
        this.HOMEPURCHASEPRICE = HOMEPURCHASEPRICE;
    }

    public Integer getHOMEPURCHASEDATEYEAR() {
        return HOMEPURCHASEDATEYEAR;
    }

    public void setHOMEPURCHASEDATEYEAR(Integer HOMEPURCHASEDATEYEAR) {
        this.HOMEPURCHASEDATEYEAR = HOMEPURCHASEDATEYEAR;
    }

    public Integer getHOMEPURCHASEDATEMONTH() {
        return HOMEPURCHASEDATEMONTH;
    }

    public void setHOMEPURCHASEDATEMONTH(Integer HOMEPURCHASEDATEMONTH) {
        this.HOMEPURCHASEDATEMONTH = HOMEPURCHASEDATEMONTH;
    }

    public Integer getHOMEPURCHASEDATEDAY() {
        return HOMEPURCHASEDATEDAY;
    }

    public void setHOMEPURCHASEDATEDAY(Integer HOMEPURCHASEDATEDAY) {
        this.HOMEPURCHASEDATEDAY = HOMEPURCHASEDATEDAY;
    }

    public Integer getHOMEYEARBUILT() {
        return HOMEYEARBUILT;
    }

    public void setHOMEYEARBUILT(Integer HOMEYEARBUILT) {
        this.HOMEYEARBUILT = HOMEYEARBUILT;
    }

    public String getESTIMATEDCURRENTHOMEVALUECODE() {
        return ESTIMATEDCURRENTHOMEVALUECODE;
    }

    public void setESTIMATEDCURRENTHOMEVALUECODE(String ESTIMATEDCURRENTHOMEVALUECODE) {
        this.ESTIMATEDCURRENTHOMEVALUECODE = ESTIMATEDCURRENTHOMEVALUECODE;
    }

    public Integer getMORTGAGEAMOUNTINTHOUSANDS() {
        return MORTGAGEAMOUNTINTHOUSANDS;
    }

    public void setMORTGAGEAMOUNTINTHOUSANDS(Integer MORTGAGEAMOUNTINTHOUSANDS) {
        this.MORTGAGEAMOUNTINTHOUSANDS = MORTGAGEAMOUNTINTHOUSANDS;
    }

    public String getMORTGAGELENDERNAME() {
        return MORTGAGELENDERNAME;
    }

    public void setMORTGAGELENDERNAME(String MORTGAGELENDERNAME) {
        this.MORTGAGELENDERNAME = MORTGAGELENDERNAME;
    }

    public Boolean getMORTGAGELENDERNAMEAVAILABLE() {
        return MORTGAGELENDERNAMEAVAILABLE;
    }

    public void setMORTGAGELENDERNAMEAVAILABLE(Boolean MORTGAGELENDERNAMEAVAILABLE) {
        this.MORTGAGELENDERNAMEAVAILABLE = MORTGAGELENDERNAMEAVAILABLE;
    }

    public String getMORTGAGERATE() {
        return MORTGAGERATE;
    }

    public void setMORTGAGERATE(String MORTGAGERATE) {
        this.MORTGAGERATE = MORTGAGERATE;
    }

    public String getMORTGAGERATETYPE() {
        return MORTGAGERATETYPE;
    }

    public void setMORTGAGERATETYPE(String MORTGAGERATETYPE) {
        this.MORTGAGERATETYPE = MORTGAGERATETYPE;
    }

    public String getMORTGAGELOANTYPE() {
        return MORTGAGELOANTYPE;
    }

    public void setMORTGAGELOANTYPE(String MORTGAGELOANTYPE) {
        this.MORTGAGELOANTYPE = MORTGAGELOANTYPE;
    }

    public String getTRANSACTIONTYPE() {
        return TRANSACTIONTYPE;
    }

    public void setTRANSACTIONTYPE(String TRANSACTIONTYPE) {
        this.TRANSACTIONTYPE = TRANSACTIONTYPE;
    }

    public Integer getDEEDDATEOFREFINANCEYEAR() {
        return DEEDDATEOFREFINANCEYEAR;
    }

    public void setDEEDDATEOFREFINANCEYEAR(Integer DEEDDATEOFREFINANCEYEAR) {
        this.DEEDDATEOFREFINANCEYEAR = DEEDDATEOFREFINANCEYEAR;
    }

    public Integer getDEEDDATEOFREFINANCEMONTH() {
        return DEEDDATEOFREFINANCEMONTH;
    }

    public void setDEEDDATEOFREFINANCEMONTH(Integer DEEDDATEOFREFINANCEMONTH) {
        this.DEEDDATEOFREFINANCEMONTH = DEEDDATEOFREFINANCEMONTH;
    }

    public Integer getDEEDDATEOFREFINANCEDAY() {
        return DEEDDATEOFREFINANCEDAY;
    }

    public void setDEEDDATEOFREFINANCEDAY(Integer DEEDDATEOFREFINANCEDAY) {
        this.DEEDDATEOFREFINANCEDAY = DEEDDATEOFREFINANCEDAY;
    }

    public Integer getREFINANCEAMOUNTINTHOUSANDS() {
        return REFINANCEAMOUNTINTHOUSANDS;
    }

    public void setREFINANCEAMOUNTINTHOUSANDS(Integer REFINANCEAMOUNTINTHOUSANDS) {
        this.REFINANCEAMOUNTINTHOUSANDS = REFINANCEAMOUNTINTHOUSANDS;
    }

    public String getREFINANCELENDERNAME() {
        return REFINANCELENDERNAME;
    }

    public void setREFINANCELENDERNAME(String REFINANCELENDERNAME) {
        this.REFINANCELENDERNAME = REFINANCELENDERNAME;
    }

    public Boolean getREFINANCELENDERNAMEAVAILABLE() {
        return REFINANCELENDERNAMEAVAILABLE;
    }

    public void setREFINANCELENDERNAMEAVAILABLE(Boolean REFINANCELENDERNAMEAVAILABLE) {
        this.REFINANCELENDERNAMEAVAILABLE = REFINANCELENDERNAMEAVAILABLE;
    }

    public String getREFINANCERATETYPE() {
        return REFINANCERATETYPE;
    }

    public void setREFINANCERATETYPE(String REFINANCERATETYPE) {
        this.REFINANCERATETYPE = REFINANCERATETYPE;
    }

    public String getREFINANCELOANTYPE() {
        return REFINANCELOANTYPE;
    }

    public void setREFINANCELOANTYPE(String REFINANCELOANTYPE) {
        this.REFINANCELOANTYPE = REFINANCELOANTYPE;
    }

    public String getCENSUSMEDIANHOMEVALUE() {
        return CENSUSMEDIANHOMEVALUE;
    }

    public void setCENSUSMEDIANHOMEVALUE(String CENSUSMEDIANHOMEVALUE) {
        this.CENSUSMEDIANHOMEVALUE = CENSUSMEDIANHOMEVALUE;
    }

    public String getCENSUSMEDIANHOUSEHOLDINCOME() {
        return CENSUSMEDIANHOUSEHOLDINCOME;
    }

    public void setCENSUSMEDIANHOUSEHOLDINCOME(String CENSUSMEDIANHOUSEHOLDINCOME) {
        this.CENSUSMEDIANHOUSEHOLDINCOME = CENSUSMEDIANHOUSEHOLDINCOME;
    }

    public Integer getCRA_INCOMECLASSIFICATIONCODE() {
        return CRA_INCOMECLASSIFICATIONCODE;
    }

    public void setCRA_INCOMECLASSIFICATIONCODE(Integer CRA_INCOMECLASSIFICATIONCODE) {
        this.CRA_INCOMECLASSIFICATIONCODE = CRA_INCOMECLASSIFICATIONCODE;
    }

    public Integer getPURCHASEMORTGAGEDATE() {
        return PURCHASEMORTGAGEDATE;
    }

    public void setPURCHASEMORTGAGEDATE(Integer PURCHASEMORTGAGEDATE) {
        this.PURCHASEMORTGAGEDATE = PURCHASEMORTGAGEDATE;
    }

    public String getMOSTRECENTLENDERCODE() {
        return MOSTRECENTLENDERCODE;
    }

    public void setMOSTRECENTLENDERCODE(String MOSTRECENTLENDERCODE) {
        this.MOSTRECENTLENDERCODE = MOSTRECENTLENDERCODE;
    }

    public String getPURCHASELENDERNAME() {
        return PURCHASELENDERNAME;
    }

    public void setPURCHASELENDERNAME(String PURCHASELENDERNAME) {
        this.PURCHASELENDERNAME = PURCHASELENDERNAME;
    }

    public String getMOSTRECENTMORTGAGEINTERESTRATE() {
        return MOSTRECENTMORTGAGEINTERESTRATE;
    }

    public void setMOSTRECENTMORTGAGEINTERESTRATE(String MOSTRECENTMORTGAGEINTERESTRATE) {
        this.MOSTRECENTMORTGAGEINTERESTRATE = MOSTRECENTMORTGAGEINTERESTRATE;
    }

    public Integer getSEWER() {
        return SEWER;
    }

    public void setSEWER(Integer SEWER) {
        this.SEWER = SEWER;
    }

    public Integer getWATER() {
        return WATER;
    }

    public void setWATER(Integer WATER) {
        this.WATER = WATER;
    }

    public String getLOANTOVALUE() {
        return LOANTOVALUE;
    }

    public void setLOANTOVALUE(String LOANTOVALUE) {
        this.LOANTOVALUE = LOANTOVALUE;
    }

    public String getPASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE() {
        return PASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE;
    }

    public void setPASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE(String PASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE) {
        this.PASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE = PASSPROSPECTORVALUEHOMEVALUEMORTGAGEFILE;
    }

    public Boolean getEMAILFLAG() {
        return EMAILFLAG;
    }

    public void setEMAILFLAG(Boolean EMAILFLAG) {
        this.EMAILFLAG = EMAILFLAG;
    }

    public Integer getPHONETYPE() {
        return PHONETYPE;
    }

    public void setPHONETYPE(Integer PHONETYPE) {
        this.PHONETYPE = PHONETYPE;
    }

    public Boolean getDNC() {
        return DNC;
    }

    public void setDNC(Boolean DNC) {
        this.DNC = DNC;
    }

    public Long getPERSONDATEOFBIRTHDATE() {
        if (getPERSONDATEOFBIRTHYEAR() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, getPERSONDATEOFBIRTHYEAR());
            calendar.set(Calendar.MONTH, getPERSONDATEOFBIRTHMONTH());
            calendar.set(Calendar.DAY_OF_MONTH, getPERSONDATEOFBIRTHDAY());

            return calendar.getTimeInMillis();
        } else {
            return PERSONDATEOFBIRTHDATE;
        }
    }

    public void setPERSONDATEOFBIRTHDATE(Long PERSONDATEOFBIRTHDATE) {
        this.PERSONDATEOFBIRTHDATE = PERSONDATEOFBIRTHDATE;
    }

    public Long getHOMEPURCHASEDATE() {
        if (getHOMEPURCHASEDATEYEAR() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, getHOMEPURCHASEDATEYEAR());
            calendar.set(Calendar.MONTH, getHOMEPURCHASEDATEMONTH());
            calendar.set(Calendar.DAY_OF_MONTH, getHOMEPURCHASEDATEDAY());

            return calendar.getTimeInMillis();
        } else {
            return null;
        }
    }

    public void setHOMEPURCHASEDATE(Long HOMEPURCHASEDATE) {
        this.HOMEPURCHASEDATE = HOMEPURCHASEDATE;
    }

    public Long getDEEDDATEOFREFINANCE() {
        if (getDEEDDATEOFREFINANCEYEAR() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, getDEEDDATEOFREFINANCEYEAR());
            calendar.set(Calendar.MONTH, getDEEDDATEOFREFINANCEMONTH());
            calendar.set(Calendar.DAY_OF_MONTH, getDEEDDATEOFREFINANCEDAY());

            return calendar.getTimeInMillis();
        } else {
            return null;
        }
    }

    public void setDEEDDATEOFREFINANCE(Long DEEDDATEOFREFINANCE) {
        this.DEEDDATEOFREFINANCE = DEEDDATEOFREFINANCE;
    }

    public Integer getPERSONAGE() {
        return PERSONAGE;
    }

    public void setPERSONAGE(Integer PERSONAGE) {
        this.PERSONAGE = PERSONAGE;
    }

    public String getHASH() {
        return HASH;
    }

    public void setHASH(String HASH) {
        this.HASH = HASH;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }
}
