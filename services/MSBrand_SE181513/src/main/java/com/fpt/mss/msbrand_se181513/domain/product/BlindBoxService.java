package com.fpt.mss.msbrand_se181513.domain.product;

import com.fpt.mss.dto.BlindBoxResponse;
import java.util.List;

public interface BlindBoxService {

    List<BlindBoxResponse> getAll();

    BlindBoxResponse create(BlindBoxRequest request);

    BlindBoxResponse update(Integer id, BlindBoxRequest request);

    void delete(Integer id);
}
