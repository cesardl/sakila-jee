package org.sanmarcux.samples.sakila;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.sanmarcux.samples.sakila.dao.model.Film;
import org.sanmarcux.samples.sakila.dao.model.Language;
import org.sanmarcux.samples.sakila.dto.FilmDTO;
import org.sanmarcux.samples.sakila.dto.LanguageDTO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SakilaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakilaApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Film.languageByLanguageId -> FilmDTO.language and Language.languageId ->
        // LanguageDTO.id are both invisible to STRICT matching, so every Film -> FilmDTO
        // mapping silently produced a null language. Only the /films list hid it, by
        // hand-building the LanguageDTO; /films/{id}, /actors/{id}/films and
        // /actors/{id}/films/{filmId} all returned a film with no language at all.
        modelMapper.typeMap(Language.class, LanguageDTO.class)
                .addMapping(Language::getLanguageId, LanguageDTO::setId);
        modelMapper.typeMap(Film.class, FilmDTO.class)
                .addMapping(Film::getLanguageByLanguageId, FilmDTO::setLanguage);

        return modelMapper;
    }

    // CORS now lives in WebSecurityConfiguration#corsConfigurationSource. A hand-rolled
    // FilterRegistrationBean sits outside the security chain, so its ordering relative to
    // AuthorizationFilter is not guaranteed and it stamps CORS headers onto 401s too.
}
