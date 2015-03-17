package ejbfallback.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;


/**
 * CDI Extension that enables the monitoring interceptor dynamically 
 * (without annotating a class).
 * 
 * @author bruno.carneiro
 *
 */
public class EJBFallBackCDIExtension implements Extension {
	
	public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> processAnnotatedType) {
		
		
		AnnotatedType<T> annotatedType = processAnnotatedType.getAnnotatedType();
		
		Class<T> javaClass = annotatedType.getJavaClass();
		if(shouldIntercept(javaClass)){
			Annotation monitoredAnnotation = new AnnotationLiteral<ResourceFallback>() {};
			AnnotatedTypeWrapper<T> wrapper = new AnnotatedTypeWrapper<T>(annotatedType, annotatedType.getAnnotations());
			wrapper.addAnnotation(monitoredAnnotation);
			processAnnotatedType.setAnnotatedType(wrapper);
		}
	}
	
	protected <T> boolean shouldIntercept(Class<T> javaClass) {
		return isNotFinal(javaClass) && 
				isNotEJB(javaClass) && 
				hasNotStaticOrFinalMethod(javaClass) &&
				notMonitoringToolsClass(javaClass) &&
				hasManagedResourceInjected(javaClass);
	}
	
	protected <T> boolean isNotFinal(Class<T> javaClass) {
		return !Modifier.isFinal(javaClass.getModifiers());
	}
	
	protected boolean isNotEJB(Class<?> clazz){
		return !(clazz.getAnnotation(Stateless.class)!=null || clazz.getAnnotation(Stateful.class) !=null);
	}
	
	protected boolean hasNotStaticOrFinalMethod(Class<?> clazz){
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if(Modifier.isFinal(method.getModifiers()) || Modifier.isStatic(method.getModifiers())){
				return false;
			}
		}
		return true;
	}

	protected <T> boolean hasManagedResourceInjected(Class<T> javaClass) {
		return isApplicationScoped(javaClass) && (
				hasEJBInjected(javaClass) || 
				hasWebServiceInjected(javaClass));
	}
	
	protected <T> boolean isApplicationScoped(Class<T> javaClass) {
		return javaClass.getAnnotation(ApplicationScoped.class)!=null;
	}
	
	protected boolean hasEJBInjected(Class<?> clazz){
		
		while(clazz!=null){
			for(Field field : clazz.getDeclaredFields()){
				if(field.getAnnotation(EJB.class)!=null){
					return true;
				}
			}
			clazz=clazz.getSuperclass();
		}
		
		return false;
	}
	
	protected boolean hasWebServiceInjected(Class<?> clazz){
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if(Modifier.isFinal(method.getModifiers()) || Modifier.isStatic(method.getModifiers())){
				return false;
			}
		}
		return true;
	}
	
	protected boolean notMonitoringToolsClass(Class<?> clazz){
		return !clazz.getName().startsWith("monitoringtools");
	}
	
}
