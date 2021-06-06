package com.tele.goldenkey.controller;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.service.H5Service;
import com.tele.goldenkey.util.N3d;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * h5组件
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/h5")
@RequiredArgsConstructor
public class H5Controller {

    private final H5Service h5Service;
    private static BufferedImage bufferedImage;

    @PostConstruct
    public void initImage() throws IOException {
        bufferedImage = ImageIO.read(this.getClass().getResourceAsStream("/img/logo.png"));
    }

    @GetMapping("/query")
    public String query(String groupId, String userId) throws ServiceException {
        return h5Service.query(N3d.decode(userId), StringUtils.isNotEmpty(groupId) ? N3d.decode(groupId) : null);
    }

    @GetMapping("/default-header")
    public void image(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }
}
