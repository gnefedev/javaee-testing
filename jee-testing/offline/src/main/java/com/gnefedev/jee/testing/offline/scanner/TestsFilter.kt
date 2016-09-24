package com.gnefedev.jee.testing.offline.scanner

import com.gnefedev.jee.testing.offline.isTest
import org.springframework.core.type.classreading.MetadataReader
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter

/**
 * Created by gerakln on 03.09.16.
 */
internal class TestsFilter : AbstractTypeHierarchyTraversingFilter(false, false) {
    override fun matchSelf(metadataReader: MetadataReader?): Boolean {
        try {
            val className = metadataReader!!.classMetadata.className
            return Class.forName(className).isTest()
        } catch (e: ClassNotFoundException) {
            return false
        }

    }
}
