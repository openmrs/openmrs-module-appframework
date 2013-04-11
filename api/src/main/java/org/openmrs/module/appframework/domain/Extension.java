package org.openmrs.module.appframework.domain;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

public class Extension implements Comparable<Extension> {
	
	@NotEmpty(message = ValidationErrorMessages.EXTENSION_ID_NOT_EMPTY_MESSAGE)
	@JsonProperty
	protected String id;
	
	@JsonProperty
	protected String appId;
	
	@JsonProperty
	protected String extensionPointId;
	
	@JsonProperty
	protected String type;
	
	@JsonProperty
	protected String label;
	
	@JsonProperty
	protected String url;
	
	@JsonProperty
	protected int order;
	
	@JsonProperty
	protected Map<String, Object> extensionParams;
	
	public Extension() {
	}
	
	public Extension(String id, String appId, String extensionPointId, String type, String label, String url, int order) {
		this.id = id;
		this.appId = appId;
		this.extensionPointId = extensionPointId;
		this.type = type;
		this.label = label;
		this.url = url;
		this.order = order;
	}
	
	public Extension(String id, String appId, String extensionPointId, String type, String label, String url, int order,
	    Map<String, Object> extensionParams) {
		this.id = id;
		this.appId = appId;
		this.extensionPointId = extensionPointId;
		this.type = type;
		this.label = label;
		this.url = url;
		this.order = order;
		this.extensionParams = extensionParams;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAppId() {
		return appId;
	}
	
	public String getExtensionPointId() {
		return extensionPointId;
	}
	
	public String getType() {
		return type;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getUrl() {
		return url;
	}
	
	public int getOrder() {
		return order;
	}
	
	public Map<String, Object> getExtensionParams() {
		return extensionParams;
	}
	
	public void setExtensionParams(Map<String, Object> extensionParams) {
		this.extensionParams = extensionParams;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Extension))
			return false;
		
		Extension extension = (Extension) o;
		
		if (id != null ? !id.equals(extension.id) : extension.id != null)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	
	@Override
	public int compareTo(Extension o) {
		return new Integer(this.order).compareTo(o.order);
	}
}
