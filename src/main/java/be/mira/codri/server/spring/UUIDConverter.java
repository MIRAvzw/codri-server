/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.spring;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author tim
 */
public class UUIDConverter implements Converter<String, UUID> {
    @Override
    public final UUID convert(final String iSource) {
        return UUID.fromString(iSource);
    }
}
