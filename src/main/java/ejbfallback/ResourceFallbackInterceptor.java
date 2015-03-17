package ejbfallback;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

public class ResourceFallbackInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@AroundInvoke
	public Object interceptAllInvocations (InvocationContext ctx) throws Exception {
		
		Object target = ctx.getTarget();
		Set<Field> resourceFields =getResourceField(target);
		for (Field resourceField: resourceFields) {
			if(resourceField!=null && resourceField.get(target)==null){
				//now should intercept. Something wrong, try fallback...
				resourceField.set(target, lookupIfNecessary(resourceField.getAnnotation(EJB.class).lookup(), resourceField.getType()));
			}	
		}
		return ctx.proceed();
	}
	
	
	protected Set<Field> getResourceField(Object object){
		
		Class<?> clazz = object.getClass();
		Set<Field> fields = new HashSet<Field>();
		while(clazz!=null){
			for(Field field : clazz.getDeclaredFields()){
				if(field.getAnnotation(EJB.class)!=null){
					fields.add(field);
				}
			}
			clazz=clazz.getSuperclass();
		}
		
		return fields;
	}
	
	public <T> T  lookupIfNecessary(String lookup, Class<T> classType) {
		
		try {
			Hashtable<String,String> env = new Hashtable<String, String>();
			Context initCtx = new InitialContext(env);
			Object myObj = initCtx.lookup(lookup);
			return (T) PortableRemoteObject.narrow(myObj, classType);
		} catch (Exception e) {
			throw new RuntimeException("Não foi encontrado EJB do tipo "+classType+" com a lookup "+lookup);
		} 
	}
}
