package hdu.dqj.Client;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author dqj
 * @Date 2020/2/14
 * @Version 1.0
 * @Description 请求信息对象，因为要序列化，所以需要实现Serializable接口
 */
public class RequestObject implements Serializable {
    private String methodName;  // 方法名
    private String className;   // 方法所属类名或接口名
    private Class<?>[] parameterTypes;  // 方法的参数类型
    private Object[] params;    // 方法参数值

    public RequestObject(String methodName, String className, Class<?>[] parameterTypes, Object[] params) {
        this.methodName = methodName;
        this.className = className;
        this.parameterTypes = parameterTypes;
        this.params = params;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
