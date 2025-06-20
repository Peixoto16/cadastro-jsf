package com.teste.sinerji.presentation.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import com.teste.sinerji.domain.enums.Sexo;

/**
 * Converter para o enum Sexo.
 * 
 * @author Teste Sinerji
 */
@FacesConverter(value = "sexoConverter", managed = true)
public class SexoConverter implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Sexo.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Sexo) {
            return ((Sexo) value).name();
        }
        return value.toString();
    }
}
