package com.openclassrooms.mddapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configures JPA auditing for the application.
 * This class enables the automatic population of audit-related fields
 * (e.g., createdDate, lastModifiedDate) in JPA entities.
 */
@Configuration
@EnableJpaAuditing
public class AuditConfig {
}