package com.example.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import java.nio.file.Path;
//返回视频流

@Component
public class NonStaticResourceHttpRequestHandler extends ResourceHttpRequestHandler{
    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    public Resource getResource(HttpServletRequest request) {
        final Path filePath = (Path) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(filePath);
    }

}

