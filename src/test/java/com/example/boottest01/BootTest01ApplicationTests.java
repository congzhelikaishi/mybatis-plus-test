package com.example.boottest01;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.boottest01.config.SnowflakeConfig;
import com.example.boottest01.dto.User;
import com.example.boottest01.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class BootTest01ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    SnowflakeConfig snowflakeConfig;

    @Test
    void contextLoads() {
        // 参数是一个Wrapper, 条件构造器, 暂时不用 填null
        // 查询所有的用户
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    void testInsert() {
        User user = new User();
        user.setName("wanggang");
        user.setAge(18);
        user.setEmail("147258369@qq.com");
//        user.insert(); // 实体类继承Model<T>的AR模式（注意：需要mapper接口继承BaseMapper<T>）
        userMapper.insert(user);
    }

    // 测试乐观锁失败，多线程下
    @Test
    void testOptimisticLocker() {
        // 线程一
        User user = userMapper.selectById(10L);
        user.setName("kkkk");
        user.setEmail("111111111@qq.com");

        // 模拟另外一个线程执行了插队操作
        User user1 = userMapper.selectById(10L);
        user1.setName("kkkk222");
        user1.setEmail("1111111112222@qq.com");
        userMapper.updateById(user1);

        // 自旋锁来多次尝试提交
        userMapper.updateById(user); // 如果没有乐观锁就会覆盖插队线程值
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    // 测试批量查询
    @Test
    void testSelectByBatchId() {
        List<User> userList = userMapper.selectBatchIds(Arrays.asList(1L,2L,3L));
        userList.forEach(System.out::println);
    }

    // 条件查询
    @Test
    void testSelectByBatchIds() {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("name", "lili");
        map.put("age", "5");

        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
    }

    // lambda表达式的条件构造器(链式)
    @Test
    void selectMy() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.likeRight(User::getName, "lili")  // likeRight:右匹配就类似在右边加上%
                .and(lqw -> lqw.lt(User::getAge, 5).or().isNotNull(User::getEmail));
        List<User> userList = userMapper.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }

    //基础条件查询（复杂查询）
    @Test
    public void testSelectQuery(){
        //创建对象
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //通过wrapper设置条件。

        //ge gt  le lt
        //>= >  <= <
        //查询age >= 30 的记录
        wrapper.ge("age",3);
//        List<User> users = userMapper.selectList(wrapper);
//        System.out.println(users);

        //  eq (=)  ne （!=）
        //表示查询name等于“修改了”的数据。
        wrapper.eq("name","修改了");
        //表示查询name不等于“修改了”的数据。
        wrapper.ne("name","修改了");
//        List<User> users1 = userMapper.selectList(wrapper);
//        System.out.println(users1);

        //between
        //查询年龄在20  - 35之间的。
        wrapper.between("age",20,35);
//        List<User> users2 = userMapper.selectList(wrapper);
//        System.out.println(users2);


        //like
        //模糊查询
        wrapper.like("name","了");
//        List<User> users3 = userMapper.selectList(wrapper);
//        System.out.println(users3);

        //orderByDesc
        //根据id降序排列
        wrapper.orderByDesc("id");


        //last
        //拼接语句
        wrapper.last("limit 1");



        //查询指定的列
        //就表示只会查出id和name字段。
        wrapper.select("id","name");
        List<User> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    // 简化构造器（没有使用链式）
    @Test
    void testLambdaWrapper() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, "lili");
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // 测试雪花算法
    @Test
    void testSnow() {
        for (int i = 0; i<10;i++) {
            System.out.println(snowflakeConfig.snowflakeId());
        }

    }

    // 测试分页插件
    @Test
    void testPage() {
        // 参数一：当前页
        // 参数二：页面大小
        Page<User> page = new Page<>(2, 5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);

        // 获取总数
        page.getTotal();
    }

    @Test
    void testDeleteById() {
        userMapper.deleteById(1L);
    }

    // 批量删除
    @Test
    void testDeleteBatchId() {
        userMapper.deleteBatchIds(Arrays.asList(2,3));
    }

    // 条件删除
    @Test
    void testDeleteMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "wanggang");
        userMapper.deleteByMap(map);
    }

    @Test
    void contextLoads1(){
        // 查询name不为null的用户，并且邮箱不为null的用户，年龄大于等于12的用户
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        wrapper.isNotNull("name");
        wrapper.isNotNull("email");
        wrapper.ge("age",12);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test2(){
        // 查询name为shuishui的用户
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        wrapper.eq("name","shuishui");
        List<User> user = userMapper.selectList(wrapper);
        System.out.println(user);
    }

    @Test
    void test3(){
        // 查询年龄在20~30岁之间的用户
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        wrapper.between("age",20,30);
        Integer count =userMapper.selectCount(wrapper);//查询结果数
        System.out.println(count);
    }

    //模糊查询
    @Test
    void test4(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();

        wrapper.notLike("name","x");//相当于NOT LIKE '%s%'
        wrapper.likeRight("email","s");//相当于LIKE 's%'
        List<Map<String,Object>> maps = userMapper.selectMaps(wrapper);//查询结果
        maps.forEach(System.out::println);
    }


    @Test
    void test5(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        //子查询
        wrapper.inSql("id","select id from user where id<10");
        List<Object> objects =userMapper.selectObjs(wrapper);
        objects.forEach(System.out::println);
    }

    @Test
    void test6(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        //通过id进行排序
        wrapper.orderByAsc("id");
        List<User> users =userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //姓王年龄大于等于25，按年龄降序，年龄相同按id升序排列
    void test7(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        wrapper.likeRight("name","王").or().ge("age",18).orderByDesc("age").orderByAsc("id");
        List<User> users =userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //创建日期为2019年2月14日并且直属上级为姓王
    void test8(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        wrapper.apply("date_fromat(create_time,'%Y-%m-%d')='2019-02-14'").inSql("manager_id","select id from user where name like '王%'");
        List<User> users =userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //姓王并且（年龄小于40或者邮箱不为空）
    void test9(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();
        //lt小于，gt大于
        wrapper.likeRight("name","王").and(wq->wq.lt("age",40).or().isNotNull("email"));
        List<User> users =userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //不列出所有字段
    @Test
    void test10(){
        QueryWrapper<User> wrapper =new QueryWrapper<>();

        wrapper.select("id","name").like("name","zhangsan").lt("age",40);
        //不显示时间和id
        //wrapper.select(User.class,info->!info.getColumn().equals("create_time")&&!info.getColumn().equals("manager_id")).like("name","雨").lt("age",40);
        List<User> users =userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // 动态条件拼接
    @Test
    void testCond() {
        String name = "lilei";
        String email = "1";
        testCondition(name, email);
    }

    void testCondition(String name, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), "name", name).like(!StringUtils.isEmpty(email), "email", email);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    // 测试alleq
    @Test
    void selectAlleq() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        // 自定义查询
        map.put("name", "lilei");
        map.put("age", "16");
        wrapper.allEq(map);

        userMapper.selectList(wrapper).forEach(System.out::println);
    }
    /*
    params : key为数据库字段名,value为字段值
    null2IsNull : 为true则在map的value为null时调用 isNull 方法,为false时则忽略value为null的
    filter : 过滤函数,是否允许字段传入比对条件中
     */

    @Test
    void test111() {
        // 传入实体类为空，不走自动填充拦截，所以更新时间不会自动填充
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("id", 10L);
        uw.set("name", "li");
        userMapper.update(null, uw);
        userMapper.deleteById(10L);

    }
}
