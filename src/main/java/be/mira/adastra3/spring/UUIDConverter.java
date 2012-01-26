/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.spring;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author tim
 */
public class UUIDConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(String iSource) {
        return UUID.fromString(iSource);
    }
}
