package cn.cloudbed.mylibrary.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationService {

    private Logger logger= LoggerFactory.getLogger(this.getClass().getCanonicalName());

    @Autowired
    OperationService operationService;


}
