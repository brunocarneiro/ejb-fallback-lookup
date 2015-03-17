# ejb-fallback-lookup
Sometimes when we inject a remote EJB and if its the remote application is not available, the injected EJB will not work anymore.
But then, if the application is restarted the injected EJB should be looked up again. But it does not work with the annotation @EJB in another EJB (@Stateless or @Stateful).

This project solve this problem. It's an interceptor (or EJB interceptor or CDI interceptor, you choose!) thata monitores the health status of this injected EJB. If it's not work, the interceptor tries to lookup and refresh the field value.

-Configuring CDI Interceptor (Recommended if you're using Java EE 6):

  pom.xml
    <dependency>
      <artifactId>monitoring-tools</artifactId>
      <groupId>com.github.brunocarneiro</groupId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>

-Configuring EJB Interceptor:

  pom.xml
    <dependency>
      <artifactId>monitoring-tools</artifactId>
      <groupId>com.github.brunocarneiro</groupId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
  
  ejb-jar.xml
    <assembly-descriptor>
  		<interceptor-binding>
  			<ejb-name>*</ejb-name>
  			<interceptor-class>ejbfallback.ResourceFallbackInterceptor</interceptor-class>
  		</interceptor-binding>
  	</assembly-descriptor>
