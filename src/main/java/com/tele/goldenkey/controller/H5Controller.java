package com.tele.goldenkey.controller;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.service.H5Service;
import com.tele.goldenkey.util.N3d;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lihongbin
 * @date 2021年04月20日 23:32
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/h5")
@RequiredArgsConstructor
public class H5Controller {

    private final H5Service h5Service;

    @GetMapping("/query")
    public String query(String groupId, String userId) throws ServiceException {
            return h5Service.query(N3d.decode(groupId), N3d.decode(userId));
    }
}
