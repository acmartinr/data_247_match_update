package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.db.dao.DataDAO;
import services.db.entity.DetailedBusinessIndustry;

import java.util.List;

@Singleton
public class Dictionary extends Controller {

    private DataDAO dataDAO;

    @Inject
    public Dictionary(DataDAO dataDAO) {
        this.dataDAO = dataDAO;
    }

    public Result cities(String state) {
        return ok(Json.toJson(Response.OK(dataDAO.findCitiesByState(state))));
    }

    public Result zipCodes(String state) {
        return ok(Json.toJson(Response.OK(dataDAO.findZipCodesByState(state))));
    }

    public Result areaCodes(String state) {
        return ok(Json.toJson(Response.OK(dataDAO.findAreaCodesByState(state))));
    }

    public Result counties(String state) {
        return ok(Json.toJson(Response.OK(dataDAO.findCountiesByState(state))));
    }

    public Result lenders(String value) {
        return ok(Json.toJson(Response.OK(dataDAO.findAllLenders(value))));
    }

    public Result industries() {
        return ok(Json.toJson(Response.OK(dataDAO.findIndustries())));
    }

    public Result categories() {
        return ok(Json.toJson(Response.OK(dataDAO.findCategories())));
    }

    public Result sources() {
        return ok(Json.toJson(Response.OK(dataDAO.findCategories())));
    }
    public Result everydataCategories() {
        return ok(Json.toJson(Response.OK(dataDAO.findEverydataCategories())));
    }


    public Result sourcesDomain(String domainSource, String tableName) {
        return ok(Json.toJson(Response.OK(dataDAO.findDomainSources(domainSource, tableName))));
    }

    public Result carrierBrand(String tableName) {
        return ok(Json.toJson(Response.OK(dataDAO.findCarrierBrand(tableName))));
    }

    public Result facebookJobs(String tableName) {
        return ok(Json.toJson(Response.OK(dataDAO.findFacebookJobs(tableName))));
    }

    public Result facebookHLastName(String strSearch) {
        if(strSearch.equals(" ")){
            strSearch = "";
        }
        return ok(Json.toJson(Response.OK(dataDAO.findFacebookHLastName(strSearch))));
    }

    public Result studentSources() {
        return ok(Json.toJson(Response.OK(dataDAO.findStudentSources())));
    }

    public Result autoMakes(String search, int limit) {
        List<String> makes = dataDAO.findAutoMakes(search, limit);
        int total = dataDAO.findAutoMakes(search, 10000).size();

        return ok(Json.toJson(Response.OK(new AutoResponse(total, null, makes))));
    }

    public Result c2carriersName(String carrierName) {
        List<String> carriers = dataDAO.findC2Carriers(carrierName);
        return ok(Json.toJson(Response.OK(carriers)));
    }

    public Result autoModels(String make, String search, int limit) {
        List<String> models = dataDAO.findAutoModels(make, search, limit);
        int total = dataDAO.findAutoModels(make, search, 10000).size();

        return ok(Json.toJson(Response.OK(new AutoResponse(total, models, null))));
    }

    public Result titles() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);

        List<String> titles = dataDAO.findTitles(request);
        Integer count = dataDAO.findTitlesCount(request);

        return ok(Json.toJson(Response.OK(new StringListResponse(count, titles))));
    }

    public Result businessTitles() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);

        List<String> titles = dataDAO.findBusinessTitles(request);
        Integer count = dataDAO.findBusinessTitlesCount(request);

        return ok(Json.toJson(Response.OK(new StringListResponse(count, titles))));
    }

    public Result detailedBusinessIndustries() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);

        List<DetailedBusinessIndustry> industries = dataDAO.findDetailedBusinessIndustries(request);
        Integer count = dataDAO.findDetailedBusinessIndustriesCount(request);

        return ok(Json.toJson(Response.OK(new DetailedBusinessIndustryListResponse(count, industries))));
    }

    public Result selectedDetailedBusinessIndustries() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);
        List<DetailedBusinessIndustry> industries = dataDAO.findDetailedBusinessIndustriesByValues(request);

        return ok(Json.toJson(Response.OK(industries)));
    }

    public Result instagramCategories() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);

        List<String> categories = dataDAO.findInstagramCategories(request);
        Integer count = dataDAO.findInstagramCategoriesCount(request);

        return ok(Json.toJson(Response.OK(new StringListResponse(count, categories))));
    }

    public Result optinSources() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);

        List<String> sources = dataDAO.findOptinSources(request);
        Integer count = dataDAO.findOptinSourcesCount(request);

        return ok(Json.toJson(Response.OK(new StringListResponse(count, sources))));
    }

}
