package com.sugestio.client.call;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RecommendationFilter {
	
	private List<String> categories;
	private List<String> segments;
	private Integer limit;
	
	
	public RecommendationFilter() {
		this.categories = new ArrayList<String>();
		this.segments = new ArrayList<String>();
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
		
		if (limit != null) {
			params.add("limit", ""+limit);
		}
		
		return params;
	}
}
