package testus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface ClassPreamble {

	String author();

	String importance();
	
	String comment() default "";

}
