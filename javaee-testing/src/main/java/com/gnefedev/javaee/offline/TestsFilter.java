package com.gnefedev.javaee.offline;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.runner.RunWith;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;

/**
 * Created by gerakln on 03.09.16.
 */
public class TestsFilter extends AbstractTypeHierarchyTraversingFilter {
    TestsFilter() {
        super(false, false);
    }

    @Override
    protected boolean matchSelf(MetadataReader metadataReader) {
        try {
            Class<?> candidate = Class.forName(metadataReader.getClassMetadata().getClassName());
            if (!candidate.isAnnotationPresent(RunWith.class)) {
                return false;
            } else {
                return candidate
                        .getAnnotation(RunWith.class)
                        .value()
                        .equals(JavaeeTestRunner.class);
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
