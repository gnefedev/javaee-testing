package com.gnefedev.javaee.testing.offline.config

import com.atomikos.jdbc.AtomikosDataSourceBean
import com.gnefedev.javaee.testing.offline.fullBind
import com.gnefedev.javaee.testing.offline.toList
import org.hsqldb.jdbc.pool.JDBCXADataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.w3c.dom.Node
import java.util.*
import javax.naming.InitialContext
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by gerakln on 13.09.16.
 */
@Configuration
internal open class PersistenceConfig {
    @Bean
    open fun entityManager(): LocalContainerEntityManagerFactoryBean {
        registerDataSources()

        val managerFactoryBean = LocalContainerEntityManagerFactoryBean()
        val properties = Properties()
        properties["hibernate.transaction.jta.platform"] = "com.atomikos.icatch.jta.hibernate4.AtomikosPlatform"
        managerFactoryBean.setJpaProperties(properties)
        return managerFactoryBean
    }

    private fun registerDataSources() {
        val persistenceUnits = getPersistenceUnits()

        for (persistenceUnit in persistenceUnits) {
            val name = persistenceUnit.attributes.getNamedItem("name").textContent
            assertHibernate(persistenceUnit)
            val jndiName = persistenceUnit
                    .childNodes
                    .toList()
                    .filter { it.nodeName == "jta-data-source" }
                    .first()
                    .textContent
            //TODO different dataTypes
            registerHsqldbResource(jndiName, name)
        }
    }

    private fun registerHsqldbResource(jndiName: String, name: String?) {
        val atomikosDataSourceBean = AtomikosDataSourceBean()
        atomikosDataSourceBean.uniqueResourceName = name

        val xaDataSource = JDBCXADataSource()
        xaDataSource.setPassword("sa")
        xaDataSource.user = ""
        xaDataSource.url = "jdbc:hsqldb:mem:unit-testing-jpa"

        atomikosDataSourceBean.xaDataSource = xaDataSource

        InitialContext().fullBind(jndiName, atomikosDataSourceBean)
    }

    private fun assertHibernate(persistenceUnit: Node) {
        val hasHibernate = persistenceUnit
                .childNodes
                .toList()
                .filter { it.nodeName == "provider" }
                .filter { it.textContent == "org.hibernate.jpa.HibernatePersistenceProvider" }
                .isNotEmpty()
        if (!hasHibernate) {
            throw UnsupportedOperationException("persistence work only with hibernate")
        }
    }

    private fun getPersistenceUnits(): List<Node> {
        return DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(javaClass.getResourceAsStream("/META-INF/persistence.xml"))
                .documentElement
                .childNodes
                .toList()
                .filter { it.nodeName == "persistence-unit" }
    }
}