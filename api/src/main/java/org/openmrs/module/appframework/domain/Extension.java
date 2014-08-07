package org.openmrs.module.appframework.domain;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;
import org.openmrs.module.appframework.template.TemplateFactory;

import java.util.Map;

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
	protected String icon;
	
	@JsonProperty
	protected int order;
	
	@JsonProperty
	protected String requiredPrivilege;

    @JsonProperty
    protected String featureToggle;

    @JsonProperty
    protected String require;
	
	@JsonProperty
	protected String script;
	
	@JsonProperty
	protected Map<String, Object> extensionParams;

    /**
     * Will be set by {@link org.openmrs.module.appframework.AppFrameworkActivator} if this extension is defined within
     * an app
     */
    @JsonIgnore
    transient private AppDescriptor belongsTo;

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
	    String requiredPrivilege, Map<String, Object> extensionParams) {
		this.id = id;
		this.appId = appId;
		this.extensionPointId = extensionPointId;
		this.type = type;
		this.label = label;
		this.url = url;
		this.order = order;
		this.requiredPrivilege = requiredPrivilege;
		this.extensionParams = extensionParams;
	}
	
	public String getId() {
		return id;
	}

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
		return appId;
	}

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtensionPointId() {
		return extensionPointId;
	}

    public void setExtensionPointId(String extensionPointId) {
        this.extensionPointId = extensionPointId;
    }

    public String getType() {
		return type;
	}

    public void setType(String type) {
        this.type = type;
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

    public String getScript() {
		return script;
	}

    public void setScript(String script) {
        this.script = script;
    }
	
	public Map<String, Object> getExtensionParams() {
		return extensionParams;
	}
	
	public void setExtensionParams(Map<String, Object> extensionParams) {
		this.extensionParams = extensionParams;
	}

    public String getFeatureToggle() {
        return featureToggle;
    }

    public void setFeatureToggle(String featureToggle) {
        this.featureToggle = featureToggle;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
        }
		if (!(o instanceof Extension)) {
			return false;
        }
		
		Extension extension = (Extension) o;
		
		if (id != null ? !id.equals(extension.id) : extension.id != null) {
			return false;
        }
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	
	@Override
	public int compareTo(Extension o) {
		return Integer.valueOf(this.order).compareTo(o.order);
	}

    public void setBelongsTo(AppDescriptor belongsTo) {
        this.belongsTo = belongsTo;
    }

    @JsonIgnore
    public AppDescriptor getBelongsTo() {
        return belongsTo;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    /**
     * If the url contains any {{var}} then these are replaced using TemplateFactory#handlebars
     *
     * @param contextPath e.g. "/openmrs"
     * @param contextModel
     * @return url, or "javascript:" + script if type == script, with contextModel substituted for any {{var}} in the url
     */
    public String url(String contextPath, Map<String, Object> contextModel) {
        return url(contextPath, contextModel, null);
    }

	public String url(String contextPath, Map<String, Object> contextModel, String returnUrl) {
		if (StringUtils.isNotBlank(contextPath)) {
			if ( !contextPath.startsWith("/") ){
				contextPath = "/" + contextPath;
			}
		}

		String url = "script".equals(this.type) ?
				("javascript:" + this.script) :
				(contextPath + "/" + this.url);
		if (url == null) {
			return null;
		}

        if (url.contains("{{")) {
            TemplateFactory templateFactory = getTemplateFactory();
            url = templateFactory.handlebars(url, contextModel);
        }

		if (!"script".equals(type) && returnUrl != null) {
			if (!url.contains("returnUrl=")) {
				returnUrl = "returnUrl=" + java.net.URLEncoder.encode(returnUrl);
				if (url.contains("?")) {
					returnUrl = "&" + returnUrl;
				} else {
					returnUrl = "?" + returnUrl;
				}

				String[] splitUrl = url.split("#");
				if (splitUrl.length == 1) {
					url += returnUrl;
				} else {
					url = splitUrl[0] + returnUrl + "#" + splitUrl[1];
				}
			}
		}
		return url;
	}

    TemplateFactory getTemplateFactory() {
        return Context.getRegisteredComponent("appframeworkTemplateFactory", TemplateFactory.class);
    }

}
