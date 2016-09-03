package com.gnefedev.javaee.offline;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.runner.RunWith;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;

/**
 * Created by gerakln on 03.09.16.
 */
class TestsFilter extends AbstractTypeHierarchyTraversingFilter {
    TestsFilter() {
        super(false, false);
    }

    @Override
    protected boolean matchSelf(MetadataReader metadataReader) {
        try {
            String className = metadataReader.getClassMetadata().getClassName();
            return isTestClass(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    static boolean isTestClass(Class<?> candidateClass) throws ClassNotFoundException {
        if (!candidateClass.isAnnotationPresent(RunWith.class)) {
            return false;
        } else {
            return candidateClass
                    .getAnnotation(RunWith.class)
                    .value()
                    .equals(JavaeeTestRunner.class);
        }
    }
}
