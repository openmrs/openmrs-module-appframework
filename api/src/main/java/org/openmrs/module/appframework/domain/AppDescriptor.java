package org.openmrs.module.appframework.domain;

import java.util.List;

import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.openmrs.module.appframework.domain.validators.NoDuplicateExtensionPoint;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

@NoDuplicateExtensionPoint
public class AppDescriptor implements Comparable<AppDescriptor> {
	
	@NotEmpty(message = ValidationErrorMessages.APP_DESCRIPTOR_ID_NOT_EMPTY_MESSAGE)
	@JsonProperty
	protected String id;
	
	@JsonProperty
	protected String description;
	
	@JsonProperty
	protected String label;
	
	@JsonProperty
	protected String url;
	
	@JsonProperty
	protected String iconUrl;
	
	@JsonProperty
	protected String tinyIconUrl;
	
	@JsonProperty
	protected int order;
	
	@Valid
	@JsonProperty
	protected List<ExtensionPoint> extensionPoints;
	
	@JsonProperty
	protected List<String> contextModel;
	
	public AppDescriptor() {
	}
	
	public AppDescriptor(String id, String description, String label, String url, String iconUrl, String tinyIconUrl,
	    int order) {
		this.id = id;
		this.description = description;
		this.label = label;
		this.url = url;
		this.iconUrl = iconUrl;
		this.tinyIconUrl = tinyIconUrl;
		this.order = order;
	}
	
	public AppDescriptor(String id, String description, String label, String url, String iconUrl, String tinyIconUrl,
	    int order, List<ExtensionPoint> extensionPoints) {
		this.id = id;
		this.description = description;
		this.label = label;
		this.url = url;
		this.iconUrl = iconUrl;
		this.tinyIconUrl = tinyIconUrl;
		this.order = order;
		this.extensionPoints = extensionPoints;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}
	
	public String getTinyIconUrl() {
		return tinyIconUrl;
	}
	
	public int getOrder() {
		return order;
	}
	
	public List<ExtensionPoint> getExtensionPoints() {
		return extensionPoints;
	}
	
	public List<String> getContextModel() {
		return contextModel;
	}
	
	public void setExtensionPoints(List<ExtensionPoint> extensionPoints) {
		this.extensionPoints = extensionPoints;
	}
	
	public void setContextModel(List<String> contextModel) {
		this.contextModel = contextModel;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AppDescriptor))
			return false;
		
		AppDescriptor that = (AppDescriptor) o;
		
		if (id != null ? !id.equals(that.id) : that.id != null)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	
	@Override
	public int compareTo(AppDescriptor o) {
		return new Integer(this.order).compareTo(new Integer(o.order));
	}
}
