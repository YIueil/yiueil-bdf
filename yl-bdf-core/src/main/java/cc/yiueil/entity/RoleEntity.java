package cc.yiueil.entity;

import cc.yiueil.lang.instance.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色实体
 * @author 弋孓 YIueil@163.com
 * @date 2023/5/30 22:13
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "role", schema = "yl_acc")
public class RoleEntity implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    @SequenceGenerator(name = "generator", schema = "yl_acc", sequenceName = "s_role", allocationSize = 1)
    private Long id;
    private String guid;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private Long createUserId;
    private Long parentId;
}