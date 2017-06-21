package org.creation.singlejob;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SingleJobConfiguration {

    @Bean()
    SinglejobAspectSupport singlejobAspectSupport()
    {
        return new SinglejobAspectSupport();
    }
    
    @Bean()
    public AnnotationSingleJobAspect annotationSingleJobAspect() {
        return new AnnotationSingleJobAspect();
    }
}
