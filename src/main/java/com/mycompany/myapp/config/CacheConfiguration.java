package com.mycompany.myapp.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.GroupeDePatient.class.getName());
            createCache(cm, com.mycompany.myapp.domain.GroupeDePatient.class.getName() + ".patients");
            createCache(cm, com.mycompany.myapp.domain.Region.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Region.class.getName() + ".centres");
            createCache(cm, com.mycompany.myapp.domain.Centre.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Centre.class.getName() + ".adminDeCentres");
            createCache(cm, com.mycompany.myapp.domain.Centre.class.getName() + ".medecins");
            createCache(cm, com.mycompany.myapp.domain.Centre.class.getName() + ".patients");
            createCache(cm, com.mycompany.myapp.domain.Specialty.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Specialty.class.getName() + ".medecins");
            createCache(cm, com.mycompany.myapp.domain.Medicament.class.getName());
            createCache(cm, com.mycompany.myapp.domain.AdminDeCentre.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Medecin.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Medecin.class.getName() + ".questions");
            createCache(cm, com.mycompany.myapp.domain.Medecin.class.getName() + ".patients");
            createCache(cm, com.mycompany.myapp.domain.Medecin.class.getName() + ".questionnaires");
            createCache(cm, com.mycompany.myapp.domain.Medecin.class.getName() + ".groupeDePatients");
            createCache(cm, com.mycompany.myapp.domain.Patient.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Patient.class.getName() + ".questionnaires");
            createCache(cm, com.mycompany.myapp.domain.Patient.class.getName() + ".groupeDePatients");
            createCache(cm, com.mycompany.myapp.domain.Notification.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Questionnaire.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Questionnaire.class.getName() + ".patientReponses");
            createCache(cm, com.mycompany.myapp.domain.Questionnaire.class.getName() + ".questions");
            createCache(cm, com.mycompany.myapp.domain.Questionnaire.class.getName() + ".patients");
            createCache(cm, com.mycompany.myapp.domain.PatientQuestionnaire.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PatientQuestionnaire.class.getName() + ".patients");
            createCache(cm, com.mycompany.myapp.domain.PatientQuestionnaire.class.getName() + ".questionnaires");
            createCache(cm, com.mycompany.myapp.domain.Question.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Question.class.getName() + ".questionAnswers");
            createCache(cm, com.mycompany.myapp.domain.Question.class.getName() + ".patientReponses");
            createCache(cm, com.mycompany.myapp.domain.Question.class.getName() + ".questionnaires");
            createCache(cm, com.mycompany.myapp.domain.QuestionAnswer.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PatientReponse.class.getName());
            createCache(cm, com.mycompany.myapp.domain.QuestionAnswer.class.getName() + ".patientReponses");
            createCache(cm, com.mycompany.myapp.domain.Patient.class.getName() + ".patientQuestionnaires");
            createCache(cm, com.mycompany.myapp.domain.Questionnaire.class.getName() + ".patientQuestionnaires");
            createCache(cm, com.mycompany.myapp.domain.PatientQuestionnaire.class.getName() + ".patientReponses");
            createCache(cm, com.mycompany.myapp.domain.Message.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
