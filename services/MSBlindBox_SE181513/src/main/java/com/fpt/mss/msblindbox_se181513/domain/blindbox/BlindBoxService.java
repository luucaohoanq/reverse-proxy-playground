package com.fpt.mss.msblindbox_se181513.domain.blindbox;

import com.fpt.mss.dto.BlindBoxResponse;
import com.fpt.mss.msblindbox_se181513.domain.category.Category;
import java.util.List;

public interface BlindBoxService {

    List<BlindBoxResponse> getAll();
    BlindBoxResponse getById(Integer id);

    BlindBoxResponse create(BlindBoxRequest request);

    BlindBoxResponse update(Integer id, BlindBoxRequest request);

    void delete(Integer id);
    
    List<Category> getAllCategory();
    
    List<?> getAllBrands();
}
