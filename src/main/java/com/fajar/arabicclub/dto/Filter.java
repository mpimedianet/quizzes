package com.fajar.arabicclub.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fajar.arabicclub.annotation.Dto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Filter implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5151185528546046666L;
	@Builder.Default
	private Integer limit = 0;
	@Builder.Default
	private Integer page = 0;
	private String orderType;
	private String orderBy;
	@Builder.Default
	private boolean contains = true;
	@Builder.Default
	private boolean exacts = false;
	@Builder.Default
	private Integer day = 0;
	@Builder.Default
	private Integer year = 0;
	@Builder.Default
	private Integer month = 0; 
	@Builder.Default
	private Map<String, Object> fieldsFilter = new HashMap<>();
	private Boolean availabilityCheck;
	
	private Integer dayTo;
	private Integer monthTo;
	private Integer yearTo; 
	
	@JsonIgnore
	private int maxValue;

}
