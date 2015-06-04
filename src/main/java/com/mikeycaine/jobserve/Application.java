package com.mikeycaine.jobserve;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {
	
	@Autowired
	JobserveAPI jobserve;

	@Override
	public void run(String... args) throws Exception {
		//countries();
		//industries();
		//salaryFrequencies();
		//currencies();
		//jobSearchDefaults();
		//jobSearchValueLists();
		//jobTypes();
		jobSearchPOST();
		//jobSearchGET();
		//singleJobDetails();
	}

	void countries() throws InterruptedException, ExecutionException {
		// Start the clock
		long start = System.currentTimeMillis();

		System.out.println("[TIME 1] " + (System.currentTimeMillis() - start));
		Future<CountryCollection> countries = jobserve.getAllCountries();
		System.out.println("[TIME 2] " + (System.currentTimeMillis() - start));

		System.out.println("[TIME 3] " + (System.currentTimeMillis() - start));

		List<Country> countryList = countries.get().getCountry();

		System.out.println("[TIME 4] " + (System.currentTimeMillis() - start));

		for (Country country : countryList) {
			System.out.println(country.displayName.getValue());
		}
	}

	void industries() throws InterruptedException, ExecutionException {
		// Start the clock
		long start = System.currentTimeMillis();

		System.out.println("[TIME 1] " + (System.currentTimeMillis() - start));
		Future<IndustryCollection> industries = jobserve.getAllIndustries();
		System.out.println("[TIME 2] " + (System.currentTimeMillis() - start));



		System.out.println("[TIME 3] " + (System.currentTimeMillis() - start));

		List<Industry> industryList = industries.get().getIndustry();

		System.out.println("[TIME 4] " + (System.currentTimeMillis() - start));

		for (Industry industry : industryList) {
			System.out.println(industry.displayName.getValue());
		}
	}
	
	void salaryFrequencies() throws InterruptedException, ExecutionException {
		// Start the clock
		long start = System.currentTimeMillis();

		System.out.println("[TIME 1] " + (System.currentTimeMillis() - start));
		Future<SalaryFrequencyCollection> salaryFrequencies = jobserve.getAllSalaryFrequencies();
		System.out.println("[TIME 2] " + (System.currentTimeMillis() - start));

		System.out.println("[TIME 3] " + (System.currentTimeMillis() - start));

		List<SalaryFrequency> salaryFreqList = salaryFrequencies.get().getSalaryFrequency();

		System.out.println("[TIME 4] " + (System.currentTimeMillis() - start));

		for (SalaryFrequency freq : salaryFreqList) {
			System.out.println(freq.getText().getValue());
		}
	}
	
	void currencies() throws InterruptedException, ExecutionException {
		Future<CurrencyCollection> currencies = jobserve.getAllSupportedCurrencies();
		List<Currency> currencyList = currencies.get().getCurrency();
		for (Currency curr : currencyList) {
			System.out.println(curr.getName().getValue());
		}
	}
	
	void jobSearchDefaults() throws InterruptedException, ExecutionException {
		Future<JobSearch> jobSearch = jobserve.getJobSearchDefaults();
		System.out.println("Job Search Defaults Max Distance: " + jobSearch.get().getMaxDistance().getValue());
	}
	
	void jobSearchValueLists() throws InterruptedException, ExecutionException {
		Future<ValueListsIndex> lists = jobserve.getJobSearchValueLists();
		List<ValueListsIndex.ValueList> valueLists = lists.get().getValueList();
		for (ValueListsIndex.ValueList valueList : valueLists) {
			System.out.println("Value list: " + valueList.getName());
		}
	}
	
	void jobTypes() throws InterruptedException, ExecutionException {
		Future<JobTypeCollection> lists = jobserve.getJobTypes();
		List<JobType> jobTypes = lists.get().getJobType();
		for (JobType jobType: jobTypes) {
			System.out.println("Job Type: " + jobType.getText().getValue());
		}
	}
	
	void jobSearchPOST() throws InterruptedException, ExecutionException {
		Future<JobSearch> defaultsFuture = jobserve.getJobSearchDefaults();
		JobSearch defaults = defaultsFuture.get();
		System.out.println("Default Max distance (should be 25): " + defaults.getMaxDistance().getValue());
		
		defaults.setAccountGroupIDs(null);
		defaults.setAccountIDs(null);
		
		IDCollection industries = new IDCollection();
		industries.getID().add("IT");
		//industries.getID().add("Legal");
		defaults.setIndustries(new ObjectFactory().createIDCollection(industries));
		
		//defaults.setJobIDsOnly(true);
		defaults.setSkills(new ObjectFactory().createJobSearchSkills("perl"));
		
		Future<JobSearchResults> results = jobserve.jobSearchPOST(defaults);
		JobSearchResults jobSearchResults = results.get();
		System.out.println("Found " + jobSearchResults.getJobCount() + " jobs");
		
	}
	
	void jobSearchGET() throws InterruptedException, ExecutionException {
		Future<JobSearch> defaultsFuture = jobserve.getJobSearchDefaults();
		JobSearch defaults = defaultsFuture.get();
		System.out.println("Default Max distance (should be 25): " + defaults.getMaxDistance().getValue());
		
		defaults.setAccountGroupIDs(null);
		defaults.setAccountIDs(null);
		
		IDCollection industries = new IDCollection();
		industries.getID().add("IT");
		industries.getID().add("Legal");
		defaults.setIndustries(new ObjectFactory().createIDCollection(industries));
		
		Future<JobSearchResults> results = jobserve.jobSearchGET(defaults);
		JobSearchResults jobSearchResults = results.get();
		System.out.println("Found " + jobSearchResults.getJobCount() + " jobs");
		
	}
	
	void singleJobDetails() throws InterruptedException, ExecutionException {

	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}