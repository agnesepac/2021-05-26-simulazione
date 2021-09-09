package it.polito.tdp.yelp.model;

import java.util.List;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao ;
	
	public List<String> getAllCities(){
		dao = new YelpDao();
		return dao.getAllCities();
	}
	
}
