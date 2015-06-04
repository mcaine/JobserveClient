package com.mikeycaine.jobserve;

import java.util.concurrent.Future;

public interface JobserveAPI {
  Future<CountryCollection> getAllCountries() throws InterruptedException;
  Future<IndustryCollection> getAllIndustries() throws InterruptedException;
  Future<SalaryFrequencyCollection> getAllSalaryFrequencies() throws InterruptedException;
  Future<CurrencyCollection> getAllSupportedCurrencies() throws InterruptedException;
  Future<JobSearch> getJobSearchDefaults() throws InterruptedException;
  Future<ValueListsIndex> getJobSearchValueLists() throws InterruptedException;
  Future<JobTypeCollection> getJobTypes() throws InterruptedException;
  Future<GeoLocationMatch> getLocationMatch(double latitude, double longitude, double maxDistance) throws InterruptedException;
  Future<JobCollection> getMultipleJobDetailsGET(IDCollection ids) throws InterruptedException;
  Future<JobCollection> getMultipleJobDetailsPOST(IDCollection ids) throws InterruptedException;
  Future<SalaryBandsCollection> getSalaryCollections(String country, String currency, String frequency) throws InterruptedException;
  Future<Country> getSingleCountry(String id) throws InterruptedException;
  Future<Industry> getSingleIndustry(String id) throws InterruptedException;
  Future<Job> getSingleJobDetails(String id) throws InterruptedException;
  Future<WebServiceVersionInfo> getVersion() throws InterruptedException;
  Future<JobSearchResults> jobSearchGET(JobSearch search) throws InterruptedException;
  Future<JobSearchResults> jobSearchPOST(JobSearch search) throws InterruptedException;
}
