package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class CountrySpecifications {

    public static Specification<Country> codeContains(String code) {
        return StringUtils.isEmpty(code) ? null : (root, query, cb) ->
                cb.like(root.get("code"), "%" + code + "%");
    }

    public static Specification<Country> nameContains(String name) {
        return StringUtils.isEmpty(name) ? null : (root, query, cb) ->
            cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Country> continentContains(String continent) {
        return StringUtils.isEmpty(continent) ? null : (root, query, cb) ->
                cb.like(root.get("continent"), "%" + continent + "%");
    }

    public static Specification<Country> localNameContains(String localName) {
        return StringUtils.isEmpty(localName) ? null : (root, query, cb) ->
                cb.like(root.get("localName"), "%" + localName + "%");
    }

}
