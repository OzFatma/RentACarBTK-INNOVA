package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.segmentRequest.CreateSegmentRequest;
import com.btkAkademi.rentACar.business.requests.segmentRequest.UpdateSegmentRequest;
import com.btkAkademi.rentACar.business.responses.SegmentListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface SegmentService {

    DataResult<List<SegmentListResponse>> findAll();

    DataResult<SegmentListResponse> findById(int id);

    Result add(CreateSegmentRequest createSegmentRequest);

    Result update(UpdateSegmentRequest createSegmentRequest);

    Result delete(int id);

}
