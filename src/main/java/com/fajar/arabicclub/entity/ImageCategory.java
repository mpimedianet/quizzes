package com.fajar.arabicclub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fajar.arabicclub.annotation.Dto;
import com.fajar.arabicclub.annotation.FormField;
import com.fajar.arabicclub.constants.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Dto("Image Category")
@Entity
@Table (name="image_category")
@Data
@Builder	
@AllArgsConstructor
@NoArgsConstructor
public class ImageCategory extends BaseEntity {/**
	 * 
	 */
	private static final long serialVersionUID = -1168912843978053906L; 
	@FormField 
	@Column(unique = true)
	private String code;
	@FormField 
	@Column(unique = true)
	private String name;
	@FormField 
	@Column(name = "icon_class")
	private String iconClassName;
	@FormField ( type= FieldType.FIELD_TYPE_TEXTAREA) 
	@Column
	private String description;
}