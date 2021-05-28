package com.parrer.util;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName BeanCopyUtil
 * @Description 类属性拷贝工具类
 * 说明：
 * 1、所有拷贝方法都是深拷贝
 * 2、此工具类依赖orika项目，基于JavaAssist动态生成字节码创建类实现，更多介绍请参考 https://github.com/orika-mapper/orika
 * @Author fw
 * @Date 2019/7/19 17:39
 * @Version 1.0.0
 **/
public final class BeanCopyUtil {
    private static MapperFactory mapperFactory;
    private static MapperFacade defaultMapper;
    private static ConcurrentHashMap<Long, BoundMapperFacade> boundMapperFacadeMap = new ConcurrentHashMap<>(16);
    private static final  int WEDGE_NUMBER = 17;

    private BeanCopyUtil(){}
    static {
        mapperFactory = getMapperFactory();
        defaultMapper = mapperFactory.getMapperFacade();
    }

    /**
     * 拷贝src对象至基于destClazz构造的对象中，返回构造对象
     *
     * @param src       拷贝源对象
     * @param destClazz 拷贝目标Class
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> D copy(S src, Class<D> destClazz) {
        validateParam(src, destClazz);
        return defaultMapper.map(src, destClazz);
    }

    /**
     * 拷贝src对象至dest对象中
     *
     * @param src  拷贝源对象
     * @param dest 拷贝目标对象
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> void copy(S src, D dest) {
        validateParam(src, dest);
        defaultMapper.map(src, dest);
    }

    /**
     * 创建类绑定器，基于绑定器拷贝src对象至基于destClazz构造的对象中，返回构造对象
     * 注意：创建的绑定器会缓存在内存中，下一次对于同样的<src.getClass,destClazz>组合拷贝时，直接获取内存中的绑定器，因此性能比直接获取高。
     * 存在内存中的拷贝器和项目拥有同样的生命周期，因此使用时根据<src.getClass,destClazz>组合拷贝频率合理选择拷贝方法
     *
     * @param src       拷贝源对象
     * @param destClazz 拷贝目标Class
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> D boundCopy(S src, Class<D> destClazz) {
        validateParam(src, destClazz);
        return (D) setAndGetBoundMapperFacade(src.getClass(), destClazz).map(src);
    }

    /**
     * 创建类绑定器，基于绑定器拷贝src对象至dest对象中
     * 注意：创建的绑定器会缓存在内存中，下一次对于同样的<src.getClass,dest.getClass>组合拷贝时，直接获取内存中的绑定器，因此性能比直接获取高。
     * 存在内存中的拷贝器和项目拥有同样的生命周期，因此使用时根据<src.getClass,dest.getClass>组合拷贝频率合理选择拷贝方法
     *
     * @param src  拷贝源对象
     * @param dest 拷贝目标对象
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> void boundCopy(S src, D dest) {
        validateParam(src, dest);
        setAndGetBoundMapperFacade(src.getClass(), dest.getClass()).map(src, dest);
    }

    /**
     * 自定义属性对应关系，拷贝src对象至基于destClazz构造的对象中，返回构造对象
     * 例子：
     * class A {
     * private String aProp;
     * //set and get method...
     * }
     * class B {
     * private String bProp;
     * //set and get method...
     * }
     * 当需要把A的对象的aProp属性拷贝至B对象的bProp属性时，如下调用返回B对象：
     * B b = copyOnCustomField(a,B.class,FieldPair.build().add("aProp","bProp"));
     * assertEquals(a.getaProp,b.getbProp);
     * 注意：
     * 该方法每次调用都会初始化MapperFactory，初始化过程较慢且耗资源，如果需要批量拷贝
     * 请调用{@link BeanCopyUtil#copyOnCustomField(List, Class, FieldPair)}
     * 或{@link BeanCopyUtil#copyOnCustomField(List, List, FieldPair)}方法
     *
     * @param src
     * @param destClazz
     * @param fieldPair
     * @param <S>
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> D copyOnCustomField(S src, Class<D> destClazz, FieldPair fieldPair) {
        validateParam(src, destClazz, fieldPair);
        MapperFacade facade = getMapperFacade(src.getClass(), destClazz, fieldPair, true);
        return facade.map(src, destClazz);
    }

    /**
     * 自定义属性对应关系，拷贝src对象至dest对象中
     * 详细使用说明见{@link BeanCopyUtil#copyOnCustom(Object, Class, MapperFacade)}
     *
     * @param src
     * @param dest
     * @param fieldPair
     * @param <S>
     * @param <D>
     * @author fw
     */
    public static <S, D> void copyOnCustomField(S src, D dest, FieldPair fieldPair) {
        validateParam(src, dest, fieldPair);
        MapperFacade facade = getMapperFacade(src.getClass(), dest.getClass(), fieldPair, true);
        facade.map(src, dest);
    }

