package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.SegmentService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.segmentRequest.CreateSegmentRequest;
import com.btkAkademi.rentACar.business.requests.segmentRequest.UpdateSegmentRequest;
import com.btkAkademi.rentACar.business.responses.SegmentListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.SegmentDao;
import com.btkAkademi.rentACar.entities.concretes.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SegmentManager implements SegmentService {

    private final ModelMapperService modelMapperService;
    private final SegmentDao segmentDao;

    @Autowired
    public SegmentManager(ModelMapperService modelMapperService, SegmentDao segmentDao) {
        super();
        this.modelMapperService = modelMapperService;
        this.segmentDao = segmentDao;
    }

    @Override
    public DataResult<List<SegmentListResponse>> findAll() {
        List<Segment> segmentList = this.segmentDao.findAll();
        List<SegmentListResponse> response = segmentList.stream()
                .map(segment -> modelMapperService.forResponse()
                        .map(segment, SegmentListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.SEGMENTLIST);
    }

    @Override
    public DataResult<SegmentListResponse> findById(int id) {
        if (segmentDao.existsById(id)) {
            SegmentListResponse response = modelMapperService.forResponse().map(segmentDao.findById(id).get(),
                    SegmentListResponse.class);
            return new SuccessDataResult<>(response, Messages.SEGMENTLIST);
        } else
            return new ErrorDataResult<>(Messages.NOTFOUND);
    }

    @Override
    public Result add(CreateSegmentRequest createSegmentRequest) {
        Result result = BusinessRules.run(
                CheckIfSegmentNameAlreadyExists(createSegmentRequest.getSegmentName()));
        if (result != null) {
            return result;
        }
        Segment segment = this.modelMapperService.forRequest().map(createSegmentRequest, Segment.class);
        this.segmentDao.save(segment);
        return new SuccessResult(Messages.SEGMENTADD);
    }

    @Override
    public Result update(UpdateSegmentRequest updateSegmentRequest) {
        Segment segment = this.modelMapperService.forRequest().map(updateSegmentRequest, Segment.class);
        this.segmentDao.save(segment);
        return new SuccessResult(Messages.SEGMENTUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (segmentDao.existsById(id)) {
            segmentDao.deleteById(id);
            return new SuccessResult(Messages.SEGMENTDELETE);
        } else
            return new ErrorResult(Messages.SEGMENTNOTFOUND);
    }

    private Result CheckIfSegmentNameAlreadyExists(String SegmentName) {
        if (segmentDao.findBySegmentName(SegmentName) != null) {
            return new ErrorResult(Messages.SEGMENTNAMEALREADYEXISTS);
        }
        return new SuccessResult();
    }
}
