package com.mikeycaine.jobserve;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class APITest {

	@Autowired
	JobserveAPI jobserve;

	@Test
	public void testGeoLocation() throws InterruptedException, ExecutionException {
		double latitude = 51.50442;
		double longitude = -0.01835;
		double maxDistance = 10000.0;
		Future<GeoLocationMatch> geoFuture = jobserve.getLocationMatch(latitude, longitude, maxDistance);
		GeoLocationMatch geo = geoFuture.get();

		Location loc = geo.getLocation().getValue();
		System.out.println("GEO Location Match " + loc.getText().getValue());

		Assert.assertEquals("Location checks OK", "Canary Wharf", loc.getText().getValue());
	}

	@Test
	public void testCountries() throws Exception {
		Future<CountryCollection> countries = jobserve.getAllCountries();
		List<Country> countryList = countries.get().getCountry();
		
		Set<String> countryNames = new HashSet<>();
		for (Country country : countryList) {
			System.out.println(country.displayName.getValue());
			countryNames.add(country.displayName.getValue());
		}
		
		Assert.assertTrue(countryNames.contains("UK"));
		Assert.assertTrue(countryNames.contains("Canada"));
	}
	
	@Test
	public void testIndustries() throws InterruptedException, ExecutionException {
		Future<IndustryCollection> industries = jobserve.getAllIndustries();
		List<Industry> industryList = industries.get().getIndustry();
		
		Set<String> industryNames = new HashSet<>();
		for (Industry industry : industryList) {
			System.out.println(industry.displayName.getValue());
			industryNames.add(industry.displayName.getValue());
		}
		
		Assert.assertTrue(industryNames.contains("IT"));
		Assert.assertTrue(industryNames.contains("Legal"));
	}
	
	@Test
	public void testSalaryFrequencies() throws InterruptedException, ExecutionException {
		Future<SalaryFrequencyCollection> salaryFrequencies = jobserve.getAllSalaryFrequencies();
		List<SalaryFrequency> salaryFreqList = salaryFrequencies.get().getSalaryFrequency();
		
		Set<String> frequencies = new HashSet<>();
		for (SalaryFrequency freq : salaryFreqList) {
			System.out.println(freq.getText().getValue());
			frequencies.add(freq.getText().getValue());
		}
		Assert.assertTrue(frequencies.contains("Annual"));
		Assert.assertTrue(frequencies.contains("Hourly"));
	}
	
	@Test
	public void testCurrencies() throws InterruptedException, ExecutionException {
		Future<CurrencyCollection> currencies = jobserve.getAllSupportedCurrencies();
		List<Currency> currencyList = currencies.get().getCurrency();
		
		Set<String> currencyNames = new HashSet<>();
		for (Currency curr : currencyList) {
			System.out.println(curr.getName().getValue());
			currencyNames.add(curr.getName().getValue());
		}
		Assert.assertTrue(currencyNames.contains("British Pound"));
		Assert.assertTrue(currencyNames.contains("US Dollar"));
	}
	
	@Test
	public void testJobSearchValueLists() throws InterruptedException, ExecutionException {
		Future<ValueListsIndex> lists = jobserve.getJobSearchValueLists();
		List<ValueListsIndex.ValueList> valueLists = lists.get().getValueList();
		
		Set<String> valueListNames = new HashSet<>();
		for (ValueListsIndex.ValueList valueList : valueLists) {
			System.out.println("Value list: " + valueList.getName());
			valueListNames.add(valueList.getName());
		}
		
		Assert.assertTrue(valueListNames.contains("MaxDistance"));
		Assert.assertTrue(valueListNames.contains("MaxAge"));
		Assert.assertTrue(valueListNames.contains("SortOrder"));
	}
	
	@Test
	public void testJobTypes() throws InterruptedException, ExecutionException {
		Future<JobTypeCollection> lists = jobserve.getJobTypes();
		List<JobType> jobTypes = lists.get().getJobType();
		
		Set<String> jobTypeNames = new HashSet<>();
		for (JobType jobType: jobTypes) {
			System.out.println("Job Type: " + jobType.getText().getValue());
			jobTypeNames.add(jobType.getText().getValue());
		}
		
		Assert.assertTrue(jobTypeNames.contains("Any"));
		Assert.assertTrue(jobTypeNames.contains("Permanent"));
		Assert.assertTrue(jobTypeNames.contains("Contract"));
	}
	
	@Test
	public void testJobSearchDefaults() throws InterruptedException, ExecutionException {
		Future<JobSearch> jobSearch = jobserve.getJobSearchDefaults();
		System.out.println("Job Search Defaults Max Distance: " + jobSearch.get().getMaxDistance().getValue());
		Assert.assertEquals("25.0", jobSearch.get().getMaxDistance().getValue().toString());
	}
	
	@Test
	public void testJobSearchPOST() throws InterruptedException, ExecutionException {
		Future<JobSearch> defaultsFuture = jobserve.getJobSearchDefaults();
		JobSearch defaults = defaultsFuture.get();
		//System.out.println("Default Max distance (should be 25): " + defaults.getMaxDistance().getValue());
		
		defaults.setAccountGroupIDs(null);
		defaults.setAccountIDs(null);
		
		IDCollection industries = new IDCollection();
		industries.getID().add("IT");
		defaults.setIndustries(new ObjectFactory().createIDCollection(industries));
		
		defaults.setSkills(new ObjectFactory().createJobSearchSkills("perl"));
		
		Future<JobSearchResults> results = jobserve.jobSearchPOST(defaults);
		JobSearchResults jobSearchResults = results.get();
		System.out.println("Found " + jobSearchResults.getJobCount() + " jobs");
		System.out.println("Page " + jobSearchResults.getPageNo() + " of " + jobSearchResults.getPageCount() + " pages");
		System.out.println("Page size is " + jobSearchResults.getPageSize());
		
		IDCollection jobIDCollection = jobSearchResults.getJobIDs().getValue();
		for (String id : jobIDCollection.getID()) {
			System.out.println("Job ID " + id);
		}
		
		JobCollection jobCollection = jobSearchResults.getJobs().getValue();
		for (Job job : jobCollection.getJob()) {
			System.out.println();
			System.out.println(job.getShortDescription().getValue());
			System.out.println(job.getRecruiterName().getValue());
		}
		
		// Usually we get ~300 or so
		Assert.assertTrue(jobSearchResults.getJobCount() > 10);
	}
	
	@Test
	public void testJobSearchGET() throws InterruptedException, ExecutionException {
		fail("Not implemented");
	}
	
	@Test
	public void testSingleJobDetails() throws InterruptedException, ExecutionException {
		fail("Not implemented");
	}
}