package demo8;

import org.springframework.beans.factory.FactoryBean;

public class FibonacciFactoryBean implements FactoryBean<Integer> {
    private int a = 0;
    private int b = 1;
    @Override
    public Integer getObject() {
        int c = a + b;
        a = b;
        b = c;
        return c;
    }

    @Override
    public Class<?> getObjectType() {
        return Integer.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
