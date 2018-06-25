package ru.kolaer.server.webportal.annotations;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by danilovey on 11.08.2016.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlDeclaration {
    String description() default "";
    RequestMethod requestMethod() default RequestMethod.GET;
    boolean isAccessAll() default false;
    boolean isOit() default true;
    boolean isUser() default true;
    boolean isOk() default false;
}
