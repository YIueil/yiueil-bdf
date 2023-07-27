package cc.yiueil.service.impl;

import cc.yiueil.data.impl.JpaBaseDao;
import cc.yiueil.dto.UserDTO;
import cc.yiueil.entity.UserEntity;
import cc.yiueil.enums.UserStateEnum;
import cc.yiueil.exception.BusinessException;
import cc.yiueil.service.OrupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * 核心ORUP服务
 * @author 弋孓 YIueil@163.com
 * @date 2023/5/30 22:14
 * @version 1.0
 */
@Service
public class OrupServiceImpl implements OrupService {
    @Autowired
    JpaBaseDao baseDao;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserEntity registerUser(UserEntity registerUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(registerUser.getUserName());
        userEntity.setLoginName(registerUser.getLoginName());
        userEntity.setPassword(registerUser.getPassword());
        userEntity.setPhoneNumber(registerUser.getPhoneNumber());
        userEntity.setExtendProperty1(registerUser.getExtendProperty1());
        userEntity.setExtendProperty2(registerUser.getExtendProperty2());
        userEntity.setExtendProperty3(registerUser.getExtendProperty3());
        userEntity.setExtendProperty4(registerUser.getExtendProperty4());
        userEntity.setCreateTime(LocalDateTime.now());
        return baseDao.save(userEntity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void passwordChange(Long userId, String newPassword) {
        baseDao.findById(UserEntity.class, userId).ifPresent(userEntity -> {
            userEntity.setPassword(newPassword);
            baseDao.save(userEntity);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUser(Number userId) {
        return baseDao.findById(UserEntity.class, userId).orElseThrow(() -> new BusinessException("用户未找到"));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserEntity addUser(UserDTO userDTO, UserEntity currentUser) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        userEntity.setCreateUserId(currentUser.getId());
        baseDao.save(userEntity);
        return userEntity;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserEntity modifyUser(UserDTO userDTO, UserEntity currentUser) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        baseDao.save(userEntity);
        return userEntity;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delUser(Number userId, UserEntity currentUser) {
        baseDao.deleteById(UserEntity.class, userId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void suspendUser(Number userId, UserEntity currentUser) {
        baseDao.findById(UserEntity.class, userId).ifPresent(userEntity -> {
            userEntity.setState(UserStateEnum.normal.getState());
            baseDao.save(userEntity);
        });
    }
}
