package smilebot.exceptions;

public class CustomIdAnnotationNotFoundException extends Exception {

    public CustomIdAnnotationNotFoundException(Class<?> clazzObject, Class<?> clazzAnnotation) {
        super("Annotation " + clazzAnnotation.getName() + " not found for " + clazzObject.getName());
    }
}
