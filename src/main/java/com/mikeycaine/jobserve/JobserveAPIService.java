package com.mikeycaine.jobserve;

import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JobserveAPIService implements JobserveAPI {

	private final static String BASE_URL = "http://services.jobserve.com/";
	
	@Value("${jobserve.api.key}")
	private String apiKey;
	
	//@Autowired
	private  RestTemplate restTemplate;
	
	private HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		
	{
		 RestTemplate theRestTemplate = new RestTemplate();
		 
		 Jaxb2RootElementHttpMessageConverter converter2 = new Jaxb2RootElementHttpMessageConverter();
		 converter2.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_XML));
		 
		 List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		 messageConverters.add(converter2);
		 
		 theRestTemplate.setMessageConverters(messageConverters);
		 
		 restTemplate = theRestTemplate;
}
	
	private HttpHeaders authorizationHeaders() {
		HttpHeaders headers = new HttpHeaders();
		addHeaders(headers);

		return headers;
	}
	
	private void addHeaders(HttpHeaders headers) {
		headers.set("Authorization", "Token " + apiKey);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		headers.setContentType(MediaType.APPLICATION_XML);
	}

	@SuppressWarnings("unchecked")
  private <T> T doGet(String url, Class<?> resultClass) {
		return (T) restTemplate.exchange(BASE_URL + url, HttpMethod.GET, new HttpEntity<Object>(authorizationHeaders()),
		    resultClass).getBody();
	}
	
	@Override
	@Async
	public Future<CountryCollection> getAllCountries() throws InterruptedException {
		System.out.println("getAllCountries()");
		CountryCollection results = doGet("Countries", CountryCollection.class);
		return new AsyncResult<CountryCollection>(results);
	}

	@Override
	@Async
	public Future<IndustryCollection> getAllIndustries() throws InterruptedException {
		System.out.println("getAllIndustries()");
		IndustryCollection results = doGet("Industries", IndustryCollection.class);
		return new AsyncResult<IndustryCollection>(results);
	}

	@Override
	@Async
  public Future<SalaryFrequencyCollection> getAllSalaryFrequencies() throws InterruptedException {
		SalaryFrequencyCollection results = doGet("Salaries/Frequencies", SalaryFrequencyCollection.class);
		return new AsyncResult<SalaryFrequencyCollection>(results);
  }

	@Override
	@Async
  public Future<CurrencyCollection> getAllSupportedCurrencies() throws InterruptedException {
		CurrencyCollection results = doGet("Currencies", CurrencyCollection.class);
		return new AsyncResult<CurrencyCollection>(results);
  }

	@Override
	@Async
  public Future<JobSearch> getJobSearchDefaults() throws InterruptedException {
		JobSearch results = doGet("JobSearchDefaults", JobSearch.class);
		return new AsyncResult<JobSearch>(results);
  }

	@Override
	@Async
  public Future<ValueListsIndex> getJobSearchValueLists() throws InterruptedException {
		ValueListsIndex results = doGet("JobSearchValueLists", ValueListsIndex.class);
		return new AsyncResult<ValueListsIndex>(results);
  }

	@Override
	@Async
  public Future<JobTypeCollection> getJobTypes() throws InterruptedException {
		JobTypeCollection results = doGet("JobTypes", JobTypeCollection.class);
		return new AsyncResult<JobTypeCollection>(results);
  }

	@Override
	@Async
  public Future<GeoLocationMatch> getLocationMatch(double latitude, double longitude, double maxDistance)
      throws InterruptedException {
		GeoLocationMatch results = doGet(String.format("Locations/%f/%f?maxDistance=%f", latitude, longitude, maxDistance), GeoLocationMatch.class);
		return new AsyncResult<GeoLocationMatch>(results);
  }

	@Override
	@Async
  public Future<JobCollection> getMultipleJobDetailsGET(IDCollection ids) throws InterruptedException {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
	@Async
  public Future<JobCollection> getMultipleJobDetailsPOST(IDCollection ids) throws InterruptedException {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
	@Async
  public Future<SalaryBandsCollection> getSalaryCollections(String country, String currency, String frequency)
      throws InterruptedException {
		SalaryBandsCollection results = doGet(String.format("Salaries/%s/%s/%s", country, currency, frequency), SalaryBandsCollection.class);
		return new AsyncResult<SalaryBandsCollection>(results);
  }

	@Override
	@Async
  public Future<Country> getSingleCountry(String id) throws InterruptedException {
		Country results = doGet(String.format("Countries/%s", id), Country.class);
		return new AsyncResult<Country>(results);
  }

	@Override
	@Async
  public Future<Industry> getSingleIndustry(String id) throws InterruptedException {
		Industry results = doGet(String.format("Industries/%s", id), Industry.class);
		return new AsyncResult<Industry>(results);
  }

	@Override
	@Async
  public Future<Job> getSingleJobDetails(String id) throws InterruptedException {
		Job results = doGet(String.format("Jobs/%s", id), Job.class);
		return new AsyncResult<Job>(results);
  }

	@Override
	@Async
  public Future<WebServiceVersionInfo> getVersion() throws InterruptedException {
		WebServiceVersionInfo results = doGet("Version", WebServiceVersionInfo.class);
		return new AsyncResult<WebServiceVersionInfo>(results);
  }

	@Override
	@Async
  public Future<JobSearchResults> jobSearchGET(JobSearch search) throws InterruptedException {
		HttpEntity<JobSearch> entity = new HttpEntity<JobSearch>(search, authorizationHeaders());
		//JobSearchResults results = restTemplate.postForObject(BASE_URL + "Jobs", entity, JobSearchResults.class);
		JobSearchResults results = (JobSearchResults) restTemplate.exchange(BASE_URL + "Jobs", HttpMethod.GET, entity, JobSearchResults.class).getBody();
		//JobSearchResults results = restTemplate.postForEntity(BASE_URL + "Jobs", entity, JobSearchResults.class).getBody();
		return new AsyncResult<JobSearchResults>(results);
  }

	@Override
	@Async
  public Future<JobSearchResults> jobSearchPOST(JobSearch search) throws InterruptedException {
    try {
    	JAXBElement<JobSearch> jobSearchElement = new ObjectFactory().createJobSearch(search);
    	JAXBContext context = JAXBContext.newInstance(JobSearch.class, JobSearchResults.class);
    	Marshaller marshaller = context.createMarshaller();
 
    	ClientHttpRequest request = requestFactory.createRequest(new URI(BASE_URL + "Jobs"), HttpMethod.POST);
    	
    	OutputStreamWriter bodyWriter = new OutputStreamWriter(request.getBody());
    	marshaller.marshal(jobSearchElement, bodyWriter);
    	bodyWriter.flush();
    	
    	addHeaders(request.getHeaders());
    
    	ClientHttpResponse response = request.execute();
    
    	Unmarshaller unmarshaller = context.createUnmarshaller();
    	
    	@SuppressWarnings("unchecked")
      JAXBElement<JobSearchResults> results = (JAXBElement<JobSearchResults>)unmarshaller.unmarshal(response.getBody());
    	
    	return new AsyncResult<JobSearchResults>(results.getValue());
    } catch (Exception e) {
	    e.printStackTrace();
    }
    
    return null;
  }
}
