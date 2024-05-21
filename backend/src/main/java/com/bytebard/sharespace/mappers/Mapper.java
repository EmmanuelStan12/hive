package com.bytebard.sharespace.mappers;

public interface Mapper<Model, DTO> {

    DTO convertToDTO(Model model);

    Model convertToModel(DTO dto);
}
