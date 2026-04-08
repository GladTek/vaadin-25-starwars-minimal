package com.gladtek.vaadin.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Service for managing Rybbit Analytics tracking.
 * Controlled by the 'app.analytics.enabled' flag in application.yaml.
 */
@Service
public class AnalyticsService implements Serializable {

    @Value("${rybbit.analytics.enabled:false}")
    private boolean enabled;

    @Value("${rybbit.analytics.site-id:}")
    private String siteId;

    @Value("${rybbit.analytics.script-url:}")
    private String scriptUrl;

    @Value("${rybbit.analytics.enable-web-vitals:false}")
    private boolean enableWebVitals;

    /**
     * Checks if analytics is currently enabled.
     */
    public boolean isEnabled() {
        return enabled && siteId != null && !siteId.isEmpty();
    }

    /**
     * Get the site ID from configuration.
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * Get the configured analytics script URL.
     */
    public String getScriptUrl() {
        return scriptUrl;
    }

    /**
     * Checks if Web Vitals tracking is enabled.
     */
    public boolean isWebVitalsEnabled() {
        return enableWebVitals;
    }
}
