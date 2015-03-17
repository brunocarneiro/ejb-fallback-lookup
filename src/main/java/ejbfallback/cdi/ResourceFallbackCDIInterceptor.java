package ejbfallback.cdi;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import ejbfallback.ResourceFallbackInterceptor;

@ResourceFallback
@Interceptor
public class ResourceFallbackCDIInterceptor extends ResourceFallbackInterceptor {
	
	private static final long serialVersionUID = 1L;
	
	@AroundInvoke
	public Object interceptAllInvocations (InvocationContext ctx) throws Exception {
		
		return super.interceptAllInvocations(ctx);
	}
}