    /**
     * 定义属性对应关系，批量拷贝src对象至基于destClazz构造的对象中，返回构造对象的ArrayList
     * 详细使用说明见{@link BeanCopyUtil#copyOnCustom(Object, Class, MapperFacade)}
     *
     * @param srcs
     * @param destClazz
     * @param fieldPair
     * @param <S>
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> List<D> copyOnCustomField(List<S> srcs, Class<D> destClazz, FieldPair fieldPair) {
        validateParam(srcs, destClazz, fieldPair);
        if (srcs.size() <= 0) {
            throw new IllegalArgumentException("Source list should not be empty!");
        }
        List<D> dests = new ArrayList<>();
        MapperFacade facade = getMapperFacade(srcs.get(0).getClass(), destClazz, fieldPair, true);
        for (S src : srcs) {
            dests.add(facade.map(src, destClazz));
        }
        return dests;
    }

    /**
     * 定义属性对应关系，批量拷贝src对象至dest对象中
     * 详细使用说明见{@link BeanCopyUtil#copyOnCustom(Object, Class, MapperFacade)}
     *
     * @param srcs
     * @param dests
     * @param fieldPair
     * @param <S>
     * @param <D>
     * @author fw
     */
    public static <S, D> void copyOnCustomField(List<S> srcs, List<D> dests, FieldPair fieldPair) {
        validateParam(srcs, dests, fieldPair);
        if (srcs.size() <= 0 || dests.size() <= 0) {
            throw new IllegalArgumentException("Source list and destination list should not be empty!");
        }
        if (srcs.size() != dests.size()) {
            throw new IllegalArgumentException("Source list should be same size with destination list!");
        }
        MapperFacade facade = getMapperFacade(srcs.get(0).getClass(), dests.get(0).getClass(), fieldPair, true);
        for (int i = 0; i < srcs.size(); i++) {
            facade.map(srcs.get(i), dests.get(i));
        }
    }

    /**
     * 自定义映射器，拷贝src对象至基于destClazz构造的对象中，返回构造对象
     *
     * @param src
     * @param destClazz
     * @param mapperFacade
     * @param <S>
     * @param <D>
     * @return
     * @author fw
     */
    public static <S, D> D copyOnCustom(S src, Class<D> destClazz, MapperFacade mapperFacade) {
        validateParam(src, destClazz, mapperFacade);
        return mapperFacade.map(src, destClazz);
    }

    /**
     * 自定义映射器，拷贝src对象至dest对象中，映射门面对象mapperFacade由{@link BeanCopyUtil#getMapperFactory()}方法生成
     * 根据需要自定义配置mapperFacadeFactory对象，常见的比如：
     * 1、mapperFactory.classMap(srcClazz,destClazz).exclude("abandonFieldName") //排除拷贝字段名
     * 2、mapperFactory.classMap(srcClazz,destClazz).field("nameParts[0]", "firstName") //把列表或数组元素映射到字段
     * 3、mapperFactory.classMap(srcClazz,destClazz).field("name.first", "firstName") //把成员对象的属性映射到字段
     * 4、mapperFactory.classMap(srcClazz,destClazz).mapNulls(false) //不拷贝null字段
     * 5、ConverterFactory converterFactory = mapperFactory.getConverterFactory()
     * converterFactory.registerConverter("myConverterIdValue", new MyConverter())//自定义转换器
     * ...
     * 关于自定义的更多使用请参考 https://github.com/orika-mapper/orika
     *
     * @param src
     * @param dest
     * @param mapperFacade
     * @param <S>
     * @param <D>
     * @author fw
     */
    public static <S, D> void copyOnCustom(S src, D dest, MapperFacade mapperFacade) {
        validateParam(src, dest, mapperFacade);
        mapperFacade.map(src, dest);
    }

