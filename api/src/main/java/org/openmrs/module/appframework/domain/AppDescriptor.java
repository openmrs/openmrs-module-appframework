package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.hibernate.validator.constraints.NotEmpty;
import org.openmrs.module.appframework.domain.validators.NoDuplicateExtensionPoint;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@NoDuplicateExtensionPoint
public class AppDescriptor implements Comparable<AppDescriptor> {
	
	@NotEmpty(message = ValidationErrorMessages.APP_DESCRIPTOR_ID_NOT_EMPTY_MESSAGE)
	@JsonProperty
	protected String id;
	
	@JsonProperty
	protected String description;

    @JsonProperty
    protected String instanceOf;

    /**
     * Will be set in {@link org.openmrs.module.appframework.AppFrameworkActivator} based on {@link #instanceOf}
     */
    transient protected AppTemplate template;

	@JsonProperty
	protected String label;
	
	@JsonProperty
	protected String url;
	
	@JsonProperty
	protected String icon;
	
	@JsonProperty
	protected String tinyIcon;
	
	@JsonProperty
	protected int order;
	
	@JsonProperty
	protected String requiredPrivilege;

    @JsonProperty
    protected String featureToggle;

    @JsonProperty
    protected ObjectNode config;
	
	@Valid
	@JsonProperty
	protected List<ExtensionPoint> extensionPoints;

    @JsonProperty
    protected List<Extension> extensions;
	
	@JsonProperty
	protected List<String> contextModel;
	
	public AppDescriptor() {
	}

	public AppDescriptor(String id, String description, String label, String url, String icon, String tinyIcon, int order) {
		this.id = id;
		this.description = description;
		this.label = label;
		this.url = url;
		this.icon = icon;
		this.tinyIcon = tinyIcon;
		this.order = order;
	}
	
	public AppDescriptor(String id, String description, String label, String url, String icon, String tinyIcon, int order,
	    String requiredPrivilege, List<ExtensionPoint> extensionPoints) {
		this.id = id;
		this.description = description;
		this.label = label;
		this.url = url;
		this.icon = icon;
		this.tinyIcon = tinyIcon;
		this.order = order;
		this.requiredPrivilege = requiredPrivilege;
		this.extensionPoints = extensionPoints;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getTinyIcon() {
		return tinyIcon;
	}

	public void setTinyIcon(String tinyIcon) {
		this.tinyIcon = tinyIcon;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getRequiredPrivilege() {
		return requiredPrivilege;
	}

	public void setRequiredPrivilege(String requiredPrivilege) {
		this.requiredPrivilege = requiredPrivilege;
	}

    public List<ExtensionPoint> getExtensionPoints() {
		return extensionPoints;
	}

    public void setExtensionPoints(List<ExtensionPoint> extensionPoints) {
        this.extensionPoints = extensionPoints;
    }

	public void addExtensionPoint(ExtensionPoint extensionPoint) {
		if (extensionPoints == null) {
			extensionPoints = new ArrayList<ExtensionPoint>();
		}
		extensionPoints.add(extensionPoint);
	}

	public List<String> getContextModel() {
		return contextModel;
	}
	
	public void setContextModel(List<String> contextModel) {
		this.contextModel = contextModel;
	}

    public String getFeatureToggle() {
        return featureToggle;
    }

    public void setFeatureToggle(String featureToggle) {
        this.featureToggle = featureToggle;
    }

    public ObjectNode getConfig() {
        if (template != null) {
            return getMergedConfig(template, config);
        } else {
            return config;
        }
    }

    /**
     * Gets config based off of template, with properties overriden by this instance's config
     * @param template
     * @param config
     * @return
     */
    private ObjectNode getMergedConfig(AppTemplate template, ObjectNode config) {
        ObjectNode merged = new ObjectMapper().createObjectNode();
        List<String> overridedObject = new ArrayList<String>();
        for (AppTemplateConfigurationOption configurationOption : template.getConfigOptions()) {
            String optionName = configurationOption.getName();
            JsonNode configuredValue = null;
            if (config != null) {
                configuredValue = config.get(optionName);
            }
            if (configuredValue != null) {
                merged.put(optionName, configuredValue);
                overridedObject.add(optionName);
            } else {
                merged.put(optionName, configurationOption.getDefaultValue());
            }
        }
		if (config != null) {
			for(Iterator<String> it = config.getFieldNames(); it.hasNext();){
                String fieldName = it.next();
				if (!overridedObject.contains(fieldName)) {
					merged.put(fieldName, config.get(fieldName));
				}
			}
		}
		return merged;
    }

    public void setConfig(ObjectNode config) {
        this.config = config;
    }

    public AppTemplate getTemplate() {
        return template;
    }

    public void setTemplate(AppTemplate template) {
        this.template = template;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
        }
		if (!(o instanceof AppDescriptor)) {
			return false;
        }
		
		AppDescriptor that = (AppDescriptor) o;
		
		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
        }
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	
	@Override
	public int compareTo(AppDescriptor o) {
		return Integer.valueOf(this.order).compareTo(Integer.valueOf(o.order));
	}

    public String getInstanceOf() {
        return instanceOf;
    }

    public void setInstanceOf(String instanceOf) {
        this.instanceOf = instanceOf;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }

	public void addExtension(Extension extension) {
		if (extensions == null) {
			extensions = new ArrayList<Extension>();
		}
		extensions.add(extension);
	}
}
