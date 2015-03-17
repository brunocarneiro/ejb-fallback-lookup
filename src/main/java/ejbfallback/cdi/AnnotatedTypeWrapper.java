package ejbfallback.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 * Wrappes an annotatedType to be possible to add interceptor dynamically.
 * @author bruno.carneiro
 *
 * @param <T>
 */
public class AnnotatedTypeWrapper<T> implements AnnotatedType<T> {

	private final AnnotatedType<T> wrapped;
	private final Set<Annotation> annotations;

	public AnnotatedTypeWrapper(AnnotatedType<T> wrapped, Set<Annotation> annotations) {
		this.wrapped = wrapped;
		this.annotations = new HashSet<Annotation>(annotations);
	}

	public void addAnnotation(Annotation annotation) {
		annotations.add(annotation);
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		A a= wrapped.getAnnotation(annotationType);
		if(a == null){
			for(Annotation ann : annotations){
				if(ann.getClass().equals(annotationType)){
					a = (A) ann;
				}
			}
		}
		return a;
	}

	@Override
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	@Override
	public Type getBaseType() {
		return wrapped.getBaseType();
	}

	@Override
	public Set<Type> getTypeClosure() {
		return wrapped.getTypeClosure();
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		for (Annotation annotation : annotations) {
			if (annotationType.isInstance(annotation)) {
				return true;
			}
		}
		return wrapped.isAnnotationPresent(annotationType);
	}

	@Override
	public Set<AnnotatedConstructor<T>> getConstructors() {
		return wrapped.getConstructors();
	}

	@Override
	public Set<AnnotatedField<? super T>> getFields() {
		return wrapped.getFields();
	}

	@Override
	public Class<T> getJavaClass() {
		return wrapped.getJavaClass();
	}

	@Override
	public Set<AnnotatedMethod<? super T>> getMethods() {
		return wrapped.getMethods();
	}

}
