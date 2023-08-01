package cc.yiueil.repository;

import cc.yiueil.entity.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * RoleRepository
 *
 * @author 弋孓 YIueil@163.com
 * @version 1.0
 * @date 2023/8/1 23:27
 */
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    /**
     * 根据用户id查询所有角色
     * @param userId 用户id
     * @return 角色集合
     */
    @Query("select r from RoleEntity r left join UserRoleEntity ur on ur.roleId = r.id left join UserEntity u on ur.userId = u.id where u.id = :userId")
    List<RoleEntity> findRolesByUserId(Long userId);
}