    private static <S, D> BoundMapperFacade<S, D> setAndGetBoundMapperFacade(Class<?> srcClazz, Class<?> destClazz) {
        long uniqueKey = computeUniqueKey(srcClazz, destClazz);
        if(null==boundMapperFacadeMap.get(uniqueKey)){
            boundMapperFacadeMap.put(uniqueKey,getBoundMapperFacade(srcClazz,destClazz));
        }
        return boundMapperFacadeMap.get(uniqueKey);
    }

    private static <S, D> BoundMapperFacade<S, D> getBoundMapperFacade(Class<S> srcClazz, Class<D> destClazz) {
        return mapperFactory.getMapperFacade(srcClazz, destClazz);
    }

    private static <S, D> long computeUniqueKey(Class<S> srcClazz, Class<D> destClazz) {
        long srcHashCode = srcClazz.hashCode();
        long destHashCode = destClazz.hashCode();
        return srcHashCode * (destHashCode % WEDGE_NUMBER) + destHashCode * (srcHashCode % WEDGE_NUMBER);
    }

    private static <D> MapperFacade getMapperFacade(Class<?> srcClazz, Class<D> destClazz, FieldPair fieldPair, boolean isUnidirection) {
        MapperFactory mapperFactory = getMapperFactory();
        ClassMapBuilder classMapBuilder = mapperFactory.classMap(srcClazz, destClazz);
        List<String> fieldList = fieldPair.getFieldList();
        if (null == fieldList || fieldList.size() <= 0) {
            classMapBuilder.byDefault().register();
            return mapperFactory.getMapperFacade();
        }
        if (isUnidirection) {
            for (int i = 0; i < fieldList.size(); i = i + 2) {
                classMapBuilder.fieldAToB(fieldList.get(i), fieldList.get(i + 1));
            }
        } else {
            for (int i = 0; i < fieldList.size(); i = i + 2) {
                classMapBuilder.field(fieldList.get(i), fieldList.get(i + 1));
            }
        }
        classMapBuilder.byDefault().register();
        return mapperFactory.getMapperFacade();
    }

    private static void validateParam(Object... params) {
        if (null == params) {
            throw new IllegalArgumentException("Parameter is illegal, you'd better have a check!");
        }
        for (Object param : params) {
            if (null == param) {
                throw new IllegalArgumentException("Parameter(s) is/are illegal, you'd better have a check!");
            }
        }
    }

    /**
     * 获取拷贝映射工厂实例，此方法主要和{@link BeanCopyUtil#copyOnCustom(Object, Class, MapperFacade)}方法
     * 配合使用，根据此方法返回的mapperFactory自定义配置后，根据MapperFactory对应方法获取mapperFacade对象，（比如：{@link MapperFactory#getMapperFacade()}）
     * {@link BeanCopyUtil#copyOnCustom(Object, Class, MapperFacade)}方法接收mapperFacade实现自定义拷贝
     *
     * @return
     */
    public static MapperFactory getMapperFactory() {
        return new DefaultMapperFactory.Builder().build();
    }

    /**
     * @ClassName FieldPair
     * @Description 映射字段存储中间类
     * @Author fw
     * @Date 2019/7/19 18:21
     * @Version 1.0.0
     * @Copyright: Copyright 2018 Thunisoft, Inc. All rights reserved.
     * THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
     **/
    public static class FieldPair {

        private List<String> fieldList = new ArrayList<>();

        private FieldPair() {
        }

        public static FieldPair build() {
            return new FieldPair();
        }

        /**
         * NOTE: The bean-copy util is an independent module; seeming not a good thing if it
         *       rely on much third party component.
         * @param fieldA
         * @param fieldB
         * @return
         * @author fw
         */
        public FieldPair add(String fieldA, String fieldB) {
            if (null == fieldA || fieldA.length() <= 0 || null == fieldB || fieldB.length() <= 0) {
                throw new IllegalArgumentException("Field name of map-from and map-to fields can not be empty; current value:fieldA-" + fieldA + ",fieldB-" + fieldB);
            }
            fieldList.add(fieldA);
            fieldList.add(fieldB);
            return this;
        }

        public List<String> getFieldList() {
            return fieldList;
        }

    }

}
