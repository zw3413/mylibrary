package cn.cloudbed.mylibrary.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationController {

    @Autowired
    private OperationService operationService;


}
