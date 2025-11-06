package com.fpt.mss.security;

import com.fpt.mss.annotations.RequireRole;
import com.fpt.mss.enums.Role;
import com.fpt.mss.utils.PermissionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RoleCheckAspect {

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint pjp, RequireRole requireRole) throws Throwable {
        // Extract role from request header
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return ResponseEntity.status(403).body("Forbidden");

        var request = attrs.getRequest();
        String role = request.getHeader("X-User-Role");
        Role required = requireRole.value();

        if (!PermissionUtils.hasPermission(role, required)) {
            log.warn("❌ Access denied for role {} on {}", role, pjp.getSignature());
            return ResponseEntity.status(403).body("Access denied: " + required + " required");
        }

        return pjp.proceed();
    }
}
