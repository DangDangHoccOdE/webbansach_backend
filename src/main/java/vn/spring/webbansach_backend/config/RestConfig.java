package vn.spring.webbansach_backend.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import vn.spring.webbansach_backend.entity.Category;
import vn.spring.webbansach_backend.entity.User;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    private final EntityManager entityManager;

    @Autowired
    public RestConfig(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // config display id
//        config.exposeIdsFor(User.class);
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));

//        // config disable method
//        HttpMethod[] methods = {HttpMethod.PUT,HttpMethod.DELETE,HttpMethod.POST,HttpMethod.PATCH};
//        disableHttpMethod(config, Category.class,methods);
//
//        HttpMethod[] methodDelete={HttpMethod.DELETE};
//        disableHttpMethod(config,User.class,methodDelete);

//        // CORS configuration
//        String url = "http://localhost:3000";
//        cors.addMapping("/**")
//                .allowedOrigins(url)
//                .allowedMethods("GET","POST","PUT","DELETE");
    }

    private void disableHttpMethod(RepositoryRestConfiguration config,Class c,HttpMethod[] methods){
        config.getExposureConfiguration().forDomainType(c)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(methods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(methods));
    }
}
