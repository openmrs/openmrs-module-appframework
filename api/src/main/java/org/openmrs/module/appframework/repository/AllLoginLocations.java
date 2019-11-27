package org.openmrs.module.appframework.repository;

import org.openmrs.Location;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent all <em>login</em> locations
 */
@Repository
public class AllLoginLocations {
	
	private List<Location> loginLocations = new ArrayList<Location>();
	
	public AllLoginLocations() {
	}
	
	public void add(List<Location> locations) {
		for (Location location : locations) {
			add(location);
		}
	}
	
	public void add(Location location) {
		loginLocations.add(location);
	}
	
	public List<Location> getLoginLocations() {
		List<Location> loginLocationsList = new ArrayList<Location>();
		loginLocationsList.addAll(this.loginLocations);
		return loginLocationsList;
	}
	
	public void clear() {
		loginLocations.clear();
	}
}
