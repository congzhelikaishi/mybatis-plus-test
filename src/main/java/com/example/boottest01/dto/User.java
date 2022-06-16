package com.example.boottest01.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User extends Model<User> {


    // 对应数据库中的主键（uuid 自增id 雪花算法 redis zookeeper）
    @TableId(type = IdType.ASSIGN_ID)  // 使用mybatis-plus自带雪花算法生成
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Version // 乐观锁Version注解
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    @TableLogic // 逻辑删除
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleted;
}
/*
@TableName("表名")

当表名与实体类名不一致时，可以在实体类上加入@TableName（）声明

@TableId声明属性为表中的主键（若属性名称不为默认id）

@TableFieId("字段") 当实体类属性与表字段不一致时，可以用来声明
 */
