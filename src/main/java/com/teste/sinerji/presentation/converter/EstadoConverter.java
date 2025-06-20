package com.teste.sinerji.presentation.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import com.teste.sinerji.domain.enums.Estado;

/**
 * Converter para o enum Estado.
 * 
 * @author Teste Sinerji
 */
@FacesConverter(value = "estadoConverter", managed = true)
public class EstadoConverter implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Estado.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Estado) {
            return ((Estado) value).name();
        }
        return value.toString();
    }
}
