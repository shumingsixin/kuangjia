package com.example.demo.dao;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.demo.annotation.Column;
import com.example.demo.annotation.Ignore;
import com.example.demo.annotation.Pk;
import com.example.demo.annotation.Table;
import com.example.demo.constant.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 泛型类两个的参数
 *
 * @param <T> 子类类型
 * @param <P> 主键
 */
@Slf4j
public class BaseDao<T, P> {
    private JdbcTemplate jdbcTemplate;
    private Class<T> clazz;

    public BaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        //获取继承此类的真实使用类
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 获取表名
     *
     * @param t 对象
     * @return 表名
     */
    private String getTableName(T t) {
        //获取@Table的映射
        Table tableAnnotation = t.getClass().getAnnotation(Table.class);
        if (ObjectUtil.isNotNull(tableAnnotation)) {
            return StrUtil.format("`{}`", tableAnnotation.name());
        } else {
            //若是映射内未标明参数 则获取类名
            return StrUtil.format("`{}`", t.getClass().getName().toLowerCase());
        }
    }

    /**
     * 获取表名
     *
     * @return
     */
    private String getTableName() {
        //获取@Table的映射
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (ObjectUtil.isNotNull(tableAnnotation)) {
            return StrUtil.format("`{}`", tableAnnotation.name());
        } else {
            //若是映射内未标明参数 则获取类名
            return StrUtil.format("`{}`", clazz.getName().toLowerCase());
        }
    }

    /**
     * 获取字段数 {@code 过滤数据库中忽略的字段,以及自增列}
     *
     * @param t          对象
     * @param ignoreNull 是否忽略空值
     * @return 字段列表
     */
    private List<Field> getField(T t, Boolean ignoreNull) {
        //获取类中所有的字段,包括父类
        Field[] fields = ReflectUtil.getFields(t.getClass());
        //过滤数据库中不存在的字段,以及自增列
        List<Field> filterField;
        //数组转化成数组 再转换成流过滤器 过滤条件有Ignore映射和Pk映射的
        Stream<Field> fieldStream = CollUtil.toList(fields).stream().filter(field -> ObjectUtil.isNull(field.getAnnotation(Ignore.class)) || ObjectUtil.isNull(field.getAnnotation(Pk.class)));
        if (ignoreNull) {
            //再过滤掉具体值为空的
            filterField = fieldStream.filter(field -> ObjectUtil.isNotNull(ReflectUtil.getFieldValue(t, field))).collect(Collectors.toList());
        } else {
            filterField = fieldStream.collect(Collectors.toList());
        }
        return filterField;
    }

