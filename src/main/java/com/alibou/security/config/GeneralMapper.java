package com.alibou.security.config;

import com.alibou.security.entity.*;
import com.alibou.security.model.response.*;
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
        modelMapper.addMappings(new PropertyMap<Notification, NotificationResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setType(source.getType());
                map().setStatus(source.getStatus());
                map().setMessage(source.getMessage());
            }
        });
        modelMapper.addMappings(new PropertyMap<Snack, SnackResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setDescription(source.getDescription());
                map().setPrice(source.getPrice());
            }
        });
        modelMapper.addMappings(new PropertyMap<User, UserResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsername());
                map().setFullName(source.getFullName());
                map().setEmail(source.getEmail());
                map().setDateOfBirth(source.getDateOfBirth());
                map().setPhone(source.getPhone());
            }
        });
        modelMapper.addMappings(new PropertyMap<Discount, DiscountResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setCode(source.getCode());
                map().setDescription(source.getDescription());
                map().setDiscountPercent(source.getDiscountPercent());
                map().setExpirationDate(source.getExpirationDate());
            }
        });
        modelMapper.addMappings(new PropertyMap<PaymentMethod, PaymentMethodResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setDescription(source.getDescription());
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
