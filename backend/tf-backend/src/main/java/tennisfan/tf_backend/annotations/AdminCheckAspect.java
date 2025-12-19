package tennisfan.tf_backend.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tennisfan.tf_backend.services.AdminService;

@Aspect
@Component
public class AdminCheckAspect {

    @Autowired
    private AdminService adminService;

    @Before("@annotation(RequiresAdmin)")
    public void checkAdmin(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Integer adminUserId)) {
            throw new SecurityException("Admin user ID is required as first parameter");
        }

        if (!adminService.isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
    }
}