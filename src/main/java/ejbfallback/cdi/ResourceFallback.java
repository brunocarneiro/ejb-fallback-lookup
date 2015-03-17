package ejbfallback.cdi;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * Add this annotations in the classes you want 
 * to monitor methods execution
 * 
 * Enable a Monitor Interceptor.
 * @author bruno.carneiro
 *
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface ResourceFallback {
}
