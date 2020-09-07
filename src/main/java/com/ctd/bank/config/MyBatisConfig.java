package com.ctd.bank.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 *
 *
 * @Title: MyBatisConfig.java
 */

@Configuration
@Slf4j
public class MyBatisConfig {

    @javax.annotation.Resource
    private DataSource dataSource;

    @javax.annotation.Resource
    private MybatisProperties properties;


    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    public static String setTypeAliasesPackage(String typeAliasesPackage) {
        ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        typeAliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(typeAliasesPackage)
                + "/" + DEFAULT_RESOURCE_PATTERN;
        try {
            List<String> result = new ArrayList<String>();
            Resource[] resources = resolver.getResources(typeAliasesPackage);
            if (resources != null && resources.length > 0) {
                MetadataReader metadataReader = null;
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        try {
                            result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (result.size() > 0) {
                HashSet<String> h = new HashSet<String>(result);
                result.clear();
                result.addAll(h);
                typeAliasesPackage=StringUtils.join(result.iterator(), ",");
                System.out.println(typeAliasesPackage);
            } else {
            	log.warn("mybatis参数typeAliasesPackage:" + typeAliasesPackage + "，未找到任何包");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return typeAliasesPackage;
    }

    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
            throws Exception {
        String typeAliasesPackage = properties.getTypeAliasesPackage();
        typeAliasesPackage=setTypeAliasesPackage(typeAliasesPackage);
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactory.setConfiguration(properties.getConfiguration());
        sessionFactory.setMapperLocations(properties.resolveMapperLocations());
        return sessionFactory.getObject();
    }
    public static void main(String[] args) throws Exception {
        setTypeAliasesPackage("com.ctd.*.*.model");
    }
}
