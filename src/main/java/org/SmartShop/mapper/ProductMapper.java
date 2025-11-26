package org.SmartShop.mapper;

import org.mapstruct.*;
import org.SmartShop.dto. product.*;
import org.SmartShop.entity.Product;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy. IGNORE
)
public interface ProductMapper {

    @Mapping(target = "deleted", source = "deleted")
    @Mapping(target = "available", expression = "java(product. getStock() > 0 && !product.isDeleted())")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    Product toEntity(CreateProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy. IGNORE)
    void updateEntityFromRequest(UpdateProductRequest request, @MappingTarget Product product);
}