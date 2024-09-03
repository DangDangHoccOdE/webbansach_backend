package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.ImageRepository;
import vn.spring.webbansach_backend.entity.Image;
import vn.spring.webbansach_backend.service.inter.IImageService;

@Service
public class ImageService implements IImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }
}
