package com.cfuv.olympus.service.contest.checker.aspect;

import com.cfuv.olympus.service.ContestService;
import com.cfuv.olympus.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeRequest;
import com.cfuv.olympus.web.dto.contest.createUsers.CreateUsersRequest;
import com.cfuv.olympus.web.dto.task.Download.DownloadUserTaskRequest;
import com.cfuv.olympus.web.dto.task.feedback.FeedbackRequest;
import com.cfuv.olympus.service.UserTaskService;
import com.cfuv.olympus.web.dto.task.Download.DownloadTaskRequest;
import com.cfuv.olympus.web.dto.task.GetAllTasks.GetAllTasksRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class ContestStateAspect {

    private final ContestService contestService;
    private final UserTaskService userTaskService;

    @Around("@annotation(com.cfuv.olympus.service.contest.checker.aspect.CheckContestState)")
    public Object checkContestState(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        Long session = null;

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestParam requestParam) {
                    if ("session".equals(requestParam.value())) {
                        session = (Long) args[i];
                        break;
                    }
                } else if (annotation instanceof PathVariable pathVariable) {
                    if ("session".equals(pathVariable.value())) {
                        session = (Long) args[i];
                        break;
                    }
                }
            }
        }

        if (session == null) {
            for (Object arg : args) {
                if (arg instanceof DownloadUserTaskRequest) {
                    session = userTaskService.getSessionByUserTasksId(((DownloadUserTaskRequest) arg).getUserTasksId());
                } else if (arg instanceof FeedbackRequest) {
                    session = userTaskService.getSessionByUserTasksId(((FeedbackRequest) arg).getUserTasksId());
                } else if (arg instanceof GetAllTasksRequest) {
                    session = ((GetAllTasksRequest) arg).getSession();
                } else if (arg instanceof CreateUsersRequest) {
                    session = ((CreateUsersRequest) arg).getSession();

                } else if (arg instanceof GetStartAndEndContestTimeRequest) {
                    session = ((GetStartAndEndContestTimeRequest) arg).getSession();
                } else if (arg instanceof DownloadTaskRequest) {
                    session = ((DownloadTaskRequest) arg).getSession();


                } else if (arg instanceof Long) {
                    session = (Long) arg;
                }
            }
        }

        if (session != null && contestService.isContestFinished(session)) {
            throw new AccessDeniedException("Доступ запрещён: Олимпиада уже завершена.");
        }

        return joinPoint.proceed();
    }
}
