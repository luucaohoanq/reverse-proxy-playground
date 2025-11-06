package com.fpt.mss.utils;

import com.fpt.mss.enums.Role;
import java.util.Map;
import java.util.Set;

public class PermissionUtils {

    private static final Map<Role, Set<Role>> PERMISSIONS =
            Map.of(
                    Role.MEMBER, Set.of(Role.MEMBER),
                    Role.MODERATOR, Set.of(Role.MEMBER, Role.MODERATOR),
                    Role.DEVELOPER, Set.of(Role.MEMBER, Role.MODERATOR, Role.DEVELOPER),
                    Role.ADMIN, Set.of(Role.MEMBER, Role.MODERATOR, Role.DEVELOPER, Role.ADMIN));

    public static boolean hasPermission(String userRole, Role requiredRole) {
        try {
            Role current = Role.valueOf(userRole);
            return PERMISSIONS.getOrDefault(current, Set.of()).contains(requiredRole);
        } catch (Exception e) {
            return false;
        }
    }
}
