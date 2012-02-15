package com.sugestio.client.call;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RecommendationFilter {
	
	private List<String> categories;
	private List<String> segments;
	private List<String> tags;
	private List<String> location_simples;
	private List<String> location_cities;
	private Integer limit;
	
	/**
	 * Creates an empty RecommendationFilter
	 */
	public RecommendationFilter() {
		this.categories = new LinkedList<String>();
		this.segments = new LinkedList<String>();
		this.tags = new LinkedList<String>();
		this.location_simples = new LinkedList<String>();
		this.location_cities = new LinkedList<String>();
		this.limit = null;
	}
	
	/**
	 * Retrieved items must (not) belong to this category
	 * @param category
	 * @param belongs
	 */
	public void inCategory(String category, boolean belongs) {
		if (belongs)
			this.categories.add(category);
		else
			this.categories.add("!" + category);
	}
	
	/**
	 * Retrieved items must (not) belong to this segment
	 * @param segment
	 * @param belongs
	 */
	public void inSegment(String segment, boolean belongs) {
		if (belongs)
			this.segments.add(segment);
		else
			this.segments.add("!" + segment);
	}
	
	/**
	 * Retrieved items must (not) have this tag
	 * @param tag
	 * @param value
	 */
	public void hasTag(String tag, boolean value) {
		if (value)
			this.tags.add(tag);
		else
			this.tags.add("!" + tag);
	}
	
	/**
	 * Retrieved items must (not) be in this location
	 * @param location
	 * @param value
	 */
	public void inLocation_simple(String location, boolean value) {
		if (value)
			this.location_simples.add(location);
		else
			this.location_simples.add("!" + location);
	}
	
	/**
	 * Retrieved items must (not) be in this location
	 * @param location
	 * @param value
	 */
	public void inLocation_city(String location, boolean value) {
		if (value)
			this.location_cities.add(location);
		else
			this.location_cities.add("!" + location);
	}
	
	/**
	 * Maximum number of recommendations to be retrieved.
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	protected MultivaluedMap<String, String> toQueryParams() {
		
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		
		for (String category : categories) {
			params.add("category[]", category);
		}
		
		for (String segment : segments) {
			params.add("segment[]", segment);
		}
		
		for (String tag : tags) {
			params.add("tag[]", tag);
		}
		
		for (String location : location_simples) {
			params.add("location_simple[]", location);
		}
		
		for (String location : location_cities) {
			params.add("location_city[]", location);
		}
		
		if (limit != null) {
			params.add("limit", ""+limit);
		}
		
		return params;
	}
}
