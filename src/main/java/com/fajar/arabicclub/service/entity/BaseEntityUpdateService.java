package com.fajar.arabicclub.service.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.arabicclub.annotation.FormField;
import com.fajar.arabicclub.config.LogProxyFactory;
import com.fajar.arabicclub.dto.WebResponse;
import com.fajar.arabicclub.entity.BaseEntity;
import com.fajar.arabicclub.entity.User;
import com.fajar.arabicclub.entity.setting.EntityUpdateInterceptor;
import com.fajar.arabicclub.entity.setting.MultipleImageModel;
import com.fajar.arabicclub.entity.setting.SingleDocumentModel;
import com.fajar.arabicclub.entity.setting.SingleImageModel;
import com.fajar.arabicclub.repository.EntityRepository;
import com.fajar.arabicclub.service.SessionValidationService;
import com.fajar.arabicclub.service.resources.DocumentUploadService;
import com.fajar.arabicclub.service.resources.FileService;
import com.fajar.arabicclub.service.resources.ImageUploadService;
import com.fajar.arabicclub.util.EntityUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BaseEntityUpdateService<T extends BaseEntity> {

	@Autowired
	protected FileService fileService;
	@Autowired
	protected EntityRepository entityRepository;
	@Autowired
	private SessionValidationService sessionValidationService; 
	@Autowired
	private ImageUploadService imageUploadService;
	@Autowired
	private DocumentUploadService documentUploadService;
	
	@PostConstruct
	public void init() {
		LogProxyFactory.setLoggers(this);
	}

	public T saveEntity(T object, boolean newRecord, HttpServletRequest httpServletRequest) throws Exception {
		log.error("SaveEntity Method NOT IMPLEMENTED IN BASEENTITY UPDATE SERVICE");
		
		throw new NotImplementedException("SaveEntity Method NOT IMPLEMENTED IN BASEENTITY UPDATE SERVICE");
	}
	
	public void postFilter(List<T> objects) {
		
	}

	public WebResponse deleteEntity(Long id, Class _class, HttpServletRequest httpServletRequest) throws Exception {
		 
		try {
			boolean deleted = entityRepository.deleteById(id, _class);
			return new WebResponse();
		}catch (Exception e) { 
			throw e;
		} finally { 
		}
	}

	protected T copyNewElement(T source, boolean newRecord) {
		try {
			return (T) EntityUtil.copyFieldElementProperty(source, source.getClass(), !newRecord);
		}catch (Exception e) {
			log.error("Error copy new element");
			e.printStackTrace();
			return source;
		}
	}

	protected List<String> removeNullItemFromArray(String[] array) {
		List<String> result = new ArrayList<>();
		for (String string : array) {
			if (string != null) {
				result.add(string);
			}
		}
		return result;

	}
	
	protected User getLoggedUser(HttpServletRequest httpServletRequest) {
		return sessionValidationService.getLoggedUser(httpServletRequest);
	}
	
	protected EntityUpdateInterceptor<T> getUpdateInterceptor(T baseEntity){
		return baseEntity.modelUpdateInterceptor();
	}
	
	/**
	 * validate object properties' value annotated with @FormField
	 * 
	 * @param object
	 * @param newRecord
	 */
	protected void validateEntityFormFields(BaseEntity object, boolean newRecord, HttpServletRequest httpServletRequest) {
		log.info("validating entity: {} newRecord: {}", object.getClass(), newRecord);
		object.validateNullValues();
		try {

			BaseEntity existingEntity = null;
			if (!newRecord) {
				existingEntity = (BaseEntity) entityRepository.findById(object.getClass(), object.getId());
				if (null == existingEntity) {
					throw new Exception("Existing Entity Not Found");
				}
				object.validateNullValues();
			}
			Class modelClass = object.getTypeArgument();
			log.info("{} -> model: {}", object.getClass(), modelClass);
			List<Field> modelFields = EntityUtil.getDeclaredFields(modelClass);
			for (int i = 0; i < modelFields.size(); i++) {
				Field modelField = modelFields.get(i);
				FormField formfield = modelField.getAnnotation(FormField.class);
				if (null == formfield) {
					log.info("FormField not present at model field: {}", modelField.getName());
					continue;
				}
				
				Field field = EntityUtil.getDeclaredField(object.getClass(), modelField.getName());
				if (null == field) {
					log.debug("no field with name {} in entity:{}", modelField.getName(), object.getClass());
					continue;
				}

				try {

					Object fieldValue = field.get(object);
					log.info("validating field: {}, type: {}", field.getName(), formfield.type());
					if (fieldValue == null) {
						log.info("!! Skipping null-valued field: {}", field.getName());
						continue;
					}
					boolean isUpdateRecord =  newRecord == false;
					switch (formfield.type()) {
					case FIELD_TYPE_DOCUMENT:
						if (isUpdateRecord &&  fieldValue.equals(field.get(existingEntity))) { 
							field.set(object, field.get(existingEntity));
							break; 
						} 
						if (object instanceof SingleDocumentModel && ((SingleDocumentModel) object).getAttachmentInfo()!=null) {
							log.info("{} is instance of SingleDocumentModel", object.getClass());
							documentUploadService.upload((SingleDocumentModel) object, httpServletRequest);
						}
						break;
					case FIELD_TYPE_IMAGE:
						
						if (isUpdateRecord &&  fieldValue.equals(field.get(existingEntity))) { 
							field.set(object, field.get(existingEntity));
							break; 
						} 
						if (object instanceof SingleImageModel) {
							log.info("{} is instance of SingleImageModel", object.getClass());
							imageUploadService.upload((SingleImageModel) object, httpServletRequest);
						}
						
						if (object instanceof MultipleImageModel) {
							log.info("{} is multiple image model", object.getClass());
							if (newRecord) {
								imageUploadService.writeNewImages((MultipleImageModel) object, httpServletRequest);
							}else {
								MultipleImageModel existing = (MultipleImageModel) existingEntity;
								imageUploadService.updateImages((MultipleImageModel) object, existing , httpServletRequest);
							}
						}
//						if (isUpdateRecord &&  fieldValue.equals(field.get(existingEntity))) {
//							Object existingImage = field.get(existingEntity);
//							log.info("existingImage : {}", existingImage);
//							if ( existingImage.equals(fieldValue)) {
//								field.set(object, existingImage);
//							}
//						} else {
//							String imageName = updateImage(field, object, formfield.iconImage());
//							field.set(object, imageName);
//						}
						break;
//					case FIELD_TYPE_FIXED_LIST:
//						
//						if (formfield.multipleSelect()) {
//							String storeToFieldName = field.getAnnotation(StoreValueTo.class).value(); 
//							
//							Field idField = CollectionUtil.getIDFieldOfUnderlyingListType(field);
//							Field storeToField = EntityUtil.getDeclaredField(object.getClass(), storeToFieldName);
//							
//							Object[] valueAsArray = ((Collection) fieldValue).toArray(); 
//							CharSequence[] actualFieldValue = new String[valueAsArray.length];
//							
//							for (int j = 0; j < valueAsArray.length; j++) {
//								actualFieldValue[j] = String.valueOf(idField.get(valueAsArray[j]));
//							}
//							
//							storeToField.set(object, String.join("~", actualFieldValue));
//						}
//						break;
					default:
						break;
					}
				} catch (Exception e) {
					log.error("Error validating field: {}", field.getName());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			//
			log.error("Error validating entity {}", object.getClass().getSimpleName());
			e.printStackTrace();
		}
	}

	

	
}
