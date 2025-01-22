package org.laokou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_user")
public class User implements Serializable {

    private Long id;
    private String name;
    private Integer tenantId;

}