    /**
     * 获取列
     *
     * @param fieldList 字段列表
     * @return 列消息列表
     */
    private List<String> getColumns(List<Field> fieldList) {
        List<String> columnList = CollUtil.newArrayList();
        for (Field field : fieldList) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            String columnName;
            if (ObjectUtil.isNotNull(columnAnnotation)) {
                columnName = columnAnnotation.name();
            } else {
                columnName = field.getName();
            }
            columnList.add(StrUtil.format("`{}`", columnName));
        }
        return columnList;
    }


    /**
     * 通用插入,自增列需要添加{@link com.example.demo.annotation.Pk}注解
     *
     * @param t          对象
     * @param ignoreNull 是否忽略null值
     * @return 操作的行数
     */
    protected Integer insert(T t, Boolean ignoreNull) {
        String table = getTableName(t);
        //字段列表
        List<Field> filterField = getField(t, ignoreNull);
        List<String> columnList = getColumns(filterField);
        //将列拼成字符串
        String columns = StrUtil.join(Const.SEPARATOR_COMMA, columnList);
        //构造占位符
        String params = StrUtil.repeatAndJoin("?", columnList.size(), Const.SEPARATOR_COMMA);
        //构造与需要新增的列值 field是key ->(对应的值)
        Object[] values = filterField.stream().map(field -> ReflectUtil.getFieldValue(t, field)).toArray();
        String sql = StrUtil.format("Insert into {table}({columns}) values({params})", Dict.create().set("table", table).set("columns", columns).set("params", params));
        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(values));
        return jdbcTemplate.update(sql, values);
    }

    /**
     * 根据对象查询
     *
     * @param t 查询条件
     * @return 对象列表
     */
    public List<T> findByExample(T t) {
        String table = getTableName(t);
        List<Field> filterField = getField(t, true);
        List<String> columnList = getColumns(filterField);
        //s表示map集合中的key
        List<String> columns = columnList.stream().map(s -> " and " + s + " = ? ").collect(Collectors.toList());
        String where = StrUtil.join(" ", columns);
        Object[] values = filterField.stream().map(field -> ReflectUtil.getFieldValue(t, field)).toArray();
        String sql = StrUtil.format("select * from {table} where 1=1 {where}", Dict.create().set("table", table).set("where", StrUtil.isBlank(where) ? "" : where));

        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(values));
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, values);
        List<T> ret = CollUtil.newArrayList();
        //使用bean工具将查出来的泛型实例化
        maps.forEach(map -> ret.add(BeanUtil.fillBeanWithMap(map, ReflectUtil.newInstance(clazz), true, false)));
        return ret;
    }


    /**
     * 根据主键查询单条记录
     *
     * @param pk 主键
     * @return 单条纪录
     */
    public T findById(P pk) {
        String table = getTableName();
        String sql = StrUtil.format("select * from {table} where id = {id}", Dict.create().set("table", table).set("id", pk));
        RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(pk));
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    /**
     * 根据主键删除
     *
     * @param pk 主键
     * @return 影响行数
     */
    protected Integer deleteById(P pk) {
        String table = getTableName();
        String sql = StrUtil.format("delete from {table} where id = ?", Dict.create().set("table", table));
        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(pk));
        return jdbcTemplate.update(sql, pk);
    }

    /**
     * 根据对象删除,排除id
     *
     * @param t 对象参数
     * @return 影响行数
     */
    protected Integer deleteByExample(T t) {
        String table = getTableName(t);
        List<Field> filterField = getField(t, true);
        List<String> columnList = getColumns(filterField);
        //条件列
        List<String> columns = columnList.stream().map(s -> " and " + s + " = ? ").collect(Collectors.toList());
        Object[] params = filterField.stream().map(field -> ReflectUtil.getFieldValue(t, field)).toArray();
        String where = StrUtil.join(" ", columns);
        //若条件是空 不处理
        if (StrUtil.isBlank(where)) {
            return -1;
        }
        String sql = StrUtil.format("delete from {table} where 1=1 {where}", Dict.create().set("table", table).set("where", where));
        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(params));
        return jdbcTemplate.update(sql, params);
    }

    /**
     * 根据主键更新对象
     *
     * @param t          更新对象
     * @param pk         主键
     * @param ignoreNull 是否忽略空值属性
     * @return 影响行数
     */
    protected Integer updateById(T t, P pk, Boolean ignoreNull) {
        String table = getTableName(t);
        List<Field> filterField = getField(t, ignoreNull);
        List<String> columnList = getColumns(filterField);
        //更新的列
        List<String> updateList = columnList.stream().map(s -> StrUtil.appendIfMissing(s, " = ?")).collect(Collectors.toList());
        //将要更新的列拼成字符串
        String updateColumns = StrUtil.join(Const.SEPARATOR_COMMA, updateList);
        String sql = StrUtil.format("update {table} set {updateColumns} where id = ?", Dict.create().set("table", table).set("updateColumns", updateColumns));
        //获取需要更新的列的具体值
        List<Object> valueList = filterField.stream().map(field -> ReflectUtil.getFieldValue(t, field)).collect(Collectors.toList());
        valueList.add(pk);
        Object[] values = valueList.toArray();
        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(values));
        return jdbcTemplate.update(sql, values);
    }

    /**
     * 根据对象条件更新对象
     *
     * @param p          更新条件的对象
     * @param t          更新的对象
     * @param ignoreNull
     * @return
     */
    protected Integer updateByExample(T p, T t, Boolean ignoreNull) {
        String table = getTableName(t);
        //获取更新对象参数
        List<Field> filterField = getField(t, ignoreNull);
        List<String> columnList = getColumns(filterField);
        List<String> updateList = columnList.stream().map(s -> StrUtil.appendIfMissing(s, " = ?")).collect(Collectors.toList());
        String updateColumns = StrUtil.join(Const.SEPARATOR_COMMA, updateList);
        //获取条件对象参数
        List<Field> whereField = getField(p, ignoreNull);
        List<String> whereList = getColumns(whereField).stream().map(s -> " and " + s + " = ? ").collect(Collectors.toList());
        String whereColumns = StrUtil.join(" ", whereList);
        String sql = StrUtil.format("update {table} set {updateColumns} where 1=1 {whereColumns}", Dict.create().set("table", table).set("updateColumns", updateColumns).set("whereColumns", StrUtil.isBlank(whereColumns) ? "" : whereColumns));
        List<Object> value = filterField.stream().map(field -> ReflectUtil.getFieldValue(t, field)).collect(Collectors.toList());
        List<Object> whereValue = whereField.stream().map(field -> ReflectUtil.getFieldValue(p, field)).collect(Collectors.toList());
        Object[] allValue = CollUtil.union(value, whereValue).toArray();
        log.debug("[执行SQL]sql:{}]", sql);
        log.debug("[执行SQL]参数:{}]", JSONUtil.toJsonStr(allValue));
        return jdbcTemplate.update(sql, allValue);
    }


}
