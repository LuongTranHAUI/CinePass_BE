package com.alibou.security.config;

import com.alibou.security.entity.Hall;
import com.alibou.security.entity.Theater;
import com.alibou.security.model.response.HallResponse;
import com.alibou.security.model.response.TheaterResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class GeneralMapper {

    private final ModelMapper modelMapper;

    public GeneralMapper() {
        this.modelMapper = new ModelMapper();
        configureMapper();
    }

    private void configureMapper() {
        Converter<Theater, TheaterResponse> theaterToTheaterResponseConverter = context ->
                modelMapper.map(context.getSource(), TheaterResponse.class);
        modelMapper.addMappings(new PropertyMap<Theater, TheaterResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setLocation(source.getLocation());
                map().setPhone(source.getPhone());
            }
        });
        modelMapper.addMappings(new PropertyMap<Hall, HallResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setSeatCapacity(source.getSeatCapacity());
                map().setStatus(source.getStatus());
                using(theaterToTheaterResponseConverter).map(source.getTheater()).setTheaterResponse(null);
            }
        });
    }

    public <D, T> D mapToDTO(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> T mapToEntity(D dto, Class<T> outClass) {
        return modelMapper.map(dto, outClass);
    }

    public <D, T> void mapToEntity(D dto, T entity) {
        modelMapper.map(dto, entity);
    }
}
