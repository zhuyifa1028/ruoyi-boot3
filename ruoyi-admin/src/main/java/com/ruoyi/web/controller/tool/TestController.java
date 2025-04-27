package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger 用户测试方法
 *
 * @author ruoyi
 */
@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/test/user")
public class TestController extends BaseController {

    private final static Map<Integer, UserEntity> users = new LinkedHashMap<>();

    static {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    public R<List<UserEntity>> userList() {
        List<UserEntity> userList = new ArrayList<>(users.values());
        return R.ok(userList);
    }

    @Operation(summary = "获取用户详细")
    @GetMapping("/{userId}")
    public R<UserEntity> getUser(@PathVariable(name = "userId")
                                     Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            return R.ok(users.get(userId));
        } else {
            throw new ServiceException("用户不存在");
        }
    }

    @Operation(summary = "新增用户")
    @PostMapping("/save")
    public R<Void> save(UserEntity user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
            return R.fail("用户ID不能为空");
        }
        users.put(user.getUserId(), user);
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping("/update")
    public R<Void> update(@RequestBody
                          UserEntity user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
            return R.fail("用户ID不能为空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId())) {
            return R.fail("用户不存在");
        }
        users.remove(user.getUserId());
        users.put(user.getUserId(), user);
        return R.ok();
    }

    @Operation(summary = "删除用户信息")
    @DeleteMapping("/{userId}")
    public R<Void> delete(@PathVariable(name = "userId")
                          Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return R.ok();
        } else {
            return R.fail("用户不存在");
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @Schema(description = "用户实体")
    public static class UserEntity {
        @Schema(title = "用户ID")
        private Integer userId;

        @Schema(title = "用户名称")
        private String username;

        @Schema(title = "用户密码")
        private String password;

        @Schema(title = "用户手机")
        private String mobile;

        public UserEntity(Integer userId, String username, String password, String mobile) {
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.mobile = mobile;
        }

    }
}

