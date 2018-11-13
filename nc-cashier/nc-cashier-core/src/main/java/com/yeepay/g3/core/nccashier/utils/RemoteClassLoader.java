package com.yeepay.g3.core.nccashier.utils;

import javassist.CtClass;

/**
 * 
 * @author haojie.yuan
 *
 */
public class RemoteClassLoader extends ClassLoader {

    private CtClass ctClass;

    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> contexClass = Thread.currentThread().getContextClassLoader().loadClass(name);
        if (contexClass == null) {
            if (ctClass == null) {
                throw new ClassNotFoundException("class " + name + " is not found");
            }
            // 如果ctClass存在则由ctClass生成class
            try {
                byte[] bytes = ctClass.toBytecode();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                throw new ClassNotFoundException("class " + name + " is not found");
            }
        } else {
            return contexClass;
        }
    }

    public CtClass getCtClass() {
        return ctClass;
    }

    public void setCtClass(CtClass ctClass) {
        this.ctClass = ctClass;
    }
}
